package com.xiong.bitmanager.pojo.dto.res;

import lombok.Data;

/**
 * @ClassName PutOnlineTaskStat
 * @Description TODO
 * @Author admin
 * @Date 2025/3/14 9:25
 * @Version 1.0
 **/
@Data
public class PutOnlineTaskStat {
    private Long productId;
    private int failCount;
    private int successCount;
}
