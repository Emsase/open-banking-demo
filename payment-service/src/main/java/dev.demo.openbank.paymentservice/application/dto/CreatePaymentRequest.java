package dev.demo.openbank.paymentservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreatePaymentRequest {

    @NotBlank
    private String debtorIban;
    @NotBlank
    private String creditorIban;
    @NotNull
    private BigDecimal amount;
    @NotBlank
    private String currency;
}