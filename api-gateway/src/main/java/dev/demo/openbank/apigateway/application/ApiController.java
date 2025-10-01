package dev.demo.openbank.apigateway.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiController {

    private final WebClient client = WebClient.create("http://localhost:8081");

    @PreAuthorize("hasAuthority('SCOPE_payments:create')")
    @PostMapping(path = "/payments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> create(@RequestBody Map<String, Object> body,
            @RequestHeader(value = "Idempotency-Key", required = false) String idem,
            @RequestHeader(value = "Authorization", required = false) String auth) {
        return client.post().uri("/payments").header("Idempotency-Key", idem == null ? "" : idem)
                .header("Authorization", auth == null ? "" : auth)
                .contentType(MediaType.APPLICATION_JSON).bodyValue(body).retrieve().bodyToMono(Map.class).block();
    }

    @PreAuthorize("hasAuthority('SCOPE_payments:read')")
    @GetMapping("/payments/{id}")
    public Map<String, Object> get(@PathVariable String id,
            @RequestHeader(value = "Authorization", required = false) String auth) {
        return client.get().uri("/payments/{id}", id).header("Authorization", auth == null ? "" : auth)
                .retrieve().bodyToMono(Map.class).block();
    }
}
