package com.wlkg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.wlkg.mapper")
public class wlkgOrderService {
    public static void main(String[] args) {
        SpringApplication.run(wlkgOrderService.class,args);
    }
}
