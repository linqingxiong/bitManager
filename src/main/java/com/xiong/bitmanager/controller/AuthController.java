package com.xiong.bitmanager.controller;

import cn.hutool.core.util.StrUtil;
import com.xiong.bitmanager.common.ResponseResult;
import com.xiong.bitmanager.pojo.dto.feign.LoginRequest;
import com.xiong.bitmanager.service.SettingService;
import com.xiong.bitmanager.service.TokenValidationScheduler;
import com.xiong.bitmanager.service.feign.BmServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private BmServerService bmServerService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private TokenValidationScheduler tokenValidationScheduler;

    @PostMapping("/login")
    public ResponseResult<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            ResponseResult<String> res = bmServerService.login(loginRequest);

            if (StrUtil.isNotBlank(res.getData())) {
                settingService.setUsercode(res.getData());
            }
            tokenValidationScheduler.setPass(true);
            return res;
        } catch (Exception e) {
            return ResponseResult.error("Login failed: " + e.getMessage());
        }
    }

    @GetMapping("/check")
    public ResponseResult<Boolean> checkToken() {
        String usercode = settingService.getUsercode();
        if (StrUtil.isBlank(usercode)) {
            return ResponseResult.error(401, "未登录");
        } else {
            return ResponseResult.success("已登录", true);
        }
    }
}
