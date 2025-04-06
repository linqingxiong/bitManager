package com.xiong.bitmanager.service;

/**
 * @ClassName LocalProductGalleryService
 * @Description TODO
 * @Author admin
 * @Date 2025/3/20 11:34
 * @Version 1.0
 **/

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xiong.bitmanager.pojo.po.LocalProduct;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class LocalProductGalleryService {


    public LocalProduct getProductByPath(File productDir) {
        Assert.isTrue(productDir.exists() && productDir.isDirectory(), "指定的路径不是一个有效的文件夹");
        LocalProduct localProduct = new LocalProduct();
        localProduct.setDescription(productDir.getName()); // 文件夹名作为产品描述

        // 获取文件夹下的所有图片文件
        List<String> imagePaths = new ArrayList<>();
        File[] imageFiles = productDir.listFiles(file -> FileUtil.isFile(file) && isImageFile(file));
        if (imageFiles != null) {
            for (File imageFile : imageFiles) {
                imagePaths.add(imageFile.getAbsolutePath());
            }
        }
        localProduct.setImages(imagePaths);
// 读取 detail.bm 文件中的详细描述
        File detailFile = new File(productDir, "detail.bm");
        if (detailFile.exists()) {
            String detail = FileUtil.readUtf8String(detailFile);
            if (JSONUtil.isTypeJSON(detail)) {
                JSONObject jsonObject = JSONUtil.parseObj(detail);
                if (jsonObject.containsKey("detail")) {
                    localProduct.setDetail(jsonObject.getStr("detail"));
                }
            }
        }
        return localProduct;
    }

    public List<LocalProduct> readProductGallery(String galleryPath) {
        List<LocalProduct> productList = new ArrayList<>();

        // 获取指定文件夹下的所有子文件夹
        File galleryDir = new File(galleryPath);
        if (!galleryDir.exists() || !galleryDir.isDirectory()) {
            throw new IllegalArgumentException("指定的路径不是一个有效的文件夹");
        }

        File[] productDirs = galleryDir.listFiles(File::isDirectory);
        if (productDirs != null) {
            for (File productDir : productDirs) {
                LocalProduct localProduct = new LocalProduct();
                localProduct.setDescription(productDir.getName()); // 文件夹名作为产品描述

                // 获取文件夹下的所有图片文件
                List<String> imagePaths = new ArrayList<>();
                File[] imageFiles = productDir.listFiles(file -> FileUtil.isFile(file) && isImageFile(file));
                if (imageFiles != null) {
                    for (File imageFile : imageFiles) {
                        imagePaths.add(imageFile.getAbsolutePath());
                    }
                }
                localProduct.setImages(imagePaths);
// 读取 detail.bm 文件中的详细描述
                File detailFile = new File(productDir, "detail.bm");
                if (detailFile.exists()) {
                    String detail = FileUtil.readUtf8String(detailFile);
                    if (JSONUtil.isTypeJSON(detail)) {
                        JSONObject jsonObject = JSONUtil.parseObj(detail);
                        if (jsonObject.containsKey("detail")) {
                            localProduct.setDetail(jsonObject.getStr("detail"));
                        }
                    }
                }
                productList.add(localProduct);
            }
        }

        return productList;
    }

    /**
     * 修改产品简单描述（修改文件夹名称）
     *
     * @param productPath    产品文件夹路径
     * @param newDescription 新的产品简单描述
     * @return 是否修改成功
     */
    public LocalProduct updateProductDescription(String productPath, String newDescription) {
        File productDir = new File(productPath);
        if (!productDir.exists() || !productDir.isDirectory()) {
            throw new IllegalArgumentException("产品文件夹路径无效");
        }

        // 构造新的文件夹路径
        File newDir = new File(productDir.getParent(), newDescription);
        Assert.isTrue(productDir.renameTo(newDir), "修改文件夹名称失败");

        return getProductByPath(newDir);
    }

    /**
     * 增加或修改产品详细描述（写入 detail.bm 文件）
     *
     * @param productPath 产品文件夹路径
     * @param detail      产品详细描述（JSON 格式）
     * @return 是否写入成功
     */
    public LocalProduct updateProductDetail(String productPath, String detail) {
        File productDir = new File(productPath);
        if (!productDir.exists() || !productDir.isDirectory()) {
            throw new IllegalArgumentException("产品文件夹路径无效");
        }

        // 构造 detail.bm 文件路径
        File detailFile = new File(productDir, "detail.bm");
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("detail", detail);
        // 将 JSON 数据写入文件
        Assert.isTrue(FileUtil.writeUtf8String(jsonObject.toString(), detailFile).exists(), "写入文件失败");
        return getProductByPath(productDir);
    }


    private boolean isImageFile(File file) {
        String extension = FileNameUtil.extName(file.getName());
        return StrUtil.equalsAnyIgnoreCase(extension, "jpg", "jpeg", "png", "gif", "bmp");
    }
}
