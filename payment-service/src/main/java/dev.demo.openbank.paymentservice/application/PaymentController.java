package dev.demo.openbank.paymentservice.application;

import dev.demo.openbank.paymentservice.application.dto.CreatePaymentRequest;
import dev.demo.openbank.paymentservice.service.PaymentService;
import dev.demo.openbank.paymentservice.service.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PreAuthorize("hasAuthority('SCOPE_payments:create')")
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody CreatePaymentRequest req,
            @RequestHeader(value = "Idempotency-Key", required = false) String idem) {
      if (!StringUtils.hasText(idem)) {
        idem = UUID.randomUUID().toString();
      }
        PaymentService.CreateResult res = service.createPayment(req.getDebtorIban(), req.getCreditorIban(), req.getAmount(), req.getCurrency(), idem);
        return ResponseEntity.status(201).body(Map.of("paymentId", res.getPaymentId().toString(), "status", res.getStatus()));
    }

    @PreAuthorize("hasAuthority('SCOPE_payments:read')")
    @GetMapping("/{id}")
    public Map<String, Object> get(@PathVariable UUID id) {
        Payment p = service.getById(id);
        return Map.of("id", p.getId().toString(), "status", p.getStatus().name(), "amount", p.getAmount(), "currency", p.getCurrency());
    }
}
