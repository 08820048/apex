package com.xuyi.blog.service;

import com.xuyi.blog.dto.FriendLinkDTO;
import com.xuyi.blog.dto.FriendLinkRequestDTO;
import com.xuyi.blog.entity.FriendLink;
import com.xuyi.blog.exception.ResourceNotFoundException;
import com.xuyi.blog.repository.FriendLinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 友链服务类
 * 
 * @author xuyi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FriendLinkService {

    private final FriendLinkRepository friendLinkRepository;

    /**
     * 获取活跃的友链
     */
    // @Cacheable(value = "friendLinks", key = "'active'")
    public List<FriendLinkDTO> getActiveFriendLinks() {
        List<FriendLink> friendLinks = friendLinkRepository.findByIsActiveTrueOrderBySortOrderAsc();
        return friendLinks.stream()
                .map(FriendLinkDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有友链（管理用）
     */
    // @Cacheable(value = "friendLinks", key = "'all'")
    public List<FriendLinkDTO> getAllFriendLinks() {
        List<FriendLink> friendLinks = friendLinkRepository.findAllByOrderBySortOrderAsc();
        return friendLinks.stream()
                .map(FriendLinkDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID获取友链
     */
    // @Cacheable(value = "friendLink", key = "#id")
    public FriendLinkDTO getFriendLinkById(Long id) {
        FriendLink friendLink = friendLinkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("友链不存在"));
        return FriendLinkDTO.from(friendLink);
    }

    /**
     * 创建友链
     */
    @Transactional
    // @CacheEvict(value = {"friendLinks", "friendLink"}, allEntries = true)
    public FriendLinkDTO createFriendLink(FriendLinkRequestDTO request) {
        FriendLink friendLink = new FriendLink();
        friendLink.setName(request.getName());
        friendLink.setUrl(request.getUrl());
        friendLink.setAvatar(request.getAvatar());
        friendLink.setDescription(request.getDescription());
        friendLink.setSortOrder(request.getSortOrder());
        friendLink.setIsActive(request.getIsActive());

        FriendLink savedFriendLink = friendLinkRepository.save(friendLink);
        log.info("创建友链成功: {}", savedFriendLink.getName());
        
        return FriendLinkDTO.from(savedFriendLink);
    }

    /**
     * 更新友链
     */
    @Transactional
    // @CacheEvict(value = {"friendLinks", "friendLink"}, allEntries = true)
    public FriendLinkDTO updateFriendLink(Long id, FriendLinkRequestDTO request) {
        FriendLink friendLink = friendLinkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("友链不存在"));

        friendLink.setName(request.getName());
        friendLink.setUrl(request.getUrl());
        friendLink.setAvatar(request.getAvatar());
        friendLink.setDescription(request.getDescription());
        friendLink.setSortOrder(request.getSortOrder());
        friendLink.setIsActive(request.getIsActive());

        FriendLink savedFriendLink = friendLinkRepository.save(friendLink);
        log.info("更新友链成功: {}", savedFriendLink.getName());
        
        return FriendLinkDTO.from(savedFriendLink);
    }

    /**
     * 删除友链
     */
    @Transactional
    // @CacheEvict(value = {"friendLinks", "friendLink"}, allEntries = true)
    public void deleteFriendLink(Long id) {
        FriendLink friendLink = friendLinkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("友链不存在"));
        
        friendLinkRepository.delete(friendLink);
        log.info("删除友链成功: {}", friendLink.getName());
    }

    /**
     * 设置友链状态
     */
    @Transactional
    // @CacheEvict(value = {"friendLinks", "friendLink"}, allEntries = true)
    public void setActive(Long id, boolean active) {
        FriendLink friendLink = friendLinkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("友链不存在"));
        
        friendLink.setIsActive(active);
        friendLinkRepository.save(friendLink);
        log.info("设置友链状态: {} -> {}", friendLink.getName(), active);
    }
}
