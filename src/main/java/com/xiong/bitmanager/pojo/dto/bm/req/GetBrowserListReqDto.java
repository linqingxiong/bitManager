package com.xiong.bitmanager.pojo.dto.bm.req;

import lombok.Data;

/**
 * @ClassName GetBrowserListReqDto
 * @Description TODO
 * @Author admin
 * @Date 2024/7/16 17:32
 * @Version 1.0
 **/
@Data
public class GetBrowserListReqDto {
    private int page;
    private int pageSize;
    private String groupID;
    private String name;
    private String sortProperties;
    private String sortDirection;

    public GetBrowserListReqDto() {
    }

    public GetBrowserListReqDto(int page, int pageSize, String name) {
        this.page = page;
        this.pageSize = pageSize;
        this.name = name;
    }
}
