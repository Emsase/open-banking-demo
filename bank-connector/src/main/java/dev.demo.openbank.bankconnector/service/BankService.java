package dev.demo.openbank.bankconnector.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.concurrent.ExecutorService;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Random;
import java.util.UUID;

@Service
public class BankService {

    private final Random rnd = new Random();

    @Bulkhead(name = "bankClient")
    @CircuitBreaker(name = "bankClient")
    @Retry(name = "bankClient")
    public String executeTransfer(UUID paymentId) {
        try (ExecutorService vts = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
            return vts.submit(() -> {
                if (rnd.nextInt(10) == 0) {
                    throw new RuntimeException("Bank timeout");
                }
                Thread.sleep(Duration.ofMillis(50));
                return "BANKREF-" + Math.abs(rnd.nextInt());
            }).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}