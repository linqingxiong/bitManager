package com.xiong.bitmanager.common.config;

import com.xiong.bitmanager.common.ResponseResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

/**
 * @ClassName GlobalExceptionHandler
 * @Description TODO
 * @Author admin
 * @Date 2025/3/7 14:29
 * @Version 1.0
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 在原有基础上添加文件上传异常处理
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseResult<?> handleMaxSizeException(MaxUploadSizeExceededException e) {
        return ResponseResult.error(413, "文件大小超过限制");
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseResult<?> handleMultipartException(MultipartException e) {
        return ResponseResult.error(400, "文件上传请求格式错误");
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult<?> handleException(Exception e) {
        return ResponseResult.error(500, e.getMessage());
    }
}
