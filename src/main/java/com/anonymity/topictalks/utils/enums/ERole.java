package com.anonymity.topictalks.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum ERole {

    ADMIN(
            Set.of(EPrivilege.READ_PRIVILEGE, EPrivilege.WRITE_PRIVILEGE, EPrivilege.UPDATE_PRIVILEGE, EPrivilege.DELETE_PRIVILEGE)
    ),
    USER(
            Set.of(EPrivilege.READ_PRIVILEGE)
    );

    @Getter
    private final Set<EPrivilege> privileges;

    public List<SimpleGrantedAuthority> getAuthorities(){
        List<SimpleGrantedAuthority> authorities = getPrivileges()
                .stream()
                .map(privilege -> new SimpleGrantedAuthority(privilege.name()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return authorities;
    }

}
