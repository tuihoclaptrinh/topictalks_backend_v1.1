package com.anonymity.topictalks.models.persists.user;

import com.anonymity.topictalks.models.persists.audit.DateAudit;
import com.anonymity.topictalks.models.persists.message.ParticipantPO;
import com.anonymity.topictalks.models.persists.post.LikePO;
import com.anonymity.topictalks.utils.enums.ERole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * The {@code UserPO} class represents a user entity in the application. It is annotated with {@code @Entity} to
 * indicate that it is a JPA entity that can be persisted in a database.
 * <p>
 * This class extends the {@code DateAudit} class, which provides auditing fields for tracking creation and modification
 * dates. It also implements the {@code UserDetails} interface, making it suitable for use as a Spring Security user
 * details object.
 * <p>
 * The {@code @Table} annotation specifies the name of the database table associated with this entity and defines unique
 * constraints on the "username" and "email" columns.
 * <p>
 * This class is also annotated with {@code @Builder} to provide a builder pattern for creating instances of the class
 * with a concise and readable syntax.
 * <p>
 * Additional annotations like {@code @DynamicInsert}, {@code @DynamicUpdate}, {@code @NoArgsConstructor}, and
 * {@code @AllArgsConstructor} provide further configuration and constructor generation.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.persists.user
 * - Created At: 14-09-2023 12:46:18
 * @see DateAudit
 * @see UserDetails
 * @since 1.0 - version of class
 */

@Data
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
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
     * <p>
     * This field is typically declared as a {@code private static final long} and should be explicitly defined
     * to ensure version compatibility between different implementations of the class.
     */
    // Unique identifier for serial version control.
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The unique identifier for the user.
     * <p>
     * This field is annotated with {@code @Id} to designate it as the primary key of the database table.
     * It is also annotated with {@code @Column} to specify the column name, which is "user_id," and to indicate
     * that the field cannot be null. Additionally, it is annotated with {@code @GeneratedValue} and
     * {@code @SequenceGenerator} to configure automatic generation of unique values for this field using a sequence.
     * The sequence generator is named "user_seq," and it allocates values in increments of 1.
     */
    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", allocationSize = 1)
    private Long id;

    /**
     * The user's full name.
     */
    @Size(max = 150)
    @Column(
            name = "full_name",
            nullable = false
    )
    private String fullName;

    /**
     * The user's last name.
     */
    @Size(max = 100)
    @Column(
            name = "nickname"
    )
    private String nickname;

    @Column(name = "otp", nullable = true)
    private String otp;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean active;

    @Column(name = "otp_generated_time", nullable = true)
    private LocalDateTime otpGeneratedTime;

    /**
     * The user's username, which is unique.
     */
    @NotNull
    @Size(max = 100)
    @Column(
            name = "username",
            unique = true,
            nullable = false
    )
    private String username;

    /**
     * The user's email address, which is unique and serves as a natural identifier.
     */
    @Email
    @Size(max = 100)
    @Column(
            name = "email",
            unique = true,
            nullable = false
    )
    private String email;

    /**
     * The user's password.
     */
    @NotNull
    @Size(max = 255)
    @Column(
            name = "password",
            nullable = false
    )
    private String password;

    /**
     * The user's biography.
     */
    @Column(
            name = "bio",
            nullable = true
    )
    private String bio;

    /**
     * The user's gender.
     */
    @Column(
            name = "gender",
            nullable = true
    )
    private String gender;

    /**
     * The user's phone number.
     */
    @Column(
            name = "phone_number",
            nullable = false
    )
    private String phoneNumber;

    /**
     * The user's country.
     */
    @Column(
            name = "country",
            nullable = true
    )
    private String country;

    /**
     * The URL of the user's profile image.
     */
    @Column(
            name = "image_url",
            nullable = true
    )
    private String imageUrl;

    /**
     * The user's date of birth.
     */
    @Column(
            name = "dob",
            nullable = true
    )
    private LocalDateTime dob;


    /**
     * Indicates whether the user is banned.
     */
    @NotNull
    @Column(
            name = "is_banned",
            nullable = false
    )
    private Boolean isBanned;

    /**
     * The date when the user was banned.
     */
    @Column(
            name = "banned_date",
            nullable = true
    )
    private LocalDateTime bannedDate;

    /**
     * The user's role, represented as an enumeration.
     */
    @Enumerated(EnumType.STRING)
    private ERole role;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<ParticipantPO> participants;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<LikePO> likes;

    /**
     * Get the authorities granted to the user based on their role.
     *
     * @return A collection of granted authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    /**
     * Get the user's password.
     *
     * @return The user's password.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Get the user's username.
     *
     * @return The user's username.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Check if the user's account is non-expired (always returns true).
     *
     * @return True, indicating the account is non-expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Check if the user's account is non-locked (always returns true).
     *
     * @return True, indicating the account is non-locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Check if the user's credentials are non-expired (always returns true).
     *
     * @return True, indicating the credentials are non-expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Check if the user's account is enabled (always returns true).
     *
     * @return True, indicating the account is enabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }


}
