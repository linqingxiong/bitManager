package com.xiong.bitmanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiong.bitmanager.common.ResponseResult;
import com.xiong.bitmanager.common.util.PageUtils;
import com.xiong.bitmanager.pojo.po.PutOnlineTask;
import com.xiong.bitmanager.service.PutOnlineTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName PutOnlineTaskController
 * @Description TODO
 * @Author admin
 * @Date 2025/3/13 15:38
 * @Version 1.0
 **/
@RestController
@RequestMapping("/api/putOnlineTasks")
public class PutOnlineTaskController {

    @Autowired
    private PutOnlineTaskService putOnlineTaskService;

    @GetMapping("/page")
    public ResponseResult<PageUtils> getPutOnlineTasksPage(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) Long productId) {

        IPage<PutOnlineTask> taskPage = putOnlineTaskService.getPutOnlineTasksPage(page, size, productId);
        return ResponseResult.success(new PageUtils(taskPage));
    }
}
