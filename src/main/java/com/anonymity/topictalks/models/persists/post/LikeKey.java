package com.anonymity.topictalks.models.persists.post;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * The {@code LikeKey} class represents a composite key used for identifying likes on posts.
 * It is marked as {@code @Embeddable} to indicate that it is embedded within another entity.
 *
 * - {@code userId}: The identifier of the user who liked the post.
 *
 * - {@code postId}: The identifier of the post that was liked.
 *
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.models.persists.post
 * - Created At: 14-09-2023 16:32:44
 * @since 1.0 - version of class
 */

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LikeKey implements Serializable {

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
     * The identifier of the user who liked the post.
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * The identifier of the post that was liked.
     */
    @Column(name = "post_id")
    private Long postId;

}
