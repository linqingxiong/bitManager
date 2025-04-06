package com.xiong.bitmanager.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiong.bitmanager.common.config.mybatis.JsonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @ClassName ProxyConfig
 * @Description TODO
 * @Author admin
 * @Date 2025/3/15 20:08
 * @Version 1.0
 **/
// src/main/java/com/xiong/bitmanager/pojo/po/ProxyConfig.java
@Data
@TableName(value = "proxy_config", autoResultMap = true)
public class ProxyConfig {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String configName; // 配置名称
    private String host;
    private Integer port;
    private String proxyType;
    private String proxyUserName;
    private String proxyPassword;

    private Boolean enabled = true;
    private String description;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    // 增加代理检测信息字段
    @TableField(typeHandler = JsonTypeHandler.class)
    private ProxyInfo proxyInfo;

    // 增加嵌套类
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProxyInfo {
        private String ip;
        private String countryCode;
        private String region;
        private String city;
        private boolean used;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime usedTime;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createTime;
    }
}


