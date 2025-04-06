package com.xiong.bitmanager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${file.access-path:/files}")
    private String accessPath;

    public String store(MultipartFile file) throws IOException {
        // 验证文件类型
        if (!isImageFile(file)) {
            throw new IllegalArgumentException("仅支持图片文件上传");
        }

        // 创建上传目录
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID() + extension;

        // 保存文件
        Path targetPath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), targetPath);

        // 返回访问路径
        return accessPath + "/" + filename;
    }

    public String store(File file) throws IOException {
        // 创建上传目录
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名
        String originalFilename = file.getName();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID() + extension;

        // 保存文件
        Path targetPath = uploadPath.resolve(filename);
        Files.copy(file.toPath(), targetPath);

        // 返回访问路径
        return accessPath + "/" + filename;
    }

    /**
     * 存储截图文件（PNG格式）
     * @param screenshotBytes 截图字节数组
     * @return 可访问的文件路径
     * @throws IOException
     */
    public String storeScreenshot(byte[] screenshotBytes) throws IOException {
        // 参数校验（增强健壮性）
        if (screenshotBytes == null || screenshotBytes.length == 0) {
            throw new IllegalArgumentException("截图数据不能为空");
        }

        // 复用目录配置
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名（强制使用PNG扩展名）
        String filename = UUID.randomUUID() + ".png";

        // 保存截图文件
        Path targetPath = uploadPath.resolve(filename);
        Files.write(targetPath, screenshotBytes);

        // 复用路径配置
        return accessPath + "/" + filename;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
}
