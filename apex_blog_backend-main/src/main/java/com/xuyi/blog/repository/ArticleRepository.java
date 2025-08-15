package com.xuyi.blog.repository;

import com.xuyi.blog.entity.Article;
import com.xuyi.blog.entity.Category;
import com.xuyi.blog.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 文章数据访问接口
 * 
 * @author xuyi
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    /**
     * 查找已发布的文章（分页）
     */
    Page<Article> findByStatusOrderByIsTopDescPublishedAtDesc(Article.ArticleStatus status, Pageable pageable);

    /**
     * 根据分类查找已发布的文章
     */
    Page<Article> findByCategoryAndStatusOrderByPublishedAtDesc(Category category, Article.ArticleStatus status, Pageable pageable);

    /**
     * 根据标签查找已发布的文章
     */
    @Query("SELECT a FROM Article a JOIN a.tags t WHERE t = :tag AND a.status = :status ORDER BY a.publishedAt DESC")
    Page<Article> findByTagAndStatus(@Param("tag") Tag tag, @Param("status") Article.ArticleStatus status, Pageable pageable);

    /**
     * 全文搜索文章（标题和内容）
     */
    @Query("SELECT a FROM Article a WHERE a.status = :status AND (a.title LIKE %:keyword% OR a.content LIKE %:keyword%) ORDER BY a.publishedAt DESC")
    Page<Article> searchByKeyword(@Param("keyword") String keyword, @Param("status") Article.ArticleStatus status, Pageable pageable);

    /**
     * 查找置顶的已发布文章
     */
    List<Article> findByIsTopTrueAndStatusOrderByPublishedAtDesc(Article.ArticleStatus status);

    /**
     * 查找最新的已发布文章
     */
    List<Article> findTop10ByStatusOrderByPublishedAtDesc(Article.ArticleStatus status);

    /**
     * 查找热门文章（按浏览量排序）
     */
    List<Article> findTop10ByStatusOrderByViewCountDesc(Article.ArticleStatus status);

    /**
     * 查找相关文章（同分类）
     */
    @Query("SELECT a FROM Article a WHERE a.category = :category AND a.id != :excludeId AND a.status = :status ORDER BY a.publishedAt DESC")
    List<Article> findRelatedArticles(@Param("category") Category category, @Param("excludeId") Long excludeId, @Param("status") Article.ArticleStatus status, Pageable pageable);

    /**
     * 查找上一篇文章
     */
    @Query("SELECT a FROM Article a WHERE a.publishedAt < :publishedAt AND a.status = :status ORDER BY a.publishedAt DESC")
    List<Article> findPreviousArticle(@Param("publishedAt") LocalDateTime publishedAt, @Param("status") Article.ArticleStatus status, Pageable pageable);

    /**
     * 查找下一篇文章
     */
    @Query("SELECT a FROM Article a WHERE a.publishedAt > :publishedAt AND a.status = :status ORDER BY a.publishedAt ASC")
    List<Article> findNextArticle(@Param("publishedAt") LocalDateTime publishedAt, @Param("status") Article.ArticleStatus status, Pageable pageable);

    /**
     * 增加文章浏览量
     */
    @Modifying
    @Query("UPDATE Article a SET a.viewCount = a.viewCount + 1 WHERE a.id = :id")
    void incrementViewCount(@Param("id") Long id);

    /**
     * 统计已发布文章数量
     */
    long countByStatus(Article.ArticleStatus status);

    /**
     * 统计分类下的文章数量
     */
    long countByCategoryAndStatus(Category category, Article.ArticleStatus status);

    /**
     * 统计标签下的文章数量
     */
    @Query("SELECT COUNT(a) FROM Article a JOIN a.tags t WHERE t = :tag AND a.status = :status")
    long countByTagAndStatus(@Param("tag") Tag tag, @Param("status") Article.ArticleStatus status);

    /**
     * 查找RSS订阅的文章
     */
    List<Article> findTop20ByStatusOrderByPublishedAtDesc(Article.ArticleStatus status);

    /**
     * 查找指定时间范围内的文章
     */
    @Query("SELECT a FROM Article a WHERE a.status = :status AND a.publishedAt BETWEEN :startTime AND :endTime ORDER BY a.publishedAt DESC")
    List<Article> findByPublishedAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("status") Article.ArticleStatus status);
}
