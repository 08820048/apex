package com.xuyi.blog.service;

import com.xuyi.blog.dto.EmailSubscribeRequestDTO;
import com.xuyi.blog.dto.EmailSubscriberDTO;
import com.xuyi.blog.entity.Article;
import com.xuyi.blog.entity.EmailSubscriber;
import com.xuyi.blog.exception.ResourceNotFoundException;
import com.xuyi.blog.repository.EmailSubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 邮箱订阅服务类
 * 
 * @author xuyi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSubscriberService {

    private final EmailSubscriberRepository emailSubscriberRepository;
    private final EmailService emailService;

    /**
     * 订阅邮箱
     */
    @Transactional
    public EmailSubscriberDTO subscribe(EmailSubscribeRequestDTO request) {
        String email = request.getEmail().toLowerCase().trim();
        
        // 检查是否已经订阅
        Optional<EmailSubscriber> existingSubscriber = emailSubscriberRepository.findByEmail(email);
        
        if (existingSubscriber.isPresent()) {
            EmailSubscriber subscriber = existingSubscriber.get();
            if (subscriber.getIsActive()) {
                log.info("邮箱已订阅: {}", email);
                return EmailSubscriberDTO.from(subscriber);
            } else {
                // 重新激活订阅
                subscriber.resubscribe();
                EmailSubscriber savedSubscriber = emailSubscriberRepository.save(subscriber);
                
                // 发送确认邮件
                emailService.sendSubscriptionConfirmationEmail(savedSubscriber);
                
                log.info("重新激活邮箱订阅: {}", email);
                return EmailSubscriberDTO.from(savedSubscriber);
            }
        } else {
            // 新订阅
            EmailSubscriber subscriber = new EmailSubscriber(email);
            EmailSubscriber savedSubscriber = emailSubscriberRepository.save(subscriber);
            
            // 发送确认邮件
            emailService.sendSubscriptionConfirmationEmail(savedSubscriber);
            
            log.info("新增邮箱订阅: {}", email);
            return EmailSubscriberDTO.from(savedSubscriber);
        }
    }

    /**
     * 取消订阅
     */
    @Transactional
    public void unsubscribe(String token) {
        EmailSubscriber subscriber = emailSubscriberRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("无效的取消订阅链接"));
        
        if (subscriber.getIsActive()) {
            subscriber.unsubscribe();
            emailSubscriberRepository.save(subscriber);
            
            // 发送取消订阅确认邮件
            emailService.sendUnsubscribeConfirmationEmail(subscriber.getEmail());
            
            log.info("取消邮箱订阅: {}", subscriber.getEmail());
        }
    }

    /**
     * 根据邮箱取消订阅
     */
    @Transactional
    public void unsubscribeByEmail(String email) {
        Optional<EmailSubscriber> subscriber = emailSubscriberRepository.findByEmail(email.toLowerCase().trim());
        
        if (subscriber.isPresent() && subscriber.get().getIsActive()) {
            subscriber.get().unsubscribe();
            emailSubscriberRepository.save(subscriber.get());
            
            // 发送取消订阅确认邮件
            emailService.sendUnsubscribeConfirmationEmail(email);
            
            log.info("根据邮箱取消订阅: {}", email);
        }
    }

    /**
     * 获取所有活跃订阅者
     */
    public List<EmailSubscriberDTO> getActiveSubscribers() {
        List<EmailSubscriber> subscribers = emailSubscriberRepository.findByIsActiveTrue();
        return subscribers.stream()
                .map(EmailSubscriberDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有订阅者（管理用）
     */
    public List<EmailSubscriberDTO> getAllSubscribers() {
        List<EmailSubscriber> subscribers = emailSubscriberRepository.findAll();
        return subscribers.stream()
                .map(EmailSubscriberDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * 获取订阅者统计
     */
    public long getActiveSubscriberCount() {
        return emailSubscriberRepository.countByIsActiveTrue();
    }

    /**
     * 检查邮箱是否已订阅
     */
    public boolean isSubscribed(String email) {
        return emailSubscriberRepository.existsByEmailAndIsActiveTrue(email.toLowerCase().trim());
    }

    /**
     * 发送新文章通知
     */
    @Transactional(readOnly = true)
    public void notifyNewArticle(Article article) {
        List<EmailSubscriber> activeSubscribers = emailSubscriberRepository.findByIsActiveTrue();
        
        if (!activeSubscribers.isEmpty()) {
            emailService.sendNewArticleNotification(activeSubscribers, article);
            log.info("发送新文章通知: {} -> {} 个订阅者", article.getTitle(), activeSubscribers.size());
        }
    }

    /**
     * 删除订阅者（管理用）
     */
    @Transactional
    public void deleteSubscriber(Long id) {
        EmailSubscriber subscriber = emailSubscriberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("订阅者不存在"));
        
        emailSubscriberRepository.delete(subscriber);
        log.info("删除订阅者: {}", subscriber.getEmail());
    }

    /**
     * 根据ID获取订阅者
     */
    public EmailSubscriberDTO getSubscriberById(Long id) {
        EmailSubscriber subscriber = emailSubscriberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("订阅者不存在"));
        return EmailSubscriberDTO.from(subscriber);
    }
}
