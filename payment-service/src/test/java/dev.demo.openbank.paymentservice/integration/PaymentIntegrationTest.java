package dev.demo.openbank.paymentservice.integration;

import dev.demo.openbank.paymentservice.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = Application.class)
class PaymentIntegrationTest {

    static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"));
    static KafkaContainer k = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));

    static {
        pg.start();
        k.start();
    }

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", pg::getJdbcUrl);
        r.add("spring.datasource.username", pg::getUsername);
        r.add("spring.datasource.password", pg::getPassword);
        r.add("spring.kafka.bootstrap-servers", k::getBootstrapServers);
    }

    @Test
    void contextLoads() {
    }
}