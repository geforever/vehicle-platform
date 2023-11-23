package org.platform.vehicle.web.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.platform.vehicle.web.constant.WarningConstant;
import org.platform.vehicle.web.constant.WarningTypeEnum;
import org.platform.vehicle.web.model.vo.VehicleSpecContextVo;
import org.platform.vehicle.web.param.TireCheckDataDetailParam;

/**
 * @Author gejiawei
 * @Date 2023/10/30 14:15
 */
public class WarningHelper {

    /**
     * 告警类型:1-一级高压报警,2-一级高温报警,3-一级低压报警,4-二级高压报警,5-二级高温报警,6-二级低压报警,7-低电压报警,8-急漏气报警,9-慢漏气报警,10-无信号报警
     */
    public static List<WarningTypeEnum> getTireWarningType(TireCheckDataDetailParam data,
            VehicleSpecContextVo vehicleSpec) {
        List<WarningTypeEnum> warningTypeResult = new ArrayList<>();
        // 低压警报等级1
        BigDecimal lowPressureAlarmLevel1 = vehicleSpec.getLowPressureAlarmLevel1();
        // 高压警报等级1
        BigDecimal highPressureAlarmLevel1 = vehicleSpec.getHighPressureAlarmLevel1();
        // 高温警报等级1
        BigDecimal highTemperatureAlarmLevel1 = vehicleSpec.getHighTemperatureAlarmLevel1();
        // 低压警报等级2
        BigDecimal lowPressureAlarmLevel2 = vehicleSpec.getLowPressureAlarmLevel2();
        // 高压警报等级2
        BigDecimal highPressureAlarmLevel2 = vehicleSpec.getHighPressureAlarmLevel2();
        // 高温警报等级2
        BigDecimal highTemperatureAlarmLevel2 = vehicleSpec.getHighTemperatureAlarmLevel2();
        // 胎压异常判断
        if (StringUtils.isNotBlank(data.getTirePressure())) {
            BigDecimal pressure = new BigDecimal(data.getTirePressure());
            // 判断低压异常等级
            if (lowPressureAlarmLevel1 != null && pressure.compareTo(lowPressureAlarmLevel1) <= 0) {
                // 一级低压报警
                warningTypeResult.add(
                        WarningTypeEnum.getByType(WarningConstant.WARNING_TYPE_FIRST_LOW_PRESSURE));
            } else if (lowPressureAlarmLevel2 != null
                    && pressure.compareTo(lowPressureAlarmLevel1) > 0
                    && pressure.compareTo(lowPressureAlarmLevel2) <= 0
                    && vehicleSpec.getEnableLev2Alarm()) {
                // 二级低压报警
                warningTypeResult.add(
                        WarningTypeEnum.getByType(
                                WarningConstant.WARNING_TYPE_SECOND_LOW_PRESSURE));
            }
            // 判断高压异常等级
            if (highPressureAlarmLevel2 != null && highPressureAlarmLevel1 != null
                    && pressure.compareTo(highPressureAlarmLevel2) >= 0
                    && pressure.compareTo(highPressureAlarmLevel1) < 0
                    && vehicleSpec.getEnableLev2Alarm()) {
                // 二级高压报警
                warningTypeResult.add(
                        WarningTypeEnum.getByType(
                                WarningConstant.WARNING_TYPE_SECOND_HIGH_PRESSURE));
            } else if (highPressureAlarmLevel1 != null
                    && pressure.compareTo(highPressureAlarmLevel1) >= 0) {
                // 一级高压报警
                warningTypeResult.add(
                        WarningTypeEnum.getByType(
                                WarningConstant.WARNING_TYPE_FIRST_HIGH_PRESSURE));
            }
        }
        // 判断高温异常等级
        if (data.getTireTemperature() != null) {
            BigDecimal temperature = new BigDecimal(data.getTireTemperature());
            if (highTemperatureAlarmLevel2 != null
                    && highTemperatureAlarmLevel1 != null
                    && temperature.compareTo(highTemperatureAlarmLevel2) >= 0
                    && temperature.compareTo(highTemperatureAlarmLevel1) < 0
                    && vehicleSpec.getEnableLev2Alarm()) {
                // 二级高温报警
                warningTypeResult.add(
                        WarningTypeEnum.getByType(
                                WarningConstant.WARNING_TYPE_SECOND_HIGH_TEMPERATURE));
            } else if (highTemperatureAlarmLevel1 != null
                    && temperature.compareTo(highTemperatureAlarmLevel1) >= 0) {
                // 一级高温报警
                warningTypeResult.add(
                        WarningTypeEnum.getByType(
                                WarningConstant.WARNING_TYPE_FIRST_HIGH_TEMPERATURE));
            }
        }
        // 低电压报警
        if (data.getTireWarningData().getBatteryVoltageStatus() == 1) {
            warningTypeResult.add(
                    WarningTypeEnum.getByType(WarningConstant.WARNING_TYPE_LOW_VOLTAGE));
        }
        // 急漏气
        if (data.getTireWarningData().getTireStatus() == 1) {
            warningTypeResult.add(
                    WarningTypeEnum.getByType(WarningConstant.WARNING_TYPE_FAST_LEAK));
        }
        return warningTypeResult;
    }
}
