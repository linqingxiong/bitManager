package com.xiong.bitmanager.pojo.dto.feign;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName LoginRequest
 * @Description TODO
 * @Author admin
 * @Date 2025/3/7 1:07
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String usercode;
}
