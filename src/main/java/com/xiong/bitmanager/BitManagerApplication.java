package com.xiong.bitmanager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@MapperScan("com.xiong.bitmanager.pojo.dao")
public class BitManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BitManagerApplication.class, args);
    }

}
