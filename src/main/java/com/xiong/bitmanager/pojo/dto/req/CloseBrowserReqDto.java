package com.xiong.bitmanager.pojo.dto.req;

import lombok.Data;

import java.util.List;

/**
 * @ClassName CloseBrowserReqDto
 * @Description TODO
 * @Author admin
 * @Date 2024/7/16 17:51
 * @Version 1.0
 **/
@Data
public class CloseBrowserReqDto {
    private String id;

    public CloseBrowserReqDto() {
    }

    public CloseBrowserReqDto(String id) {
        this.id = id;
    }
}
