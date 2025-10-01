package dev.demo.openbank.bankconnector.infrastructure.messaging;

import dev.demo.openbank.common.events.PaymentEvents;
import dev.demo.openbank.bankconnector.service.BankService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BankKafkaProcessor {

    private final BankService bank;
    private final KafkaTemplate<String, Object> kafka;

    public BankKafkaProcessor(BankService bank, KafkaTemplate<String, Object> kafka) {
        this.bank = bank;
        this.kafka = kafka;
    }

    @KafkaListener(topics = "payment.risk.approved", groupId = "bank")
    public void onApproved(ConsumerRecord<String, PaymentEvents.PaymentRiskApproved> rec) {
        String ref = bank.executeTransfer(rec.value().getPaymentId());
        kafka.send("payment.bank.accepted", rec.key(),
                PaymentEvents.PaymentBankAccepted.builder().paymentId(rec.value().getPaymentId()).bankRef(ref).build());
    }
}