package com.anonymity.topictalks.models.persists.topic;

import com.anonymity.topictalks.models.persists.audit.DateAudit;
import com.anonymity.topictalks.models.persists.user.UserPO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code UserTopicPO} class represents a user-topic mapping entity in the application.
 * It extends the {@code DateAudit} class, inheriting fields for auditing creation and modification dates.
 *
 * This entity is used to associate users with specific topic children.
 *
 * - {@code userId}: The identifier of the user associated with the topic.
 *
 * - {@code topicChildrenId}: The identifier of the topic children associated with the user.
 *
 * - {@code userInfo}: Represents the user information associated with this user-topic mapping.
 *   It is annotated with {@code @ManyToOne} to establish a many-to-one relationship with the {@code UserPO} class.
 *
 * - {@code topicChildrenInfo}: Represents the topic children information associated with this user-topic mapping.
 *   It is annotated with {@code @ManyToOne} to establish a many-to-one relationship with the {@code TopicChildrenPO} class.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.persists.topic
 * - Created At: 14-09-2023 16:12:28
 * @since 1.0 - version of class
 */
@Data
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_topic")
@IdClass(UserTopicKey.class)
@EqualsAndHashCode(callSuper = false)
public class UserTopicPO extends DateAudit implements Serializable {

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
     * The identifier of the user associated with the topic.
     */
    @Id
    private Long userId;

    /**
     * The identifier of the topic children associated with the user.
     */
    @Id
    private Long topicParentId;

    /**
     * Represents the user information associated with this user-topic mapping.
     * It is annotated with {@code @ManyToOne} to establish a many-to-one relationship with the {@code UserPO} class.
     */
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserPO userInfo;

    /**
     * Represents the topic parent information associated with this user-topic mapping.
     * It is annotated with {@code @ManyToOne} to establish a many-to-one relationship with the {@code TopicParentPO} class.
     */
    @ManyToOne
    @MapsId("topicParentId")
    @JoinColumn(name = "topic_parent_id")
    private TopicParentPO topicParentInfo;

}
