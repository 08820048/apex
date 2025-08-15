package com.xuyi.blog.dto;

import com.xuyi.blog.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 用户DTO
 * 
 * @author xuyi
 */
@Getter
@Setter
public class UserDTO {
    
    private Long id;
    private String username;
    private String email;
    private String avatar;
    private String nickname;
    private String bio;
    private LocalDateTime createdAt;

    public UserDTO() {}

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.avatar = user.getAvatar();
        this.nickname = user.getNickname();
        this.bio = user.getBio();
        this.createdAt = user.getCreatedAt();
    }

    public static UserDTO from(User user) {
        return new UserDTO(user);
    }
}
