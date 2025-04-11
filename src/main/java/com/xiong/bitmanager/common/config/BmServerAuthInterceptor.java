package com.xiong.bitmanager.common.config;

import com.xiong.bitmanager.common.util.DeviceUtil;
import com.xiong.bitmanager.service.SettingService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.annotation.Resource;

/**
 * @ClassName BmServerAuthInterceptor
 * @Description TODO
 * @Author admin
 * @Date 2025/3/21 16:51
 * @Version 1.0
 **/
public class BmServerAuthInterceptor implements RequestInterceptor {

    private final String deviceId;
    {
        try {
            deviceId = DeviceUtil.generateDeviceId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Resource
    private SettingService settingService;
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String usercode = settingService.getUsercode();
        requestTemplate.header("X-LICENSE-KEY", usercode);
        requestTemplate.header("X-DEVICE-ID", deviceId);
    }
}
