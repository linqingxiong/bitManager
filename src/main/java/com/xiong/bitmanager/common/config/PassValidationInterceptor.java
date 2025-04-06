package com.xiong.bitmanager.common.config;

import com.xiong.bitmanager.service.TokenValidationScheduler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@Slf4j
public class PassValidationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取当前 isPass 状态
        boolean isValid = TokenValidationScheduler.isPass;
        if (!isValid) {
            // 直接返回错误码 4.1
            response.setStatus(401); // 或自定义状态码
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            try {

                response.getWriter().write("Token验证未通过");
            } catch (IOException e) {
                log.error("PassValidationInterceptor", e);
            }
            return false;
        }
        return true;
    }
}

