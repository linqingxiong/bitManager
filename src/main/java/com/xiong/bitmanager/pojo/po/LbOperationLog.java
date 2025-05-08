package com.xiong.bitmanager.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName LbOperationLog
 * @Description 操作日志实体类
 * @Author admin
 * @Date 2025/3/4 11:59
 * @Version 1.0
 **/
@Data
@TableName("lb_operation_log")
public class LbOperationLog {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("product_id")
    private Long productId;

    @TableField("browser_id")
    private String browserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime operationTime;
    @TableField("screen_shot_url")
    private String screenShotUrl;

    @TableField("operation_type")
    private String operationType;

    @TableField("operation_param")
    private String operationParam;

    @TableField("operation_result")
    private String operationResult;

    @TableField("error_message")
    private String errorMessage;
}
