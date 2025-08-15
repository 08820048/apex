package com.xuyi.blog.controller.admin;

import com.xuyi.blog.dto.ApiResponse;
import com.xuyi.blog.dto.FriendLinkDTO;
import com.xuyi.blog.dto.FriendLinkRequestDTO;
import com.xuyi.blog.service.FriendLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理后台 - 友链控制器
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/admin/friend-links")
@RequiredArgsConstructor
@Tag(name = "管理后台 - 友链管理", description = "友链管理相关接口")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminFriendLinkController {

    private final FriendLinkService friendLinkService;

    @GetMapping
    @Operation(summary = "获取所有友链", description = "获取所有友链列表")
    public ApiResponse<List<FriendLinkDTO>> getAllFriendLinks() {
        List<FriendLinkDTO> friendLinks = friendLinkService.getAllFriendLinks();
        return ApiResponse.success(friendLinks);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取友链详情", description = "根据ID获取友链详情")
    public ApiResponse<FriendLinkDTO> getFriendLink(
            @Parameter(description = "友链ID") @PathVariable Long id) {
        FriendLinkDTO friendLink = friendLinkService.getFriendLinkById(id);
        return ApiResponse.success(friendLink);
    }

    @PostMapping
    @Operation(summary = "创建友链", description = "创建新友链")
    public ApiResponse<FriendLinkDTO> createFriendLink(@Valid @RequestBody FriendLinkRequestDTO request) {
        FriendLinkDTO friendLink = friendLinkService.createFriendLink(request);
        return ApiResponse.success("友链创建成功", friendLink);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新友链", description = "更新友链信息")
    public ApiResponse<FriendLinkDTO> updateFriendLink(
            @Parameter(description = "友链ID") @PathVariable Long id,
            @Valid @RequestBody FriendLinkRequestDTO request) {
        FriendLinkDTO friendLink = friendLinkService.updateFriendLink(id, request);
        return ApiResponse.success("友链更新成功", friendLink);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除友链", description = "删除指定友链")
    public ApiResponse<Void> deleteFriendLink(
            @Parameter(description = "友链ID") @PathVariable Long id) {
        friendLinkService.deleteFriendLink(id);
        return ApiResponse.success("友链删除成功");
    }

    @PostMapping("/{id}/active")
    @Operation(summary = "设置友链状态", description = "设置友链的激活状态")
    public ApiResponse<Void> setActive(
            @Parameter(description = "友链ID") @PathVariable Long id,
            @Parameter(description = "是否激活") @RequestParam boolean active) {
        friendLinkService.setActive(id, active);
        return ApiResponse.success("友链状态设置成功");
    }
}
