package com.anonymity.topictalks.models.persists.user;

import com.anonymity.topictalks.models.persists.audit.DateAudit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code FriendListPO} class represents a friend list entity in the application. It extends the {@code DateAudit}
 * class, inheriting fields for auditing creation and modification dates.
 *
 * The class is annotated with {@code @Entity} to indicate that it is a JPA entity that can be persisted in a database.
 * It defines the mapping between the class and the database table using {@code @Table}.
 *
 * - {@code id}: The unique identifier for the friend list entry. It is annotated with {@code @Id} and configured to
 *   be generated automatically using a sequence.
 *
 * - {@code userId}: Represents the user who owns the friend list entry. It is annotated with {@code @ManyToOne} to
 *   establish a many-to-one relationship with the {@code UserPO} class.
 *
 * - {@code friendId}: Represents the user who is a friend in the friend list entry. It is also annotated with
 *   {@code @ManyToOne} to establish a many-to-one relationship with the {@code UserPO} class.
 *
 * - {@code isPublic}: Indicates whether the friend list is public or not.
 *
 * - {@code isAccept}: Indicates whether the friend request has been accepted or not.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.persists.user
 * - Created At: 14-09-2023 15:30:39
 * @since 1.0 - version of class
 */

@Data
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Table(name = "friend_list", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "friend_id",
                "user_id"
        })
})
public class FriendListPO extends DateAudit implements Serializable {

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
     * The unique identifier for the friend list entry.
     */
    @Id
    @Column(name = "friend_list_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "friend_list_seq")
    @SequenceGenerator(name = "friend_list_seq", allocationSize = 1)
    private Long id;

    /**
     * Represents the user who owns the friend list entry.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserPO userId;

    /**
     * Represents the user who is a friend in the friend list entry.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "friend_id", nullable = false)
    private UserPO friendId;

    /**
     * Indicates whether the friend list is public or not.
     */
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    /**
     * Indicates whether the friend request has been accepted or not.
     */
    @Column(name = "is_accept", nullable = false)
    private Boolean isAccept;

}
