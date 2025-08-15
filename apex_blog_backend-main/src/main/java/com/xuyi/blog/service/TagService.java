package com.xuyi.blog.service;

import com.xuyi.blog.dto.TagDTO;
import com.xuyi.blog.entity.Tag;
import com.xuyi.blog.exception.ResourceNotFoundException;
import com.xuyi.blog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签服务类
 * 
 * @author xuyi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagRepository tagRepository;

    /**
     * 获取所有标签
     */
    @Cacheable(value = "tags", key = "'all'")
    public List<TagDTO> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(TagDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * 获取热门标签
     */
    @Cacheable(value = "tags", key = "'popular'")
    public List<TagDTO> getPopularTags() {
        List<Tag> tags = tagRepository.findPopularTags();
        return tags.stream()
                .map(TagDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID获取标签
     */
    @Cacheable(value = "tags", key = "#id")
    public TagDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("标签不存在"));
        return TagDTO.from(tag);
    }

    /**
     * 根据名称获取标签
     */
    public TagDTO getTagByName(String name) {
        Tag tag = tagRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("标签不存在"));
        return TagDTO.from(tag);
    }

    /**
     * 创建标签
     */
    @Transactional
    @CacheEvict(value = "tags", allEntries = true)
    public TagDTO createTag(String name, String color) {
        if (tagRepository.existsByName(name)) {
            throw new IllegalArgumentException("标签名称已存在");
        }

        Tag tag = new Tag();
        tag.setName(name);
        tag.setColor(color != null ? color : "#007bff");

        Tag savedTag = tagRepository.save(tag);
        log.info("创建标签成功: {}", savedTag.getName());
        
        return TagDTO.from(savedTag);
    }

    /**
     * 更新标签
     */
    @Transactional
    @CacheEvict(value = "tags", allEntries = true)
    public TagDTO updateTag(Long id, String name, String color) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("标签不存在"));

        // 检查名称是否重复（排除自己）
        if (!tag.getName().equals(name) && tagRepository.existsByName(name)) {
            throw new IllegalArgumentException("标签名称已存在");
        }

        tag.setName(name);
        tag.setColor(color != null ? color : "#007bff");

        Tag savedTag = tagRepository.save(tag);
        log.info("更新标签成功: {}", savedTag.getName());
        
        return TagDTO.from(savedTag);
    }

    /**
     * 删除标签
     */
    @Transactional
    @CacheEvict(value = "tags", allEntries = true)
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("标签不存在"));
        
        tagRepository.delete(tag);
        log.info("删除标签成功: {}", tag.getName());
    }

    /**
     * 检查标签名称是否存在
     */
    public boolean existsByName(String name) {
        return tagRepository.existsByName(name);
    }
}
