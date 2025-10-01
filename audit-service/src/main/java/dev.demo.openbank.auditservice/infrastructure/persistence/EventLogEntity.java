package dev.demo.openbank.auditservice.infrastructure.persistence;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "event_log")
@Getter
@Setter
public class EventLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String topic;
    String keyRef;
    Instant createdAt = Instant.now();
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    Map<String, Object> payload;
}