package com.xiong.bitmanager.pojo.dto.req;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName FingerprintRandomReqDto
 * @Description TODO
 * @Author admin
 * @Date 2024/12/26 17:31
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
public class FingerprintRandomReqDto {
    private String browserId;

    public FingerprintRandomReqDto(String browserId) {
        this.browserId = browserId;
    }

    public FingerprintRandomReqDto() {
    }
}
