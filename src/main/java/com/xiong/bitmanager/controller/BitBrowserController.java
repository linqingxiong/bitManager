package com.xiong.bitmanager.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiong.bitmanager.common.ResponseResult;
import com.xiong.bitmanager.pojo.po.Setting;
import com.xiong.bitmanager.service.ExeLauncherService;
import com.xiong.bitmanager.service.SettingService;
import com.xiong.bitmanager.service.feign.BitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api/bitBrowser")
@Slf4j
public class BitBrowserController {

    @Autowired
    private BitService bitService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private ExeLauncherService exeLauncherService;

    @PostMapping("/health")
    public ResponseResult<Boolean> health() {
        return ResponseResult.success(bitService.health().isSuccess());
    }

    // 新增执行方法
    @GetMapping("/start")
    public ResponseResult<Boolean> startBrowser() {
        // 从数据库获取路径
        Setting setting = settingService.getOne(
                new LambdaQueryWrapper<Setting>()
                        .eq(Setting::getSettingKey, "bt_browser")
        );
        if (setting == null || StrUtil.isEmpty(setting.getSettingValue())) {
            return ResponseResult.error("未配置比特浏览器路径");
        }
        File exeFile = new File(setting.getSettingValue());
        if (!exeFile.exists()) {
            return ResponseResult.error("可执行文件不存在");
        }
        exeLauncherService.launchExe(setting.getSettingValue());
        return ResponseResult.success("启动成功", true);
    }



}
