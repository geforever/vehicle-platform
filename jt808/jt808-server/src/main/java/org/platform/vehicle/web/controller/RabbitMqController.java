package org.platform.vehicle.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.platform.vehicle.web.config.RabbitMqConfig;

/**
 * @Author gejiawei
 * @Date 2023/11/8 13:25
 */
@Slf4j
@RestController
@RequestMapping("/direct")
public class RabbitMqController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/location/send")
    public Object sendLocationMsg() {
        rabbitTemplate.convertAndSend(RabbitMqConfig.VEHICLE_EXCHANGE,
                RabbitMqConfig.VEHICLE_LOCATION_QUEUE_ROUTING_KEY,
                "发送一条车辆位置测试消息：direct");
        return "direct消息发送成功！！";
    }


    @GetMapping("/tireData/send")
    public Object sendTireDataMsg() {
        rabbitTemplate.convertAndSend(RabbitMqConfig.VEHICLE_EXCHANGE,
                RabbitMqConfig.VEHICLE_TIRE_DATA_QUEUE_ROUTING_KEY,
                "发送一条车辆轮胎信息测试消息：direct");
        return "direct消息发送成功！！";
    }
}
