package com.xiong.bitmanager.pojo.dto.req;

import lombok.Data;

/**
 * @ClassName PutProductsOnlineDto
 * @Description TODO
 * @Author admin
 * @Date 2025/3/11 10:11
 * @Version 1.0
 **/
@Data
public class PutProductsOnlineDto {
    private String browserId;
    private Long id;
    private Long delayMs = System.currentTimeMillis();
    private String proxyIp;
}
