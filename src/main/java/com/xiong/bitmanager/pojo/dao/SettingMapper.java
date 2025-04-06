package com.xiong.bitmanager.pojo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiong.bitmanager.pojo.po.Setting;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName LbOperationLogMapper
 * @Description 操作日志Mapper接口
 * @Author admin
 * @Date 2025/3/4 12:05
 * @Version 1.0
 **/
@Mapper
public interface SettingMapper extends BaseMapper<Setting> {
}
