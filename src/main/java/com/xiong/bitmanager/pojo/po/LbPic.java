package com.xiong.bitmanager.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName LbPic
 * @Description TODO
 * @Author admin
 * @Date 2025/3/4 12:00
 * @Version 1.0
 **/
@Data
@TableName("lb_pic")
public class LbPic {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("img_url")
    private String imgUrl;

    @TableField("product_id")
    private Long productId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
