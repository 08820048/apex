package com.xuyi.blog.service;

import com.xuyi.blog.dto.ArticleDTO;
import com.xuyi.blog.dto.ArticleRequestDTO;
import com.xuyi.blog.dto.ArticleSummaryDTO;
import com.xuyi.blog.dto.PageResponse;
import com.xuyi.blog.entity.Article;
import com.xuyi.blog.entity.Category;
import com.xuyi.blog.entity.User;
import com.xuyi.blog.exception.ResourceNotFoundException;
import com.xuyi.blog.repository.ArticleRepository;
import com.xuyi.blog.repository.CategoryRepository;
import com.xuyi.blog.repository.TagRepository;
import com.xuyi.blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * 文章服务测试类
 * 
 * @author xuyi
 */
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ArticleService articleService;

    private User testUser;
    private Category testCategory;
    private Article testArticle;

    @BeforeEach
    void setUp() {
        testUser = new User("xuyi", "password");
        testUser.setId(1L);

        testCategory = new Category("技术", "技术相关文章");
        testCategory.setId(1L);

        testArticle = new Article("测试文章", "测试内容", testUser);
        testArticle.setId(1L);
        testArticle.setCategory(testCategory);
        testArticle.setStatus(Article.ArticleStatus.PUBLISHED);
        testArticle.setPublishedAt(LocalDateTime.now());
    }

    @Test
    void testGetPublishedArticles() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Article> articlePage = new PageImpl<>(List.of(testArticle));
        
        when(articleRepository.findByStatusOrderByIsTopDescPublishedAtDesc(
                eq(Article.ArticleStatus.PUBLISHED), eq(pageable)))
                .thenReturn(articlePage);

        // When
        PageResponse<ArticleSummaryDTO> result = articleService.getPublishedArticles(0, 10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("测试文章", result.getContent().get(0).getTitle());
        
        verify(articleRepository).findByStatusOrderByIsTopDescPublishedAtDesc(
                eq(Article.ArticleStatus.PUBLISHED), eq(pageable));
    }

    @Test
    void testGetArticleById() {
        // Given
        when(articleRepository.findById(1L)).thenReturn(Optional.of(testArticle));

        // When
        ArticleDTO result = articleService.getArticleById(1L);

        // Then
        assertNotNull(result);
        assertEquals("测试文章", result.getTitle());
        assertEquals("测试内容", result.getContent());
        assertEquals("xuyi", result.getAuthor().getUsername());
        
        verify(articleRepository).findById(1L);
    }

    @Test
    void testGetArticleByIdNotFound() {
        // Given
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            articleService.getArticleById(1L);
        });
        
        verify(articleRepository).findById(1L);
    }

    @Test
    void testCreateArticle() {
        // Given
        ArticleRequestDTO request = new ArticleRequestDTO();
        request.setTitle("新文章");
        request.setContent("新内容");
        request.setStatus("PUBLISHED");
        request.setCategoryId(1L);

        when(userRepository.findByUsername("xuyi")).thenReturn(Optional.of(testUser));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(articleRepository.save(any(Article.class))).thenReturn(testArticle);

        // When
        ArticleDTO result = articleService.createArticle(request, "xuyi");

        // Then
        assertNotNull(result);
        assertEquals("测试文章", result.getTitle());
        
        verify(userRepository).findByUsername("xuyi");
        verify(categoryRepository).findById(1L);
        verify(articleRepository).save(any(Article.class));
    }

    @Test
    void testCreateArticleUserNotFound() {
        // Given
        ArticleRequestDTO request = new ArticleRequestDTO();
        request.setTitle("新文章");
        request.setContent("新内容");

        when(userRepository.findByUsername("xuyi")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            articleService.createArticle(request, "xuyi");
        });
        
        verify(userRepository).findByUsername("xuyi");
        verify(articleRepository, never()).save(any(Article.class));
    }

    @Test
    void testUpdateArticle() {
        // Given
        ArticleRequestDTO request = new ArticleRequestDTO();
        request.setTitle("更新文章");
        request.setContent("更新内容");
        request.setStatus("PUBLISHED");

        when(articleRepository.findById(1L)).thenReturn(Optional.of(testArticle));
        when(articleRepository.save(any(Article.class))).thenReturn(testArticle);

        // When
        ArticleDTO result = articleService.updateArticle(1L, request);

        // Then
        assertNotNull(result);
        
        verify(articleRepository).findById(1L);
        verify(articleRepository).save(any(Article.class));
    }

    @Test
    void testDeleteArticle() {
        // Given
        when(articleRepository.findById(1L)).thenReturn(Optional.of(testArticle));

        // When
        articleService.deleteArticle(1L);

        // Then
        verify(articleRepository).findById(1L);
        verify(articleRepository).delete(testArticle);
    }

    @Test
    void testIncrementViewCount() {
        // When
        articleService.incrementViewCount(1L);

        // Then
        verify(articleRepository).incrementViewCount(1L);
    }

    @Test
    void testPublishArticle() {
        // Given
        testArticle.setStatus(Article.ArticleStatus.DRAFT);
        when(articleRepository.findById(1L)).thenReturn(Optional.of(testArticle));
        when(articleRepository.save(any(Article.class))).thenReturn(testArticle);

        // When
        articleService.publishArticle(1L);

        // Then
        verify(articleRepository).findById(1L);
        verify(articleRepository).save(any(Article.class));
    }
}
