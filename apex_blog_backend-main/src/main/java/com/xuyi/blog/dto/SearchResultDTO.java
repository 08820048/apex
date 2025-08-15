package com.xuyi.blog.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 搜索结果DTO
 * 
 * @author xuyi
 */
@Getter
@Setter
public class SearchResultDTO {
    
    private String keyword;
    private PageResponse<ArticleSummaryDTO> articles;
    private long totalCount;
    private long searchTime;

    public SearchResultDTO() {}

    public SearchResultDTO(String keyword, PageResponse<ArticleSummaryDTO> articles, long totalCount) {
        this.keyword = keyword;
        this.articles = articles;
        this.totalCount = totalCount;
        this.searchTime = System.currentTimeMillis();
    }
}
