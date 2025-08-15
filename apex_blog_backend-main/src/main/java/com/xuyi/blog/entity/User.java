package com.xuyi.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户实体类
 * 
 * @author xuyi
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_username", columnList = "username")
})
@Getter
@Setter
@ToString(exclude = "password")
public class User extends BaseEntity {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Column(name = "password", nullable = false)
    private String password;

    @Email(message = "邮箱格式不正确")
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "avatar")
    private String avatar;

    @Size(max = 50, message = "昵称长度不能超过50个字符")
    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
