package com.xuyi.blog.controller;

import com.xuyi.blog.service.RssService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * RSS订阅控制器
 * 
 * @author xuyi
 */
@RestController
@RequestMapping("/rss")
@RequiredArgsConstructor
@Tag(name = "RSS订阅", description = "RSS订阅相关接口")
public class RssController {

    private final RssService rssService;

    @GetMapping(value = "/feed.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "RSS订阅源", description = "获取RSS 2.0格式的订阅源")
    public ResponseEntity<String> getRssFeed() {
        String rssFeed = rssService.generateRssFeed();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .header("Cache-Control", "public, max-age=3600")
                .body(rssFeed);
    }

    @GetMapping(value = "/atom.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "Atom订阅源", description = "获取Atom 1.0格式的订阅源")
    public ResponseEntity<String> getAtomFeed() {
        String atomFeed = rssService.generateAtomFeed();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .header("Cache-Control", "public, max-age=3600")
                .body(atomFeed);
    }

    @GetMapping(value = "/category/{categoryId}/feed.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "分类RSS订阅源", description = "获取指定分类的RSS订阅源")
    public ResponseEntity<String> getCategoryRssFeed(
            @Parameter(description = "分类ID") @PathVariable Long categoryId) {
        String rssFeed = rssService.generateCategoryRssFeed(categoryId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .header("Cache-Control", "public, max-age=3600")
                .body(rssFeed);
    }

    // 兼容性路径
    @GetMapping(value = "/feed", produces = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "RSS订阅源（兼容路径）", description = "获取RSS订阅源的兼容路径")
    public ResponseEntity<String> getRssFeedCompat() {
        return getRssFeed();
    }

    @GetMapping(value = "/rss.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "RSS订阅源（兼容路径）", description = "获取RSS订阅源的兼容路径")
    public ResponseEntity<String> getRssXml() {
        return getRssFeed();
    }
}
