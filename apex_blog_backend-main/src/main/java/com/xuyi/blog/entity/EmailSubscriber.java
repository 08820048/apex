package com.xuyi.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 邮箱订阅实体类
 * 
 * @author xuyi
 */
@Entity
@Table(name = "email_subscribers", indexes = {
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_token", columnList = "token"),
    @Index(name = "idx_is_active", columnList = "isActive")
})
@Getter
@Setter
public class EmailSubscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "subscribed_at", nullable = false)
    private LocalDateTime subscribedAt;

    @Column(name = "unsubscribed_at")
    private LocalDateTime unsubscribedAt;

    public EmailSubscriber() {
        this.subscribedAt = LocalDateTime.now();
        this.token = UUID.randomUUID().toString();
    }

    public EmailSubscriber(String email) {
        this();
        this.email = email;
    }

    /**
     * 取消订阅
     */
    public void unsubscribe() {
        this.isActive = false;
        this.unsubscribedAt = LocalDateTime.now();
    }

    /**
     * 重新订阅
     */
    public void resubscribe() {
        this.isActive = true;
        this.unsubscribedAt = null;
        this.subscribedAt = LocalDateTime.now();
        this.token = UUID.randomUUID().toString();
    }

    @PrePersist
    protected void onCreate() {
        if (subscribedAt == null) {
            subscribedAt = LocalDateTime.now();
        }
        if (token == null) {
            token = UUID.randomUUID().toString();
        }
    }
}
