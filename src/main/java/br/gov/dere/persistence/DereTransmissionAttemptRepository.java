package br.gov.dere.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DereTransmissionAttemptRepository extends JpaRepository<DereTransmissionAttemptEntity, Long> {
  List<DereTransmissionAttemptEntity> findByEventIdOrderByAttemptAsc(Long eventId);
}
