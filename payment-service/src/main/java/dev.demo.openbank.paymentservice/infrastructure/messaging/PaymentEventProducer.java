package dev.demo.openbank.paymentservice.infrastructure.messaging;

import dev.demo.openbank.common.events.PaymentEvents.PaymentRequested;
import dev.demo.openbank.paymentservice.service.model.Payment;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafka;

    public void publishPaymentRequested(Payment p) {
        PaymentRequested evt = PaymentRequested.builder().paymentId(p.getId()).debtorIban(p.getDebtorIban()).creditorIban(p.getCreditorIban())
                .amount(p.getAmount()).currency(p.getCurrency()).build();
        kafka.send(new ProducerRecord<>("payment.requested", p.getId().toString(), evt));
    }
}