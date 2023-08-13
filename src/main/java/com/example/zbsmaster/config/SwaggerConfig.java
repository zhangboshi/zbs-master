package com.example.zbsmaster.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.zbsmaster.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        Contact DEFAULT_CONTACT = new Contact("作者", "作者网址", "邮箱");
        ApiInfo apiInfo = new ApiInfo("标题", "简介", "V1.0", "小组网站",
                DEFAULT_CONTACT, "小组名", "主页地址", new ArrayList<VendorExtension>());

        return apiInfo;
    }
}
