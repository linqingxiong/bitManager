package com.xiong.bitmanager.common.util;

import cn.hutool.core.io.FileUtil;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * @ClassName ScreenshotUtils
 * @Description TODO
 * @Author admin
 * @Date 2025/3/13 10:12
 * @Version 1.0
 **/
public class ScreenshotUtils {
    public static void capturePage(WebDriver driver, String url, String savePath) {
        try {
            driver.get(url);
            // 等待页面加载（根据实际情况调整）
            Thread.sleep(2000);
            // 截图并转成字节数组
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

            // 使用Hutool保存文件
            FileUtil.writeBytes(screenshot, savePath);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit(); // 确保关闭浏览器
        }
    }
}
