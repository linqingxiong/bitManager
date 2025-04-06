package com.xiong.bitmanager.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiong.bitmanager.pojo.dao.ProxyConfigMapper;
import com.xiong.bitmanager.pojo.po.ProxyConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName ProxyConfigService
 * @Description TODO
 * @Author admin
 * @Date 2025/3/15 20:14
 * @Version 1.0
 **/
// src/main/java/com/xiong/bitmanager/service/ProxyConfigService.java
@Service
public class ProxyConfigService extends ServiceImpl<ProxyConfigMapper, ProxyConfig> {
    public List<ProxyConfig> getActiveConfigs() {
        return list(new LambdaQueryWrapper<ProxyConfig>().eq(ProxyConfig::getEnabled, true));
    }

    public IPage<ProxyConfig> getProxyConfigPage(int page, int size, String search) {
        LambdaQueryWrapper<ProxyConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(search), ProxyConfig::getConfigName, search)
                .or().like(StringUtils.isNotBlank(search), ProxyConfig::getProxyInfo, search)
                .or().like(StringUtils.isNotBlank(search), ProxyConfig::getHost, search)
                .orderByDesc(ProxyConfig::getCreateTime);

        return page(new Page<>(page, size), queryWrapper);
    }
}

