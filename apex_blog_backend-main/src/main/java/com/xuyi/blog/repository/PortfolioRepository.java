package com.xuyi.blog.repository;

import com.xuyi.blog.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作品集数据访问接口
 * 
 * @author xuyi
 */
@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    /**
     * 按排序顺序查找所有作品
     */
    List<Portfolio> findAllByOrderBySortOrderAsc();

    /**
     * 查找精选作品
     */
    List<Portfolio> findByIsFeaturedTrueOrderBySortOrderAsc();
}
