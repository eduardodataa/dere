package br.gov.dere.persistence;
import java.util.Optional; import org.springframework.data.jpa.repository.JpaRepository;
public interface DereBatchRepository extends JpaRepository<DereBatchEntity,Long>{Optional<DereBatchEntity> findByProtocol(String protocol);}
