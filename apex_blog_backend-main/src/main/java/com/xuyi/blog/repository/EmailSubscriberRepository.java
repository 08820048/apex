package com.xuyi.blog.repository;

import com.xuyi.blog.entity.EmailSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 邮箱订阅数据访问接口
 * 
 * @author xuyi
 */
@Repository
public interface EmailSubscriberRepository extends JpaRepository<EmailSubscriber, Long> {

    /**
     * 根据邮箱查找订阅者
     */
    Optional<EmailSubscriber> findByEmail(String email);

    /**
     * 根据token查找订阅者
     */
    Optional<EmailSubscriber> findByToken(String token);

    /**
     * 检查邮箱是否已订阅
     */
    boolean existsByEmailAndIsActiveTrue(String email);

    /**
     * 查找所有活跃的订阅者
     */
    List<EmailSubscriber> findByIsActiveTrue();

    /**
     * 统计活跃订阅者数量
     */
    long countByIsActiveTrue();
}
