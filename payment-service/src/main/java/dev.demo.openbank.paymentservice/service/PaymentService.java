package dev.demo.openbank.paymentservice.service;

import dev.demo.openbank.paymentservice.service.model.Payment;
import dev.demo.openbank.paymentservice.infrastructure.messaging.PaymentEventProducer;
import dev.demo.openbank.paymentservice.infrastructure.persistence.PaymentRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentEventProducer producer;
    private final ExecutorService vts = Executors.newVirtualThreadPerTaskExecutor();

    @Value
    @Builder
    public static class CreateResult {

        UUID paymentId;
        String status;
    }

    @Transactional
    public CreateResult createPayment(String debtorIban, String creditorIban, java.math.BigDecimal amount, String currency, String idempotencyKey) {
        Optional<Payment> existing = repository.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) {
            Payment p = existing.get();
            return CreateResult.builder().paymentId(p.getId()).status(p.getStatus().name()).build();
        }
        Payment payment = PaymentFactory.create(debtorIban, creditorIban, amount, currency, idempotencyKey);
        repository.save(payment);
        vts.execute(() -> producer.publishPaymentRequested(payment));
        return CreateResult.builder().paymentId(payment.getId()).status(payment.getStatus().name()).build();
    }

    public Payment getById(UUID id) {
        return repository.findById(id).orElseThrow();
    }
}
