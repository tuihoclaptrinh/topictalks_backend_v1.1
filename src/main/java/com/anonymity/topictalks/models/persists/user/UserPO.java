package com.anonymity.topictalks.models.persists.user;

import com.anonymity.topictalks.models.persists.audit.DateAudit;
import com.anonymity.topictalks.utils.enums.ERole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;

/**
 * The {@code UserPO} class represents a user entity in the application. It is annotated with {@code @Entity} to
 * indicate that it is a JPA entity that can be persisted in a database.
 *
 * This class extends the {@code DateAudit} class, which provides auditing fields for tracking creation and modification
 * dates. It also implements the {@code UserDetails} interface, making it suitable for use as a Spring Security user
 * details object.
 *
 * The {@code @Table} annotation specifies the name of the database table associated with this entity and defines unique
 * constraints on the "username" and "email" columns.
 *
 * This class is also annotated with {@code @Builder} to provide a builder pattern for creating instances of the class
 * with a concise and readable syntax.
 *
 * Additional annotations like {@code @DynamicInsert}, {@code @DynamicUpdate}, {@code @NoArgsConstructor}, and
 * {@code @AllArgsConstructor} provide further configuration and constructor generation.
 *
 * @see DateAudit
 * @see UserDetails
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.persists.user
 * - Created At: 14-09-2023 12:46:18
 * @since 1.0 - version of class
 */

@Data
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class UserPO extends DateAudit implements UserDetails, Serializable {

    /**
     * The {@code serialVersionUID} is a unique identifier for a serializable class. It is used during deserialization
     * to verify that the sender and receiver of a serialized object have loaded classes for that object that are compatible
     * with respect to serialization. If the receiver has loaded a class for the object that has a different
     * {@code serialVersionUID} than the corresponding class on the sender's side, then deserialization will result in
     * an {@code InvalidClassException}.
     *
     * This field is typically declared as a {@code private static final long} and should be explicitly defined
     * to ensure version compatibility between different implementations of the class.
     */
    // Unique identifier for serial version control.
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Size(max = 15)
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Email
    @NotNull
    @NaturalId
    @Size(max = 40)
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotNull
    @Size(max = 100)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "bio", nullable = true)
    private String bio;

    @Column(name = "gender", nullable = true)
    private boolean gender;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "country", nullable = true)
    private String country;

    @Column(name = "image_url", nullable = true)
    private String imageUrl;

    @Column(name = "dob", nullable = true)
    private Instant dob;

    @NotNull
    @Column(name = "is_banned", nullable = false)
    private Boolean isBanned;

    @Column(name = "banned_date", nullable = true)
    private Instant bannedDate;

    @Enumerated(EnumType.STRING)
    private ERole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
