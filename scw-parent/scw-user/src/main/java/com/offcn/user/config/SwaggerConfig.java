package com.offcn.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private ApiInfo getInfo(){
       return new ApiInfoBuilder().title("用户微服务接口文档").contact(new Contact("优就业","http://www.ujiuye.com","hk109@126.com"))
                .description("提供给前端使用用户接口文档").version("1.0").build();
    }

    @Bean
    public Docket createDocket(){
      return   new Docket(DocumentationType.SWAGGER_2).apiInfo(getInfo()).groupName("开发文档").select().build();
    }
}
