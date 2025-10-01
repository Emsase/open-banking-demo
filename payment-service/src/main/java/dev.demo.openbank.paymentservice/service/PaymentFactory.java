package dev.demo.openbank.paymentservice.service;

import dev.demo.openbank.paymentservice.service.model.Payment;
import java.math.BigDecimal;
import java.util.regex.Pattern;

public final class PaymentFactory {

    private static final Pattern IBAN = Pattern.compile("^[A-Z]{2}[0-9A-Z]{13,30}$");

    public static Payment create(String d, String c, BigDecimal a, String cur, String idem) {
        if (d == null || !IBAN.matcher(d).matches()) {
            throw new IllegalArgumentException("Invalid debtor IBAN");
        }
        if (c == null || !IBAN.matcher(c).matches()) {
            throw new IllegalArgumentException("Invalid creditor IBAN");
        }
        if (a == null || a.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be > 0");
        }
        if (cur == null || cur.length() != 3) {
            throw new IllegalArgumentException("Invalid currency");
        }
        return Payment.newCreated(d, c, a, cur, idem);
    }
}