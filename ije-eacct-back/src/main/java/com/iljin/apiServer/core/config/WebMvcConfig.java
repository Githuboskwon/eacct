package com.iljin.apiServer.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring 6(Boot 3) 는 trailing slash 매칭을 기본 비활성화했다.
 * (Boot 2.x/Spring 5 는 기본 허용) 프론트엔드가 일부 API 를 끝 슬래시(예: /api/erp/slip/fund/list/)로 호출하므로,
 * 기존 동작 보존을 위해 trailing slash 매칭을 전역 복원한다.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    @SuppressWarnings("deprecation")
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(true);
    }
}
