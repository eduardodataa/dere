package br.gov.dere.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface DereTentativaTransmissaoRepository extends JpaRepository<DereTentativaTransmissaoEntity, Long> {
  List<DereTentativaTransmissaoEntity> findByEventIdOrderByAttemptAsc(Long eventId);

  @Modifying(clearAutomatically = true)
  void deleteByEventId(Long eventId);
}
