package com.xiong.bitmanager.controller;

import cn.hutool.core.io.FileUtil;
import com.xiong.bitmanager.common.ResponseResult;
import com.xiong.bitmanager.pojo.po.LocalProduct;
import com.xiong.bitmanager.service.LocalProductGalleryService;
import com.xiong.bitmanager.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName LocalProductController
 * @Description TODO
 * @Author admin
 * @Date 2025/3/20 11:35
 * @Version 1.0
 **/
@RestController
@RequestMapping("api/local")
public class LocalProductController {
    @Autowired
    private LocalProductGalleryService localProductGalleryService;
    @Autowired
    private SettingService settingService;

    @GetMapping("/products")
    public ResponseResult<List<LocalProduct>> getProducts() {

        return ResponseResult.success(localProductGalleryService.readProductGallery(settingService.getLocalProductGallery()));
    }

    @PutMapping("/products/description")
    public ResponseResult<LocalProduct> updateProductDescription(@RequestParam String oldDescription, @RequestParam String newDescription) {
        return ResponseResult.success(localProductGalleryService.updateProductDescription(FileUtil.file(settingService.getLocalProductGallery(), oldDescription).getAbsolutePath(), newDescription));
    }

    @PostMapping("/products/detail")
    public ResponseResult<LocalProduct> updateProductDetail(@RequestParam String productPath, @RequestParam String detail) {
        return ResponseResult.success(localProductGalleryService.updateProductDetail(FileUtil.file(settingService.getLocalProductGallery(), productPath).getAbsolutePath(), detail));
    }

}
