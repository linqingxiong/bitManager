package com.xiong.bitmanager.pojo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiong.bitmanager.pojo.po.ProxyConfig;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName ProxyConfigMapper
 * @Description TODO
 * @Author admin
 * @Date 2025/3/15 20:14
 * @Version 1.0
 **/
// src/main/java/com/xiong/bitmanager/pojo/dao/ProxyConfigMapper.java
public interface ProxyConfigMapper extends BaseMapper<ProxyConfig> {
    @Select("SELECT * FROM proxy_config WHERE enabled = 1")
    List<ProxyConfig> selectActiveConfigs();
}

