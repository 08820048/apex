package com.xuyi.blog.service;

import com.xuyi.blog.dto.*;
import com.xuyi.blog.entity.Article;
import com.xuyi.blog.entity.Category;
import com.xuyi.blog.entity.Tag;
import com.xuyi.blog.entity.User;
import com.xuyi.blog.exception.ResourceNotFoundException;
import com.xuyi.blog.repository.ArticleRepository;
import com.xuyi.blog.repository.CategoryRepository;
import com.xuyi.blog.repository.TagRepository;
import com.xuyi.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 文章服务类
 * 
 * @author xuyi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final EmailSubscriberService emailSubscriberService;

    /**
     * 获取已发布文章列表（分页）
     */
    @Cacheable(value = "articles", key = "'published_page_' + #page + '_' + #size")
    public PageResponse<ArticleSummaryDTO> getPublishedArticles(int page, int size) {
        // 先获取所有已发布文章（包含标签）
        List<Article> allArticles = articleRepository.findByStatusWithTagsOrderByIsTopDescPublishedAtDesc(
                Article.ArticleStatus.PUBLISHED);

        // 手动分页
        int start = page * size;
        int end = Math.min(start + size, allArticles.size());
        List<Article> pageArticles = allArticles.subList(start, end);

        // 转换为DTO
        List<ArticleSummaryDTO> dtoList = pageArticles.stream()
                .map(ArticleSummaryDTO::from)
                .collect(Collectors.toList());

        // 创建分页响应
        PageResponse<ArticleSummaryDTO> pageResponse = new PageResponse<>();
        pageResponse.setContent(dtoList);
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        pageResponse.setTotalElements((long) allArticles.size());
        pageResponse.setTotalPages((int) Math.ceil((double) allArticles.size() / size));
        pageResponse.setFirst(page == 0);
        pageResponse.setLast(end >= allArticles.size());
        pageResponse.setHasNext(end < allArticles.size());
        pageResponse.setHasPrevious(page > 0);

        return pageResponse;
    }

    /**
     * 根据分类获取文章列表
     */
    @Cacheable(value = "articles", key = "'category_' + #categoryId + '_page_' + #page + '_' + #size")
    public PageResponse<ArticleSummaryDTO> getArticlesByCategory(Long categoryId, int page, int size) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在"));
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articlePage = articleRepository.findByCategoryAndStatusOrderByPublishedAtDesc(
                category, Article.ArticleStatus.PUBLISHED, pageable);
        
        Page<ArticleSummaryDTO> dtoPage = articlePage.map(ArticleSummaryDTO::from);
        return PageResponse.of(dtoPage);
    }

    /**
     * 根据标签获取文章列表
     */
    @Cacheable(value = "articles", key = "'tag_' + #tagId + '_page_' + #page + '_' + #size")
    public PageResponse<ArticleSummaryDTO> getArticlesByTag(Long tagId, int page, int size) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("标签不存在"));
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articlePage = articleRepository.findByTagAndStatus(
                tag, Article.ArticleStatus.PUBLISHED, pageable);
        
        Page<ArticleSummaryDTO> dtoPage = articlePage.map(ArticleSummaryDTO::from);
        return PageResponse.of(dtoPage);
    }

    /**
     * 搜索文章
     */
    public PageResponse<ArticleSummaryDTO> searchArticles(String keyword, int page, int size) {
        if (!StringUtils.hasText(keyword)) {
            return getPublishedArticles(page, size);
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articlePage = articleRepository.searchByKeyword(
                keyword, Article.ArticleStatus.PUBLISHED, pageable);
        
        Page<ArticleSummaryDTO> dtoPage = articlePage.map(ArticleSummaryDTO::from);
        return PageResponse.of(dtoPage);
    }

    /**
     * 获取文章详情
     */
    @Cacheable(value = "article", key = "#id")
    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("文章不存在"));
        
        // 只有已发布的文章才能被访问（管理员除外）
        if (article.getStatus() != Article.ArticleStatus.PUBLISHED) {
            throw new ResourceNotFoundException("文章不存在");
        }
        
        return ArticleDTO.from(article);
    }

    /**
     * 增加文章浏览量
     */
    @Transactional
    @CacheEvict(value = "article", key = "#id")
    public void incrementViewCount(Long id) {
        articleRepository.incrementViewCount(id);
    }

    /**
     * 获取置顶文章
     */
    @Cacheable(value = "articles", key = "'top_articles'")
    public List<ArticleSummaryDTO> getTopArticles() {
        List<Article> articles = articleRepository.findByIsTopTrueAndStatusOrderByPublishedAtDesc(
                Article.ArticleStatus.PUBLISHED);
        return articles.stream()
                .map(ArticleSummaryDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * 获取最新文章
     */
    @Cacheable(value = "articles", key = "'latest_articles'")
    public List<ArticleSummaryDTO> getLatestArticles() {
        List<Article> articles = articleRepository.findTop10ByStatusOrderByPublishedAtDesc(
                Article.ArticleStatus.PUBLISHED);
        return articles.stream()
                .map(ArticleSummaryDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * 获取热门文章
     */
    @Cacheable(value = "articles", key = "'popular_articles'")
    public List<ArticleSummaryDTO> getPopularArticles() {
        List<Article> articles = articleRepository.findTop10ByStatusOrderByViewCountDesc(
                Article.ArticleStatus.PUBLISHED);
        return articles.stream()
                .map(ArticleSummaryDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * 获取相关文章
     */
    @Cacheable(value = "articles", key = "'related_' + #articleId")
    public List<ArticleSummaryDTO> getRelatedArticles(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("文章不存在"));

        if (article.getCategory() == null) {
            return List.of();
        }

        Pageable pageable = PageRequest.of(0, 5);
        List<Article> articles = articleRepository.findRelatedArticles(
                article.getCategory(), articleId, Article.ArticleStatus.PUBLISHED, pageable);

        return articles.stream()
                .map(ArticleSummaryDTO::from)
                .collect(Collectors.toList());
    }

    // ========== 管理功能 ==========

    /**
     * 创建文章
     */
    @Transactional
    @CacheEvict(value = {"articles", "article", "blogStats"}, allEntries = true)
    public ArticleDTO createArticle(ArticleRequestDTO request, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));

        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setSummary(request.getSummary());
        article.setContent(request.getContent());
        article.setCoverImage(request.getCoverImage());
        article.setAuthor(author);
        article.setIsTop(request.getIsTop());

        // 设置分类
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("分类不存在"));
            article.setCategory(category);
        }

        // 设置状态
        try {
            Article.ArticleStatus status = Article.ArticleStatus.valueOf(request.getStatus());
            article.setStatus(status);
            if (status == Article.ArticleStatus.PUBLISHED) {
                article.setPublishedAt(LocalDateTime.now());
            }
        } catch (IllegalArgumentException e) {
            article.setStatus(Article.ArticleStatus.DRAFT);
        }

        // 处理标签
        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) {
            Set<Tag> tags = processTagNames(request.getTagNames());
            article.setTags(tags);
        }

        Article savedArticle = articleRepository.save(article);
        log.info("创建文章成功: {}", savedArticle.getTitle());

        // 如果是发布状态，发送邮件通知
        if (savedArticle.getStatus() == Article.ArticleStatus.PUBLISHED) {
            try {
                emailSubscriberService.notifyNewArticle(savedArticle);
            } catch (Exception e) {
                log.error("发送新文章邮件通知失败: {}", e.getMessage());
            }
        }

        return ArticleDTO.from(savedArticle);
    }

    /**
     * 更新文章
     */
    @Transactional
    @CacheEvict(value = {"articles", "article", "blogStats"}, allEntries = true)
    public ArticleDTO updateArticle(Long id, ArticleRequestDTO request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("文章不存在"));

        article.setTitle(request.getTitle());
        article.setSummary(request.getSummary());
        article.setContent(request.getContent());
        article.setCoverImage(request.getCoverImage());
        article.setIsTop(request.getIsTop());

        // 更新分类
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("分类不存在"));
            article.setCategory(category);
        } else {
            article.setCategory(null);
        }

        // 更新状态
        Article.ArticleStatus oldStatus = article.getStatus();
        boolean shouldNotifySubscribers = false;
        try {
            Article.ArticleStatus newStatus = Article.ArticleStatus.valueOf(request.getStatus());
            article.setStatus(newStatus);

            // 如果从非发布状态变为发布状态，设置发布时间并标记需要发送通知
            if (oldStatus != Article.ArticleStatus.PUBLISHED &&
                newStatus == Article.ArticleStatus.PUBLISHED) {
                article.setPublishedAt(LocalDateTime.now());
                shouldNotifySubscribers = true;
            }
        } catch (IllegalArgumentException e) {
            // 保持原状态
        }

        // 更新标签
        article.getTags().clear();
        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) {
            Set<Tag> tags = processTagNames(request.getTagNames());
            article.setTags(tags);
        }

        Article savedArticle = articleRepository.save(article);
        log.info("更新文章成功: {}", savedArticle.getTitle());

        // 如果需要发送邮件通知
        if (shouldNotifySubscribers) {
            try {
                emailSubscriberService.notifyNewArticle(savedArticle);
            } catch (Exception e) {
                log.error("发送新文章邮件通知失败: {}", e.getMessage());
            }
        }

        return ArticleDTO.from(savedArticle);
    }

    /**
     * 删除文章
     */
    @Transactional
    @CacheEvict(value = {"articles", "article", "blogStats"}, allEntries = true)
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("文章不存在"));

        articleRepository.delete(article);
        log.info("删除文章成功: {}", article.getTitle());
    }

    /**
     * 发布文章
     */
    @Transactional
    @CacheEvict(value = {"articles", "article", "blogStats"}, allEntries = true)
    public ArticleDTO publishArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("文章不存在"));

        article.publish();
        Article savedArticle = articleRepository.save(article);
        log.info("发布文章成功: {}", savedArticle.getTitle());

        // 发送邮件通知订阅者
        try {
            emailSubscriberService.notifyNewArticle(savedArticle);
        } catch (Exception e) {
            log.error("发送新文章邮件通知失败: {}", e.getMessage());
        }

        return ArticleDTO.from(savedArticle);
    }

    /**
     * 取消发布文章
     */
    @Transactional
    @CacheEvict(value = {"articles", "article", "blogStats"}, allEntries = true)
    public ArticleDTO unpublishArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("文章不存在"));

        article.setStatus(Article.ArticleStatus.DRAFT);
        article.setPublishedAt(null);
        Article savedArticle = articleRepository.save(article);
        log.info("取消发布文章成功: {}", savedArticle.getTitle());

        return ArticleDTO.from(savedArticle);
    }

    /**
     * 归档文章
     */
    @Transactional
    @CacheEvict(value = {"articles", "article", "blogStats"}, allEntries = true)
    public void archiveArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("文章不存在"));

        article.archive();
        articleRepository.save(article);
        log.info("归档文章成功: {}", article.getTitle());
    }

    /**
     * 获取所有文章（管理用）
     */
    public PageResponse<ArticleSummaryDTO> getAllArticles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articlePage = articleRepository.findAll(pageable);

        Page<ArticleSummaryDTO> dtoPage = articlePage.map(ArticleSummaryDTO::from);
        return PageResponse.of(dtoPage);
    }

    /**
     * 处理标签名称，创建不存在的标签
     */
    private Set<Tag> processTagNames(List<String> tagNames) {
        Set<Tag> tags = new HashSet<>();

        for (String tagName : tagNames) {
            if (StringUtils.hasText(tagName)) {
                Tag tag = tagRepository.findByName(tagName.trim())
                        .orElseGet(() -> {
                            Tag newTag = new Tag(tagName.trim());
                            return tagRepository.save(newTag);
                        });
                tags.add(tag);
            }
        }

        return tags;
    }
}
