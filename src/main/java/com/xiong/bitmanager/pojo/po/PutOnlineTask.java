package com.xiong.bitmanager.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName PutOnlineTask
 * @Description TODO
 * @Author admin
 * @Date 2025/3/13 14:05
 * @Version 1.0
 **/
@Data
@TableName("put_online_task")
public class PutOnlineTask {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long productId;
    private String browserId;
    private String proxyIp;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Long scheduleTime;
    private String status;
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private int retryCount;

    @Version
    private Integer version; // 乐观锁版本号
}
