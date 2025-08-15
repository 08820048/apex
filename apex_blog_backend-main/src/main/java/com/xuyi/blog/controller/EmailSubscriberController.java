package com.xuyi.blog.controller;

import com.xuyi.blog.dto.ApiResponse;
import com.xuyi.blog.dto.EmailSubscribeRequestDTO;
import com.xuyi.blog.dto.EmailSubscriberDTO;
import com.xuyi.blog.service.EmailSubscriberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 邮箱订阅控制器
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/email-subscribers")
@RequiredArgsConstructor
@Tag(name = "邮箱订阅", description = "邮箱订阅相关接口")
public class EmailSubscriberController {

    private final EmailSubscriberService emailSubscriberService;

    @PostMapping("/subscribe")
    @Operation(summary = "订阅邮箱", description = "订阅博客更新通知")
    public ApiResponse<EmailSubscriberDTO> subscribe(@Valid @RequestBody EmailSubscribeRequestDTO request) {
        EmailSubscriberDTO subscriber = emailSubscriberService.subscribe(request);
        return ApiResponse.success("订阅成功，请查收确认邮件", subscriber);
    }

    @GetMapping("/unsubscribe")
    @Operation(summary = "取消订阅", description = "通过token取消订阅")
    public ApiResponse<Void> unsubscribe(
            @Parameter(description = "取消订阅token") @RequestParam String token) {
        emailSubscriberService.unsubscribe(token);
        return ApiResponse.success("取消订阅成功");
    }

    @DeleteMapping("/unsubscribe")
    @Operation(summary = "根据邮箱取消订阅", description = "根据邮箱地址取消订阅")
    public ApiResponse<Void> unsubscribeByEmail(
            @Parameter(description = "邮箱地址") @RequestParam String email) {
        emailSubscriberService.unsubscribeByEmail(email);
        return ApiResponse.success("取消订阅成功");
    }

    @GetMapping("/check")
    @Operation(summary = "检查订阅状态", description = "检查邮箱是否已订阅")
    public ApiResponse<Boolean> checkSubscription(
            @Parameter(description = "邮箱地址") @RequestParam String email) {
        boolean isSubscribed = emailSubscriberService.isSubscribed(email);
        return ApiResponse.success(isSubscribed);
    }

    @GetMapping("/count")
    @Operation(summary = "获取订阅者数量", description = "获取活跃订阅者数量")
    public ApiResponse<Long> getSubscriberCount() {
        long count = emailSubscriberService.getActiveSubscriberCount();
        return ApiResponse.success(count);
    }
}
