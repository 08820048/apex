package com.xuyi.blog.controller;

import com.xuyi.blog.dto.ApiResponse;
import com.xuyi.blog.dto.BlogStatsDTO;
import com.xuyi.blog.dto.QueryStatsDTO;
import com.xuyi.blog.service.BlogStatsService;
import com.xuyi.blog.service.QueryStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 博客统计控制器（前台）
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@Tag(name = "博客统计", description = "博客统计相关接口")
public class BlogStatsController {

    private final BlogStatsService blogStatsService;
    private final QueryStatsService queryStatsService;

    @GetMapping("/overview")
    @Operation(summary = "获取博客综合统计", description = "获取博客的综合统计信息，包括文章数、访问量、标签数、分类数等")
    public ApiResponse<BlogStatsDTO> getBlogStats(
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        BlogStatsDTO stats = blogStatsService.getBlogStats();
        
        if (includeStats) {
            QueryStatsDTO queryStats = queryStatsService.finishRequestStats();
            return ApiResponse.successWithStats(stats, queryStats);
        }
        
        return ApiResponse.success(stats);
    }

    @GetMapping("/articles/count")
    @Operation(summary = "获取文章总数", description = "获取已发布文章的总数")
    public ApiResponse<Long> getTotalArticles(
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        Long count = blogStatsService.getTotalArticles();
        
        if (includeStats) {
            QueryStatsDTO queryStats = queryStatsService.finishRequestStats();
            return ApiResponse.successWithStats(count, queryStats);
        }
        
        return ApiResponse.success(count);
    }

    @GetMapping("/tags/count")
    @Operation(summary = "获取标签总数", description = "获取标签的总数")
    public ApiResponse<Long> getTotalTags(
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        Long count = blogStatsService.getTotalTags();
        
        if (includeStats) {
            QueryStatsDTO queryStats = queryStatsService.finishRequestStats();
            return ApiResponse.successWithStats(count, queryStats);
        }
        
        return ApiResponse.success(count);
    }

    @GetMapping("/categories/count")
    @Operation(summary = "获取分类总数", description = "获取分类的总数")
    public ApiResponse<Long> getTotalCategories(
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        Long count = blogStatsService.getTotalCategories();
        
        if (includeStats) {
            QueryStatsDTO queryStats = queryStatsService.finishRequestStats();
            return ApiResponse.successWithStats(count, queryStats);
        }
        
        return ApiResponse.success(count);
    }

    @GetMapping("/friend-links/count")
    @Operation(summary = "获取友链总数", description = "获取友链的总数")
    public ApiResponse<Long> getTotalFriendLinks(
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        Long count = blogStatsService.getTotalFriendLinks();
        
        if (includeStats) {
            QueryStatsDTO queryStats = queryStatsService.finishRequestStats();
            return ApiResponse.successWithStats(count, queryStats);
        }
        
        return ApiResponse.success(count);
    }

    @GetMapping("/portfolios/count")
    @Operation(summary = "获取作品集总数", description = "获取作品集的总数")
    public ApiResponse<Long> getTotalPortfolios(
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        Long count = blogStatsService.getTotalPortfolios();
        
        if (includeStats) {
            QueryStatsDTO queryStats = queryStatsService.finishRequestStats();
            return ApiResponse.successWithStats(count, queryStats);
        }
        
        return ApiResponse.success(count);
    }

    @GetMapping("/subscribers/count")
    @Operation(summary = "获取订阅者总数", description = "获取活跃订阅者的总数")
    public ApiResponse<Long> getTotalSubscribers(
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        Long count = blogStatsService.getTotalSubscribers();

        if (includeStats) {
            QueryStatsDTO queryStats = queryStatsService.finishRequestStats();
            return ApiResponse.successWithStats(count, queryStats);
        }

        return ApiResponse.success(count);
    }

    @GetMapping("/visits/today")
    @Operation(summary = "获取今日访问量", description = "获取今日访问量统计")
    public ApiResponse<Long> getTodayVisits(
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        Long count = blogStatsService.getTodayVisits();

        if (includeStats) {
            QueryStatsDTO queryStats = queryStatsService.finishRequestStats();
            return ApiResponse.successWithStats(count, queryStats);
        }

        return ApiResponse.success(count);
    }

    @GetMapping("/visits/total")
    @Operation(summary = "获取总访问量", description = "获取总访问量统计")
    public ApiResponse<Long> getTotalVisits(
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        Long count = blogStatsService.getTotalVisits();

        if (includeStats) {
            QueryStatsDTO queryStats = queryStatsService.finishRequestStats();
            return ApiResponse.successWithStats(count, queryStats);
        }

        return ApiResponse.success(count);
    }

    @GetMapping("/visitors/unique")
    @Operation(summary = "获取独立访客数", description = "获取独立访客数统计")
    public ApiResponse<Long> getUniqueVisitors(
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        Long count = blogStatsService.getUniqueVisitors();

        if (includeStats) {
            QueryStatsDTO queryStats = queryStatsService.finishRequestStats();
            return ApiResponse.successWithStats(count, queryStats);
        }

        return ApiResponse.success(count);
    }
}
