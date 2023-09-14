package com.anonymity.topictalks.models.persists.message;

import com.anonymity.topictalks.models.persists.audit.DateAudit;
import com.anonymity.topictalks.models.persists.user.UserPO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code ParticipantPO} class represents a participant entity in the application.
 * It extends the {@code DateAudit} class, inheriting fields for auditing creation and modification dates.
 *
 * - {@code userId}: The identifier of the user who is a participant in the conversation.
 *
 * - {@code conversationId}: The identifier of the conversation in which the user is a participant.
 *
 * - {@code userInfo}: Represents the user who is a participant in the conversation.
 *   It is annotated with {@code @ManyToOne} to establish a many-to-one relationship with the {@code UserPO} class.
 *
 * - {@code conversationInfo}: Represents the conversation to which the participant belongs.
 *   It is annotated with {@code @ManyToOne} to establish a many-to-one relationship with the {@code ConversationPO} class.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.persists.message
 * - Created At: 14-09-2023 18:08:42
 * @since 1.0 - version of class
 */

@Data
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "participant")
@IdClass(ParticipantKey.class)
@EqualsAndHashCode(callSuper = false)
public class ParticipantPO extends DateAudit implements Serializable {

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
     * The identifier of the user who is a participant in the conversation.
     */
    @Id
    private Long userId;

    /**
     * The identifier of the conversation in which the user is a participant.
     */
    @Id
    private Long conversationId;

    /**
     * Represents the user who is a participant in the conversation.
     */
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserPO userInfo;

    /**
     * Represents the conversation to which the participant belongs.
     */
    @ManyToOne(cascade = CascadeType.REMOVE)
    @MapsId("conversationId")
    @JoinColumn(name = "conversation_id")
    private ConversationPO conversationInfo;

}
