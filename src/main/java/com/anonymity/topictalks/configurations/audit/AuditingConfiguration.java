package com.anonymity.topictalks.configurations.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * The {@code AuditingConfiguration} class is a configuration class that enables Spring Data JPA's auditing feature
 * by using the {@code @EnableJpaAuditing} annotation. It also defines a bean of type {@code AuditorAware<Long>} to
 * provide auditing information for the application.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.configurations
 * - Created At: 14-09-2023 12:32:13
 * @since 1.0 - version of class
 */

@Configuration
@EnableJpaAuditing
public class AuditingConfiguration {

    /**
     * Provides an implementation of {@code AuditorAware<Long>} to determine the current auditor (typically a user's
     * ID) during auditing.
     *
     * @return An instance of {@code SpringSecurityAuditAwareImpl} to resolve the current auditor.
     */
    @Bean
    public AuditorAware<Long> auditorProvider() {
        return new SpringSecurityAuditAwareImpl();
    }

}


