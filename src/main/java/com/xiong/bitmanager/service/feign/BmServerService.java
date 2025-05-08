package com.xiong.bitmanager.service.feign;

import com.xiong.bitmanager.common.ResponseResult;
import com.xiong.bitmanager.common.config.BmServerAuthInterceptor;
import com.xiong.bitmanager.pojo.dto.feign.*;
import com.xiong.bitmanager.pojo.dto.req.ChatRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName BmServerService
 * @Description TODO
 * @Author admin
 * @Date 2025/3/7 17:59
 * @Version 1.0
 **/
@FeignClient(
        name = "bmServerService",
        url = "${myfeign.bmserver}/bmserver-api/api",
        configuration = {BmServerAuthInterceptor.class, CustomFeignErrorDecoder.class}
)
public interface BmServerService {
    @PostMapping("/auth/login")
    ResponseResult<String> login(@RequestBody LoginRequest loginRequest);

    @PostMapping("/ai/img2detail")
    ResponseResult<String> img2detail(@RequestBody Img2detailRequest loginRequest);

    @PostMapping("/ai/generateText")
    ResponseResult<String> generateText(@RequestBody ChatRequestDto chatRequest);

    @PostMapping("/ai/selectOption")
    ResponseResult<AiSelectOptionRes> selectOption(@RequestBody AiSelectOptionRequest aiSelectOptionRequest);

    @PostMapping("/ai/selectCategory")
    ResponseResult<AiSelectCategoryRes> selectCategory(@RequestBody AiSelectCategoryDto aiSelectCategoryDto);

    @GetMapping("/auth")
    ResponseResult<Boolean> auth(AuthRequest request);

    @GetMapping("/auth/validateToken/{usercode}")
    ResponseResult<Boolean> validateToken(@PathVariable String usercode);

    @PostMapping("/license/bind")
    ResponseResult<Void> bindDevice(
            @RequestParam String licenseKey,
            @RequestParam String deviceId,
            @RequestParam(required = false) String deviceType);

    @GetMapping("/license/{licenseKey}/devices")
    ResponseResult<List<String>> getBoundDevices(
            @PathVariable String licenseKey);

    @DeleteMapping("/license/unbind")
    ResponseResult<Void> unbindDevice(
            @RequestParam String licenseKey,
            @RequestParam String deviceId);
}
