package dev.demo.openbank.paymentrisk.infrastructure.messaging;

import dev.demo.openbank.common.events.PaymentEvents;
import dev.demo.openbank.paymentrisk.service.RiskEngine;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RiskKafkaProcessor {

    private final KafkaTemplate<String, Object> kafka;
    private final Map<String, Integer> velocity = new ConcurrentHashMap<>();
    private final RiskEngine engine = new RiskEngine(java.util.List.of(new AmountThresholdRule(new BigDecimal("1000.00")), new VelocityRule(5)));

    public RiskKafkaProcessor(KafkaTemplate<String, Object> k) {
        this.kafka = k;
    }

    @KafkaListener(topics = "payment.requested", groupId = "risk")
    public void onRequested(ConsumerRecord<String, PaymentEvents.PaymentRequested> rec) {
        String key = rec.key();
        int v = velocity.merge(key.substring(0, 8), 1, Integer::sum);
        PaymentEvents.PaymentRequested evt = rec.value();
        String reason = engine.firstRejectionReason(evt.getAmount(), v);
        if (reason != null) {
            kafka.send("payment.risk.rejected", key,
                    PaymentEvents.PaymentRiskRejected.builder().paymentId(evt.getPaymentId()).reason(reason).build());
        } else {
            kafka.send("payment.risk.approved", key, PaymentEvents.PaymentRiskApproved.builder().paymentId(evt.getPaymentId()).build());
        }
    }
}