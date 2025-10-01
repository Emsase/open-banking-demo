package dev.demo.openbank.paymentrisk.infrastructure.messaging;

import dev.demo.openbank.paymentrisk.service.RiskRule;
import java.math.BigDecimal;

public class AmountThresholdRule implements RiskRule {

    private final BigDecimal threshold;

    public AmountThresholdRule(BigDecimal threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean rejects(BigDecimal amount, int value) {
        return amount.compareTo(threshold) > 0;
    }

    @Override
    public String reason() {
        return "Amount > " + threshold;
    }
}
