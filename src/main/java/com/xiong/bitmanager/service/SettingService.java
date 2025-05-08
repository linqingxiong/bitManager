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

    public void setLocalProductGallery(String data) {
        update(new LambdaUpdateWrapper<Setting>().eq(Setting::getSettingKey, "local_product_gallery").set(Setting::getSettingValue, data));
    }

    public String getLocalProductGallery() {
        Setting localProductGallery = getOne(new LambdaQueryWrapper<Setting>().eq(Setting::getSettingKey, "local_product_gallery"));
        return localProductGallery == null?"":localProductGallery.getSettingValue();
    }

    public void setLicenseKey(String licenseKey) {
        update(new LambdaUpdateWrapper<Setting>().eq(Setting::getSettingKey, "license").set(Setting::getSettingValue, licenseKey));
    }

    public String getLicenseKey() {
        Setting license = getOne(new LambdaQueryWrapper<Setting>().eq(Setting::getSettingKey, "license"));
        return license == null?"":license.getSettingValue();
    }
}
