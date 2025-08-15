package com.xuyi.blog.dto;

import com.xuyi.blog.entity.Article;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章DTO
 * 
 * @author xuyi
 */
@Getter
@Setter
public class ArticleDTO {
    
    private Long id;
    private String title;
    private String summary;
    private String content;
    private String coverImage;
    private CategoryDTO category;
    private UserDTO author;
    private String status;
    private Boolean isTop;
    private Long viewCount;


    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TagDTO> tags;

    public ArticleDTO() {}

    public ArticleDTO(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.summary = article.getSummary();
        this.content = article.getContent();
        this.coverImage = article.getCoverImage();
        this.status = article.getStatus().name();
        this.isTop = article.getIsTop();
        this.viewCount = article.getViewCount();


        this.publishedAt = article.getPublishedAt();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        
        if (article.getCategory() != null) {
            this.category = new CategoryDTO(article.getCategory());
        }
        
        if (article.getAuthor() != null) {
            this.author = new UserDTO(article.getAuthor());
        }
        
        if (article.getTags() != null) {
            this.tags = article.getTags().stream()
                    .map(TagDTO::new)
                    .collect(Collectors.toList());
        }
    }

    public static ArticleDTO from(Article article) {
        return new ArticleDTO(article);
    }
}
