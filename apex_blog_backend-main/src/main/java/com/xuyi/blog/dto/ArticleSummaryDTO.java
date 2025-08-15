package com.xuyi.blog.dto;

import com.xuyi.blog.entity.Article;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章摘要DTO（用于列表显示，不包含完整内容）
 * 
 * @author xuyi
 */
@Getter
@Setter
public class ArticleSummaryDTO {
    
    private Long id;
    private String title;
    private String summary;
    private String coverImage;
    private CategoryDTO category;
    private String authorName;
    private String status;
    private Boolean isTop;
    private Long viewCount;


    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private List<TagDTO> tags;

    public ArticleSummaryDTO() {}

    public ArticleSummaryDTO(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.summary = article.getSummary();
        this.coverImage = article.getCoverImage();
        this.status = article.getStatus().name();
        this.isTop = article.getIsTop();
        this.viewCount = article.getViewCount();


        this.publishedAt = article.getPublishedAt();
        this.createdAt = article.getCreatedAt();
        
        if (article.getCategory() != null) {
            this.category = new CategoryDTO(article.getCategory());
        }
        
        if (article.getAuthor() != null) {
            this.authorName = article.getAuthor().getNickname() != null ? 
                article.getAuthor().getNickname() : article.getAuthor().getUsername();
        }
        
        if (article.getTags() != null) {
            this.tags = article.getTags().stream()
                    .map(TagDTO::new)
                    .collect(Collectors.toList());
        }
    }

    public static ArticleSummaryDTO from(Article article) {
        return new ArticleSummaryDTO(article);
    }
}
