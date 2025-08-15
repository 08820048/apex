package com.xuyi.blog.controller.admin;

import com.xuyi.blog.dto.ApiResponse;
import com.xuyi.blog.dto.TagDTO;
import com.xuyi.blog.dto.TagRequestDTO;
import com.xuyi.blog.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理后台 - 标签控制器
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/admin/tags")
@RequiredArgsConstructor
@Tag(name = "管理后台 - 标签管理", description = "标签管理相关接口")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminTagController {

    private final TagService tagService;

    @GetMapping
    @Operation(summary = "获取所有标签", description = "获取所有标签列表")
    public ApiResponse<List<TagDTO>> getAllTags() {
        List<TagDTO> tags = tagService.getAllTags();
        return ApiResponse.success(tags);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取标签详情", description = "根据ID获取标签详情")
    public ApiResponse<TagDTO> getTag(
            @Parameter(description = "标签ID") @PathVariable Long id) {
        TagDTO tag = tagService.getTagById(id);
        return ApiResponse.success(tag);
    }

    @PostMapping
    @Operation(summary = "创建标签", description = "创建新标签")
    public ApiResponse<TagDTO> createTag(@Valid @RequestBody TagRequestDTO request) {
        TagDTO tag = tagService.createTag(request.getName(), request.getColor());
        return ApiResponse.success("标签创建成功", tag);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新标签", description = "更新指定标签")
    public ApiResponse<TagDTO> updateTag(
            @Parameter(description = "标签ID") @PathVariable Long id,
            @Valid @RequestBody TagRequestDTO request) {
        TagDTO tag = tagService.updateTag(id, request.getName(), request.getColor());
        return ApiResponse.success("标签更新成功", tag);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除标签", description = "删除指定标签")
    public ApiResponse<Void> deleteTag(
            @Parameter(description = "标签ID") @PathVariable Long id) {
        tagService.deleteTag(id);
        return ApiResponse.success("标签删除成功");
    }
}
