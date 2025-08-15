package com.xuyi.blog.controller.admin;

import com.xuyi.blog.dto.ApiResponse;
import com.xuyi.blog.dto.PortfolioDTO;
import com.xuyi.blog.dto.PortfolioRequestDTO;
import com.xuyi.blog.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理后台 - 作品集控制器
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/admin/portfolios")
@RequiredArgsConstructor
@Tag(name = "管理后台 - 作品集管理", description = "作品集管理相关接口")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminPortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping
    @Operation(summary = "获取所有作品集", description = "获取所有作品集列表")
    public ApiResponse<List<PortfolioDTO>> getAllPortfolios() {
        List<PortfolioDTO> portfolios = portfolioService.getAllPortfolios();
        return ApiResponse.success(portfolios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取作品集详情", description = "根据ID获取作品集详情")
    public ApiResponse<PortfolioDTO> getPortfolio(
            @Parameter(description = "作品集ID") @PathVariable Long id) {
        PortfolioDTO portfolio = portfolioService.getPortfolioById(id);
        return ApiResponse.success(portfolio);
    }

    @PostMapping
    @Operation(summary = "创建作品集", description = "创建新作品集")
    public ApiResponse<PortfolioDTO> createPortfolio(@Valid @RequestBody PortfolioRequestDTO request) {
        PortfolioDTO portfolio = portfolioService.createPortfolio(request);
        return ApiResponse.success("作品集创建成功", portfolio);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新作品集", description = "更新作品集信息")
    public ApiResponse<PortfolioDTO> updatePortfolio(
            @Parameter(description = "作品集ID") @PathVariable Long id,
            @Valid @RequestBody PortfolioRequestDTO request) {
        PortfolioDTO portfolio = portfolioService.updatePortfolio(id, request);
        return ApiResponse.success("作品集更新成功", portfolio);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除作品集", description = "删除指定作品集")
    public ApiResponse<Void> deletePortfolio(
            @Parameter(description = "作品集ID") @PathVariable Long id) {
        portfolioService.deletePortfolio(id);
        return ApiResponse.success("作品集删除成功");
    }

    @PostMapping("/{id}/featured")
    @Operation(summary = "设置精选状态", description = "设置作品集的精选状态")
    public ApiResponse<Void> setFeatured(
            @Parameter(description = "作品集ID") @PathVariable Long id,
            @Parameter(description = "是否精选") @RequestParam boolean featured) {
        portfolioService.setFeatured(id, featured);
        return ApiResponse.success("精选状态设置成功");
    }
}
