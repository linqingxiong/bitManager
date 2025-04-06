package com.xiong.bitmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @ClassName HashedWheelScheduler
 * @Description TODO
 * @Author admin
 * @Date 2025/3/13 14:33
 * @Version 1.0
 **/
@RestController
@RequestMapping("api/pool")
public class PoolMonitorController {
    @Autowired
    private ThreadPoolTaskExecutor bizExecutor;

    // 业务线程池状态
    @GetMapping("/biz-status")
    public Map<String, Object> bizStatus() {
        return Map.of(
                "active", bizExecutor.getActiveCount(),
                "queue", bizExecutor.getThreadPoolExecutor().getQueue().size(),
                "completed", bizExecutor.getThreadPoolExecutor().getCompletedTaskCount()
        );
    }

    // 动态调整业务线程池
    @PostMapping("/adjust-biz-pool")
    public String adjustBizPool(@RequestParam int core, @RequestParam int max) {
        bizExecutor.setCorePoolSize(core);
        bizExecutor.setMaxPoolSize(max);
        return "业务线程池已调整: core=" + core + ", max=" + max;
    }
}
