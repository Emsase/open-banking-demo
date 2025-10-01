package dev.demo.openbank.paymentservice.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    public enum Status {CREATED, RISK_APPROVED, REJECTED, EXECUTED, FAILED}

    private UUID id;
    private String debtorIban;
    private String creditorIban;
    private BigDecimal amount;
    private String currency;
    private Instant createdAt;
    private Status status;
    private String idempotencyKey;

    public static Payment newCreated(String d, String c, BigDecimal a, String cur, String idem) {
        return Payment.builder().id(UUID.randomUUID()).debtorIban(d).creditorIban(c).amount(a).currency(cur).createdAt(Instant.now())
                .status(Status.CREATED).idempotencyKey(idem).build();
    }
}