package com.xuyi.blog.controller;

import com.xuyi.blog.dto.ApiResponse;
import com.xuyi.blog.dto.PortfolioDTO;
import com.xuyi.blog.service.PortfolioService;
import com.xuyi.blog.service.QueryStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 作品集控制器（前台）
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/portfolios")
@RequiredArgsConstructor
@Tag(name = "作品集管理", description = "作品集相关接口")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final QueryStatsService queryStatsService;

    @GetMapping
    @Operation(summary = "获取所有作品集", description = "获取所有作品集列表")
    public ApiResponse<List<PortfolioDTO>> getAllPortfolios(
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        List<PortfolioDTO> portfolios = portfolioService.getAllPortfolios();
        return includeStats ?
            ApiResponse.successWithStats(portfolios, queryStatsService.finishRequestStats()) :
            ApiResponse.success(portfolios);
    }

    @GetMapping("/featured")
    @Operation(summary = "获取精选作品集", description = "获取精选作品集列表")
    public ApiResponse<List<PortfolioDTO>> getFeaturedPortfolios(
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        List<PortfolioDTO> portfolios = portfolioService.getFeaturedPortfolios();
        return includeStats ?
            ApiResponse.successWithStats(portfolios, queryStatsService.finishRequestStats()) :
            ApiResponse.success(portfolios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取作品集详情", description = "根据ID获取作品集详情")
    public ApiResponse<PortfolioDTO> getPortfolio(
            @Parameter(description = "作品集ID") @PathVariable Long id,
            @Parameter(description = "是否包含查询统计信息") @RequestParam(defaultValue = "false") boolean includeStats) {
        PortfolioDTO portfolio = portfolioService.getPortfolioById(id);
        return includeStats ?
            ApiResponse.successWithStats(portfolio, queryStatsService.finishRequestStats()) :
            ApiResponse.success(portfolio);
    }
}
