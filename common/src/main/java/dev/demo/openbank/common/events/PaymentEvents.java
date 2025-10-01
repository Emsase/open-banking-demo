package dev.demo.openbank.common.events;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentEvents {

  @Value @Builder
  class PaymentRequested {
    UUID paymentId;
    String debtorIban;
    String creditorIban;
    BigDecimal amount;
    String currency;
  }

  @Value @Builder
  class PaymentRiskApproved { UUID paymentId; }

  @Value @Builder
  class PaymentRiskRejected {
    UUID paymentId;
    String reason;
  }

  @Value @Builder
  class PaymentBankAccepted {
    UUID paymentId;
    String bankRef;
  }
}
