package com.anonymity.topictalks.models.persists.post;

import com.anonymity.topictalks.models.persists.audit.DateAudit;
import jakarta.persistence.*;
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
@Table(name = "reason_reject")
@EqualsAndHashCode(callSuper = false)
public class ReasonRejectPO extends DateAudit implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reason_content")
    private String reasonContent;

}
