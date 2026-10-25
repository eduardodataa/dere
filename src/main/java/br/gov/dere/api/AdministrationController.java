package br.gov.dere.api;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.persistence.DereEntidadeLogoEntity;
import br.gov.dere.persistence.DereEntidadeLogoRepository;
import br.gov.dere.persistence.DereEntityEntity;
import br.gov.dere.persistence.DereEntityRepository;
import br.gov.dere.persistence.DereUserEntity;
import br.gov.dere.persistence.DereUserRepository;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/admin")
public class AdministrationController {
  private static final int LOGO_MAXIMO = 1024 * 1024;
  private final AccessService access;
  private final DereUserRepository users;
  private final DereEntityRepository entities;
  private final DereEntidadeLogoRepository logos;

  public AdministrationController(AccessService access, DereUserRepository users, DereEntityRepository entities, DereEntidadeLogoRepository logos) {
    this.access = access;
    this.users = users;
    this.entities = entities;
    this.logos = logos;
  }

  @PostMapping("/users")
  @Transactional
  public UserDto user(@RequestHeader("X-User-Id") Long actorId, @RequestBody UserRequest request) {
    assertMaster(actorId);
    var created = access.create(request.login(), request.password(), request.name());
    created.setMaster(Boolean.TRUE.equals(request.master()));
    return toDto(users.save(created));
  }

  @GetMapping("/users")
  public List<UserDto> users(@RequestHeader("X-User-Id") Long actorId) {
    assertMaster(actorId);
    return users.findAll().stream().sorted(Comparator.comparing(DereUserEntity::getLogin)).map(this::toDto).toList();
  }

  @PutMapping("/users/{id}")
  @Transactional
  public UserDto updateUser(@RequestHeader("X-User-Id") Long actorId, @PathVariable Long id, @RequestBody UserRequest request) {
    assertMaster(actorId);
    var user = users.findById(id).orElseThrow();
    var password = request.password() == null || request.password().isBlank() ? null : access.encode(request.password());
    user.update(request.login(), request.name() == null || request.name().isBlank() ? user.getDisplayName() : request.name(), password);
    if (request.master() != null) user.setMaster(request.master());
    return toDto(users.save(user));
  }

  @DeleteMapping("/users/{id}")
  @Transactional
  public void deleteUser(@RequestHeader("X-User-Id") Long actorId, @PathVariable Long id) {
    assertMaster(actorId);
    if (actorId.equals(id)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O usuário autenticado não pode se excluir");
    var user = users.findById(id).orElseThrow();
    user.getEntities().clear();
    users.save(user);
    users.delete(user);
  }

  @PostMapping("/entities")
  @Transactional
  public EntityDto entity(@RequestHeader("X-User-Id") Long actorId, @RequestBody EntityRequest request) {
    assertMaster(actorId);
    var raiz = raizCnpj(request.cnpjRoot());
    requireEntity(raiz, request.legalName());
    var entity = entities.save(new DereEntityEntity(raiz, request.legalName().trim()));
    aplicarLogo(entity.getId(), request);
    return toDto(entity, link(entity, request.userIds()));
  }

  @GetMapping("/entities")
  public List<EntityDto> entities(@RequestHeader("X-User-Id") Long actorId) {
    var actor = users.findById(actorId).orElseThrow();
    var visible = entities.findAll().stream();
    if (!actor.isMaster()) {
      var allowed = actor.getEntities().stream().map(DereEntityEntity::getId).toList();
      visible = visible.filter(entity -> allowed.contains(entity.getId()));
    }
    var comLogo = Set.copyOf(logos.identificadores());
    return visible.sorted(Comparator.comparing(DereEntityEntity::getLegalName)).map(entity -> toDto(entity, userIds(entity), comLogo.contains(entity.getId()))).toList();
  }

  @GetMapping("/entities/{id}/logo")
  public ResponseEntity<byte[]> logo(@RequestHeader("X-User-Id") Long actorId, @PathVariable Long id) {
    access.assertAccess(actorId, id);
    var logo = logos.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entidade sem logo"));
    return ResponseEntity.ok().contentType(MediaType.parseMediaType(logo.getTipo())).cacheControl(CacheControl.noCache()).body(logo.getConteudo());
  }

  @PutMapping("/entities/{id}")
  @Transactional
  public EntityDto updateEntity(@RequestHeader("X-User-Id") Long actorId, @PathVariable Long id, @RequestBody EntityRequest request) {
    assertMaster(actorId);
    var raiz = raizCnpj(request.cnpjRoot());
    requireEntity(raiz, request.legalName());
    var entity = entities.findById(id).orElseThrow();
    entity.update(raiz, request.legalName().trim());
    entities.save(entity);
    aplicarLogo(entity.getId(), request);
    return toDto(entity, link(entity, request.userIds()));
  }

  @DeleteMapping("/entities/{id}")
  @Transactional
  public void deleteEntity(@RequestHeader("X-User-Id") Long actorId, @PathVariable Long id) {
    assertMaster(actorId);
    var entity = entities.findById(id).orElseThrow();
    for (var user : new ArrayList<>(entity.getUsers())) {
      user.getEntities().removeIf(item -> item.getId().equals(entity.getId()));
      users.saveAndFlush(user);
    }
    logos.deleteById(entity.getId());
    entities.delete(entity);
  }

  private void aplicarLogo(Long entityId, EntityRequest request) {
    var base64 = request.logoBase64() == null ? "" : request.logoBase64().trim();
    var virgula = base64.indexOf(',');
    if (base64.startsWith("data:") && virgula >= 0) base64 = base64.substring(virgula + 1);
    if (!base64.isBlank()) {
      byte[] bytes;
      try {
        bytes = Base64.getDecoder().decode(base64);
      } catch (IllegalArgumentException ex) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A logo enviada não é uma imagem válida.");
      }
      if (bytes.length > LOGO_MAXIMO) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A logo deve ter no máximo 1 MB.");
      var tipo = tipoDaImagem(bytes);
      var logo = logos.findById(entityId).orElseGet(() -> new DereEntidadeLogoEntity(entityId));
      logo.definir(tipo, bytes);
      logos.save(logo);
      return;
    }
    if (Boolean.TRUE.equals(request.removerLogo())) logos.deleteById(entityId);
  }

