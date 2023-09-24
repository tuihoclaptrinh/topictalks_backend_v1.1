package com.anonymity.topictalks.models.persists.message;

import com.anonymity.topictalks.models.persists.audit.DateAudit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Data
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MessageDemoPO")
@EqualsAndHashCode(callSuper = true)
public class MessageDemoPO extends DateAudit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    
    private String room;

    private String username;

}
