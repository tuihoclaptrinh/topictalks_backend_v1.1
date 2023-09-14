package com.anonymity.topictalks.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The {@code ERole} enum represents different roles that can be assigned to users in the application.
 * Roles define sets of privileges and access levels that users with those roles possess.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.utils.enums
 * - Created At: 14-09-2023 14:55:45
 * @since 1.0 - version of class
 */
@RequiredArgsConstructor
public enum ERole {

    /**
     * The ADMIN role grants full access with read, write, update, and delete privileges.
     */
    ADMIN(
            Set.of(EPrivilege.READ_PRIVILEGE, EPrivilege.WRITE_PRIVILEGE, EPrivilege.UPDATE_PRIVILEGE, EPrivilege.DELETE_PRIVILEGE)
    ),
    /**
     * The USER role grants read privilege, allowing users to view resources or data.
     */
    USER(
            Set.of(EPrivilege.READ_PRIVILEGE)
    );

    /**
     * A set of privileges associated with the role.
     */
    @Getter
    private final Set<EPrivilege> privileges;

    /**
     * Get a list of authorities granted to users with this role.
     *
     * @return A list of authorities, including role-based authority.
     */
    public List<SimpleGrantedAuthority> getAuthorities(){
        List<SimpleGrantedAuthority> authorities = getPrivileges()
                .stream()
                .map(privilege -> new SimpleGrantedAuthority(privilege.name()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return authorities;
    }

}
