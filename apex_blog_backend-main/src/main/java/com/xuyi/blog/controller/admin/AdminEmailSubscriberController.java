package com.xuyi.blog.controller.admin;

import com.xuyi.blog.dto.ApiResponse;
import com.xuyi.blog.dto.EmailSubscriberDTO;
import com.xuyi.blog.service.EmailSubscriberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理后台 - 邮箱订阅控制器
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/admin/email-subscribers")
@RequiredArgsConstructor
@Tag(name = "管理后台 - 邮箱订阅管理", description = "邮箱订阅管理相关接口")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminEmailSubscriberController {

    private final EmailSubscriberService emailSubscriberService;

    @GetMapping
    @Operation(summary = "获取所有订阅者", description = "获取所有邮箱订阅者列表")
    public ApiResponse<List<EmailSubscriberDTO>> getAllSubscribers() {
        List<EmailSubscriberDTO> subscribers = emailSubscriberService.getAllSubscribers();
        return ApiResponse.success(subscribers);
    }

    @GetMapping("/active")
    @Operation(summary = "获取活跃订阅者", description = "获取所有活跃的邮箱订阅者")
    public ApiResponse<List<EmailSubscriberDTO>> getActiveSubscribers() {
        List<EmailSubscriberDTO> subscribers = emailSubscriberService.getActiveSubscribers();
        return ApiResponse.success(subscribers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取订阅者详情", description = "根据ID获取订阅者详情")
    public ApiResponse<EmailSubscriberDTO> getSubscriber(
            @Parameter(description = "订阅者ID") @PathVariable Long id) {
        EmailSubscriberDTO subscriber = emailSubscriberService.getSubscriberById(id);
        return ApiResponse.success(subscriber);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除订阅者", description = "删除指定订阅者")
    public ApiResponse<Void> deleteSubscriber(
            @Parameter(description = "订阅者ID") @PathVariable Long id) {
        emailSubscriberService.deleteSubscriber(id);
        return ApiResponse.success("订阅者删除成功");
    }

    @GetMapping("/stats")
    @Operation(summary = "获取订阅统计", description = "获取邮箱订阅统计信息")
    public ApiResponse<Long> getSubscriberStats() {
        long count = emailSubscriberService.getActiveSubscriberCount();
        return ApiResponse.success(count);
    }
}
