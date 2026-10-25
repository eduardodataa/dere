package br.gov.dere.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DereEntidadeLogoRepository extends JpaRepository<DereEntidadeLogoEntity, Long> {
  @Query("select logo.entityId from DereEntidadeLogoEntity logo")
  List<Long> identificadores();
}
