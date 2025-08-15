package com.xuyi.blog.service;

import com.xuyi.blog.dto.VisitStatsDTO;
import com.xuyi.blog.entity.VisitLog;
import com.xuyi.blog.repository.ArticleRepository;
import com.xuyi.blog.repository.VisitLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 访问统计服务类
 * 
 * @author xuyi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VisitService {

    private final VisitLogRepository visitLogRepository;
    private final ArticleRepository articleRepository;

    /**
     * 记录访问日志（异步）
     */
    @Async
    @Transactional
    public void recordVisit(HttpServletRequest request) {
        try {
            String ipAddress = getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");
            String referer = request.getHeader("Referer");
            String requestUri = request.getRequestURI();
            LocalDate today = LocalDate.now();

            // 查找今天该IP的访问记录
            Optional<VisitLog> existingLog = visitLogRepository.findByIpAddressAndVisitDate(ipAddress, today);

            if (existingLog.isPresent()) {
                // 如果已存在，增加访问次数
                VisitLog visitLog = existingLog.get();
                visitLog.incrementVisitCount();
                visitLogRepository.save(visitLog);
                log.debug("更新访问记录: IP={}, 访问次数={}", ipAddress, visitLog.getVisitCount());
            } else {
                // 如果不存在，创建新记录
                VisitLog visitLog = new VisitLog(ipAddress, userAgent, referer, requestUri);
                visitLogRepository.save(visitLog);
                log.debug("创建访问记录: IP={}", ipAddress);
            }
        } catch (Exception e) {
            log.error("记录访问日志失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 获取访问统计信息
     */
    @Cacheable(value = "visitStats", key = "'overview'")
    public VisitStatsDTO getVisitStats() {
        VisitStatsDTO stats = new VisitStatsDTO();

        // 总访问量
        Long totalVisits = visitLogRepository.getTotalVisitCount();
        stats.setTotalVisits(totalVisits != null ? totalVisits : 0L);

        // 今日访问量
        Long todayVisits = visitLogRepository.getTodayVisitCount(LocalDate.now());
        stats.setTodayVisits(todayVisits != null ? todayVisits : 0L);

        // 独立访客数
        Long uniqueVisitors = visitLogRepository.getUniqueVisitorCount();
        stats.setUniqueVisitors(uniqueVisitors != null ? uniqueVisitors : 0L);

        // 文章总数
        Long totalArticles = articleRepository.countByStatus(com.xuyi.blog.entity.Article.ArticleStatus.PUBLISHED);
        stats.setTotalArticles(totalArticles);

        // 最近7天访问统计
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(6);
        List<Object[]> recentVisitData = visitLogRepository.getRecentVisitStats(sevenDaysAgo);
        List<VisitStatsDTO.DailyVisitDTO> recentVisits = recentVisitData.stream()
                .map(data -> new VisitStatsDTO.DailyVisitDTO((LocalDate) data[0], (Long) data[1]))
                .collect(Collectors.toList());
        stats.setRecentVisits(recentVisits);

        // 热门页面
        List<Object[]> popularPageData = visitLogRepository.getPopularPages();
        List<VisitStatsDTO.PopularPageDTO> popularPages = popularPageData.stream()
                .limit(10)
                .map(data -> new VisitStatsDTO.PopularPageDTO((String) data[0], (Long) data[1]))
                .collect(Collectors.toList());
        stats.setPopularPages(popularPages);

        return stats;
    }

    /**
     * 获取指定日期范围的访问量
     */
    @Cacheable(value = "visitStats", key = "'range_' + #startDate + '_' + #endDate")
    public Long getVisitCountBetween(LocalDate startDate, LocalDate endDate) {
        Long count = visitLogRepository.getVisitCountBetween(startDate, endDate);
        return count != null ? count : 0L;
    }

    /**
     * 获取今日访问量
     */
    @Cacheable(value = "visitStats", key = "'today'")
    public Long getTodayVisitCount() {
        Long count = visitLogRepository.getTodayVisitCount(LocalDate.now());
        return count != null ? count : 0L;
    }

    /**
     * 获取总访问量
     */
    @Cacheable(value = "visitStats", key = "'total'")
    public Long getTotalVisitCount() {
        Long count = visitLogRepository.getTotalVisitCount();
        return count != null ? count : 0L;
    }

    /**
     * 获取独立访客数
     */
    @Cacheable(value = "visitStats", key = "'unique'")
    public Long getUniqueVisitorCount() {
        Long count = visitLogRepository.getUniqueVisitorCount();
        return count != null ? count : 0L;
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headerNames = {
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
        };

        for (String headerName : headerNames) {
            String ip = request.getHeader(headerName);
            if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
                // 如果有多个IP，取第一个
                if (ip.contains(",")) {
                    ip = ip.split(",")[0].trim();
                }
                return ip;
            }
        }

        String ip = request.getRemoteAddr();
        
        // 如果是本地回环地址，尝试获取本机IP
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            try {
                java.net.InetAddress addr = java.net.InetAddress.getLocalHost();
                ip = addr.getHostAddress();
            } catch (Exception e) {
                log.warn("获取本机IP失败: {}", e.getMessage());
            }
        }

        return ip;
    }
}
