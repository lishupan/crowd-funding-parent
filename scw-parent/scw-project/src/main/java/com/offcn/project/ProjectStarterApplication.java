package com.offcn.project;

import com.offcn.utils.OssTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.offcn.project.mapper")
public class ProjectStarterApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectStarterApplication.class,args);
    }

    //创建OssTemplate
    @Bean
    @ConfigurationProperties(prefix = "oss")
    public OssTemplate getOssTemplate(){
        return new OssTemplate();
    }
}
