package com.anonymity.topictalks.models.persists.post;

import com.anonymity.topictalks.models.persists.audit.DateAudit;
import com.anonymity.topictalks.models.persists.topic.TopicParentPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code PostPO} class represents a post entity in the application.
 * It extends the {@code DateAudit} class, inheriting fields for auditing creation and modification dates.
 *
 * - {@code id}: The unique identifier for the post. It is automatically generated and serves as the primary key.
 *
 * - {@code authorId}: Represents the user who authored this post.
 *   It is annotated with {@code @ManyToOne} to establish a many-to-one relationship with the {@code UserPO} class.
 *
 * - {@code topicParentId}: Represents the parent topic associated with this post.
 *   It is annotated with {@code @ManyToOne} to establish a many-to-one relationship with the {@code TopicParentPO} class.
 *
 * - {@code title}: The title of the post, which cannot be null.
 *
 * - {@code isApproved}: Indicates whether the post is approved or not.
 *
 * - {@code content}: The content of the post, which is stored as a large text.
 *
 * - {@code image}: An optional image associated with the post.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.persists.post
 * - Created At: 14-09-2023 16:33:07
 * @since 1.0 - version of class
 */


@Data
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
@EqualsAndHashCode(callSuper = true)
public class PostPO extends DateAudit implements Serializable {

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
     * The unique identifier for the post. It is automatically generated and serves as the primary key.
     */
    @Id
    @Column(name = "post_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq")
    @SequenceGenerator(name = "post_seq", allocationSize = 1)
    private Long id;

    /**
     * Represents the user who authored this post.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private UserPO authorId;

    /**
     * Represents the parent topic associated with this post.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "topic_parent_id", nullable = false)
    private TopicParentPO topicParentId;

    /**
     * The title of the post, which cannot be null.
     */
    @NotNull
    private String title;

    /**
     * Indicates whether the post is approved or not.
     */
    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved;

    /**
     * The content of the post, which is stored as a large text.
     */
    @Lob
    @NotNull
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    /**
     * An optional image associated with the post.
     */
    @Column(name = "image", nullable = true)
    private String image;

}
