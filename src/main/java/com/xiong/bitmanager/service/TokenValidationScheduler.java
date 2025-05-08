package com.xiong.bitmanager.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.xiong.bitmanager.common.ResponseResult;
import com.xiong.bitmanager.common.util.DeviceUtil;
import com.xiong.bitmanager.service.feign.BmServerService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName TokenValidationScheduler
 * @Description TODO
 * @Author admin
 * @Date 2025/3/19 15:08
 * @Version 1.0
 **/
@Component
@EnableScheduling
@Slf4j
public class TokenValidationScheduler {
    public static boolean isPass = false;
    private final SettingService settingService;
    private final BmServerService bmServerService;
    private final List<ChangeListener> listeners = new CopyOnWriteArrayList<>();

    public interface ChangeListener {
        void onIsPassChanged(boolean newValue);

    }

    // 修改set逻辑
    public synchronized void setPass(boolean newValue) {
        boolean oldValue = isPass;
        if (oldValue != newValue) {
            isPass = newValue;
            listeners.forEach(l -> l.onIsPassChanged(newValue));
        }
    }

    public TokenValidationScheduler(SettingService settingService, BmServerService bmServerService) {
        this.settingService = settingService;
        this.bmServerService = bmServerService;
    }

    @PostConstruct
    public void init() {
        validateToken();
    }

    @Scheduled(fixedRate = 10000)
    public synchronized void validateToken() {

        String licenseKey = settingService.getLicenseKey();
        if (StrUtil.isBlank(licenseKey)) {
            setPass(false);
            return; // 无token，不验证
        }
        boolean isValid;
        try {
            ResponseResult<List<String>> boundDevices = bmServerService.getBoundDevices(licenseKey);
            Assert.isTrue(boundDevices.getCode() == 0, boundDevices.getMessage());
            Assert.isTrue(boundDevices.getData().contains(DeviceUtil.generateDeviceId()), "设备未绑定");
            isValid = true;
        } catch (Exception e) {
            log.error("定期验证token错误", e);
            isValid = false;
        }
        if (!isValid) {
            setPass(false);
            return;
//            settingService.deleteUsercode();
            // 或者使用 SpringApplication.exit(applicationContext, () -> 0);
        }
        setPass(true);
    }

    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    public boolean isPass() {
        return isPass;
    }

}
