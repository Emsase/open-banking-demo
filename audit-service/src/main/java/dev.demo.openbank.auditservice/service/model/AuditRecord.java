package dev.demo.openbank.auditservice.service.model;

import lombok.Builder;
import lombok.Value;
import java.time.Instant;
import java.util.Map;

@Value
@Builder
public class AuditRecord {

    Long id;
    String topic;
    String keyRef;
    Map<String, Object> payload;
    @Builder.Default
    Instant createdAt = Instant.now();
}