package com.anonymity.topictalks.models.persists.message;

import com.anonymity.topictalks.models.persists.audit.DateAudit;
import com.anonymity.topictalks.models.persists.user.UserPO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code MessagePO} class represents a message entity in the application.
 * It extends the {@code DateAudit} class, inheriting fields for auditing creation and modification dates.
 *
 * - {@code id}: The unique identifier for the message. It is automatically generated and serves as the primary key.
 *
 * - {@code content}: The content of the message.
 *
 * - {@code senderId}: Represents the user who sent the message.
 *   It is annotated with {@code @ManyToOne} to establish a many-to-one relationship with the {@code UserPO} class.
 *
 * - {@code conversationId}: Represents the conversation to which this message belongs.
 *   It is annotated with {@code @ManyToOne} to establish a many-to-one relationship with the {@code ConversationPO} class.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.persists.message
 * - Created At: 14-09-2023 18:07:04
 * @since 1.0 - version of class
 */


@Data
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
@EqualsAndHashCode(callSuper = true)
public class MessagePO extends DateAudit implements Serializable {

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
     * The unique identifier for the message. It is automatically generated and serves as the primary key.
     */
    @Id
    @Column(name = "message_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
    @SequenceGenerator(name = "message_seq", allocationSize = 1)
    private Long id;

    /**
     * The content of the message.
     */
    @Column(name = "content", length = 1000)
    private String content;

    /**
     * Represents the user who sent the message.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserPO senderId;

    /**
     * Represents the conversation to which this message belongs.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ConversationPO conversationId;

}
