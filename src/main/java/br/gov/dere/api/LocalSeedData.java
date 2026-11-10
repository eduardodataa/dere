package br.gov.dere.api;

import br.gov.dere.application.access.AccessService;
import br.gov.dere.persistence.DereEntityEntity;
import br.gov.dere.persistence.DereEntityRepository;
import br.gov.dere.persistence.DereUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class LocalSeedData {
  @Bean
  CommandLineRunner seed(AccessService access, DereUserRepository users, DereEntityRepository entities) {
    return args -> {
      if (users.count() == 0) {
        var user = access.create("admin", "admin", "Administrador local", "admin@localhost.local");
        var entity = entities.save(new DereEntityEntity("12345678", "Entidade EFPC de teste"));
        user.getEntities().add(entity);
        users.save(user);
      }
    };
  }
}
