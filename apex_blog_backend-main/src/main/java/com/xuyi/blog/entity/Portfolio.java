package com.xuyi.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 作品集实体类
 * 
 * @author xuyi
 */
@Entity
@Table(name = "portfolios", indexes = {
    @Index(name = "idx_sort_order", columnList = "sortOrder"),
    @Index(name = "idx_is_featured", columnList = "isFeatured")
})
@Getter
@Setter
public class Portfolio extends BaseEntity {

    @NotBlank(message = "作品名称不能为空")
    @Size(max = 100, message = "作品名称长度不能超过100个字符")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "url")
    private String url;

    @Column(name = "cover_image")
    private String coverImage;

    @Size(max = 500, message = "技术栈长度不能超过500个字符")
    @Column(name = "technologies", length = 500)
    private String technologies;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;

    public Portfolio() {}

    public Portfolio(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Portfolio(String name, String description, String url) {
        this.name = name;
        this.description = description;
        this.url = url;
    }
}
