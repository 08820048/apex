package com.xuyi.blog.controller;

import com.xuyi.blog.dto.ApiResponse;
import com.xuyi.blog.dto.CategoryDTO;
import com.xuyi.blog.service.CategoryService;
import com.xuyi.blog.service.QueryStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器（前台）
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "分类管理", description = "分类相关接口")
public class CategoryController {

    private final CategoryService categoryService;
    private final QueryStatsService queryStatsService;

    @GetMapping
    @Operation(summary = "获取所有分类", description = "获取所有分类列表")
    public ApiResponse<List<CategoryDTO>> getAllCategories(
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return includeStats ?
            ApiResponse.successWithStats(categories, queryStatsService.finishRequestStats()) :
            ApiResponse.success(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取分类详情", description = "根据ID获取分类详情")
    public ApiResponse<CategoryDTO> getCategory(
            @Parameter(description = "分类ID") @PathVariable Long id,
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        CategoryDTO category = categoryService.getCategoryById(id);
        return includeStats ?
            ApiResponse.successWithStats(category, queryStatsService.finishRequestStats()) :
            ApiResponse.success(category);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "根据名称获取分类", description = "根据名称获取分类信息")
    public ApiResponse<CategoryDTO> getCategoryByName(
            @Parameter(description = "分类名称") @PathVariable String name,
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        CategoryDTO category = categoryService.getCategoryByName(name);
        return includeStats ?
            ApiResponse.successWithStats(category, queryStatsService.finishRequestStats()) :
            ApiResponse.success(category);
    }
}
