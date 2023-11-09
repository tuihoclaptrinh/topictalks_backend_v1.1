package com.anonymity.topictalks.models.persists.topic;

import com.anonymity.topictalks.models.persists.audit.DateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code TopicParentPO} class represents a parent topic entity in the application.
 * It extends the {@code DateAudit} class, inheriting fields for auditing creation and modification dates.
 *
 * - {@code id}: The unique identifier for the parent topic. It is automatically generated and serves as the primary key.
 *
 * - {@code topicParentName}: The name of the parent topic, which cannot be null.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.persists.topic
 * - Created At: 14-09-2023 16:14:32
 * @since 1.0 - version of class
 */


@Data
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "topic_parent")
@EqualsAndHashCode(callSuper = true)
public class TopicParentPO extends DateAudit implements Serializable {

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
     * The unique identifier for the parent topic. It is automatically generated and serves as the primary key.
     */
    @Id
    @Column(name = "topic_parent_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "topic_parent_seq")
    @SequenceGenerator(name = "topic_parent_seq", allocationSize = 1)
    private Long id;

    /**
     * The name of the parent topic, which cannot be null.
     */
    @NotNull
    @Column(name = "topic_parent_name", nullable = false)
    private String topicParentName;

    @NotNull
    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;

}
