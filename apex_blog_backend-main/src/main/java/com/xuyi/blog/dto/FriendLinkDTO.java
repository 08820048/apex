package com.xuyi.blog.dto;

import com.xuyi.blog.entity.FriendLink;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 友链DTO
 * 
 * @author xuyi
 */
@Getter
@Setter
public class FriendLinkDTO {
    
    private Long id;
    private String name;
    private String url;
    private String avatar;
    private String description;
    private Integer sortOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FriendLinkDTO() {}

    public FriendLinkDTO(FriendLink friendLink) {
        this.id = friendLink.getId();
        this.name = friendLink.getName();
        this.url = friendLink.getUrl();
        this.avatar = friendLink.getAvatar();
        this.description = friendLink.getDescription();
        this.sortOrder = friendLink.getSortOrder();
        this.isActive = friendLink.getIsActive();
        this.createdAt = friendLink.getCreatedAt();
        this.updatedAt = friendLink.getUpdatedAt();
    }

    public static FriendLinkDTO from(FriendLink friendLink) {
        return new FriendLinkDTO(friendLink);
    }
}
