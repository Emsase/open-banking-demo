package dev.demo.openbank.paymentrisk.service;

import java.math.BigDecimal;
import java.util.List;

public class RiskEngine {

    private final List<RiskRule> rules;

    public RiskEngine(List<RiskRule> rules) {
        this.rules = rules;
    }

    public String firstRejectionReason(BigDecimal amount, int velocity) {
        return rules.stream()
                .filter(r -> r.rejects(amount, velocity))
                .map(RiskRule::reason)
                .findFirst()
                .orElse(null);
    }
}
