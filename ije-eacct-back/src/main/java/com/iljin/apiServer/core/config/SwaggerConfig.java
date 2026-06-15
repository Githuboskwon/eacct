package com.iljin.apiServer.core.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.iljin.apiServer.ijeas"))
                .paths(PathSelectors.any())
                .build().apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "일진 전기 전자전표 시스템"
                ,"일진 전지 전자전표 API 서버 입니다."
                ,"1.0.0"
                ,"TERMS OF SERVICE URL"
                ,new Contact("NAME", "URL", "EMAIL")
                ,"LICENSE"
                ,"LICENSE URL"
                , Collections.emptyList()
        );
    }
}
