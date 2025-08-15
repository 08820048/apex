package com.xuyi.blog.controller.admin;

import com.xuyi.blog.dto.ApiResponse;
import com.xuyi.blog.dto.CategoryDTO;
import com.xuyi.blog.dto.CategoryRequestDTO;
import com.xuyi.blog.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理后台 - 分类控制器
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Tag(name = "管理后台 - 分类管理", description = "分类管理相关接口")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "获取所有分类", description = "获取所有分类列表")
    public ApiResponse<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ApiResponse.success(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取分类详情", description = "根据ID获取分类详情")
    public ApiResponse<CategoryDTO> getCategory(
            @Parameter(description = "分类ID") @PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        return ApiResponse.success(category);
    }

    @PostMapping
    @Operation(summary = "创建分类", description = "创建新分类")
    public ApiResponse<CategoryDTO> createCategory(@Valid @RequestBody CategoryRequestDTO request) {
        CategoryDTO category = categoryService.createCategory(request);
        return ApiResponse.success("分类创建成功", category);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新分类", description = "更新指定分类")
    public ApiResponse<CategoryDTO> updateCategory(
            @Parameter(description = "分类ID") @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDTO request) {
        CategoryDTO category = categoryService.updateCategory(id, request);
        return ApiResponse.success("分类更新成功", category);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类", description = "删除指定分类")
    public ApiResponse<Void> deleteCategory(
            @Parameter(description = "分类ID") @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.success("分类删除成功");
    }
}
