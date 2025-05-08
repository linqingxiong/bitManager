package com.xiong.bitmanager.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;
    @Autowired
    private PassValidationInterceptor passValidationInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/files/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截所有 /api/** 的请求
        registry.addInterceptor(passValidationInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/**")
        ;
    }

}
