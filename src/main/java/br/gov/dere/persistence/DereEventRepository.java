package br.gov.dere.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DereEventRepository extends JpaRepository<DereEventEntity, Long> {
  Optional<DereEventEntity> findByEventIdentifier(String eventIdentifier);
  List<DereEventEntity> findByBatchId(Long batchId);
  List<DereEventEntity> findByEntityIdOrderByIdDesc(Long entityId);
}
