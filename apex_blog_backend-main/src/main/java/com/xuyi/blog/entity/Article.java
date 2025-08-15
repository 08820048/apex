package com.xuyi.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 文章实体类
 * 
 * @author xuyi
 */
@Entity
@Table(name = "articles", indexes = {
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_published_at", columnList = "publishedAt"),
    @Index(name = "idx_category_id", columnList = "category_id"),
    @Index(name = "idx_author_id", columnList = "author_id"),
    @Index(name = "idx_is_top", columnList = "isTop"),
    @Index(name = "idx_view_count", columnList = "viewCount")
})
@Getter
@Setter
public class Article extends BaseEntity {

    @NotBlank(message = "文章标题不能为空")
    @Size(max = 200, message = "文章标题长度不能超过200个字符")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @NotBlank(message = "文章内容不能为空")
    @Column(name = "content", nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "cover_image")
    private String coverImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ArticleStatus status = ArticleStatus.DRAFT;

    @Column(name = "is_top", nullable = false)
    private Boolean isTop = false;

    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;





    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "article_tags",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    public enum ArticleStatus {
        DRAFT("草稿"),
        PUBLISHED("已发布"),
        ARCHIVED("已归档");

        private final String description;

        ArticleStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public Article() {}

    public Article(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    /**
     * 添加标签
     */
    public void addTag(Tag tag) {
        tags.add(tag);
    }

    /**
     * 移除标签
     */
    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    /**
     * 增加浏览量
     */
    public void incrementViewCount() {
        this.viewCount++;
    }



    /**
     * 发布文章
     */
    public void publish() {
        this.status = ArticleStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
    }

    /**
     * 归档文章
     */
    public void archive() {
        this.status = ArticleStatus.ARCHIVED;
    }
}
