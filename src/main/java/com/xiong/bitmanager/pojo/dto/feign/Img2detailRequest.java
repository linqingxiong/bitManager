package com.xiong.bitmanager.pojo.dto.feign;

import lombok.Data;

import java.util.List;

/**
 * @ClassName Img2detailRequest
 * @Description TODO
 * @Author admin
 * @Date 2025/3/12 11:53
 * @Version 1.0
 **/
@Data
public class Img2detailRequest {
    private String prompt;
    private List<String> images;
}
