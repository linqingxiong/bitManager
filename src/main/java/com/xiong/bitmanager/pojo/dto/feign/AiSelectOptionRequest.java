package com.xiong.bitmanager.pojo.dto.feign;

import lombok.Data;

import java.util.List;

/**
 * @ClassName AiSelectOptionRequest
 * @Description TODO
 * @Author admin
 * @Date 2025/3/19 1:09
 * @Version 1.0
 **/
@Data
public class AiSelectOptionRequest {
    private String jsonData;
    private List<String> contents;
    private String targetKey;
    private List<String> options;
}
