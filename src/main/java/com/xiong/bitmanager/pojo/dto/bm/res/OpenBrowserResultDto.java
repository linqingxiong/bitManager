package com.xiong.bitmanager.pojo.dto.bm.res;

import lombok.Data;

/**
 * @ClassName OpenBrowserResult
 * @Description TODO
 * @Author admin
 * @Date 2024/7/16 17:27
 * @Version 1.0
 **/
@Data
public class OpenBrowserResultDto {
    private String ws;
    private String http;
    private String coreVersion;
    private String driver;
    private long seq;
    private String name;
    private String remark;
    private String groupID;
    private long pid;
}
