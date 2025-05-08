package com.xiong.bitmanager.service.feign;

import com.xiong.bitmanager.service.TokenValidationScheduler;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.ObjectProvider;

/**
 * @ClassName CustomFeignErrorDecoder
 * @Description TODO
 * @Author admin
 * @Date 2025/5/6 16:58
 * @Version 1.0
 **/
public class CustomFeignErrorDecoder implements ErrorDecoder {
    private final ObjectProvider<TokenValidationScheduler> tokenValidationScheduler;

    public CustomFeignErrorDecoder(ObjectProvider<TokenValidationScheduler> tokenValidationScheduler) {
        this.tokenValidationScheduler = tokenValidationScheduler;
    }
    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();
        String reason = response.reason();
        // 从spring上下文获取注册过的 spring bean
        this.tokenValidationScheduler.getIfAvailable().setPass(false);
        return new RuntimeException(reason);
    }
}
