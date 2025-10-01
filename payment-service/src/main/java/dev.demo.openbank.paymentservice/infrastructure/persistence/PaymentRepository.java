package dev.demo.openbank.paymentservice.infrastructure.persistence;

import dev.demo.openbank.paymentservice.service.model.Payment;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment save(Payment p);

    Optional<Payment> findById(UUID id);

    Optional<Payment> findByIdempotencyKey(String key);
}