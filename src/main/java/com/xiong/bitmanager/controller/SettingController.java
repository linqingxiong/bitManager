package com.xiong.bitmanager.controller;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiong.bitmanager.common.ResponseResult;
import com.xiong.bitmanager.common.util.PageUtils;
import com.xiong.bitmanager.pojo.po.Setting;
import com.xiong.bitmanager.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/setting")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @GetMapping("/page")
    public ResponseResult<PageUtils> getOperationLogsPage(@RequestParam int page, @RequestParam int size, @RequestParam(required = false) String key) {
        IPage<Setting> operationLogPage = settingService.getOperationLogsPage(page, size, key);
        PageUtils pageUtils = new PageUtils(operationLogPage);
        return ResponseResult.success(pageUtils);
    }
    // 新增接口
    @GetMapping("/list")
    public ResponseResult<List<Setting>> getAllSettings() {
        List<Setting> settings = settingService.list(new LambdaQueryWrapper<Setting>().ne(Setting::getType, "系统"));
        return ResponseResult.success(settings);
    }

    @PostMapping("/update")
    public ResponseResult<String> updateSetting(@RequestBody Setting setting) {
        Setting findSetting = settingService.getById(setting.getSettingKey());
        boolean success;

        if (ObjUtil.isEmpty(findSetting)) {
            setting.setType("隐藏");
            success = settingService.save(setting);
        } else {
            success = settingService.updateById(setting);
        }
        return success ?
                ResponseResult.success("更新成功") :
                ResponseResult.error("更新失败");
    }
}
