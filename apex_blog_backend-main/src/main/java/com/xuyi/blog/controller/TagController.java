package com.xuyi.blog.controller;

import com.xuyi.blog.dto.ApiResponse;
import com.xuyi.blog.dto.TagDTO;
import com.xuyi.blog.service.TagService;
import com.xuyi.blog.service.QueryStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签控制器（前台）
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
@Tag(name = "标签管理", description = "标签相关接口")
public class TagController {

    private final TagService tagService;
    private final QueryStatsService queryStatsService;

    @GetMapping
    @Operation(summary = "获取所有标签", description = "获取所有标签列表")
    public ApiResponse<List<TagDTO>> getAllTags(
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        List<TagDTO> tags = tagService.getAllTags();
        return includeStats ?
            ApiResponse.successWithStats(tags, queryStatsService.finishRequestStats()) :
            ApiResponse.success(tags);
    }

    @GetMapping("/popular")
    @Operation(summary = "获取热门标签", description = "获取热门标签列表")
    public ApiResponse<List<TagDTO>> getPopularTags(
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        List<TagDTO> tags = tagService.getPopularTags();
        return includeStats ?
            ApiResponse.successWithStats(tags, queryStatsService.finishRequestStats()) :
            ApiResponse.success(tags);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取标签详情", description = "根据ID获取标签详情")
    public ApiResponse<TagDTO> getTag(
            @Parameter(description = "标签ID") @PathVariable Long id,
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        TagDTO tag = tagService.getTagById(id);
        return includeStats ?
            ApiResponse.successWithStats(tag, queryStatsService.finishRequestStats()) :
            ApiResponse.success(tag);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "根据名称获取标签", description = "根据名称获取标签信息")
    public ApiResponse<TagDTO> getTagByName(
            @Parameter(description = "标签名称") @PathVariable String name,
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        TagDTO tag = tagService.getTagByName(name);
        return includeStats ?
            ApiResponse.successWithStats(tag, queryStatsService.finishRequestStats()) :
            ApiResponse.success(tag);
    }
}
