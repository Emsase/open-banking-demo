package dev.demo.openbank.paymentservice.infrastructure.persistence;

import dev.demo.openbank.paymentservice.service.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final SpringPaymentJpaRepository jpa;

    public Payment save(Payment p) {
        PaymentEntity e = new PaymentEntity();
        e.setId(p.getId());
        e.setDebtorIban(p.getDebtorIban());
        e.setCreditorIban(p.getCreditorIban());
        e.setAmount(p.getAmount());
        e.setCurrency(p.getCurrency());
        e.setStatus(PaymentEntity.Status.valueOf(p.getStatus().name()));
        e.setCreatedAt(p.getCreatedAt());
        e.setIdempotencyKey(p.getIdempotencyKey());
        PaymentEntity s = jpa.save(e);
        return map(s);
    }

    public Optional<Payment> findById(java.util.UUID id) {
        return jpa.findById(id).map(this::map);
    }

    public Optional<Payment> findByIdempotencyKey(String key) {
        return jpa.findByIdempotencyKey(key).map(this::map);
    }

    private Payment map(PaymentEntity s) {
        return Payment.builder().id(s.getId()).debtorIban(s.getDebtorIban()).creditorIban(s.getCreditorIban()).amount(s.getAmount())
                .currency(s.getCurrency()).createdAt(s.getCreatedAt()).status(Payment.Status.valueOf(s.getStatus().name()))
                .idempotencyKey(s.getIdempotencyKey()).build();
    }
}