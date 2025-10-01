package dev.demo.openbank.auditservice.infrastructure.persistence;

import dev.demo.openbank.auditservice.service.model.AuditRecord;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class AuditRepositoryImpl implements AuditRepository {

    private final EventLogRepo jpa;

    public AuditRecord save(AuditRecord r) {
        EventLogEntity e = new EventLogEntity();
        e.setTopic(r.getTopic());
        e.setKeyRef(r.getKeyRef());
        e.setPayload(r.getPayload());
        e.setCreatedAt(r.getCreatedAt());
        EventLogEntity s = jpa.save(e);
        return AuditRecord.builder().id(s.getId()).topic(s.getTopic()).keyRef(s.getKeyRef()).payload(s.getPayload()).createdAt(s.getCreatedAt())
                .build();
    }

    public java.util.List<AuditRecord> findAll() {
        return jpa.findAll().stream().map(s -> AuditRecord.builder().id(s.getId()).topic(s.getTopic()).keyRef(s.getKeyRef()).payload(s.getPayload())
                .createdAt(s.getCreatedAt()).build()).collect(Collectors.toList());
    }
}