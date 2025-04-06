package com.xiong.bitmanager.pojo.dto.bm.req;

import com.xiong.bitmanager.pojo.dto.bm.BrowserFingerPrint;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @ClassName UpdatePartialReqDto
 * @Description TODO
 * @Author admin
 * @Date 2024/12/26 16:31
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
public class UpdatePartialReqDto {
    private List<String> ids;
    private BrowserFingerPrint browserFingerPrint;
    private String name;
}
