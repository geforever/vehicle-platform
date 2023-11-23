package org.platform.vehicle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@MapperScan(basePackages = "org.platform.vehicle.mapper")
@EnableScheduling
@EnableFeignClients(basePackages = {"org.platform.vehicle.feign"})
@SpringBootApplication()
public class VehicleManageApplication {


    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(VehicleManageApplication.class, args);
    }
}
