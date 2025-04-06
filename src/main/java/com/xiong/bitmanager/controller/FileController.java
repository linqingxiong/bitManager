package com.xiong.bitmanager.controller;

import com.xiong.bitmanager.common.ResponseResult;
import com.xiong.bitmanager.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseResult<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileService.store(file);
            return ResponseResult.success(fileUrl);
        } catch (IOException e) {
            return ResponseResult.error(500, "文件上传失败");
        } catch (IllegalArgumentException e) {
            return ResponseResult.error(400, e.getMessage());
        }
    }

    @GetMapping("/localImg")
    public ResponseEntity<InputStreamResource> getImage(@RequestParam String filePath) {
        try {
            File imageFile = new File(filePath);
            if (!imageFile.exists()) {
                return ResponseEntity.notFound().build();
            }
            FileInputStream inputStream = new FileInputStream(imageFile);
            // 根据文件类型设置 MediaType（例如 JPEG、PNG）
            MediaType mediaType = MediaType.IMAGE_JPEG;
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(new InputStreamResource(inputStream));
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build(); // 文件不存在时返回 404
        }
    }
}
