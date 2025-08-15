package com.xuyi.blog.service;

import com.xuyi.blog.dto.CategoryDTO;
import com.xuyi.blog.dto.CategoryRequestDTO;
import com.xuyi.blog.entity.Category;
import com.xuyi.blog.exception.ResourceNotFoundException;
import com.xuyi.blog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类服务类
 * 
 * @author xuyi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 获取所有分类
     */
    @Cacheable(value = "categories", key = "'all'")
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAllByOrderBySortOrderAsc();
        return categories.stream()
                .map(CategoryDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID获取分类
     */
    @Cacheable(value = "categories", key = "#id")
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在"));
        return CategoryDTO.from(category);
    }

    /**
     * 根据名称获取分类
     */
    public CategoryDTO getCategoryByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在"));
        return CategoryDTO.from(category);
    }

    /**
     * 创建分类（使用DTO）
     */
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryDTO createCategory(CategoryRequestDTO request) {
        return createCategory(request.getName(), request.getDescription(), request.getSortOrder());
    }

    /**
     * 创建分类
     */
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryDTO createCategory(String name, String description, Integer sortOrder) {
        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("分类名称已存在");
        }

        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setSortOrder(sortOrder != null ? sortOrder : 0);

        Category savedCategory = categoryRepository.save(category);
        log.info("创建分类成功: {}", savedCategory.getName());
        
        return CategoryDTO.from(savedCategory);
    }

    /**
     * 更新分类（使用DTO）
     */
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryDTO updateCategory(Long id, CategoryRequestDTO request) {
        return updateCategory(id, request.getName(), request.getDescription(), request.getSortOrder());
    }

    /**
     * 更新分类
     */
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryDTO updateCategory(Long id, String name, String description, Integer sortOrder) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在"));

        // 检查名称是否重复（排除自己）
        if (!category.getName().equals(name) && categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("分类名称已存在");
        }

        category.setName(name);
        category.setDescription(description);
        category.setSortOrder(sortOrder != null ? sortOrder : 0);

        Category savedCategory = categoryRepository.save(category);
        log.info("更新分类成功: {}", savedCategory.getName());
        
        return CategoryDTO.from(savedCategory);
    }

    /**
     * 删除分类
     */
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在"));
        
        categoryRepository.delete(category);
        log.info("删除分类成功: {}", category.getName());
    }

    /**
     * 检查分类名称是否存在
     */
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
}
