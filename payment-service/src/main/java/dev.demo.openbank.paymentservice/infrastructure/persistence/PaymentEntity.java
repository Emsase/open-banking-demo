package dev.demo.openbank.paymentservice.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payment_order", uniqueConstraints = @UniqueConstraint(columnNames = "idempotency_key"))
@Getter
@Setter
public class PaymentEntity {

    @Id
    private UUID id;
    private String debtorIban;
    private String creditorIban;
    private BigDecimal amount;
    private String currency;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Instant createdAt;
    @Column(name = "idempotency_key")
    private String idempotencyKey;

    public enum Status {CREATED, RISK_APPROVED, REJECTED, EXECUTED, FAILED}
}