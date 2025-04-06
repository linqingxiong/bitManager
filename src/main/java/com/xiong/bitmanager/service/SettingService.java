package com.xiong.bitmanager.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiong.bitmanager.pojo.dao.SettingMapper;
import com.xiong.bitmanager.pojo.po.Setting;
import org.springframework.stereotype.Service;

@Service
public class SettingService extends ServiceImpl<SettingMapper, Setting> {


    public IPage<Setting> getOperationLogsPage(int page, int size, String key) {
        LambdaQueryWrapper<Setting> queryWrapper = new LambdaQueryWrapper<>();
        return page(new Page<>(page, size), queryWrapper.like(StrUtil.isNotBlank(key), Setting::getSettingKey, key)
                .or().like(StrUtil.isNotBlank(key), Setting::getSettingValue, key)
                .or().like(StrUtil.isNotBlank(key), Setting::getDescription, key));
    }

    public String getUsercode() {
        Setting usercode = getOne(new LambdaQueryWrapper<Setting>().eq(Setting::getSettingKey, "usercode"),false);
        return usercode == null?null:usercode.getSettingValue();
    }

    public void setUsercode(String data) {
        update(new LambdaUpdateWrapper<Setting>().eq(Setting::getSettingKey, "usercode").set(Setting::getSettingValue, data));
    }

    public void deleteUsercode() {
        update(new LambdaUpdateWrapper<Setting>().eq(Setting::getSettingKey, "usercode").set(Setting::getSettingValue, ""));

    }

    public void setLocalProductGallery(String data) {
        update(new LambdaUpdateWrapper<Setting>().eq(Setting::getSettingKey, "local_product_gallery").set(Setting::getSettingValue, data));
    }

    public String getLocalProductGallery() {
        Setting localProductGallery = getOne(new LambdaQueryWrapper<Setting>().eq(Setting::getSettingKey, "local_product_gallery"));
        return localProductGallery == null?"":localProductGallery.getSettingValue();
    }
}
