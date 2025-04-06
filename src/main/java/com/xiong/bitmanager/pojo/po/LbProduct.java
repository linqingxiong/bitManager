package com.xiong.bitmanager.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
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

    @TableField("first_name")
    private String firstName;

    @TableField("last_name")
    private String lastName;

    @TableField("title1")
    private String title1;

    @TableField("remark")
    private String remark;

    @TableField("title2")
    private String title2;

    @TableField("desc1")
    private String desc1;

    @TableField("title3")
    private String title3;

    @TableField("price")
    private String price;

    @TableField("shipping_cost")
    private String shippingCost;

    @TableField("desc2")
    private String desc2;

    @TableField("addr_keyword")
    private String addrKeyword;

    @TableField("category")
    private String category;

    @TableField("size")
    private String size;

    @TableField("cn_title")
    private String cnTitle;

    @TableField("cn_desc")
    private String cnDesc;

    @TableField(exist = false)
    private List<String> pics;

    @TableField(exist = false)
    private int putOnlineTaskSuccessCount;

    @TableField(exist = false)
    private int putOnlineTaskTotalCount;

    @TableField(exist = false)
    private int browserTotalCount;

    @TableField(exist = false)
    private int browserSuccessCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
