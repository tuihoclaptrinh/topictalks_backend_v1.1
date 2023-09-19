package com.anonymity.topictalks.configs.audit;

import com.anonymity.topictalks.models.persists.user.UserPO;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * The {@code SpringSecurityAuditAwareImpl} class implements the {@code AuditorAware<Long>} interface to determine
 * the current auditor (typically a user's ID) during auditing operations.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.configurations.audit
 * - Created At: 14-09-2023 12:44:28
 * @since 1.0 - version of class
 */
public class SpringSecurityAuditAwareImpl implements AuditorAware<Long> {

    /**
     * Retrieves the current auditor's ID from the Spring Security context, if available.
     *
     * @return An {@code Optional} containing the ID of the current auditor, or {@code Optional.empty()} if no auditor
     *         is available (e.g., if the user is not authenticated or is an anonymous user).
     */
    @Override
    public Optional<Long> getCurrentAuditor() {
        // Get the current authentication context from Spring Security.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is null, not authenticated, or represents an anonymous user.
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty(); // No auditor information available.
        }

        // Extract the user principal from the authentication context.
        UserPO userPrincipal = (UserPO) authentication.getPrincipal();

        // Retrieve and return the ID of the current auditor (user).
        return Optional.ofNullable(userPrincipal.getId());
    }
}
