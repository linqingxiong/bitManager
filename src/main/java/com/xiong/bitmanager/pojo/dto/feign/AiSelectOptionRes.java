package com.xiong.bitmanager.pojo.dto.feign;

import lombok.Data;

/**
 * @ClassName AiSelectOptionRes
 * @Description TODO
 * @Author admin
 * @Date 2025/3/18 19:40
 * @Version 1.0
 **/
@Data
public class AiSelectOptionRes {
    private String selectedValue;
    private String reasoning;
}
