package com.xiong.bitmanager.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName CorsConfig
 * @Description TODO
 * @Author admin
 * @Date 2025/2/27 15:35
 * @Version 1.0
 **/
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 允许所有路径
                        .allowedOriginPatterns("*") // 允许所有来源
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的 HTTP 方法
                        .allowedHeaders("*") // 允许所有请求头
                        .allowCredentials(true) // 允许携带凭证（如 Cookie）
                        .maxAge(3600); // 预检请求的缓存时间（单位：秒）
            }
        };
    }
}
