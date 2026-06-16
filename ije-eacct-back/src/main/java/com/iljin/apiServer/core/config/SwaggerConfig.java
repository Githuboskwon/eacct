package com.iljin.apiServer.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("일진 전기 전자전표 시스템")
                        .description("일진 전지 전자전표 API 서버 입니다.")
                        .version("1.0.0")
                        .contact(new Contact().name("NAME").url("URL").email("EMAIL")));
    }

    @Bean
    public GroupedOpenApi ijeasApi() {
        return GroupedOpenApi.builder()
                .group("ijeas")
                .packagesToScan("com.iljin.apiServer.ijeas")
                .build();
    }
}
