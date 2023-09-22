package com.anonymity.topictalks.models.persists.message;

import com.anonymity.topictalks.models.persists.audit.DateAudit;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code ConversationPO} class represents a conversation entity in the application.
 * It extends the {@code DateAudit} class, inheriting fields for auditing creation and modification dates.
 *
 * - {@code id}: The unique identifier for the conversation. It is automatically generated and serves as the primary key.
 *
 * - {@code chatName}: The name of the chat conversation, which can be nullable.
 *
 * - {@code isGroupChat}: Indicates whether the conversation is a group chat or not.
 *
 * - {@code topicChildren}: Represents the topic within which this conversation is associated.
 *   It is annotated with {@code @ManyToOne} to establish a many-to-one relationship with the {@code TopicChildrenPO} class.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.persists.message
 * - Created At: 14-09-2023 18:07:48
 * @since 1.0 - version of class
 */

@Data
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conversation")
@EqualsAndHashCode(callSuper = true)
public class ConversationPO extends DateAudit implements Serializable {

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
     * The unique identifier for the conversation. It is automatically generated and serves as the primary key.
     */
    @Id
    @Column(name = "conversation_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conversation_seq")
    @SequenceGenerator(name = "conversation_seq", allocationSize = 1)
    private Long id;

    /**
     * The name of the chat conversation, which can be nullable.
     */
    @Column(name = "chat_name", nullable = true)
    private String chatName;

    /**
     * Indicates whether the conversation is a group chat or not.
     */
    @Column(name = "is_group_chat")
    private Boolean isGroupChat;

    /**
     * Represents the topic within which this conversation is associated.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "topic_children_id", nullable = false)
    private TopicChildrenPO topicChildren;

}
