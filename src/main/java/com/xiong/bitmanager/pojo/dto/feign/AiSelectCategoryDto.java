package com.xiong.bitmanager.pojo.dto.feign;

import lombok.Data;

import java.util.List;

/**
 * @ClassName AiSelectOptionDto
 * @Description TODO
 * @Author admin
 * @Date 2025/3/18 19:25
 * @Version 1.0
 **/
@Data
public class AiSelectCategoryDto {
    private String title;
    private String desc;
    private List<String> categoryNames;
}
