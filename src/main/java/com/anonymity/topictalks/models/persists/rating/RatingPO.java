package com.anonymity.topictalks.models.persists.rating;

import com.anonymity.topictalks.models.persists.audit.DateAudit;
import com.anonymity.topictalks.models.persists.message.ConversationPO;
import com.anonymity.topictalks.models.persists.message.ParticipantKey;
import com.anonymity.topictalks.models.persists.topic.TopicChildrenPO;
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
 * @author de140172 - author
 * @version 2.0 - version of software
 * - Package Name: com.anonymity.topictalks.models.persists.rating
 * - Created At: 27-11-2023 16:10:09
 * @since 1.0 - version of class
 */

@Data
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rating")
@EqualsAndHashCode(callSuper = false)
public class RatingPO extends DateAudit implements Serializable {

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

    @EmbeddedId
    private RatingKey id = new RatingKey();

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
    @ManyToOne
    @MapsId("topicChildrenId")
    @JoinColumn(name = "topic_children_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TopicChildrenPO topicChildrenInfo;

    /**
     * Indicates whether this user has been approved by the admin to become a member of the chat group or not.
     */
    @Column(name = "rating")
    private int rating;

}
