package dev.demo.openbank.paymentrisk.service;

import java.math.BigDecimal;

public interface RiskRule {

    boolean rejects(BigDecimal amount, int velocity);

    String reason();
}