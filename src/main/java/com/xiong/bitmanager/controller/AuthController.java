package com.xiong.bitmanager.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.xiong.bitmanager.common.ResponseResult;
import com.xiong.bitmanager.common.util.DeviceUtil;
import com.xiong.bitmanager.service.SettingService;
import com.xiong.bitmanager.service.TokenValidationScheduler;
import com.xiong.bitmanager.service.feign.BmServerService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private BmServerService bmServerService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private TokenValidationScheduler tokenValidationScheduler;
    // 设置license
    @PutMapping("/license/{licenseKey}")
    public ResponseResult<Void> setLicense(@PathVariable String licenseKey) {
        if (StrUtil.isBlank(licenseKey)) {
            return ResponseResult.error("licenseKey不能为空");
        }
        settingService.setLicenseKey(licenseKey);
        return ResponseResult.success(null);
    }

    // 获取 license
    @GetMapping("/license")
    public ResponseResult<String> getLicense() {
        return ResponseResult.success(settingService.getLicenseKey());
    }

    @SneakyThrows
    @GetMapping("/deviceId")
    public ResponseResult<String> getOperationLogsPage() {
        return ResponseResult.success(DeviceUtil.generateDeviceId());
    }

    @PostMapping("/license/bind")
    public ResponseResult<Void> bindDevice(@RequestParam(required = false) String deviceType) {
        try {
            String licenseKey = settingService.getLicenseKey();
            Assert.notBlank(licenseKey, "licenseKey不能为空");
            ResponseResult<Void> result = bmServerService.bindDevice(licenseKey, DeviceUtil.generateDeviceId(), deviceType);
            if (result.getCode() == 0) {
                result.setCode(200);
                tokenValidationScheduler.setPass(true);
            } else if (result.getCode() == 11001) {
                settingService.setLicenseKey("");
            }
            return result;
        } catch (Exception e) {
            return ResponseResult.error("绑定设备失败: " + e.getMessage());
        }
    }

    @GetMapping("/license/devices")
    public ResponseResult<List<String>> getBoundDevices() {
        try {
            String licenseKey = settingService.getLicenseKey();
            Assert.notBlank(licenseKey, "licenseKey不能为空");
            ResponseResult<List<String>> boundDevices = bmServerService.getBoundDevices(licenseKey);
            if (boundDevices.getCode() == 0) {
                boundDevices.setCode(200);
            }
            return boundDevices;
        } catch (Exception e) {
            return ResponseResult.error("获取已绑定设备列表失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/license/unbind")
    public ResponseResult<Void> unbindDevice(
            @RequestParam String licenseKey,
            @RequestParam String deviceId) {
        try {
            ResponseResult<Void> result = bmServerService.unbindDevice(licenseKey, deviceId);
            if (result.getCode() == 0) {
                result.setCode(200);
            }
            return result;
        } catch (Exception e) {
            return ResponseResult.error("解绑设备失败: " + e.getMessage());
        }
    }


}
