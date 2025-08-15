package com.xuyi.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 文章创建/更新请求DTO
 * 
 * @author xuyi
 */
@Getter
@Setter
public class ArticleRequestDTO {
    
    @NotBlank(message = "文章标题不能为空")
    @Size(max = 200, message = "文章标题长度不能超过200个字符")
    private String title;
    
    private String summary;
    
    @NotBlank(message = "文章内容不能为空")
    private String content;
    
    private String coverImage;
    
    private Long categoryId;
    
    private String status = "DRAFT";
    
    private Boolean isTop = false;
    
    private List<String> tagNames;

    public ArticleRequestDTO() {}
}
