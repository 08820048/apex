package com.xuyi.blog.controller;

import com.xuyi.blog.dto.*;
import com.xuyi.blog.service.ArticleService;
import com.xuyi.blog.service.QueryStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章控制器（前台）
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
@Tag(name = "文章管理", description = "文章相关接口")
public class ArticleController {

    private final ArticleService articleService;
    private final QueryStatsService queryStatsService;

    @GetMapping
    @Operation(summary = "获取文章列表", description = "分页获取已发布的文章列表")
    public ApiResponse<PageResponse<ArticleSummaryDTO>> getArticles(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "是否包含查询统计") @RequestParam(defaultValue = "false") boolean includeStats) {
        PageResponse<ArticleSummaryDTO> articles = articleService.getPublishedArticles(page, size);

        if (includeStats) {
            QueryStatsDTO stats = queryStatsService.finishRequestStats();
            return ApiResponse.successWithStats(articles, stats);
        }

        return ApiResponse.success(articles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文章详情", description = "根据ID获取文章详情")
    public ApiResponse<ArticleDTO> getArticle(
            @Parameter(description = "文章ID") @PathVariable Long id,
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        ArticleDTO article = articleService.getArticleById(id);

        // 增加浏览量
        articleService.incrementViewCount(id);

        // 如果需要包含查询统计，则添加统计信息
        if (includeStats) {
            QueryStatsDTO queryStats = queryStatsService.finishRequestStats();
            return ApiResponse.successWithStats(article, queryStats);
        }

        return ApiResponse.success(article);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "根据分类获取文章", description = "分页获取指定分类的文章列表")
    public ApiResponse<PageResponse<ArticleSummaryDTO>> getArticlesByCategory(
            @Parameter(description = "分类ID") @PathVariable Long categoryId,
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        PageResponse<ArticleSummaryDTO> articles = articleService.getArticlesByCategory(categoryId, page, size);
        return ApiResponse.success(articles);
    }

    @GetMapping("/tag/{tagId}")
    @Operation(summary = "根据标签获取文章", description = "分页获取指定标签的文章列表")
    public ApiResponse<PageResponse<ArticleSummaryDTO>> getArticlesByTag(
            @Parameter(description = "标签ID") @PathVariable Long tagId,
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        PageResponse<ArticleSummaryDTO> articles = articleService.getArticlesByTag(tagId, page, size);
        return ApiResponse.success(articles);
    }

    @GetMapping("/search")
    @Operation(summary = "搜索文章", description = "根据关键词搜索文章")
    public ApiResponse<PageResponse<ArticleSummaryDTO>> searchArticles(
            @Parameter(description = "搜索关键词") @RequestParam String keyword,
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        PageResponse<ArticleSummaryDTO> articles = articleService.searchArticles(keyword, page, size);
        return ApiResponse.success(articles);
    }

    @GetMapping("/top")
    @Operation(summary = "获取置顶文章", description = "获取所有置顶文章")
    public ApiResponse<List<ArticleSummaryDTO>> getTopArticles() {
        List<ArticleSummaryDTO> articles = articleService.getTopArticles();
        return ApiResponse.success(articles);
    }

    @GetMapping("/latest")
    @Operation(summary = "获取最新文章", description = "获取最新的10篇文章")
    public ApiResponse<List<ArticleSummaryDTO>> getLatestArticles() {
        List<ArticleSummaryDTO> articles = articleService.getLatestArticles();
        return ApiResponse.success(articles);
    }

    @GetMapping("/popular")
    @Operation(summary = "获取热门文章", description = "获取浏览量最高的10篇文章")
    public ApiResponse<List<ArticleSummaryDTO>> getPopularArticles() {
        List<ArticleSummaryDTO> articles = articleService.getPopularArticles();
        return ApiResponse.success(articles);
    }

    @GetMapping("/{id}/related")
    @Operation(summary = "获取相关文章", description = "获取与指定文章相关的文章")
    public ApiResponse<List<ArticleSummaryDTO>> getRelatedArticles(
            @Parameter(description = "文章ID") @PathVariable Long id) {
        List<ArticleSummaryDTO> articles = articleService.getRelatedArticles(id);
        return ApiResponse.success(articles);
    }
}
