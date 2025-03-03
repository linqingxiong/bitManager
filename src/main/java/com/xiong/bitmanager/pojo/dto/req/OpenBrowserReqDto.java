package com.xiong.bitmanager.pojo.dto.req;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName OpenBrowserResult
 * @Description TODO
 * @Author admin
 * @Date 2024/7/16 17:27
 * @Version 1.0
 **/
@Data
public class OpenBrowserReqDto {
    private String id;
    private List<String> args = new ArrayList<>();
    private boolean extractIp;
    private boolean queue;

    public OpenBrowserReqDto() {
    }

    public OpenBrowserReqDto(String id) {
        this.id = id;
    }
}
