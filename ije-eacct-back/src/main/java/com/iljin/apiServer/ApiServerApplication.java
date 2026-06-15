package com.iljin.apiServer;

import com.iljin.apiServer.core.config.FileStorageConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.Locale;

@EnableConfigurationProperties(FileStorageConfig.class)
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class ApiServerApplication {
	
	/*
	* ERP(ORACLE) NLS_Lang과 동일하게 사용
	* */
//	@PostConstruct
//	public void started() {
//		Locale.setDefault(Locale.US);
//	}

	public static void main(String[] args) {
		SpringApplication.run(ApiServerApplication.class, args);
	}

}
