package org.platform.vehicle.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.platform.vehicle.config.RabbitMqConfig;
import org.platform.vehicle.context.GeoLocationContext;
import org.platform.vehicle.context.TirePressureContext;
import org.platform.vehicle.context.VehicleContext;
import org.platform.vehicle.dto.GeoLocationDto;
import org.platform.vehicle.entity.TirePressure;
import org.platform.vehicle.util.TireDataUtil;
import org.platform.vehicle.vo.VehicleContextVo;
import com.rabbitmq.client.Channel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/11/8 13:30
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VehicleQueueListener {

    private final VehicleContext vehicleContext;
    private final GeoLocationContext geoLocationContext;
    private final TirePressureContext tirePressureContext;
    private final TireDataUtil tireDataUtil;


    @RabbitHandler
    @RabbitListener(queues = RabbitMqConfig.VEHICLE_LOCATION_QUEUE)
    public void geoLocationProcess(List<Object> vehicleLocationList, Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        log.info("车辆位置信息消息:{}", JSON.toJSONString(vehicleLocationList));
        try {
            for (Object obj : vehicleLocationList) {
                GeoLocationDto geoLocationDto = JSONObject.parseObject(String.valueOf(obj),
                        GeoLocationDto.class);
                VehicleContextVo context = vehicleContext.getContext(
                        geoLocationDto.getReceiverId());
                if (context != null) {
                    geoLocationDto.setFleetId(context.getFleetId());
                    geoLocationDto.setCreateTime(new Date());
                    geoLocationContext.push(geoLocationDto);
                }
            }
        } catch (Exception e) {
            log.error("车辆位置信息消息处理失败：" + e);
        }
        try {
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("手动确认消息失败：" + e);
            try {
                channel.basicReject(tag, false);
            } catch (Exception ee) {
                log.error("拒绝消息失败：" + ee);
            }
        }
    }

    @RabbitHandler
    @RabbitListener(queues = RabbitMqConfig.VEHICLE_TIRE_DATA_QUEUE)
    public void tireDataProcess(List<Object> tireDataMessage, Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        log.info("车辆轮胎信息消息:{}", tireDataMessage);
        try {
            List<TirePressure> tirePressureList = new ArrayList<>();
            for (Object obj : tireDataMessage) {
                TirePressure tirePressure = JSONObject.parseObject(String.valueOf(obj),
                        TirePressure.class);
                tirePressureContext.push(tirePressure);
                tirePressureList.add(tirePressure);
            }
            // 校验并推送告警
            try {
                tireDataUtil.prepareData(tirePressureList);
            } catch (Exception e) {
                log.error("校验轮胎数据失败,param:{}", tirePressureList, e);
            }
        } catch (Exception e) {
            log.error("车辆轮胎信息消息处理失败：" + e);
        }
        try {
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("手动确认消息失败：" + e);
            try {
                channel.basicReject(tag, false);
            } catch (Exception ee) {
                log.error("拒绝消息失败：" + ee);
            }
        }
    }

}
