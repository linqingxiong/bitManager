package com.xiong.bitmanager.pojo.dto.feign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName AuthRequest
 * @Description TODO
 * @Author admin
 * @Date 2025/3/19 11:07
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    private String deviceId;
    private String token;
}
