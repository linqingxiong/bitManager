package com.xiong.bitmanager.common.util;

import cn.hutool.crypto.digest.DigestUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DeviceUtil {
    // 获取设备唯一标识（Windows实现）
    public static String generateDeviceId() throws Exception {
        String mac = getWindowsMacAddress();
        String disk = getWindowsDiskSerial();

        return DigestUtil.sha256Hex(mac + disk);
    }

    private static String getWindowsMacAddress() throws IOException {
        Process process = Runtime.getRuntime().exec("getmac /fo csv /nh");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line = reader.readLine();
            return line.split(",")[0].replace("\"", "").trim();
        }
    }

    private static String getWindowsDiskSerial() throws IOException {
        Process process = Runtime.getRuntime().exec(
                "wmic diskdrive get serialnumber");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            reader.readLine(); // 跳过标题行
            return reader.readLine().trim();
        }
    }
}
