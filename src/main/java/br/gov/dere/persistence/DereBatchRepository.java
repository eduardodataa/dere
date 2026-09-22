package br.gov.dere.persistence;
import br.gov.dere.domain.EventStatus; import java.time.Instant; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface DereBatchRepository extends JpaRepository<DereBatchEntity,Long>{Optional<DereBatchEntity> findByProtocol(String protocol); List<DereBatchEntity> findTop100ByStatusAndNextQueryAtLessThanEqualOrderByNextQueryAtAsc(EventStatus status,Instant now);}
