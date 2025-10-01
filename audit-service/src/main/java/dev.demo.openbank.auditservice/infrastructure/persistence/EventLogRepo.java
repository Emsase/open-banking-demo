package dev.demo.openbank.auditservice.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventLogRepo extends JpaRepository<EventLogEntity, Long> {

}