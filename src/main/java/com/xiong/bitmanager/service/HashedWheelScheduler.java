package com.xiong.bitmanager.service;

import io.netty.util.HashedWheelTimer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName HashedWheelScheduler
 * @Description TODO
 * @Author admin
 * @Date 2025/3/13 11:32
 * @Version 1.0
 **/
@Component
public class HashedWheelScheduler {
    private final HashedWheelTimer timer = new HashedWheelTimer(
            r -> {
                Thread t = new Thread(r, "TimeWheel-Thread");
                t.setDaemon(true);  // 守护线程不阻止JVM关闭
                return t;
            },
            1000,  // 时间刻度1000ms
            TimeUnit.MILLISECONDS,
            512   // 时间轮槽数量
    );

    @Autowired
    private ThreadPoolTaskExecutor bizExecutor;

    public void schedule(Runnable task, long delayMs) {
        timer.newTimeout(timeout ->
                        bizExecutor.execute(task),  // 延迟结束后提交到业务线程池
                delayMs,
                TimeUnit.MILLISECONDS
        );
    }
}
