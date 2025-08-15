package com.xuyi.blog.dto;

import com.xuyi.blog.entity.EmailSubscriber;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 邮箱订阅者DTO
 * 
 * @author xuyi
 */
@Getter
@Setter
public class EmailSubscriberDTO {
    
    private Long id;
    private String email;
    private Boolean isActive;
    private LocalDateTime subscribedAt;
    private LocalDateTime unsubscribedAt;

    public EmailSubscriberDTO() {}

    public EmailSubscriberDTO(EmailSubscriber subscriber) {
        this.id = subscriber.getId();
        this.email = subscriber.getEmail();
        this.isActive = subscriber.getIsActive();
        this.subscribedAt = subscriber.getSubscribedAt();
        this.unsubscribedAt = subscriber.getUnsubscribedAt();
    }

    public static EmailSubscriberDTO from(EmailSubscriber subscriber) {
        return new EmailSubscriberDTO(subscriber);
    }
}
