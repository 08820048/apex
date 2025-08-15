package com.xuyi.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 标签实体类
 * 
 * @author xuyi
 */
@Entity
@Table(name = "tags", indexes = {
    @Index(name = "idx_name", columnList = "name")
})
@Getter
@Setter
public class Tag extends BaseEntity {

    @NotBlank(message = "标签名称不能为空")
    @Size(max = 30, message = "标签名称长度不能超过30个字符")
    @Column(name = "name", nullable = false, unique = true, length = 30)
    private String name;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "颜色格式不正确")
    @Column(name = "color", length = 7)
    private String color = "#007bff";

    public Tag() {}

    public Tag(String name) {
        this.name = name;
    }

    public Tag(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
