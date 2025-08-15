package com.xuyi.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 友链创建/更新请求DTO
 * 
 * @author xuyi
 */
@Getter
@Setter
public class FriendLinkRequestDTO {
    
    @NotBlank(message = "友链名称不能为空")
    @Size(max = 50, message = "友链名称长度不能超过50个字符")
    private String name;
    
    @NotBlank(message = "友链地址不能为空")
    private String url;
    
    private String avatar;
    
    @Size(max = 200, message = "友链描述长度不能超过200个字符")
    private String description;
    
    private Integer sortOrder = 0;
    
    private Boolean isActive = true;

    public FriendLinkRequestDTO() {}
}
