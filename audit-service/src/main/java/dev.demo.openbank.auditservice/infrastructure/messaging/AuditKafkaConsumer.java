package dev.demo.openbank.auditservice.infrastructure.messaging;

import dev.demo.openbank.auditservice.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditKafkaConsumer {

    private final AuditService svc;

    @KafkaListener(topics = {"payment.requested", "payment.risk.approved", "payment.risk.rejected", "payment.bank.accepted"}, groupId = "audit")
    public void consume(org.apache.kafka.clients.consumer.ConsumerRecord<String, Object> rec) {
        svc.store(rec.topic(), rec.key(), rec.value());
    }
}