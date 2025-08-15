package com.xuyi.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 分类请求DTO
 * 
 * @author xuyi
 */
@Data
@Schema(description = "分类请求对象")
public class CategoryRequestDTO {

    @NotBlank(message = "分类名称不能为空")
    @Size(max = 50, message = "分类名称不能超过50个字符")
    @Schema(description = "分类名称", example = "技术分享")
    private String name;

    @Size(max = 200, message = "分类描述不能超过200个字符")
    @Schema(description = "分类描述", example = "分享各种技术文章和教程")
    private String description;

    @Schema(description = "排序顺序", example = "1")
    private Integer sortOrder = 0;
}
