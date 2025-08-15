package com.xuyi.blog.controller;

import com.xuyi.blog.dto.ApiResponse;
import com.xuyi.blog.dto.VisitStatsDTO;
import com.xuyi.blog.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 访问统计控制器
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/visit")
@RequiredArgsConstructor
@Tag(name = "访问统计", description = "访问统计相关接口")
public class VisitController {

    private final VisitService visitService;

    @GetMapping("/stats")
    @Operation(summary = "获取访问统计", description = "获取网站访问统计信息")
    public ApiResponse<VisitStatsDTO> getVisitStats() {
        VisitStatsDTO stats = visitService.getVisitStats();
        return ApiResponse.success(stats);
    }

    @GetMapping("/total")
    @Operation(summary = "获取总访问量", description = "获取网站总访问量")
    public ApiResponse<Long> getTotalVisitCount() {
        Long count = visitService.getTotalVisitCount();
        return ApiResponse.success(count);
    }

    @GetMapping("/today")
    @Operation(summary = "获取今日访问量", description = "获取今日访问量")
    public ApiResponse<Long> getTodayVisitCount() {
        Long count = visitService.getTodayVisitCount();
        return ApiResponse.success(count);
    }

    @GetMapping("/unique")
    @Operation(summary = "获取独立访客数", description = "获取独立访客数")
    public ApiResponse<Long> getUniqueVisitorCount() {
        Long count = visitService.getUniqueVisitorCount();
        return ApiResponse.success(count);
    }

    @GetMapping("/range")
    @Operation(summary = "获取指定日期范围访问量", description = "获取指定日期范围内的访问量")
    public ApiResponse<Long> getVisitCountBetween(
            @Parameter(description = "开始日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Long count = visitService.getVisitCountBetween(startDate, endDate);
        return ApiResponse.success(count);
    }
}
