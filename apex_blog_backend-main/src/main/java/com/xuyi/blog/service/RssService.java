package com.xuyi.blog.service;

import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import com.xuyi.blog.entity.Article;
import com.xuyi.blog.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * RSS订阅服务类
 * 
 * @author xuyi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RssService {

    private final ArticleRepository articleRepository;

    @Value("${blog.title:Xuyi's Blog}")
    private String blogTitle;

    @Value("${blog.description:个人技术博客}")
    private String blogDescription;

    @Value("${blog.url:http://localhost:8888}")
    private String blogUrl;

    @Value("${blog.author:xuyi}")
    private String blogAuthor;

    @Value("${blog.rss.max-items:20}")
    private int maxItems;

    /**
     * 生成RSS Feed
     */
    public String generateRssFeed() {
        try {
            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType("rss_2.0");
            feed.setTitle(blogTitle);
            feed.setLink(blogUrl);
            feed.setDescription(blogDescription);
            feed.setLanguage("zh-CN");
            feed.setAuthor(blogAuthor);
            feed.setPublishedDate(new Date());

            // 获取最新文章
            List<Article> articles = articleRepository.findTop20ByStatusOrderByPublishedAtDesc(
                    Article.ArticleStatus.PUBLISHED);

            List<SyndEntry> entries = new ArrayList<>();
            for (Article article : articles) {
                if (entries.size() >= maxItems) {
                    break;
                }

                SyndEntry entry = new SyndEntryImpl();
                entry.setTitle(article.getTitle());
                entry.setLink(blogUrl + "/articles/" + article.getId());
                entry.setAuthor(article.getAuthor().getNickname() != null ? 
                               article.getAuthor().getNickname() : article.getAuthor().getUsername());

                // 设置发布时间
                if (article.getPublishedAt() != null) {
                    entry.setPublishedDate(Date.from(article.getPublishedAt()
                            .atZone(ZoneId.systemDefault()).toInstant()));
                }

                // 设置内容
                SyndContent description = new SyndContentImpl();
                description.setType("text/html");
                
                // 使用摘要或截取内容前200字符
                String content = article.getSummary();
                if (content == null || content.trim().isEmpty()) {
                    content = article.getContent();
                    if (content.length() > 200) {
                        content = content.substring(0, 200) + "...";
                    }
                }
                description.setValue(content);
                entry.setDescription(description);

                // 设置分类
                if (article.getCategory() != null) {
                    List<SyndCategory> categories = new ArrayList<>();
                    SyndCategory category = new SyndCategoryImpl();
                    category.setName(article.getCategory().getName());
                    categories.add(category);
                    entry.setCategories(categories);
                }

                entries.add(entry);
            }

            feed.setEntries(entries);

            // 生成RSS XML
            SyndFeedOutput output = new SyndFeedOutput();
            return output.outputString(feed);

        } catch (FeedException e) {
            log.error("生成RSS Feed失败: {}", e.getMessage(), e);
            throw new RuntimeException("生成RSS Feed失败", e);
        }
    }

    /**
     * 生成Atom Feed
     */
    public String generateAtomFeed() {
        try {
            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType("atom_1.0");
            feed.setTitle(blogTitle);
            feed.setLink(blogUrl);
            feed.setDescription(blogDescription);
            feed.setLanguage("zh-CN");
            feed.setAuthor(blogAuthor);
            feed.setPublishedDate(new Date());

            // 获取最新文章
            List<Article> articles = articleRepository.findTop20ByStatusOrderByPublishedAtDesc(
                    Article.ArticleStatus.PUBLISHED);

            List<SyndEntry> entries = new ArrayList<>();
            for (Article article : articles) {
                if (entries.size() >= maxItems) {
                    break;
                }

                SyndEntry entry = new SyndEntryImpl();
                entry.setTitle(article.getTitle());
                entry.setLink(blogUrl + "/articles/" + article.getId());
                entry.setAuthor(article.getAuthor().getNickname() != null ? 
                               article.getAuthor().getNickname() : article.getAuthor().getUsername());

                // 设置发布时间
                if (article.getPublishedAt() != null) {
                    entry.setPublishedDate(Date.from(article.getPublishedAt()
                            .atZone(ZoneId.systemDefault()).toInstant()));
                }

                // 设置内容
                SyndContent content = new SyndContentImpl();
                content.setType("html");
                content.setValue(article.getContent());
                entry.setDescription(content);

                // 设置分类
                if (article.getCategory() != null) {
                    List<SyndCategory> categories = new ArrayList<>();
                    SyndCategory category = new SyndCategoryImpl();
                    category.setName(article.getCategory().getName());
                    categories.add(category);
                    entry.setCategories(categories);
                }

                entries.add(entry);
            }

            feed.setEntries(entries);

            // 生成Atom XML
            SyndFeedOutput output = new SyndFeedOutput();
            return output.outputString(feed);

        } catch (FeedException e) {
            log.error("生成Atom Feed失败: {}", e.getMessage(), e);
            throw new RuntimeException("生成Atom Feed失败", e);
        }
    }

    /**
     * 生成分类RSS Feed
     */
    public String generateCategoryRssFeed(Long categoryId) {
        // 这里可以实现按分类生成RSS的逻辑
        // 为了简化，暂时返回通用RSS
        return generateRssFeed();
    }

    /**
     * 清除RSS缓存
     */
    public void clearRssCache() {
        // 这个方法可以在发布新文章时调用，清除RSS缓存
        log.info("清除RSS缓存");
    }
}
