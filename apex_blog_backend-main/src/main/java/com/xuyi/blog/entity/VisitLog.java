package com.xuyi.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 访问统计实体类
 * 
 * @author xuyi
 */
@Entity
@Table(name = "visit_logs", 
    uniqueConstraints = @UniqueConstraint(name = "uk_ip_date", columnNames = {"ipAddress", "visitDate"}),
    indexes = {
        @Index(name = "idx_visit_date", columnList = "visitDate"),
        @Index(name = "idx_ip_address", columnList = "ipAddress")
    }
)
@Getter
@Setter
public class VisitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "IP地址不能为空")
    @Column(name = "ip_address", nullable = false, length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "referer", length = 500)
    private String referer;

    @Column(name = "request_uri", length = 500)
    private String requestUri;

    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @Column(name = "visit_count", nullable = false)
    private Integer visitCount = 1;

    @Column(name = "first_visit_time", nullable = false)
    private LocalDateTime firstVisitTime;

    @Column(name = "last_visit_time", nullable = false)
    private LocalDateTime lastVisitTime;

    public VisitLog() {
        LocalDateTime now = LocalDateTime.now();
        this.visitDate = now.toLocalDate();
        this.firstVisitTime = now;
        this.lastVisitTime = now;
    }

    public VisitLog(String ipAddress) {
        this();
        this.ipAddress = ipAddress;
    }

    public VisitLog(String ipAddress, String userAgent, String referer, String requestUri) {
        this(ipAddress);
        this.userAgent = userAgent;
        this.referer = referer;
        this.requestUri = requestUri;
    }

    /**
     * 增加访问次数
     */
    public void incrementVisitCount() {
        this.visitCount++;
        this.lastVisitTime = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (firstVisitTime == null) {
            firstVisitTime = LocalDateTime.now();
        }
        if (lastVisitTime == null) {
            lastVisitTime = LocalDateTime.now();
        }
        if (visitDate == null) {
            visitDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastVisitTime = LocalDateTime.now();
    }
}
