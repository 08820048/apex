package com.xuyi.blog.dto;

import com.xuyi.blog.entity.Portfolio;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 作品集DTO
 * 
 * @author xuyi
 */
@Getter
@Setter
public class PortfolioDTO {
    
    private Long id;
    private String name;
    private String description;
    private String url;
    private String coverImage;
    private String technologies;
    private Integer sortOrder;
    private Boolean isFeatured;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PortfolioDTO() {}

    public PortfolioDTO(Portfolio portfolio) {
        this.id = portfolio.getId();
        this.name = portfolio.getName();
        this.description = portfolio.getDescription();
        this.url = portfolio.getUrl();
        this.coverImage = portfolio.getCoverImage();
        this.technologies = portfolio.getTechnologies();
        this.sortOrder = portfolio.getSortOrder();
        this.isFeatured = portfolio.getIsFeatured();
        this.createdAt = portfolio.getCreatedAt();
        this.updatedAt = portfolio.getUpdatedAt();
    }

    public static PortfolioDTO from(Portfolio portfolio) {
        return new PortfolioDTO(portfolio);
    }
}
