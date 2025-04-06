package com.xiong.bitmanager.controller;

import com.xiong.bitmanager.common.ResponseResult;
import com.xiong.bitmanager.pojo.dto.req.PutProductsOnlineDto;
import com.xiong.bitmanager.pojo.po.LbPic;
import com.xiong.bitmanager.pojo.po.LbProduct;
import com.xiong.bitmanager.service.LbPicService;
import com.xiong.bitmanager.service.LbProductService;
import com.xiong.bitmanager.service.LeboncoinService;
import com.xiong.bitmanager.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/leboncoin")
@Slf4j
public class LeboncoinController {

    @Autowired
    private LeboncoinService leboncoinService;
    @Autowired
    private LbProductService lbProductService;
    @Autowired
    private LbPicService lbPicService;
    @Autowired
    private TaskService taskService;

    @PostMapping("/putProductsOnline")
    public ResponseResult<String> putProductsOnline(@RequestBody PutProductsOnlineDto putProductsOnlineDto) {
        try {
            LbProduct lbProduct = lbProductService.getById(putProductsOnlineDto.getId());
            lbProduct.setPics(lbPicService.getPicsByProductId(lbProduct.getId()).stream().map(LbPic::getImgUrl).collect(Collectors.toList()));
            taskService.submitDelayTask(putProductsOnlineDto);
            log.info("提交任务成功: {}", lbProduct);
            return ResponseResult.success("提交任务成功");
        } catch (Exception e) {
            log.error("putProductsOnline failed for product: {}", putProductsOnlineDto.getId(), e);
            return ResponseResult.error("提交任务失败: " + e.getMessage());
        }
    }


}
