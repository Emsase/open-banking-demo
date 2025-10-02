# Open Banking Demo — Microservice Architecture

This project arose from the need to offering a “personal finance app” or “wallet” to initiate payments with full security, traceability, and resilience—while also meeting regulatory audit requirements.

The need is to:

- Manage bank payment initiation with explicit customer consent. (public API Gateway)
- Perform risk/AML (anti–money laundering, fraud) checks before sending to the bank. (risk-Service)
- Guarantee idempotency (if the client repeats the request, transfers are not duplicated). (payment-Service)
- Record all activity in an immutable log for auditors/regulators. (audit-Service)
- Ensure resilience: controlled retries on bank failures; elastic scaling under traffic spikes. (bank-connector)

## Architecture

![ArchitectureDiagram](docs/ArchitectureDiagram.png)

![InteractionDiagram](docs/InteractionDiagram.png)

OpenAPI Specifications:

- Public Gateway -> [docs/openapi-gateway.yaml](./docs/openapi-gateway.yaml)
- Payment Service -> [docs/openapi-payments.yaml](./docs/openapi-payments.yaml)
- Audit Service -> [docs/openapi-audit.yaml](./docs/openapi-audit.yaml)

## Technical details

**Stack:** Java 21 · Spring Boot 3.3 · Kafka · PostgreSQL · OpenAPI · **Virtual Threads** · Resilience4j · **JWT (HS256)** · Testcontainers · **Lombok**

**Layers (per service):**

- `application` — REST controllers & DTOs
- `service` — business logic (domain models & services)
- `infrastructure` — JPA, Kafka, HTTP clients, Security config

## Design patterns

- **Strategy + Chain of Responsibility** for risk rules (amount threshold, velocity)
- **Factory Method** (`PaymentFactory`) validates & builds `Payment`
- **Repository** mapped to JPA entities
- **Resilience** via Resilience4j (circuit breaker)
- **Idempotency** built into creation flow (unique key)

## Virtual Threads

- **payment-service**: publishes Kafka events via `Executors.newVirtualThreadPerTaskExecutor()`
- **bank-connector**: executes external bank call on a virtual thread with timeout

## JWT (HS256) — Resource Servers

- Protected endpoints:
  - Gateway: `/api/v1/**` (requires `scope=payments:create|payments:read`)
  - Payment: `/payments/**` (requires same)
  - Audit: `/events/**` (requires `scope=audit:read`)
- Secret: `dev-secret-please-change` (demo! move to env/secret manager in real life)
- Ready-to-use token (valid ~30 days):
  ```
  eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJkZW1vLW9wZW5iYW5rIiwic3ViIjoidXNlci0xMjMiLCJhdWQiOiJvcGVuLWJhbmtpbmctZGVtbyIsInNjb3BlIjoicGF5bWVudHM6Y3JlYXRlIHBheW1lbnRzOnJlYWQgYXVkaXQ6cmVhZCIsImV4cCI6MTc2MTg0OTYxMiwiaWF0IjoxNzU5MjU3NjEyfQ.SUsi206Pl5lznEDvkXkWRNuBwlJsT2mDu3uzzFzGhi8
  ```

## Quickstart

```bash
docker compose -f infra/docker-compose.yml up -d
./mvnw -q -T 1C clean package
# Terminal A
./mvnw -pl :payment-service,:payment-risk,:bank-connector,:audit-service spring-boot:run
# Terminal B
./mvnw -pl :api-gateway spring-boot:run
```

### Test the flow (with JWT)

```bash
TOKEN='eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJkZW1vLW9wZW5iYW5rIiwic3ViIjoidXNlci0xMjMiLCJhdWQiOiJvcGVuLWJhbmtpbmctZGVtbyIsInNjb3BlIjoicGF5bWVudHM6Y3JlYXRlIHBheW1lbnRzOnJlYWQgYXVkaXQ6cmVhZCIsImV4cCI6MTc2MTg0OTYxMiwiaWF0IjoxNzU5MjU3NjEyfQ.SUsi206Pl5lznEDvkXkWRNuBwlJsT2mDu3uzzFzGhi8'

# Create payment (idempotent)
curl -X POST http://localhost:8080/api/v1/payments   -H "Authorization: Bearer $TOKEN"   -H "Content-Type: application/json"   -H "Idempotency-Key: 11111111-2222-3333-4444-555555555555"   -d '{"debtorIban":"ES7921000418450200051332","creditorIban":"DE75512108001245126199","amount":"12.34","currency":"EUR"}'

# Get payment
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/v1/payments/{paymentId}

# Audit (needs audit:read)
curl -H "Authorization: Bearer $TOKEN" http://localhost:8084/events
```

## Notes

- Integration tests use **Testcontainers** for Postgres + Kafka.
- Replace HS256 with OAuth2 / JWKS (Keycloak, Auth0…) for production.
