package com.xiong.bitmanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiong.bitmanager.common.ResponseResult;
import com.xiong.bitmanager.common.util.PageUtils;
import com.xiong.bitmanager.pojo.po.ProxyConfig;
import com.xiong.bitmanager.service.ProxyConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName ProxyConfigController
 * @Description TODO
 * @Author admin
 * @Date 2025/3/15 20:16
 * @Version 1.0
 **/
// src/main/java/com/xiong/bitmanager/controller/ProxyConfigController.java
@RestController
@RequestMapping("/api/proxyConfigs")
public class ProxyConfigController {

    @Autowired
    private ProxyConfigService proxyConfigService;

    @PostMapping
    public ResponseResult<Long> createConfig(@RequestBody ProxyConfig config) {
        proxyConfigService.save(config);
        return ResponseResult.success(config.getId());
    }

    @GetMapping("/active")
    public ResponseResult<List<ProxyConfig>> getActiveConfigs() {
        return ResponseResult.success(proxyConfigService.getActiveConfigs());
    }

    @PutMapping
    public ResponseResult<Boolean> updateConfig(@RequestBody ProxyConfig config) {
        return ResponseResult.success(proxyConfigService.updateById(config));
    }

    @DeleteMapping("/{id}")
    public ResponseResult<Boolean> deleteConfig(@PathVariable String id) {
        return ResponseResult.success(proxyConfigService.removeById(id));
    }

    @GetMapping("/page")
    public ResponseResult<PageUtils> getConfigPage(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String search) {

        IPage<ProxyConfig> operationLogPage = proxyConfigService.getProxyConfigPage(page, size, search);
        PageUtils pageUtils = new PageUtils(operationLogPage);
        return ResponseResult.success(pageUtils);
    }
}


