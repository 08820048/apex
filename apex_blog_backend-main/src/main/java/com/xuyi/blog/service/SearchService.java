package com.xuyi.blog.service;

import com.xuyi.blog.dto.ArticleSummaryDTO;
import com.xuyi.blog.dto.PageResponse;
import com.xuyi.blog.dto.SearchResultDTO;
import com.xuyi.blog.entity.Article;
import com.xuyi.blog.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 搜索服务类
 * 
 * @author xuyi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final ArticleRepository articleRepository;

    /**
     * 搜索文章
     */
    public PageResponse<ArticleSummaryDTO> searchArticles(String keyword, int page, int size) {
        if (!StringUtils.hasText(keyword)) {
            return new PageResponse<>();
        }

        // 清理和预处理关键词
        String cleanKeyword = cleanKeyword(keyword);
        if (!StringUtils.hasText(cleanKeyword)) {
            return new PageResponse<>();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articlePage = articleRepository.searchByKeyword(
                cleanKeyword, Article.ArticleStatus.PUBLISHED, pageable);

        Page<ArticleSummaryDTO> dtoPage = articlePage.map(article -> {
            ArticleSummaryDTO dto = ArticleSummaryDTO.from(article);
            // 高亮搜索关键词
            dto.setTitle(highlightKeyword(dto.getTitle(), cleanKeyword));
            dto.setSummary(highlightKeyword(dto.getSummary(), cleanKeyword));
            return dto;
        });

        log.info("搜索关键词: {}, 找到 {} 篇文章", cleanKeyword, articlePage.getTotalElements());
        return PageResponse.of(dtoPage);
    }

    /**
     * 获取搜索建议
     */
    public List<String> getSearchSuggestions(String keyword) {
        if (!StringUtils.hasText(keyword) || keyword.length() < 2) {
            return new ArrayList<>();
        }

        String cleanKeyword = cleanKeyword(keyword);
        if (!StringUtils.hasText(cleanKeyword)) {
            return new ArrayList<>();
        }

        // 这里可以实现更复杂的搜索建议逻辑
        // 目前简单返回基于标题的建议
        Pageable pageable = PageRequest.of(0, 10);
        Page<Article> articles = articleRepository.searchByKeyword(
                cleanKeyword, Article.ArticleStatus.PUBLISHED, pageable);

        return articles.getContent().stream()
                .map(Article::getTitle)
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * 获取热门搜索关键词
     */
    public List<String> getHotSearchKeywords() {
        // 这里可以实现基于搜索统计的热门关键词
        // 目前返回一些预设的热门关键词
        return List.of("Java", "Spring Boot", "MySQL", "Redis", "Docker", "微服务", "算法", "数据结构");
    }

    /**
     * 全文搜索（包含内容）
     */
    public SearchResultDTO fullTextSearch(String keyword, int page, int size) {
        if (!StringUtils.hasText(keyword)) {
            return new SearchResultDTO();
        }

        String cleanKeyword = cleanKeyword(keyword);
        if (!StringUtils.hasText(cleanKeyword)) {
            return new SearchResultDTO();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articlePage = articleRepository.searchByKeyword(
                cleanKeyword, Article.ArticleStatus.PUBLISHED, pageable);

        List<ArticleSummaryDTO> articles = articlePage.getContent().stream()
                .map(article -> {
                    ArticleSummaryDTO dto = ArticleSummaryDTO.from(article);
                    // 生成搜索摘要
                    dto.setSummary(generateSearchSummary(article.getContent(), cleanKeyword));
                    // 高亮关键词
                    dto.setTitle(highlightKeyword(dto.getTitle(), cleanKeyword));
                    dto.setSummary(highlightKeyword(dto.getSummary(), cleanKeyword));
                    return dto;
                })
                .collect(Collectors.toList());

        SearchResultDTO result = new SearchResultDTO();
        result.setKeyword(cleanKeyword);
        result.setArticles(PageResponse.of(articlePage.map(ArticleSummaryDTO::from)));
        result.setTotalCount(articlePage.getTotalElements());
        result.setSearchTime(System.currentTimeMillis());

        log.info("全文搜索关键词: {}, 找到 {} 篇文章", cleanKeyword, articlePage.getTotalElements());
        return result;
    }

    /**
     * 清理搜索关键词
     */
    private String cleanKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return "";
        }

        // 移除特殊字符，只保留字母、数字、中文和空格
        String cleaned = keyword.replaceAll("[^\\w\\s\\u4e00-\\u9fa5]", " ");
        
        // 移除多余的空格
        cleaned = cleaned.replaceAll("\\s+", " ").trim();
        
        // 限制长度
        if (cleaned.length() > 100) {
            cleaned = cleaned.substring(0, 100);
        }

        return cleaned;
    }

    /**
     * 高亮搜索关键词
     */
    private String highlightKeyword(String text, String keyword) {
        if (!StringUtils.hasText(text) || !StringUtils.hasText(keyword)) {
            return text;
        }

        try {
            // 使用正则表达式进行不区分大小写的替换
            String pattern = "(?i)" + Pattern.quote(keyword);
            return text.replaceAll(pattern, "<mark>$0</mark>");
        } catch (Exception e) {
            log.warn("高亮关键词失败: {}", e.getMessage());
            return text;
        }
    }

    /**
     * 生成搜索摘要
     */
    private String generateSearchSummary(String content, String keyword) {
        if (!StringUtils.hasText(content) || !StringUtils.hasText(keyword)) {
            return content != null && content.length() > 200 ? 
                   content.substring(0, 200) + "..." : content;
        }

        try {
            // 查找关键词在内容中的位置
            String lowerContent = content.toLowerCase();
            String lowerKeyword = keyword.toLowerCase();
            int index = lowerContent.indexOf(lowerKeyword);

            if (index == -1) {
                // 如果没找到关键词，返回前200个字符
                return content.length() > 200 ? content.substring(0, 200) + "..." : content;
            }

            // 以关键词为中心，提取前后各100个字符
            int start = Math.max(0, index - 100);
            int end = Math.min(content.length(), index + keyword.length() + 100);

            String summary = content.substring(start, end);
            
            // 如果不是从开头开始，添加省略号
            if (start > 0) {
                summary = "..." + summary;
            }
            
            // 如果不是到结尾，添加省略号
            if (end < content.length()) {
                summary = summary + "...";
            }

            return summary;
        } catch (Exception e) {
            log.warn("生成搜索摘要失败: {}", e.getMessage());
            return content.length() > 200 ? content.substring(0, 200) + "..." : content;
        }
    }
}
