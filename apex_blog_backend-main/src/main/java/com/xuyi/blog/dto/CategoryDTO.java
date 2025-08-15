package com.xuyi.blog.dto;

import com.xuyi.blog.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 分类DTO
 * 
 * @author xuyi
 */
@Getter
@Setter
public class CategoryDTO {
    
    private Long id;
    private String name;
    private String description;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CategoryDTO() {}

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.sortOrder = category.getSortOrder();
        this.createdAt = category.getCreatedAt();
        this.updatedAt = category.getUpdatedAt();
    }

    public static CategoryDTO from(Category category) {
        return new CategoryDTO(category);
    }
}
