package com.xiong.bitmanager.pojo.dto.req;

import com.xiong.bitmanager.pojo.dto.feign.Img2detailRequest;
import lombok.Data;

import java.util.List;

/**
 * @ClassName Img2detailDto
 * @Description TODO
 * @Author admin
 * @Date 2025/3/12 13:33
 * @Version 1.0
 **/
@Data
public class Img2detailDto extends Img2detailRequest {
    private List<String> descKws;
    private List<String> titleKws;

}
