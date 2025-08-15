package com.xuyi.blog.service;

import com.xuyi.blog.dto.PortfolioDTO;
import com.xuyi.blog.dto.PortfolioRequestDTO;
import com.xuyi.blog.entity.Portfolio;
import com.xuyi.blog.exception.ResourceNotFoundException;
import com.xuyi.blog.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 作品集服务类
 * 
 * @author xuyi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    /**
     * 获取所有作品集
     */
    // @Cacheable(value = "portfolios", key = "'all'")
    public List<PortfolioDTO> getAllPortfolios() {
        List<Portfolio> portfolios = portfolioRepository.findAllByOrderBySortOrderAsc();
        return portfolios.stream()
                .map(PortfolioDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * 获取精选作品集
     */
    // @Cacheable(value = "portfolios", key = "'featured'")
    public List<PortfolioDTO> getFeaturedPortfolios() {
        List<Portfolio> portfolios = portfolioRepository.findByIsFeaturedTrueOrderBySortOrderAsc();
        return portfolios.stream()
                .map(PortfolioDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID获取作品集
     */
    // @Cacheable(value = "portfolio", key = "#id")
    public PortfolioDTO getPortfolioById(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("作品集不存在"));
        return PortfolioDTO.from(portfolio);
    }

    /**
     * 创建作品集
     */
    @Transactional
    // @CacheEvict(value = {"portfolios", "portfolio"}, allEntries = true)
    public PortfolioDTO createPortfolio(PortfolioRequestDTO request) {
        Portfolio portfolio = new Portfolio();
        portfolio.setName(request.getName());
        portfolio.setDescription(request.getDescription());
        portfolio.setUrl(request.getUrl());
        portfolio.setCoverImage(request.getCoverImage());
        portfolio.setTechnologies(request.getTechnologies());
        portfolio.setSortOrder(request.getSortOrder());
        portfolio.setIsFeatured(request.getIsFeatured());

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        log.info("创建作品集成功: {}", savedPortfolio.getName());
        
        return PortfolioDTO.from(savedPortfolio);
    }

    /**
     * 更新作品集
     */
    @Transactional
    // @CacheEvict(value = {"portfolios", "portfolio"}, allEntries = true)
    public PortfolioDTO updatePortfolio(Long id, PortfolioRequestDTO request) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("作品集不存在"));

        portfolio.setName(request.getName());
        portfolio.setDescription(request.getDescription());
        portfolio.setUrl(request.getUrl());
        portfolio.setCoverImage(request.getCoverImage());
        portfolio.setTechnologies(request.getTechnologies());
        portfolio.setSortOrder(request.getSortOrder());
        portfolio.setIsFeatured(request.getIsFeatured());

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        log.info("更新作品集成功: {}", savedPortfolio.getName());
        
        return PortfolioDTO.from(savedPortfolio);
    }

    /**
     * 删除作品集
     */
    @Transactional
    // @CacheEvict(value = {"portfolios", "portfolio"}, allEntries = true)
    public void deletePortfolio(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("作品集不存在"));
        
        portfolioRepository.delete(portfolio);
        log.info("删除作品集成功: {}", portfolio.getName());
    }

    /**
     * 设置精选状态
     */
    @Transactional
    // @CacheEvict(value = {"portfolios", "portfolio"}, allEntries = true)
    public void setFeatured(Long id, boolean featured) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("作品集不存在"));
        
        portfolio.setIsFeatured(featured);
        portfolioRepository.save(portfolio);
        log.info("设置作品集精选状态: {} -> {}", portfolio.getName(), featured);
    }
}
