package com.xiong.bitmanager.pojo.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ChatRequestDto
 * @Description TODO
 * @Author admin
 * @Date 2025/3/18 11:47
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequestDto {
    private String prompt;
}
