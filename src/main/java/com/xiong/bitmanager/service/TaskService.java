package com.xiong.bitmanager.service;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xiong.bitmanager.pojo.dao.PutOnlineTaskMapper;
import com.xiong.bitmanager.pojo.dto.req.PutProductsOnlineDto;
import com.xiong.bitmanager.pojo.po.PutOnlineTask;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName TaskService
 * @Description TODO
 * @Author admin
 * @Date 2025/3/13 14:09
 * @Version 1.0
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {
    private ConcurrentHashSet<String> browserLock = new ConcurrentHashSet<>();

    private final PutOnlineTaskMapper putOnlineTaskMapper;
    private final HashedWheelScheduler scheduler;
    private final ThreadPoolTaskExecutor bizExecutor;
    private final LeboncoinService leboncoinService;
    private final TokenValidationScheduler tokenValidationScheduler;


    @PostConstruct
    public void initPendingTasks() {
        LambdaQueryWrapper<PutOnlineTask> query = new LambdaQueryWrapper<>();
        query.in(PutOnlineTask::getStatus, "准备","处理中", "暂停");

        putOnlineTaskMapper.selectList(query).forEach(task -> {
            log.info("重新提交任务: {}", task);
            updateTaskStatus(task.getId(), "准备");
            long delay = Math.max(0, task.getScheduleTime() - System.currentTimeMillis());
            scheduler.schedule(() -> processTask(task.getId()), delay);
        });
    }
    @PostConstruct
    public void initIsPassListenner() {
        tokenValidationScheduler.addChangeListener(newValue -> {
            if (newValue) { // isPass 恢复为 true
                // 重新提交所有「暂停」状态的任务
                LambdaQueryWrapper<PutOnlineTask> query = new LambdaQueryWrapper<>();
                query.eq(PutOnlineTask::getStatus, "暂停");
                putOnlineTaskMapper.selectList(query).forEach(task -> {
                    updateTaskStatus(task.getId(), "准备");
                    scheduler.schedule(() -> processTask(task.getId()), 0); // 立即重试
                });
            }
        });
    }
    // 提交延迟任务
    @Transactional
    public void submitDelayTask(PutProductsOnlineDto putProductsOnlineDto) {
        PutOnlineTask task = new PutOnlineTask();
        task.setProductId(putProductsOnlineDto.getId());
        task.setBrowserId(putProductsOnlineDto.getBrowserId());
        task.setScheduleTime(putProductsOnlineDto.getDelayMs());
        task.setStatus("准备");
        task.setProxyIp(putProductsOnlineDto.getProxyIp());

        putOnlineTaskMapper.insert(task);
        // 提交到时间轮
        scheduler.schedule(() -> processTask(task.getId()), Math.max(0, task.getScheduleTime() - System.currentTimeMillis()));
    }

    // 任务处理（带重试）
    public void processTask(Long taskId) {
        if (!tokenValidationScheduler.isPass()) {
            // 更新任务状态为「暂停」并返回
            updateTaskStatus(taskId, "暂停");
            return;
        }
        bizExecutor.execute(() -> {
            PutOnlineTask task = putOnlineTaskMapper.selectById(taskId);
            if (!"准备".equals(task.getStatus()) && !"延迟".equals(task.getStatus()) && !"失败".equals(task.getStatus())) {
                return;
            }
            try {
                if (browserLock.contains(task.getBrowserId())) {
                    updateTaskStatus(taskId, "延迟");
                    scheduler.schedule(() -> processTask(task.getId()), System.currentTimeMillis() + 600_000);
                    return;
                }
                browserLock.add(task.getBrowserId());
                // 1. 更新状态为处理中
                updateTaskStatus(taskId, "处理中");
                // 2. 执行业务逻辑
                leboncoinService.putProductsOnline2(task);

                // 3. 更新为完成状态
                updateTaskStatus(taskId, "完成");
            } catch (Exception e) {
                handleTaskFailure(taskId, e);
            } finally {
                browserLock.remove(task.getBrowserId());
            }
        });
    }

    // 带版本号的更新
    private void updateTaskStatus(Long taskId, String status) {
        LambdaUpdateWrapper<PutOnlineTask> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(PutOnlineTask::getStatus, status)
                .eq(PutOnlineTask::getId, taskId);
//                .eq(PutOnlineTask::getVersion, task.getVersion()); // 乐观锁

        int rows = putOnlineTaskMapper.update(null, wrapper);
        if (rows == 0) {
            throw new RuntimeException("任务状态变更冲突");
        }
    }

    // 失败处理（指数退避）
    private void handleTaskFailure(Long taskId, Exception e) {
        PutOnlineTask task = putOnlineTaskMapper.selectById(taskId);
        int retryCount = task.getRetryCount();

        // 最大重试3次
        if (retryCount < 3) {
            long delay = (long) Math.pow(2, retryCount) * 5000;

            LambdaUpdateWrapper<PutOnlineTask> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(PutOnlineTask::getStatus, "失败")
                    .set(PutOnlineTask::getRetryCount, retryCount + 1)
                    .eq(PutOnlineTask::getId, taskId);
            putOnlineTaskMapper.update(null, wrapper);
            scheduler.schedule(() -> processTask(taskId), delay);
        } else {
            LambdaUpdateWrapper<PutOnlineTask> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(PutOnlineTask::getStatus, "丢弃")
                    .eq(PutOnlineTask::getId, taskId);
            putOnlineTaskMapper.update(null, wrapper);
        }
    }
}
