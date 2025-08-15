package com.xuyi.blog.repository;

import com.xuyi.blog.entity.FriendLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 友链数据访问接口
 * 
 * @author xuyi
 */
@Repository
public interface FriendLinkRepository extends JpaRepository<FriendLink, Long> {

    /**
     * 查找活跃的友链
     */
    List<FriendLink> findByIsActiveTrueOrderBySortOrderAsc();

    /**
     * 按排序顺序查找所有友链
     */
    List<FriendLink> findAllByOrderBySortOrderAsc();
}
