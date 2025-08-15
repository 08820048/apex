package com.xuyi.blog.repository;

import com.xuyi.blog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 标签数据访问接口
 * 
 * @author xuyi
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * 根据名称查找标签
     */
    Optional<Tag> findByName(String name);

    /**
     * 检查标签名称是否存在
     */
    boolean existsByName(String name);

    /**
     * 查找热门标签（按文章数量排序）
     */
    @Query("SELECT t FROM Tag t JOIN Article a ON t MEMBER OF a.tags WHERE a.status = 'PUBLISHED' GROUP BY t ORDER BY COUNT(a) DESC")
    List<Tag> findPopularTags();

    /**
     * 根据名称列表查找标签
     */
    List<Tag> findByNameIn(List<String> names);
}
