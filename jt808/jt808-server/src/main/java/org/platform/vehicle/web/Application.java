package org.platform.vehicle.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableCaching
@EnableScheduling
@EnableFeignClients(basePackages = {"org.platform.vehicle.web.feign"})
@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		// Hikari连接池提供的JDBC Connection连接有效性检测,默认为500毫秒
		System.setProperty("com.zaxxer.hikari.aliveBypassWindowMs", "2000");
		SpringApplication.run(Application.class, args);
		log.info("***Spring 启动成功***");
	}
}
