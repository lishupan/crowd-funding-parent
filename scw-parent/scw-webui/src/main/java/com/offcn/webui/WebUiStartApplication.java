package com.offcn.webui;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude ={DruidDataSourceAutoConfigure.class, DataSourceAutoConfiguration.class} )
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class WebUiStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebUiStartApplication.class,args);
    }
}