  private static String tipoDaImagem(byte[] bytes) {
    if (bytes.length >= 8 && bytes[0] == (byte) 0x89 && bytes[1] == 0x50 && bytes[2] == 0x4E && bytes[3] == 0x47) return "image/png";
    if (bytes.length >= 3 && bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD8 && bytes[2] == (byte) 0xFF) return "image/jpeg";
    if (bytes.length >= 6 && bytes[0] == 'G' && bytes[1] == 'I' && bytes[2] == 'F' && bytes[3] == '8') return "image/gif";
    if (bytes.length >= 12 && bytes[0] == 'R' && bytes[1] == 'I' && bytes[2] == 'F' && bytes[3] == 'F' && bytes[8] == 'W' && bytes[9] == 'E' && bytes[10] == 'B' && bytes[11] == 'P') return "image/webp";
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Use uma logo PNG, JPEG, WEBP ou GIF.");
  }

  private List<Long> link(DereEntityEntity entity, List<Long> userIds) {
    var desired = new HashSet<>(userIds == null ? List.<Long>of() : userIds);
    for (var user : users.findAll()) {
      var linked = user.getEntities().stream().anyMatch(item -> item.getId().equals(entity.getId()));
      var shouldLink = desired.contains(user.getId());
      if (shouldLink && !linked) {
        user.getEntities().add(entity);
        entity.getUsers().add(user);
        users.save(user);
      } else if (!shouldLink && linked) {
        user.getEntities().removeIf(item -> item.getId().equals(entity.getId()));
        entity.getUsers().removeIf(item -> item.getId().equals(user.getId()));
        users.save(user);
      }
    }
    return desired.stream().sorted().toList();
  }

  private void assertMaster(Long actorId) {
    if (actorId == null || !users.findById(actorId).orElseThrow().isMaster()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas usuário master pode gerenciar este cadastro");
    }
  }

  private static String raizCnpj(String valor) {
    if (valor == null) return "";
    var digitos = valor.replaceAll("\\D", "");
    return digitos.length() >= 8 ? digitos.substring(0, 8) : digitos;
  }

  private static void requireEntity(String raiz, String razao) {
    if (raiz == null || raiz.length() != 8 || razao == null || razao.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNPJ raiz deve ter 8 dígitos e a razão social é obrigatória");
    }
  }

  private UserDto toDto(DereUserEntity user) {
    return new UserDto(user.getId(), user.getLogin(), user.getDisplayName(), user.isMaster());
  }

  private EntityDto toDto(DereEntityEntity entity, List<Long> userIds) {
    return toDto(entity, userIds, logos.existsById(entity.getId()));
  }

  private EntityDto toDto(DereEntityEntity entity, List<Long> userIds, boolean temLogo) {
    return new EntityDto(entity.getId(), entity.getCnpjRoot(), entity.getLegalName(), userIds, temLogo);
  }

  private static List<Long> userIds(DereEntityEntity entity) {
    return entity.getUsers().stream().map(DereUserEntity::getId).sorted().toList();
  }

  public record UserRequest(String login, String password, String name, Boolean master) {}
  public record EntityRequest(String cnpjRoot, String legalName, List<Long> userIds, String logoBase64, String logoTipo, Boolean removerLogo) {}
  public record UserDto(Long id, String login, String name, boolean master) {}
  public record EntityDto(Long id, String cnpjRoot, String legalName, List<Long> userIds, boolean temLogo) {}
}
