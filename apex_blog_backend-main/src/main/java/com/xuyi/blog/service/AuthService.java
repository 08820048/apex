package com.xuyi.blog.service;

import com.xuyi.blog.dto.LoginRequestDTO;
import com.xuyi.blog.dto.LoginResponseDTO;
import com.xuyi.blog.dto.UserDTO;
import com.xuyi.blog.entity.User;
import com.xuyi.blog.repository.UserRepository;
import com.xuyi.blog.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务类
 * 
 * @author xuyi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 用户登录
     */
    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            // 验证用户名和密码
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // 获取用户信息
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("用户不存在"));

            // 生成JWT token
            String token = jwtUtil.generateToken(user.getUsername());

            log.info("用户 {} 登录成功", user.getUsername());
            
            return new LoginResponseDTO(token, UserDTO.from(user));
            
        } catch (AuthenticationException e) {
            log.warn("用户 {} 登录失败: {}", request.getUsername(), e.getMessage());
            throw new BadCredentialsException("用户名或密码错误");
        }
    }

    /**
     * 获取当前用户信息
     */
    public UserDTO getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("用户不存在"));
        return UserDTO.from(user);
    }

    /**
     * 验证密码
     */
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 加密密码
     */
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
