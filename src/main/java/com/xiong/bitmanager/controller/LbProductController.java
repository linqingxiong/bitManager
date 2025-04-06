package com.xiong.bitmanager.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiong.bitmanager.common.ResponseResult;
import com.xiong.bitmanager.common.util.PageUtils;
import com.xiong.bitmanager.pojo.po.LbPic;
import com.xiong.bitmanager.pojo.po.LbProduct;
import com.xiong.bitmanager.service.*;
import com.xiong.bitmanager.service.feign.BmServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class LbProductController {

    @Autowired
    private LbProductService lbProductService;
    @Autowired
    private LbPicService lbPicService;
    @Autowired
    private PutOnlineTaskService putOnlineTaskService;
    @Autowired
    private LbOperationLogService lbOperationLogService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private LocalProductGalleryService localProductGalleryService;
    @Autowired
    private BmServerService bmServerService;
    @Autowired
    private FileService fileService;

    @GetMapping
    public ResponseResult<List<LbProduct>> getAllProducts() {
        List<LbProduct> products = lbProductService.getAllProducts();
        return ResponseResult.success(products);
    }

    @PostMapping
    public ResponseResult<LbProduct> saveProduct(@RequestBody LbProduct product) {
        String localProductGallery = settingService.getLocalProductGallery();

        List<String> finalPics = new ArrayList<>();
        for (String pic : product.getPics()) {
            if (StrUtil.startWith(pic, localProductGallery)) {
                try {
                    String storedPath = fileService.store(new File(pic));
                    if (storedPath != null) {
                        finalPics.add(storedPath);
                    }
                } catch (IOException e) {
                    // 保留原始路径当存储失败
//                    finalPics.add(pic);
                }
            } else {
                finalPics.add(pic);
            }
        }
        product.setPics(finalPics);
        LbProduct savedProduct = lbProductService.saveProduct(product);
        return ResponseResult.success(savedProduct);
    }

    @PutMapping
    public ResponseResult<Boolean> updateProduct(@RequestBody LbProduct product) {
        lbProductService.updateProduct(product);
        return ResponseResult.success(true);
    }

    @GetMapping("/page")
    public ResponseResult<PageUtils> getProductsPage(@RequestParam int page, @RequestParam int size, @RequestParam(required = false) String name) {
        IPage<LbProduct> productPage = lbProductService.getProductsPage(page, size, name);
        productPage.getRecords().stream().forEach(product -> {
            putOnlineTaskService.statByProductId(product);
            lbOperationLogService.statByProductId(product);
            product.setPics(lbPicService.getPicsByProductId(product.getId()).stream().map(LbPic::getImgUrl).collect(Collectors.toList()));
        });
        PageUtils pageUtils = new PageUtils(productPage);
        return ResponseResult.success(pageUtils);
    }

    @DeleteMapping("/{id}")
    public ResponseResult<Boolean> deleteProduct(@PathVariable Long id) {
        lbProductService.removeById(id);
        return ResponseResult.success(true);
    }
}
