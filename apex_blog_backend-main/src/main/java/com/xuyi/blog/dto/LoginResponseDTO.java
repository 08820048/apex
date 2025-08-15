package com.xuyi.blog.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录响应DTO
 * 
 * @author xuyi
 */
@Getter
@Setter
public class LoginResponseDTO {
    
    private String token;
    private String tokenType = "Bearer";
    private UserDTO user;

    public LoginResponseDTO() {}

    public LoginResponseDTO(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }
}
