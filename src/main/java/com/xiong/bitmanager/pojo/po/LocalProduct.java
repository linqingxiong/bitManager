package com.xiong.bitmanager.pojo.po;

import lombok.Data;

import java.util.List;

/**
 * @ClassName LocalProduct
 * @Description TODO
 * @Author admin
 * @Date 2025/3/20 11:34
 * @Version 1.0
 **/
@Data
public class LocalProduct {
    private String description; // 产品简单描述
    private List<String> images; // 产品图片路径列表
    private String detail; // 产品详细描述
}
