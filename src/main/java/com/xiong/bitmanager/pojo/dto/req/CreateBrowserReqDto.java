package com.xiong.bitmanager.pojo.dto.req;

import com.xiong.bitmanager.pojo.dto.BrowserFingerPrint;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName CreateBrowserReqDto
 * @Description TODO
 * @Author admin
 * @Date 2024/12/23 11:28
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
public class CreateBrowserReqDto {
    private BrowserFingerPrint browserFingerPrint;
}
