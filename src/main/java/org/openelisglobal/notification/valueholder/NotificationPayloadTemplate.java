package org.openelisglobal.notification.valueholder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.openelisglobal.common.valueholder.BaseObject;

@Entity
@Table(name = "notification_payload_template")
public class NotificationPayloadTemplate extends BaseObject<Integer> {

    public enum NotificationPayloadType {
        CLIENT_RESULTS
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_payload_template_generator")
    @SequenceGenerator(name = "notification_payload_template_generator", sequenceName = "notification_payload_template_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "message_template")
    private String messageTemplate;

    @Column(name = "subject_template")
    private String subjectTemplate;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private NotificationPayloadType type;

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getSubjectTemplate() {
        return subjectTemplate;
    }

    public void setSubjectTemplate(String subjectTemplate) {
        this.subjectTemplate = subjectTemplate;
    }

    public NotificationPayloadType getType() {
        return type;
    }

    public void setType(NotificationPayloadType type) {
        this.type = type;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

}
