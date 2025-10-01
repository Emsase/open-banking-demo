package dev.demo.openbank.paymentservice.service;

import dev.demo.openbank.paymentservice.infrastructure.messaging.PaymentEventProducer;
import dev.demo.openbank.paymentservice.infrastructure.persistence.PaymentRepository;
import dev.demo.openbank.paymentservice.service.model.Payment;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentServiceTest {

    @Test
    void idempotency_returns_existing() {
        PaymentRepository repo = mock(PaymentRepository.class);
        PaymentEventProducer prod = mock(PaymentEventProducer.class);
        PaymentService svc = new PaymentService(repo, prod);

        Payment existing = Payment.newCreated(
                "ES7921000418450200051332",
                "DE75512108001245126199",
                new BigDecimal("1.00"),
                "EUR",
                "idem"
        );

        when(repo.findByIdempotencyKey("idem")).thenReturn(Optional.of(existing));

        PaymentService.CreateResult res = svc.createPayment(
                existing.getDebtorIban(),
                existing.getCreditorIban(),
                existing.getAmount(),
                existing.getCurrency(),
                "idem"
        );

        assertEquals(existing.getId(), res.getPaymentId());
        assertEquals(existing.getStatus().name(), res.getStatus());
        verify(repo, never()).save(any());
        verify(prod, never()).publishPaymentRequested(any());
    }

    @Test
    void getById_returns_payment_when_present() {
        PaymentRepository repo = mock(PaymentRepository.class);
        PaymentEventProducer prod = mock(PaymentEventProducer.class);
        PaymentService svc = new PaymentService(repo, prod);

        UUID id = UUID.randomUUID();
        Payment existing = Payment.newCreated(
                "ESxxxx", "DEyyyy", new BigDecimal("10.00"), "EUR", "idem-2");

        when(repo.findById(id)).thenReturn(Optional.of(existing));

        Payment found = svc.getById(id);
        assertNotNull(found);
        assertEquals(existing, found);
    }

    @Test
    void getById_throws_when_absent() {
        PaymentRepository repo = mock(PaymentRepository.class);
        PaymentEventProducer prod = mock(PaymentEventProducer.class);
        PaymentService svc = new PaymentService(repo, prod);

        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> svc.getById(id));
    }
}
