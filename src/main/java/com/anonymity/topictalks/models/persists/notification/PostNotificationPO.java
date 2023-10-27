package com.anonymity.topictalks.models.persists.notification;

import com.anonymity.topictalks.models.persists.audit.DateAudit;
import com.anonymity.topictalks.models.persists.message.MessagePO;
import com.anonymity.topictalks.models.persists.post.PostPO;
import com.anonymity.topictalks.models.persists.user.UserPO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post_notification")
@EqualsAndHashCode(callSuper = false)
public class PostNotificationPO extends DateAudit implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "post_notification_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_notification_seq")
    @SequenceGenerator(name = "post_notification_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserPO userId;

    @NotNull
    @Column(name = "partner_id")
    private Long partnerId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "post_id",
            referencedColumnName = "post_id",
            foreignKey = @ForeignKey(name = "FKdvsdvsdvsvsvdvsdvsdvsdvsd",
                    foreignKeyDefinition = "FOREIGN KEY (post_id) REFERENCES post (post_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private PostPO postId;

    @Column(name = "is_read", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isRead;

    @NotNull
    @Column(name = "message_noti")
    private String messageNoti;
}
