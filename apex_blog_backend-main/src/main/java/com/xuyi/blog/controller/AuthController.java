package com.xuyi.blog.controller;

import com.xuyi.blog.dto.ApiResponse;
import com.xuyi.blog.dto.LoginRequestDTO;
import com.xuyi.blog.dto.LoginResponseDTO;
import com.xuyi.blog.dto.UserDTO;
import com.xuyi.blog.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "用户认证", description = "用户认证相关接口")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录获取JWT token")
    public ApiResponse<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ApiResponse.success("登录成功", response);
    }

    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的信息")
    public ApiResponse<UserDTO> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        UserDTO user = authService.getCurrentUser(username);
        return ApiResponse.success(user);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出（客户端删除token即可）")
    public ApiResponse<Void> logout() {
        // JWT是无状态的，登出只需要客户端删除token即可
        return ApiResponse.success("登出成功");
    }
}
