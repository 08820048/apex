package com.xuyi.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 标签请求DTO
 * 
 * @author xuyi
 */
@Data
@Schema(description = "标签请求对象")
public class TagRequestDTO {

    @NotBlank(message = "标签名称不能为空")
    @Size(max = 30, message = "标签名称不能超过30个字符")
    @Schema(description = "标签名称", example = "Java")
    private String name;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "颜色格式不正确，应为十六进制颜色值")
    @Schema(description = "标签颜色", example = "#007396")
    private String color = "#007bff";
}
