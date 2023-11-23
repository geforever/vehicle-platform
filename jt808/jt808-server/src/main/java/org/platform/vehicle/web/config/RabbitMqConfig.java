package org.platform.vehicle.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author gejiawei
 * @Date 2021/5/12 9:50 上午
 */
@Slf4j
@Configuration
public class RabbitMqConfig {

    /**
     * 交换机
     */
    public static final String VEHICLE_EXCHANGE = "vehicle.exchange";

    /**
     * 队列
     */
    public static final String VEHICLE_LOCATION_QUEUE = "vehicle.location.queue";
    public static final String VEHICLE_TIRE_DATA_QUEUE = "vehicle.tire.data.queue";

    /**
     * 路由键
     */
    public static final String VEHICLE_LOCATION_QUEUE_ROUTING_KEY = "vehicle.location.queue.routingKey";
    public static final String VEHICLE_TIRE_DATA_QUEUE_ROUTING_KEY = "vehicle.tire.data.queue.routingKey";

    @Bean
    public Queue locationQueue() {
        return new Queue(VEHICLE_LOCATION_QUEUE, true);
    }

    @Bean
    public Queue tireDataQueue() {
        return new Queue(VEHICLE_TIRE_DATA_QUEUE, true);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(VEHICLE_EXCHANGE, true, false);
    }


    @Bean
    public Binding bindingDirectExchange(Queue locationQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(locationQueue).to(directExchange)
                .with(VEHICLE_LOCATION_QUEUE_ROUTING_KEY);
    }

    @Bean
    public Binding bindingDirectExchange2(Queue tireDataQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(tireDataQueue).to(directExchange)
                .with(VEHICLE_TIRE_DATA_QUEUE_ROUTING_KEY);
    }
}
