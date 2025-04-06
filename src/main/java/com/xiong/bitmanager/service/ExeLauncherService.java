package com.xiong.bitmanager.service;

import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExeLauncherService {

    public void launchExe(String exePath) {
        // 参数校验
        if (StrUtil.isBlank(exePath)) {
            throw new IllegalArgumentException("EXE路径不能为空");
        }

        // 执行命令并自动处理流（Hutool 会处理输入/输出流防止阻塞）
        Process process = RuntimeUtil.exec(exePath);

        // 同步等待结果（带超时）
        String result = RuntimeUtil.getResult(process);

        // 记录日志
        log.info("EXE执行成功: {}", exePath);
        log.debug("执行输出: {}", result);

        // 检查退出码（可选）
        int exitCode = process.exitValue();
        if (exitCode != 0) {
            log.warn("EXE非零退出: exitCode={}, path={}", exitCode, exePath);
        }
    }
}
