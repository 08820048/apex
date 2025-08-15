package com.xuyi.blog.dto;

import com.xuyi.blog.entity.Tag;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 标签DTO
 * 
 * @author xuyi
 */
@Getter
@Setter
public class TagDTO {
    
    private Long id;
    private String name;
    private String color;
    private LocalDateTime createdAt;

    public TagDTO() {}

    public TagDTO(Tag tag) {
        this.id = tag.getId();
        this.name = tag.getName();
        this.color = tag.getColor();
        this.createdAt = tag.getCreatedAt();
    }

    public static TagDTO from(Tag tag) {
        return new TagDTO(tag);
    }
}
