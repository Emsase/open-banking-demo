package dev.demo.openbank.auditservice.infrastructure.persistence;

import dev.demo.openbank.auditservice.service.model.AuditRecord;

public interface AuditRepository {

    AuditRecord save(AuditRecord r);

    java.util.List<AuditRecord> findAll();
}