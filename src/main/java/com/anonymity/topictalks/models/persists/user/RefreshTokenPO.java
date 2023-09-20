package com.anonymity.topictalks.models.persists.user;

import com.anonymity.topictalks.models.persists.audit.DateAudit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * The {@code RefreshToken} class represents a refresh token entity used in authentication mechanisms.
 *
 * - {@code id}: The unique identifier for the refresh token. It is automatically generated and serves as the primary key.
 *
 * - {@code user}: Represents the user associated with this refresh token. It is annotated with {@code @ManyToOne} to
 *   establish a many-to-one relationship with the {@code User} class.
 *
 * - {@code token}: The refresh token value, which is unique and cannot be null.
 *
 * - {@code expiryDate}: The date and time when the refresh token expires, ensuring its validity.
 *
 * - {@code revoked}: A boolean indicating whether the refresh token has been revoked.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.persists.user
 * - Created At: 14-09-2023 15:36:46
 * @since 1.0 - version of class
 */

@Data
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_token")
@EqualsAndHashCode(callSuper=false)
public class RefreshTokenPO extends DateAudit implements Serializable {

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

    /**
     * The unique identifier for the refresh token. It is automatically generated and serves as the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Represents the user associated with this refresh token.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserPO user;

    /**
     * The refresh token value, which is unique and cannot be null.
     */
    @Column(nullable = false, unique = true)
    private String token;

    /**
     * The date and time when the refresh token expires, ensuring its validity.
     */
    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    /**
     * A boolean indicating whether the refresh token has been revoked.
     */
    public boolean revoked;

}
