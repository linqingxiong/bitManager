package com.xiong.bitmanager.controller;

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
    public List<LbPic> getPicsByProductId(@PathVariable Long productId) {
        return lbPicService.getPicsByProductId(productId);
    }

    @PostMapping
    public LbPic savePic(@RequestBody LbPic pic) {
        return lbPicService.savePic(pic);
    }
}
