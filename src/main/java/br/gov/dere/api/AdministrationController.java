package br.gov.dere.api;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.persistence.DereEntityEntity;
import br.gov.dere.persistence.DereEntityRepository;
import br.gov.dere.persistence.DereUserEntity;
import br.gov.dere.persistence.DereUserRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import org.springframework.http.HttpStatus;
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
  private final AccessService access;
  private final DereUserRepository users;
  private final DereEntityRepository entities;

  public AdministrationController(AccessService access, DereUserRepository users, DereEntityRepository entities) {
    this.access = access;
    this.users = users;
    this.entities = entities;
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
    requireEntity(request);
    var entity = entities.save(new DereEntityEntity(request.cnpjRoot().trim(), request.legalName().trim()));
    return toDto(entity, link(entity, request.userIds()));
  }

  @GetMapping("/entities")
  public List<EntityDto> entities(@RequestHeader(value = "X-User-Id", required = false) Long actorId) {
    var visible = entities.findAll().stream();
    if (actorId != null) {
      var actor = users.findById(actorId).orElseThrow();
      if (!actor.isMaster()) {
        var allowed = actor.getEntities().stream().map(DereEntityEntity::getId).toList();
        visible = visible.filter(entity -> allowed.contains(entity.getId()));
      }
    }
    return visible.sorted(Comparator.comparing(DereEntityEntity::getLegalName)).map(entity -> toDto(entity, userIds(entity))).toList();
  }

  @PutMapping("/entities/{id}")
  @Transactional
  public EntityDto updateEntity(@RequestHeader("X-User-Id") Long actorId, @PathVariable Long id, @RequestBody EntityRequest request) {
    assertMaster(actorId);
    requireEntity(request);
    var entity = entities.findById(id).orElseThrow();
    entity.update(request.cnpjRoot().trim(), request.legalName().trim());
    entities.save(entity);
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
    entities.delete(entity);
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

  private static void requireEntity(EntityRequest request) {
    if (request.cnpjRoot() == null || request.cnpjRoot().isBlank() || request.legalName() == null || request.legalName().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNPJ raiz e razão social são obrigatórios");
    }
  }

  private UserDto toDto(DereUserEntity user) {
    return new UserDto(user.getId(), user.getLogin(), user.getDisplayName(), user.isMaster());
  }

  private EntityDto toDto(DereEntityEntity entity, List<Long> userIds) {
    return new EntityDto(entity.getId(), entity.getCnpjRoot(), entity.getLegalName(), userIds);
  }

  private static List<Long> userIds(DereEntityEntity entity) {
    return entity.getUsers().stream().map(DereUserEntity::getId).sorted().toList();
  }

  public record UserRequest(String login, String password, String name, Boolean master) {}
  public record EntityRequest(String cnpjRoot, String legalName, List<Long> userIds) {}
  public record UserDto(Long id, String login, String name, boolean master) {}
  public record EntityDto(Long id, String cnpjRoot, String legalName, List<Long> userIds) {}
}
