package com.xiong.bitmanager.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
}
