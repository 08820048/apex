package com.xuyi.blog.controller.admin;

import com.xuyi.blog.dto.ApiResponse;
import com.xuyi.blog.dto.VisitStatsDTO;
import com.xuyi.blog.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理后台 - 统计控制器
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/admin/stats")
@RequiredArgsConstructor
@Tag(name = "管理后台 - 统计管理", description = "统计管理相关接口")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminStatsController {

    private final VisitService visitService;

    @GetMapping("/visits")
    @Operation(summary = "获取访问统计", description = "获取网站访问统计信息")
    public ApiResponse<VisitStatsDTO> getVisitStats() {
        VisitStatsDTO stats = visitService.getVisitStats();
        return ApiResponse.success(stats);
    }
}
