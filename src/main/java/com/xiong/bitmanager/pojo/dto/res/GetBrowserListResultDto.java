package com.xiong.bitmanager.pojo.dto.res;

import lombok.Data;

import java.util.List;

/**
 * @ClassName GetBrowserListResultDto
 * @Description TODO
 * @Author admin
 * @Date 2024/7/16 17:35
 * @Version 1.0
 **/
@Data
public class GetBrowserListResultDto {
    private int page;
    private int pageSize;
    private int totalNum;
    private List<BrowerInfo> list;

    @Data
    public static class BrowerInfo {
        private String id;
        private String name;
    }
}
