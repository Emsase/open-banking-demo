package dev.demo.openbank.paymentservice.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface SpringPaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {

    Optional<PaymentEntity> findByIdempotencyKey(String idempotencyKey);
}