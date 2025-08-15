package com.xuyi.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 作品集创建/更新请求DTO
 * 
 * @author xuyi
 */
@Getter
@Setter
public class PortfolioRequestDTO {
    
    @NotBlank(message = "作品名称不能为空")
    @Size(max = 100, message = "作品名称长度不能超过100个字符")
    private String name;
    
    private String description;
    
    private String url;
    
    private String coverImage;
    
    @Size(max = 500, message = "技术栈长度不能超过500个字符")
    private String technologies;
    
    private Integer sortOrder = 0;
    
    private Boolean isFeatured = false;

    public PortfolioRequestDTO() {}
}
