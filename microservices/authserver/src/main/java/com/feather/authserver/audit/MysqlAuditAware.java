package com.feather.authserver.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class MysqlAuditAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        // if (authentication != null) {
        // authentication.
        // }
        return Optional.of("System-User");
    }

}
