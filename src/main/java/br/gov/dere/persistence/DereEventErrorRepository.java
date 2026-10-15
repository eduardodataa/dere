package br.gov.dere.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface DereEventErrorRepository extends JpaRepository<DereEventErrorEntity, Long> {
  @Modifying(clearAutomatically = true)
  void deleteByEventId(Long eventId);
}
