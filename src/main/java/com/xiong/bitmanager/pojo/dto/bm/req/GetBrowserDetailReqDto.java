package com.xiong.bitmanager.pojo.dto.bm.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @ClassName GetBrowserDetailReqDto
 * @Description TODO
 * @Author admin
 * @Date 2024/12/26 16:41
 * @Version 1.0
 **/
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GetBrowserDetailReqDto {
    private String id;
}
