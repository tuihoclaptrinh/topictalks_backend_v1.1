package com.anonymity.topictalks.models.persists.notification;

import com.anonymity.topictalks.models.persists.audit.DateAudit;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.MessagePO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import jakarta.persistence.*;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code MessageNotificationPO} class represents a message notification entity in the application.
 * It extends the {@code DateAudit} class, inheriting fields for auditing creation and modification dates.
 *
 * - {@code id}: The unique identifier for the message notification.
 *
 * - {@code userId}: Represents the user to whom the notification is associated.
 *   It is annotated with {@code @ManyToOne} to establish a many-to-one relationship with the {@code UserPO} class.
 *
 * - {@code messageId}: Represents the message for which the notification is generated.
 *   It is annotated with {@code @ManyToOne} to establish a many-to-one relationship with the {@code MessagePO} class.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.persists.notification
 * - Created At: 14-09-2023 18:05:25
 * @since 1.0 - version of class
 */
@Data
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message_notification")
@EqualsAndHashCode(callSuper = false)
public class MessageNotificationPO extends DateAudit implements Serializable {

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
     * The unique identifier for the message notification.
     */
    @Id
    @Column(name = "message_notification_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_notification_seq")
    @SequenceGenerator(name = "message_notification_seq", allocationSize = 1)
    private Long id;

    /**
     * Represents the user to whom the notification is associated.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserPO userId;

    @NotNull
    private Long partnerId;

    /**
     * Represents the message for which the notification is generated.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "conversation_id",
            referencedColumnName = "conversation_id",
            foreignKey = @ForeignKey(name = "FKzqwe9h5n8tc94ojsislgnb2de",
                    foreignKeyDefinition = "FOREIGN KEY (conversation_id) REFERENCES conversation (conversation_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private ConversationPO conversationId;

    @Column(name = "is_read", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isRead;

    @NotNull
    @Column(name = "message_noti")
    private String messageNoti;

}
