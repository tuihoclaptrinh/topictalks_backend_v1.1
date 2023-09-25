package com.anonymity.topictalks.models.persists.topic;

import com.anonymity.topictalks.models.persists.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code TopicChildrenPO} class represents a child topic entity in the application.
 * It extends the {@code DateAudit} class, inheriting fields for auditing creation and modification dates.
 *
 * - {@code id}: The unique identifier for the child topic. It is automatically generated and serves as the primary key.
 *
 * - {@code topicChildrenName}: The name of the child topic.
 *
 * - {@code topicParentId}: Represents the parent topic associated with this child topic.
 *   It is annotated with {@code @ManyToOne} to establish a many-to-one relationship with the {@code TopicParentPO} class.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.persists.topic
 * - Created At: 14-09-2023 16:12:39
 * @since 1.0 - version of class
 */

@Data
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "topic_children")
@EqualsAndHashCode(callSuper=false)
public class TopicChildrenPO extends DateAudit implements Serializable {

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
     * The unique identifier for the child topic. It is automatically generated and serves as the primary key.
     */
    @Id
    @Column(name = "topic_children_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "topic_children_seq")
    @SequenceGenerator(name = "topic_children_seq", allocationSize = 1)
    private Long id;

    /**
     * The name of the child topic.
     */
    @Column(name = "topic_children_name")
    private String topicChildrenName;

    /**
     * Represents the parent topic associated with this child topic.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_parent_id", nullable = false)
    @JsonIgnore
    private TopicParentPO topicParentId;

    /**
     * An optional image associated with the children topic.
     */
    @Lob
    @Column(name = "image", columnDefinition = "LONGTEXT", nullable = false)
    private String image;
}
