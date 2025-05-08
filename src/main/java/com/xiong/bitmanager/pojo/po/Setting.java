package com.xiong.bitmanager.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("setting")
public class Setting {
    @TableId(type = IdType.INPUT)
    @TableField("setting_key")
    private String settingKey;

    @TableField("setting_value")
    private String settingValue;

    @TableField("description")
    private String description;

    @TableField("type")
    private String type;
}
