package dev.demo.openbank.paymentrisk.infrastructure.messaging;

import dev.demo.openbank.paymentrisk.service.RiskRule;
import java.math.BigDecimal;

public class VelocityRule implements RiskRule {

    private final int max;

    public VelocityRule(int max) {
        this.max = max;
    }

    @Override
    public boolean rejects(BigDecimal amount, int velocity) {
        return velocity > max;
    }

    @Override
    public String reason() {
        return "Velocity > " + max;
    }
}
