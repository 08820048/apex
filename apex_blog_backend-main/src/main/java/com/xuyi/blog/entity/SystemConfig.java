package com.xuyi.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统配置实体类
 * 
 * @author xuyi
 */
@Entity
@Table(name = "system_configs", indexes = {
    @Index(name = "idx_config_key", columnList = "configKey")
})
@Getter
@Setter
public class SystemConfig extends BaseEntity {

    @NotBlank(message = "配置键不能为空")
    @Size(max = 100, message = "配置键长度不能超过100个字符")
    @Column(name = "config_key", nullable = false, unique = true, length = 100)
    private String configKey;

    @Column(name = "config_value", columnDefinition = "TEXT")
    private String configValue;

    @Size(max = 200, message = "配置描述长度不能超过200个字符")
    @Column(name = "description", length = 200)
    private String description;

    public SystemConfig() {}

    public SystemConfig(String configKey, String configValue) {
        this.configKey = configKey;
        this.configValue = configValue;
    }

    public SystemConfig(String configKey, String configValue, String description) {
        this.configKey = configKey;
        this.configValue = configValue;
        this.description = description;
    }
}
