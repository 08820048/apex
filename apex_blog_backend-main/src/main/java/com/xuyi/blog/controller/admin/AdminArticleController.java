package com.xuyi.blog.controller.admin;

import com.xuyi.blog.dto.*;
import com.xuyi.blog.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 管理后台 - 文章控制器
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/admin/articles")
@RequiredArgsConstructor
@Tag(name = "管理后台 - 文章管理", description = "文章管理相关接口")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminArticleController {

    private final ArticleService articleService;

    @GetMapping
    @Operation(summary = "获取所有文章", description = "分页获取所有文章（包括草稿）")
    public ApiResponse<PageResponse<ArticleSummaryDTO>> getAllArticles(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        PageResponse<ArticleSummaryDTO> articles = articleService.getAllArticles(page, size);
        return ApiResponse.success(articles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文章详情", description = "根据ID获取文章详情（包括草稿）")
    public ApiResponse<ArticleDTO> getArticle(
            @Parameter(description = "文章ID") @PathVariable Long id) {
        ArticleDTO article = articleService.getArticleById(id);
        return ApiResponse.success(article);
    }

    @PostMapping
    @Operation(summary = "创建文章", description = "创建新文章")
    public ApiResponse<ArticleDTO> createArticle(
            @Valid @RequestBody ArticleRequestDTO request,
            Authentication authentication) {
        String username = authentication.getName();
        ArticleDTO article = articleService.createArticle(request, username);
        return ApiResponse.success("文章创建成功", article);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新文章", description = "更新文章信息")
    public ApiResponse<ArticleDTO> updateArticle(
            @Parameter(description = "文章ID") @PathVariable Long id,
            @Valid @RequestBody ArticleRequestDTO request) {
        ArticleDTO article = articleService.updateArticle(id, request);
        return ApiResponse.success("文章更新成功", article);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文章", description = "删除指定文章")
    public ApiResponse<Void> deleteArticle(
            @Parameter(description = "文章ID") @PathVariable Long id) {
        articleService.deleteArticle(id);
        return ApiResponse.success("文章删除成功");
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "发布文章", description = "发布指定文章")
    public ApiResponse<Void> publishArticle(
            @Parameter(description = "文章ID") @PathVariable Long id) {
        articleService.publishArticle(id);
        return ApiResponse.success("文章发布成功");
    }

    @PostMapping("/{id}/archive")
    @Operation(summary = "归档文章", description = "归档指定文章")
    public ApiResponse<Void> archiveArticle(
            @Parameter(description = "文章ID") @PathVariable Long id) {
        articleService.archiveArticle(id);
        return ApiResponse.success("文章归档成功");
    }
}
