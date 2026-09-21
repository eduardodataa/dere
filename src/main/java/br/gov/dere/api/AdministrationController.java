package br.gov.dere.api;
import br.gov.dere.application.access.AccessService; import br.gov.dere.persistence.*; import java.util.*; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/admin") public class AdministrationController { private final AccessService access; private final DereUserRepository users; private final DereEntityRepository entities; public AdministrationController(AccessService a,DereUserRepository u,DereEntityRepository e){access=a;users=u;entities=e;}
 @PostMapping("/users") public UserDto user(@RequestBody UserRequest r){var u=access.create(r.login(),r.password(),r.name());return new UserDto(u.getId(),u.getLogin(),u.getDisplayName());}
 @GetMapping("/users") public List<UserDto> users(){return users.findAll().stream().map(u->new UserDto(u.getId(),u.getLogin(),u.getDisplayName())).toList();}
 @PutMapping("/users/{id}") public UserDto updateUser(@PathVariable Long id,@RequestBody UserRequest r){var u=users.findById(id).orElseThrow();u.update(r.name(),r.password().isBlank()?null:access.encode(r.password()));users.save(u);return new UserDto(u.getId(),u.getLogin(),u.getDisplayName());}
 @DeleteMapping("/users/{id}") public void deleteUser(@PathVariable Long id){var u=users.findById(id).orElseThrow();u.getEntities().clear();users.save(u);users.delete(u);}
 @PostMapping("/entities") public EntityDto entity(@RequestBody EntityRequest r){var e=entities.save(new DereEntityEntity(r.cnpjRoot(),r.legalName()));return new EntityDto(e.getId(),e.getCnpjRoot(),e.getLegalName());}
 @GetMapping("/entities") public List<EntityDto> entities(){return entities.findAll().stream().map(e->new EntityDto(e.getId(),e.getCnpjRoot(),e.getLegalName())).toList();}
 @PutMapping("/entities/{id}") public EntityDto updateEntity(@PathVariable Long id,@RequestBody EntityRequest r){var e=entities.findById(id).orElseThrow();e.update(r.cnpjRoot(),r.legalName());entities.save(e);return new EntityDto(e.getId(),e.getCnpjRoot(),e.getLegalName());}
 @DeleteMapping("/entities/{id}") public void deleteEntity(@PathVariable Long id){var e=entities.findById(id).orElseThrow();for(var u:new java.util.ArrayList<>(e.getUsers())){u.getEntities().remove(e);users.save(u);}entities.delete(e);}
 @PostMapping("/users/{userId}/entities/{entityId}") public void associate(@PathVariable Long userId,@PathVariable Long entityId){users.findById(userId).orElseThrow().getEntities().add(entities.findById(entityId).orElseThrow());users.save(users.findById(userId).orElseThrow());}
 public record UserRequest(String login,String password,String name){} public record EntityRequest(String cnpjRoot,String legalName){} public record UserDto(Long id,String login,String name){} public record EntityDto(Long id,String cnpjRoot,String legalName){}
}
