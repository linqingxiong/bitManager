package com.xiong.bitmanager.pojo.dto.req;

import lombok.Data;

import java.util.List;
import java.util.PrimitiveIterator;

/**
 * @ClassName GetPidReqDto
 * @Description TODO
 * @Author admin
 * @Date 2024/7/16 17:59
 * @Version 1.0
 **/
@Data
public class GetPidReqDto {
    private List<String> ids;

    public GetPidReqDto(List<String> ids) {
        this.ids = ids;
    }

    public GetPidReqDto() {

    }
    public GetPidReqDto(String id) {
        this.ids = List.of(id);
    }

}
