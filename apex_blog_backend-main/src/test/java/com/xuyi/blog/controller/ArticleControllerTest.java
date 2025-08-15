package com.xuyi.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuyi.blog.dto.ArticleDTO;
import com.xuyi.blog.dto.ArticleSummaryDTO;
import com.xuyi.blog.dto.PageResponse;
import com.xuyi.blog.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 文章控制器测试类
 * 
 * @author xuyi
 */
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetArticles() throws Exception {
        // Given
        ArticleSummaryDTO article = new ArticleSummaryDTO();
        article.setId(1L);
        article.setTitle("测试文章");
        article.setCreatedAt(LocalDateTime.now());

        PageResponse<ArticleSummaryDTO> pageResponse = new PageResponse<>();
        pageResponse.setContent(List.of(article));
        pageResponse.setTotalElements(1);
        pageResponse.setTotalPages(1);

        when(articleService.getPublishedArticles(eq(0), eq(10)))
                .thenReturn(pageResponse);

        // When & Then
        mockMvc.perform(get("/articles")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].title").value("测试文章"))
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    void testGetArticle() throws Exception {
        // Given
        ArticleDTO article = new ArticleDTO();
        article.setId(1L);
        article.setTitle("测试文章");
        article.setContent("测试内容");

        when(articleService.getArticleById(1L)).thenReturn(article);

        // When & Then
        mockMvc.perform(get("/articles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.title").value("测试文章"))
                .andExpect(jsonPath("$.data.content").value("测试内容"));
    }

    @Test
    void testSearchArticles() throws Exception {
        // Given
        ArticleSummaryDTO article = new ArticleSummaryDTO();
        article.setId(1L);
        article.setTitle("Java教程");

        PageResponse<ArticleSummaryDTO> pageResponse = new PageResponse<>();
        pageResponse.setContent(List.of(article));
        pageResponse.setTotalElements(1);

        when(articleService.searchArticles(eq("Java"), eq(0), eq(10)))
                .thenReturn(pageResponse);

        // When & Then
        mockMvc.perform(get("/articles/search")
                        .param("keyword", "Java")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.content[0].title").value("Java教程"));
    }

    @Test
    void testGetTopArticles() throws Exception {
        // Given
        ArticleSummaryDTO article = new ArticleSummaryDTO();
        article.setId(1L);
        article.setTitle("置顶文章");
        article.setIsTop(true);

        when(articleService.getTopArticles()).thenReturn(List.of(article));

        // When & Then
        mockMvc.perform(get("/articles/top")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].title").value("置顶文章"))
                .andExpect(jsonPath("$.data[0].isTop").value(true));
    }

    @Test
    void testGetLatestArticles() throws Exception {
        // Given
        ArticleSummaryDTO article = new ArticleSummaryDTO();
        article.setId(1L);
        article.setTitle("最新文章");

        when(articleService.getLatestArticles()).thenReturn(List.of(article));

        // When & Then
        mockMvc.perform(get("/articles/latest")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].title").value("最新文章"));
    }

    @Test
    void testGetPopularArticles() throws Exception {
        // Given
        ArticleSummaryDTO article = new ArticleSummaryDTO();
        article.setId(1L);
        article.setTitle("热门文章");
        article.setViewCount(1000L);

        when(articleService.getPopularArticles()).thenReturn(List.of(article));

        // When & Then
        mockMvc.perform(get("/articles/popular")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].title").value("热门文章"))
                .andExpect(jsonPath("$.data[0].viewCount").value(1000));
    }
}
