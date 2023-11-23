package org.platform.vehicle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author gejiawei
 * @Date 2023/11/8 12:11
 */
@EnableAsync
@MapperScan(basePackages = "org.platform.vehicle.mapper")
@EnableFeignClients(basePackages = {"org.platform.vehicle.feign"})
@EnableScheduling
@SpringBootApplication()
public class VehicleConsumerApplication {

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(VehicleConsumerApplication.class, args);
    }
}
