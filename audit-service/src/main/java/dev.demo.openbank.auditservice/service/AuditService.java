package dev.demo.openbank.auditservice.service;

import dev.demo.openbank.auditservice.infrastructure.persistence.AuditRepository;
import dev.demo.openbank.auditservice.service.model.AuditRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository repo;

    public void store(String topic, String key, Object value) {
        repo.save(AuditRecord.builder().topic(topic).keyRef(key).payload(Map.of("value", String.valueOf(value))).build());
    }

    public java.util.List<AuditRecord> all() {
        return repo.findAll();
    }
}