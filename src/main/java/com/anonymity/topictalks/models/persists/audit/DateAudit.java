package com.anonymity.topictalks.models.persists.audit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * - The {@code DateAudit} class is a base class for entities that require auditing of creation and modification dates.
 * It is annotated with {@code @MappedSuperclass} to indicate that it is not an entity itself but provides common fields
 * and functionality that can be inherited by other entity classes.
 *
 * - The class is also annotated with {@code @EntityListeners(AuditingEntityListener.class)} to enable automatic
 * auditing of creation and modification dates using Spring Data JPA's auditing feature.
 *
 * - The {@code @JsonIgnoreProperties} annotation is used to specify properties to be ignored during JSON serialization
 * and deserialization. In this case, it ignores the "createdAt" and "updatedAt" properties when generating JSON
 * responses, allowing clients to focus on other relevant data.
 *
 * - This class implements the {@code Serializable} interface to support serialization and deserialization.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.persists.audit
 * - Created At: 14-09-2023 08:53:41
 * @since 1.0 - version of class
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdAt", "updatedAt"},
        allowGetters = true
)
@Getter
@Setter
public class DateAudit implements Serializable {

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
     * The {@code createdAt} field represents the instant when the entity was created. It is annotated with
     * {@code @CreatedDate} to indicate that it should be automatically populated by the JPA provider with the
     * current date and time when an entity is first persisted to the database. It is also annotated with
     * {@code @Column(nullable = false, updatable = false)} to specify that it cannot be null and should not
     * be updated after creation.
     */
    // Represents the instant when the entity was created.
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * The {@code updatedAt} field represents the instant when the entity was last modified. It is annotated with
     * {@code @LastModifiedDate} to indicate that it should be automatically updated by the JPA provider with the
     * current date and time whenever an entity is modified and persisted to the database. It is annotated with
     * {@code @Column(nullable = true)} to allow it to be nullable, as it may not be set initially.
     */
    // Represents the instant when the entity was last modified.
    @LastModifiedDate
    @Column(nullable = true)
    private LocalDateTime updatedAt;

}
