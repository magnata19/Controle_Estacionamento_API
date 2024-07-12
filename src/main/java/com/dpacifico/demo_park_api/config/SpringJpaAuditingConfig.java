package com.dpacifico.demo_park_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class SpringJpaAuditingConfig implements AuditorAware<String> {//o tipo do auditor é o campo q vamos auditoriar, no caso username, se fosse id seria long etc

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            return Optional.of(authentication.getName());
        }
        return null;
    }

}
