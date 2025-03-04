package com.xiong.bitmanager.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * @ClassName LbProduct
 * @Description TODO
 * @Author admin
 * @Date 2025/3/4 11:59
 * @Version 1.0
 **/
@Data
@TableName("lb_product")
public class LbProduct {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("title")
    private String title;

    @TableField("key")
    private String key;

    @TableField(exist = false)
    private List<LbPic> pics;
}
