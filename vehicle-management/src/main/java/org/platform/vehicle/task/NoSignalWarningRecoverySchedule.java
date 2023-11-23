package org.platform.vehicle.task;

import static org.platform.vehicle.constant.LocalWarningConstant.VEHICLE_STATUS_PARKING;
import static org.platform.vehicle.constant.LocalWarningConstant.VEHICLE_STATUS_POWER_OFF;
import static org.platform.vehicle.constant.LocalWarningConstant.VEHICLE_STATUS_RUNNING;
import static org.platform.vehicle.constant.LocalWarningConstant.WARNING_TYPE_MAIN_POWER_OFF;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.platform.vehicle.conf.VehicleSpecContext;
import org.platform.vehicle.conf.WarningSimpleConfig;
import org.platform.vehicle.constant.LocalWarningTypeEnum;
import org.platform.vehicle.constant.WarningConstant;
import org.platform.vehicle.entity.JT808.NewestGeoLocation;
import org.platform.vehicle.entity.JT808.TireNewestData;
import org.platform.vehicle.entity.VehicleEntity;
import org.platform.vehicle.entity.WarningDetail;
import org.platform.vehicle.mapper.VehicleMapper;
import org.platform.vehicle.mapper.WarningDetailMapper;
import org.platform.vehicle.mapper.jt808.NewestGeoLocationMapper;
import org.platform.vehicle.mapper.jt808.TireNewestDataMapper;
import org.platform.vehicle.util.WarningHelper;
import org.platform.vehicle.vo.context.VehicleSpecContextVo;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/11/22 10:05
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class NoSignalWarningRecoverySchedule {

    private final WarningDetailMapper warningDetailMapper;
    private final VehicleMapper vehicleMapper;
    private final NewestGeoLocationMapper newestGeoLocationMapper;
    private final WarningSimpleConfig warningSimpleConfig;
    private final TireNewestDataMapper tireNewestDataMapper;
    private final VehicleSpecContext vehicleSpecContext;

    // 半小时执行一次
    @Scheduled(cron = "0 */30 * * * *")
    private void recovery() {
        log.info("开始执行无信号预警恢复任务");
        // 开始时间
        Date startTime = new Date();
        //     * 1 车辆断电状态下存在全部轮位3天内（天数可配置）有胎温、胎压数据回传
        //     * 2 车辆通电状态下，全部轮位,6小时之内（小时数可配置）有温压数据回传
        //     * 3 车辆处于停运期时，不做无信号报警且设置停运期修改告警是否恢复状态
        Date now = new Date();
        // 查询所有存在无信号告警的车辆
        List<WarningDetail> warningDetailHistoryList = warningDetailMapper.selectList(
                new LambdaQueryWrapper<WarningDetail>()
                        .eq(WarningDetail::getWarningType, WarningConstant.WARNING_TYPE_NO_SIGNAL)
                        .eq(WarningDetail::getIsRecovery, WarningConstant.IS_RECOVERY_NO)
                        .groupBy(WarningDetail::getLicensePlate));
        if (warningDetailHistoryList.isEmpty()) {
            return;
        }
        List<VehicleEntity> vehicleList = vehicleMapper.selectList(
                new LambdaQueryWrapper<VehicleEntity>()
                        .in(VehicleEntity::getLicensePlate, warningDetailHistoryList.stream()
                                .map(WarningDetail::getLicensePlate)
                                .collect(Collectors.toList())));
        // 查询车辆位置信息
        List<NewestGeoLocation> geoLocationList = newestGeoLocationMapper.selectList(
                new LambdaQueryWrapper<NewestGeoLocation>()
                        .in(NewestGeoLocation::getReceiverId, warningDetailHistoryList.stream()
                                .map(WarningDetail::getLicensePlate)
                                .collect(Collectors.toList())));
        // 查询车辆轮胎信息
        List<TireNewestData> tireNewestDataList = tireNewestDataMapper.selectList(
                new LambdaQueryWrapper<TireNewestData>()
                        .in(TireNewestData::getClientId, warningDetailHistoryList.stream()
                                .map(WarningDetail::getLicensePlate)
                                .collect(Collectors.toList())));
        for (WarningDetail warningDetail : warningDetailHistoryList) {
            VehicleEntity vehicle = this.getVehicleEntity(warningDetail, vehicleList);
            if (vehicle == null) {
                continue;
            }
            // 车辆处于停运期时，不做无信号报警
            if (vehicle.getRunningStartTime() != null && vehicle.getRunningEndTime() != null
                    && vehicle.getRunningStartTime().before(now)
                    && vehicle.getRunningEndTime().after(now)) {
                continue;
            }
            VehicleSpecContextVo vehicleSpecContext = this.vehicleSpecContext.getContext(
                    vehicle.getSpecId());
            if (vehicleSpecContext == null) {
                continue;
            }
            List<TireNewestData> currentVehicleTireData = this.getCurrentVehicleTireNewestData(
                    warningDetail, tireNewestDataList);
            // 获取车辆最新位置信息
            NewestGeoLocation geoLocation = this.getNewestGeoLocation(geoLocationList,
                    warningDetail);
            // 判断车辆是否断电
            Integer vehicleDrivingStatus = this.getVehicleDrivingStatus(geoLocation,
                    warningDetail, currentVehicleTireData);
            List<Integer> tireSiteList = this.getTireSiteList(vehicleSpecContext);
            if (VEHICLE_STATUS_POWER_OFF == vehicleDrivingStatus) {
                // 车辆断电状态下存在全部轮位3天内（天数可配置）有全部轮胎胎温、胎压数据回传
                // 当前时间3天内
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);
                int interval = warningSimpleConfig.getCommonPowerOffOverInterval();
                calendar.add(Calendar.HOUR, -interval);
                this.execute(warningDetail, tireSiteList, currentVehicleTireData, calendar);
            } else {
                // 全部轮位,6小时之内（小时数可配置）有温压数据回传
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);
                // 将当前时间减去6个小时
                int interval = warningSimpleConfig.getCommonPowerOnOverInterval();
                calendar.add(Calendar.HOUR, -interval);
                this.execute(warningDetail, tireSiteList, currentVehicleTireData, calendar);
            }
        }
        log.info("无信号预警恢复任务执行完成,耗时:{}ms",
                System.currentTimeMillis() - startTime.getTime());
    }

    private void execute(WarningDetail warningDetail, List<Integer> tireSiteList,
            List<TireNewestData> currentVehicleTireData, Calendar calendar) {
        int count = 0;
        for (Integer tireSiteId : tireSiteList) {
            for (TireNewestData currentVehicleTireDatum : currentVehicleTireData) {
                if (tireSiteId.equals(
                        Integer.valueOf(currentVehicleTireDatum.getTireSiteId()))
                        && currentVehicleTireDatum.getDeviceTime()
                        .after(calendar.getTime())) {
                    count++;
                }
            }
        }
        // 全部轮胎3天内有温压数据,则恢复无信号告警
        if (count == tireSiteList.size()) {
            // 恢复告警
            this.recoveryWarning(warningDetail);
        }
    }

    private VehicleEntity getVehicleEntity(WarningDetail warningDetail,
            List<VehicleEntity> vehicleList) {
        VehicleEntity vehicle = new VehicleEntity();
        for (VehicleEntity entity : vehicleList) {
            if (entity.getLicensePlate().equals(warningDetail.getLicensePlate())) {
                vehicle = entity;
                break;
            }
        }
        return vehicle;
    }

    private void recoveryWarning(WarningDetail warningDetail) {
        WarningDetail updateParam = new WarningDetail();
        updateParam.setId(warningDetail.getId());
        updateParam.setIsRecovery(WarningConstant.IS_RECOVERY_YES);
        warningDetailMapper.updateById(updateParam);
    }

    private List<TireNewestData> getCurrentVehicleTireNewestData(WarningDetail warningDetail,
            List<TireNewestData> tireNewestDataList) {
        List<TireNewestData> currentVehicleTireData = new ArrayList<>();
        for (TireNewestData tireNewestData : tireNewestDataList) {
            if (tireNewestData.getClientId().equals(warningDetail.getReceiverId())) {
                currentVehicleTireData.add(tireNewestData);
            }
        }
        return currentVehicleTireData;
    }

    /**
     * 获取车辆驾驶状态
     *
     * @param geoLocation
     * @param warningDetail
     * @param tireNewestDataList
     * @return
     */
    private Integer getVehicleDrivingStatus(NewestGeoLocation geoLocation,
            WarningDetail warningDetail, List<TireNewestData> tireNewestDataList) {
        Integer vehicleStatus = null;
        String warnBinaryStr = this.toBinaryString(geoLocation.getWarnBit());
        if (geoLocation == null) {
            return vehicleStatus;
        }
        // 获取行驶信息告警
        List<LocalWarningTypeEnum> geoLocationWarningType = WarningHelper.getGeoLocationWarningType(
                warnBinaryStr);
        // 判断是否是掉电报警标志
        if (!geoLocationWarningType.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            // 将当前时间减去10分钟
            calendar.add(Calendar.MINUTE, -10);
            // 判断是否是掉电报警标志
            if (this.isMainPowerOff(geoLocationWarningType)) {
                // 若当前车辆的GPS_ID最新一条数据状态是“掉电报警标志”，且10分钟内该车辆无任何轮位回传过温压数据
                int count = 0;
                for (TireNewestData tireNewestData : tireNewestDataList) {
                    if (tireNewestData.getClientId().equals(warningDetail.getReceiverId())) {
                        // 10分钟内该车辆无任何轮位回传过温压数据
                        if (tireNewestData.getDeviceTime().after(calendar.getTime())) {
                            count++;
                        }
                    }
                }
                if (count == 0) {
                    vehicleStatus = VEHICLE_STATUS_POWER_OFF;
                } else {
                    vehicleStatus = this.getVehicleDrivingStatus(geoLocation);
                }
            } else {
                vehicleStatus = this.getVehicleDrivingStatus(geoLocation);
            }
        } else {
            vehicleStatus = this.getVehicleDrivingStatus(geoLocation);
        }
        return vehicleStatus;
    }

    private Integer getVehicleDrivingStatus(NewestGeoLocation geoLocation) {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(new Date());
        // 将当前时间减去5分钟
        newCalendar.add(Calendar.MINUTE, -5);
        // 判断距离系统当前时间5分钟内是否有速度大于0的数据
        if (geoLocation.getDeviceTime().after(newCalendar.getTime())) {
            return VEHICLE_STATUS_RUNNING;
        } else {
            return VEHICLE_STATUS_PARKING;
        }
    }

    /**
     * 判断是否是掉电报警标志
     *
     * @param geoLocationWarningType
     * @return
     */
    private boolean isMainPowerOff(List<LocalWarningTypeEnum> geoLocationWarningType) {
        for (LocalWarningTypeEnum localWarningTypeEnum : geoLocationWarningType) {
            if (WARNING_TYPE_MAIN_POWER_OFF == localWarningTypeEnum.getType()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 转换位二进制
     *
     * @param param
     * @return
     */
    public String toBinaryString(int param) {
        StringBuilder stringBuilder = new StringBuilder(Integer.toBinaryString(param));
        while (stringBuilder.length() < 9) {
            stringBuilder.insert(0, "0");
        }
        return stringBuilder.toString();
    }

    private NewestGeoLocation getNewestGeoLocation(List<NewestGeoLocation> geoLocationList,
            WarningDetail mainVehicle) {
        NewestGeoLocation geoLocation = null;
        for (NewestGeoLocation newestGeoLocation : geoLocationList) {
            if (newestGeoLocation.getReceiverId().equals(mainVehicle.getReceiverId())) {
                geoLocation = newestGeoLocation;
                break;
            }
        }
        return geoLocation;
    }

    private List<Integer> getTireSiteList(VehicleSpecContextVo vehicleSpec) {
        List<Integer> tireSiteList = new ArrayList<>();
        if (vehicleSpec.getWheelArrange() == null) {
            return tireSiteList;
        }
        for (String wheel : vehicleSpec.getWheelArrange().split(";")) {
            for (String tireSite : wheel.split(",")) {
                tireSiteList.add(Integer.valueOf(tireSite));
            }
        }
        return tireSiteList;
    }
}
