package com.xuyi.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 友链实体类
 * 
 * @author xuyi
 */
@Entity
@Table(name = "friend_links", indexes = {
    @Index(name = "idx_sort_order", columnList = "sortOrder"),
    @Index(name = "idx_is_active", columnList = "isActive")
})
@Getter
@Setter
public class FriendLink extends BaseEntity {

    @NotBlank(message = "友链名称不能为空")
    @Size(max = 50, message = "友链名称长度不能超过50个字符")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @NotBlank(message = "友链地址不能为空")
    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "avatar")
    private String avatar;

    @Size(max = 200, message = "友链描述长度不能超过200个字符")
    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public FriendLink() {}

    public FriendLink(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public FriendLink(String name, String url, String description) {
        this.name = name;
        this.url = url;
        this.description = description;
    }
}
