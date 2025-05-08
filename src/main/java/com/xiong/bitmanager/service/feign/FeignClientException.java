package com.xiong.bitmanager.service.feign;

/**
 * @ClassName FeignClientException
 * @Description TODO
 * @Author admin
 * @Date 2025/5/6 16:59
 * @Version 1.0
 **/
public class FeignClientException extends RuntimeException {
    private final int code;

    public FeignClientException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
