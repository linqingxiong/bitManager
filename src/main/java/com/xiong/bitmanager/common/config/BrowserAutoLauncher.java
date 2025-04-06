package com.xiong.bitmanager.common.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

@Configuration
public class BrowserAutoLauncher {

    private final WebServerApplicationContext webServerAppContext;

    public BrowserAutoLauncher(WebServerApplicationContext webServerAppContext) {
        this.webServerAppContext = webServerAppContext;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void autoLaunchBrowser() {
        // 获取运行端口
        int port = webServerAppContext.getWebServer().getPort();

        // 获取上下文路径（Spring Boot 3.x 使用 server.servlet.context-path）
        String contextPath = webServerAppContext.getEnvironment().getProperty("server.servlet.context-path", "");

        // 构建完整URL
        String url = String.format("http://localhost:%d%s", port, contextPath);

        try {
            // 优先使用 Desktop 类打开浏览器（支持 Windows/MacOS）
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(URI.create(url));
                    return;
                }
            }

            // 如果 Desktop 不可用，改用命令行（支持 Linux）
            String os = System.getProperty("os.name").toLowerCase();
            Runtime runtime = Runtime.getRuntime();

            if (os.contains("win")) {
                runtime.exec("cmd /c start " + url);
            } else if (os.contains("mac")) {
                runtime.exec("open " + url);
            } else {
                // Linux 或其他 Unix 系统
                runtime.exec(new String[]{"xdg-open", url});
            }
        } catch (IOException e) {
            System.err.println("自动打开浏览器失败: " + e.getMessage());
        }
    }
}
