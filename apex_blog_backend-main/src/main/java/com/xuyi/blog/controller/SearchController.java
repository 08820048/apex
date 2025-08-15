package com.xuyi.blog.controller;

import com.xuyi.blog.dto.ApiResponse;
import com.xuyi.blog.dto.ArticleSummaryDTO;
import com.xuyi.blog.dto.PageResponse;
import com.xuyi.blog.dto.SearchResultDTO;
import com.xuyi.blog.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 搜索控制器
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Tag(name = "搜索功能", description = "搜索相关接口")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/articles")
    @Operation(summary = "搜索文章", description = "根据关键词搜索文章")
    public ApiResponse<PageResponse<ArticleSummaryDTO>> searchArticles(
            @Parameter(description = "搜索关键词") @RequestParam String keyword,
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        PageResponse<ArticleSummaryDTO> result = searchService.searchArticles(keyword, page, size);
        return ApiResponse.success(result);
    }

    @GetMapping("/suggestions")
    @Operation(summary = "获取搜索建议", description = "根据输入获取搜索建议")
    public ApiResponse<List<String>> getSearchSuggestions(
            @Parameter(description = "搜索关键词") @RequestParam String keyword) {
        List<String> suggestions = searchService.getSearchSuggestions(keyword);
        return ApiResponse.success(suggestions);
    }

    @GetMapping("/hot-keywords")
    @Operation(summary = "获取热门搜索关键词", description = "获取热门搜索关键词列表")
    public ApiResponse<List<String>> getHotSearchKeywords() {
        List<String> keywords = searchService.getHotSearchKeywords();
        return ApiResponse.success(keywords);
    }

    @GetMapping("/full-text")
    @Operation(summary = "全文搜索", description = "全文搜索文章内容")
    public ApiResponse<SearchResultDTO> fullTextSearch(
            @Parameter(description = "搜索关键词") @RequestParam String keyword,
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        SearchResultDTO result = searchService.fullTextSearch(keyword, page, size);
        return ApiResponse.success(result);
    }
}
