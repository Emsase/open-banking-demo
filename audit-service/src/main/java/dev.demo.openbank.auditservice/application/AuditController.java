package dev.demo.openbank.auditservice.application;

import dev.demo.openbank.auditservice.service.AuditService;
import dev.demo.openbank.auditservice.service.model.AuditRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService svc;

    @PreAuthorize("hasAuthority('SCOPE_audit:read')")
    @GetMapping
    public java.util.List<AuditRecord> all() {
        return svc.all();
    }
}