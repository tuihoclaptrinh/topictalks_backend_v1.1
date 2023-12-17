package com.anonymity.topictalks.models.persists.user;

import com.anonymity.topictalks.models.persists.audit.DateAudit;
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
@Table(name = "interact")
@EqualsAndHashCode(callSuper = true)
public class InteractPO  extends DateAudit implements Serializable {
    // Unique identifier for serial version control.
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "interact_seq")
    @SequenceGenerator(name = "interact_seq", allocationSize = 1)
    private Long id;

    @NotNull
    private String subject;

    @Lob
    @NotNull
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserPO senderId;

    @Lob
    @Column(name = "answer_content", columnDefinition = "LONGTEXT",nullable = true)
    private String replyContent;

    @Lob
    @Column(name = "evd_img_url", columnDefinition = "LONGTEXT",nullable = true)
    private String evdImageUrl;

    @Column(name = "adminId",nullable = true)
    private long adminReplyId;

    @Column(name = "is_answered",nullable = false, columnDefinition = "boolean default false")
    private boolean isAnswered;

}
