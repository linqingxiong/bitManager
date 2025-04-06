package com.xiong.bitmanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiong.bitmanager.common.ResponseResult;
import com.xiong.bitmanager.common.util.PageUtils;
import com.xiong.bitmanager.pojo.po.LbOperationLog;
import com.xiong.bitmanager.service.LbOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operationLogs")
public class LbOperationLogController {

    @Autowired
    private LbOperationLogService lbOperationLogService;

    @GetMapping("/page")
    public ResponseResult<PageUtils> getOperationLogsPage(@RequestParam int page, @RequestParam int size,@RequestParam(required = false)Integer productId
    ) {
        IPage<LbOperationLog> operationLogPage = lbOperationLogService.getOperationLogsPage(page, size,productId);
        PageUtils pageUtils = new PageUtils(operationLogPage);
        return ResponseResult.success(pageUtils);
    }

}
