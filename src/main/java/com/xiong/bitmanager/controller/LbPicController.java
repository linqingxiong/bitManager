package com.xiong.bitmanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiong.bitmanager.common.ResponseResult;
import com.xiong.bitmanager.common.util.PageUtils;
import com.xiong.bitmanager.pojo.po.LbPic;
import com.xiong.bitmanager.service.LbPicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pics")
public class LbPicController {

    @Autowired
    private LbPicService lbPicService;

    @GetMapping("/product/{productId}")
    public ResponseResult<List<LbPic>> getPicsByProductId(@PathVariable Long productId) {
        List<LbPic> pics = lbPicService.getPicsByProductId(productId);
        return ResponseResult.success(pics);
    }

    @GetMapping("/page")
    public ResponseResult<PageUtils> getPicsPage(@RequestParam int page, @RequestParam int size) {
        IPage<LbPic> picPage = lbPicService.getPicsPage(page, size);
        PageUtils pageUtils = new PageUtils(picPage);
        return ResponseResult.success(pageUtils);
    }

    @PostMapping
    public ResponseResult<LbPic> savePic(@RequestBody LbPic pic) {
        LbPic savedPic = lbPicService.savePic(pic);
        return ResponseResult.success(savedPic);
    }
}
