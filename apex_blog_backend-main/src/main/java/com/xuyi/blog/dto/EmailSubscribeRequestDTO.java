package com.xuyi.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 邮箱订阅请求DTO
 * 
 * @author xuyi
 */
@Getter
@Setter
public class EmailSubscribeRequestDTO {
    
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    public EmailSubscribeRequestDTO() {}

    public EmailSubscribeRequestDTO(String email) {
        this.email = email;
    }
}
