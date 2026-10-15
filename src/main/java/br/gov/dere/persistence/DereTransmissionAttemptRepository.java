package br.gov.dere.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface DereTransmissionAttemptRepository extends JpaRepository<DereTransmissionAttemptEntity, Long> {
  List<DereTransmissionAttemptEntity> findByEventIdOrderByAttemptAsc(Long eventId);

  @Modifying(clearAutomatically = true)
  void deleteByEventId(Long eventId);
}
