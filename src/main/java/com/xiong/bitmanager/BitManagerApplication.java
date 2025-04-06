package com.xiong.bitmanager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients
@MapperScan("com.xiong.bitmanager.pojo.dao")
@EnableAsync
public class BitManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BitManagerApplication.class, args);
    }

}
