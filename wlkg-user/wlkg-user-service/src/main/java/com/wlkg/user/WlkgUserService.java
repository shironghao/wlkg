package com.wlkg.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.wlkg.user.mapper")
public class WlkgUserService {
    public static void main(String[] args) {
        SpringApplication.run(WlkgUserService.class,args);
    }
}
