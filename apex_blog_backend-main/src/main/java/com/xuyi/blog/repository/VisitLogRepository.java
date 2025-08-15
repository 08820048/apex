package com.xuyi.blog.repository;

import com.xuyi.blog.entity.VisitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 访问统计数据访问接口
 * 
 * @author xuyi
 */
@Repository
public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {

    /**
     * 根据IP和日期查找访问记录
     */
    Optional<VisitLog> findByIpAddressAndVisitDate(String ipAddress, LocalDate visitDate);

    /**
     * 统计总访问量
     */
    @Query("SELECT SUM(v.visitCount) FROM VisitLog v")
    Long getTotalVisitCount();

    /**
     * 统计今日访问量
     */
    @Query("SELECT SUM(v.visitCount) FROM VisitLog v WHERE v.visitDate = :date")
    Long getTodayVisitCount(@Param("date") LocalDate date);

    /**
     * 统计独立访客数
     */
    @Query("SELECT COUNT(DISTINCT v.ipAddress) FROM VisitLog v")
    Long getUniqueVisitorCount();

    /**
     * 统计指定日期范围内的访问量
     */
    @Query("SELECT SUM(v.visitCount) FROM VisitLog v WHERE v.visitDate BETWEEN :startDate AND :endDate")
    Long getVisitCountBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 获取最近7天的访问统计
     */
    @Query("SELECT v.visitDate, SUM(v.visitCount) FROM VisitLog v WHERE v.visitDate >= :startDate GROUP BY v.visitDate ORDER BY v.visitDate")
    List<Object[]> getRecentVisitStats(@Param("startDate") LocalDate startDate);

    /**
     * 获取热门页面统计
     */
    @Query("SELECT v.requestUri, SUM(v.visitCount) FROM VisitLog v WHERE v.requestUri IS NOT NULL GROUP BY v.requestUri ORDER BY SUM(v.visitCount) DESC")
    List<Object[]> getPopularPages();
}
