package com.xuyi.blog.controller;

import com.xuyi.blog.dto.ApiResponse;
import com.xuyi.blog.dto.FriendLinkDTO;
import com.xuyi.blog.service.FriendLinkService;
import com.xuyi.blog.service.QueryStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 友链控制器（前台）
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/friend-links")
@RequiredArgsConstructor
@Tag(name = "友链管理", description = "友链相关接口")
public class FriendLinkController {

    private final FriendLinkService friendLinkService;
    private final QueryStatsService queryStatsService;

    @GetMapping
    @Operation(summary = "获取友链列表", description = "获取所有活跃的友链列表")
    public ApiResponse<List<FriendLinkDTO>> getFriendLinks(
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        List<FriendLinkDTO> friendLinks = friendLinkService.getActiveFriendLinks();
        return includeStats ?
            ApiResponse.successWithStats(friendLinks, queryStatsService.finishRequestStats()) :
            ApiResponse.success(friendLinks);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取友链详情", description = "根据ID获取友链详情")
    public ApiResponse<FriendLinkDTO> getFriendLink(
            @Parameter(description = "友链ID") @PathVariable Long id,
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        FriendLinkDTO friendLink = friendLinkService.getFriendLinkById(id);
        return includeStats ?
            ApiResponse.successWithStats(friendLink, queryStatsService.finishRequestStats()) :
            ApiResponse.success(friendLink);
    }
}
