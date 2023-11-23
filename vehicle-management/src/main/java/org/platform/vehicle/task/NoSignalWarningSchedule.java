package org.platform.vehicle.task;

import static org.platform.vehicle.constant.LocalWarningConstant.VEHICLE_STATUS_PARKING;
import static org.platform.vehicle.constant.LocalWarningConstant.VEHICLE_STATUS_POWER_OFF;
import static org.platform.vehicle.constant.LocalWarningConstant.VEHICLE_STATUS_RUNNING;
import static org.platform.vehicle.constant.LocalWarningConstant.WARNING_TYPE_MAIN_POWER_OFF;
import static org.platform.vehicle.constant.WarningTypeEnum.NO_SIGNAL;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.platform.vehicle.conf.CustomerContext;
import org.platform.vehicle.conf.VehicleSpecContext;
import org.platform.vehicle.conf.WarningSimpleConfig;
import org.platform.vehicle.constant.AssetTireConstant;
import org.platform.vehicle.constant.LocalWarningTypeEnum;
import org.platform.vehicle.constant.NoticeConfigConstant;
import org.platform.vehicle.constant.WarningConstant;
import org.platform.vehicle.constant.WarningTypeEnum;
import org.platform.vehicle.entity.AssetTire;
import org.platform.vehicle.entity.JT808.NewestGeoLocation;
import org.platform.vehicle.entity.JT808.TireNewestData;
import org.platform.vehicle.entity.VehicleEntity;
import org.platform.vehicle.entity.VehicleTrailerInfo;
import org.platform.vehicle.entity.WarningDetail;
import org.platform.vehicle.entity.WarningTraceRecord;
import org.platform.vehicle.helper.jdbc.WarningDetailJdbc;
import org.platform.vehicle.helper.jdbc.WarningTraceRecordJdbc;
import org.platform.vehicle.mapper.AssetTireMapper;
import org.platform.vehicle.mapper.VehicleMapper;
import org.platform.vehicle.mapper.VehicleTrailerInfoMapper;
import org.platform.vehicle.mapper.WarningDetailMapper;
import org.platform.vehicle.mapper.jt808.NewestGeoLocationMapper;
import org.platform.vehicle.mapper.jt808.TireNewestDataMapper;
import org.platform.vehicle.param.SysNoticeWarningParam;
import org.platform.vehicle.service.SysNoticeService;
import org.platform.vehicle.util.AmapUtil;
import org.platform.vehicle.util.TireSiteUtil;
import org.platform.vehicle.util.WarningHelper;
import org.platform.vehicle.vo.TireSiteResult;
import org.platform.vehicle.vo.amap.AddressComponentVo;
import org.platform.vehicle.vo.amap.ConvertCoordVo;
import org.platform.vehicle.vo.amap.RegeocodeResultVo;
import org.platform.vehicle.vo.amap.RegeocodeVo;
import org.platform.vehicle.vo.context.CustomerContextVo;
import org.platform.vehicle.vo.context.VehicleSpecContextVo;
import org.platform.vehicle.utils.IdWorker;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/10/27 09:44
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class NoSignalWarningSchedule {

    private final WarningDetailMapper warningDetailMapper;
    private final VehicleMapper vehicleMapper;
    private final AssetTireMapper assetTireMapper;
    private final WarningDetailJdbc warningDetailJdbc;
    private final AmapUtil amapUtil;
    private final SysNoticeService sysNoticeService;
    private final NewestGeoLocationMapper newestGeoLocationMapper;
    private final WarningSimpleConfig warningSimpleConfig;
    private final TireNewestDataMapper tireNewestDataMapper;
    private final WarningTraceRecordJdbc warningTraceRecordJdbc;
    private final VehicleSpecContext vehicleSpecContext;
    private final CustomerContext customerContext;
    private final IdWorker ID_WORKER = new IdWorker();
    private final VehicleTrailerInfoMapper vehicleTrailerInfoMapper;


    /**
     * 无信号报警,每半小时执行一次 无信号报警的定义为： 1）车辆断电状态下存在轮位（一个、多个、全部）超过3天（天数可配置）无胎温、胎压数据回传；
     * 2）车辆通电状态下，存在轮位（一个、多个、全部）超过6小时（小时数可配置）无胎温、胎压数据回传； 3）车辆处于停运期时，不做无信号报警。
     */
    @Scheduled(cron = "0 0 * * * ?")
    // 5秒执行一次
//    @Scheduled(cron = "*/5 * * * * ?")
    // 十分钟执行一次
//    @Scheduled(cron = "0 */10 * ? * *")
    public void noSignalWarning() {
        log.info("执行无信号报警定时任务");
        // 获取开始时间
        long startTimestamp = System.currentTimeMillis();
        List<WarningDetail> batchInsertWarningDetailList = new ArrayList<>();
        List<WarningTraceRecord> batchInsertWarningTraceRecordList = new ArrayList<>();
        Date currentDate = new Date();
        // vehicleListHistory 1000条数据分批查询
        Long totalVehicleCount = vehicleMapper.selectCount(
                new LambdaQueryWrapper<VehicleEntity>()
                        .eq(VehicleEntity::getIsDeleted, false));
        int pageSize = 1000;
        int totalPage = (int) Math.ceil(totalVehicleCount / (double) pageSize);
        for (int i = 0; i < totalPage; i++) {
            List<VehicleEntity> vehicleListHistory = vehicleMapper.selectList(
                    new LambdaQueryWrapper<VehicleEntity>()
                            .eq(VehicleEntity::getIsDeleted, false)
                            .last("limit " + i * pageSize + "," + pageSize));
            if (vehicleListHistory.isEmpty()) {
                continue;
            }
            Map<Integer, VehicleSpecContextVo> vehicleSpecContextMap = vehicleSpecContext.getAllContexts();
            List<VehicleEntity> vehicleList = new ArrayList<>();
            List<String> receiverIdList = new ArrayList<>();
            // 停运车辆不校验
            for (VehicleEntity vehicle : vehicleListHistory) {
                if (vehicle.getRunningEndTime() == null || vehicle.getRunningEndTime()
                        .before(currentDate)) {
                    vehicleList.add(vehicle);
                    if (StringUtils.isNotBlank(vehicle.getReceiverIdNumber())) {
                        receiverIdList.add(vehicle.getReceiverIdNumber());
                    }
                }
            }
            if (vehicleListHistory.isEmpty()) {
                return;
            }
            // 查询车辆位置信息
            List<NewestGeoLocation> geoLocationList = newestGeoLocationMapper.selectList(
                    new LambdaQueryWrapper<NewestGeoLocation>()
                            .in(NewestGeoLocation::getReceiverId, receiverIdList));
            // 查询车辆轮胎信息
            List<TireNewestData> tireNewestDataList = tireNewestDataMapper.selectList(
                    new LambdaQueryWrapper<TireNewestData>()
                            .in(TireNewestData::getClientId, receiverIdList));
            // 查询车辆绑定关系
            List<VehicleTrailerInfo> vehicleTrailerInfoList = vehicleTrailerInfoMapper.selectList(
                    new LambdaQueryWrapper<VehicleTrailerInfo>()
                            .eq(VehicleTrailerInfo::getType, 1));
            List<String> licensePlateList = new ArrayList<>();
            for (VehicleEntity vehicle : vehicleList) {
                licensePlateList.add(vehicle.getLicensePlate());
            }
            // 查询轮胎库存信息
            List<AssetTire> assetTireList = assetTireMapper.selectList(
                    new LambdaQueryWrapper<AssetTire>()
                            .in(AssetTire::getLicensePlate, licensePlateList)
                            .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE)
                            .select(AssetTire::getCode, AssetTire::getSensorId));

            // 判断车辆断电还是通电
            for (VehicleEntity vehicle : vehicleList) {
                VehicleEntity mainVehicle = null;
                VehicleEntity minorVehicle = null;
                VehicleSpecContextVo mainVehicleSpec = null;
                VehicleSpecContextVo minorVehicleSpec = null;
                VehicleSpecContextVo vehicleSpec = this.getVehicleSpecList(vehicle,
                        vehicleSpecContextMap);
                if (vehicleSpec == null) {
                    continue;
                }
                if (vehicleSpec.getSpecType() == 1) {
                    mainVehicle = vehicle;
                    mainVehicleSpec = vehicleSpec;
                    // 查询挂车
                    minorVehicle = this.getMinorVehicle(vehicleTrailerInfoList, mainVehicle,
                            vehicleList);
                    minorVehicleSpec = this.getVehicleSpecList(minorVehicle, vehicleSpecContextMap);
                } else if (vehicleSpec.getSpecType() == 2) {
                    minorVehicle = vehicle;
                    minorVehicleSpec = vehicleSpec;
                    mainVehicle = this.getMainVehicle(vehicleTrailerInfoList, minorVehicle,
                            vehicleList);
                    mainVehicleSpec = this.getVehicleSpecList(mainVehicle, vehicleSpecContextMap);
                } else {
                    mainVehicle = vehicle;
                    mainVehicleSpec = vehicleSpec;
                    // 查询挂车
                    minorVehicle = this.getMinorVehicle(vehicleTrailerInfoList, mainVehicle,
                            vehicleList);
                    minorVehicleSpec = this.getVehicleSpecList(minorVehicle, vehicleSpecContextMap);
                }
                if (mainVehicle == null || mainVehicleSpec == null) {
                    continue;
                }
                // 主车gpsID未填写不校验
                if (StringUtils.isBlank(mainVehicle.getReceiverIdNumber())) {
                    continue;
                }
                // 获取车辆最新位置信息
                NewestGeoLocation geoLocation = this.getNewestGeoLocation(geoLocationList,
                        mainVehicle);
                if (geoLocation == null) {
                    continue;
                }
                Integer vehicleDrivingStatus = this.getVehicleDrivingStatus(geoLocation,
                        vehicle, tireNewestDataList);
                // 判断车辆是否断电
                if (VEHICLE_STATUS_POWER_OFF == vehicleDrivingStatus) {
                    // 获取当前时间的 Calendar 实例
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(currentDate);
                    // 将当前时间减去72小时
                    int interval = warningSimpleConfig.getCommonPowerOffOverInterval();
                    calendar.add(Calendar.HOUR, -interval);
                    this.process(vehicle, mainVehicleSpec, minorVehicleSpec, mainVehicle,
                            vehicleSpec,
                            calendar.getTime(),
                            assetTireList, geoLocation, tireNewestDataList,
                            batchInsertWarningTraceRecordList,
                            batchInsertWarningDetailList);
                } else {
                    // 获取当前时间的 Calendar 实例
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(currentDate);
                    // 将当前时间减去6个小时
                    int interval = warningSimpleConfig.getCommonPowerOnOverInterval();
                    calendar.add(Calendar.HOUR, -interval);
                    this.process(vehicle, mainVehicleSpec, minorVehicleSpec, mainVehicle,
                            vehicleSpec,
                            calendar.getTime(),
                            assetTireList, geoLocation, tireNewestDataList,
                            batchInsertWarningTraceRecordList,
                            batchInsertWarningDetailList);
                }
            }
        }
        // 保存报警跟进记录
        try {
            warningTraceRecordJdbc.saveBatch(batchInsertWarningTraceRecordList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // 保存报警记录
        try {
            warningDetailJdbc.saveBatch(batchInsertWarningDetailList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // 获取结束时间
        long endTimestamp = System.currentTimeMillis();
        log.info("执行无信号报警定时任务结束,耗时：{}ms", endTimestamp - startTimestamp);
    }

    private NewestGeoLocation getNewestGeoLocation(List<NewestGeoLocation> geoLocationList,
            VehicleEntity mainVehicle) {
        NewestGeoLocation geoLocation = null;
        for (NewestGeoLocation newestGeoLocation : geoLocationList) {
            if (newestGeoLocation.getReceiverId()
                    .equals(mainVehicle.getReceiverIdNumber())) {
                geoLocation = newestGeoLocation;
                break;
            }
        }
        return geoLocation;
    }

    private void process(VehicleEntity vehicle,
            VehicleSpecContextVo mainVehicleSpec,
            VehicleSpecContextVo minorVehicleSpec,
            VehicleEntity mainVehicle,
            VehicleSpecContextVo vehicleSpec,
            Date intervalTime,
            List<AssetTire> assetTireList,
            NewestGeoLocation geoLocation,
            List<TireNewestData> tireNewestDataList,
            List<WarningTraceRecord> batchInsertWarningTraceRecordList,
            List<WarningDetail> batchInsertWarningDetailList) {
        List<Integer> mainTireSiteList = this.getTireSiteList(mainVehicleSpec);
        List<Integer> minorTireSiteList = new ArrayList<>();
        if (minorVehicleSpec != null) {
            minorTireSiteList = this.getTireSiteList(minorVehicleSpec);
        }
        List<TireNewestData> tirePressureDataList = new ArrayList<>();
        for (TireNewestData tireNewestData : tireNewestDataList) {
            if (tireNewestData.getClientId()
                    .equals(mainVehicle.getReceiverIdNumber())) {
                tirePressureDataList.add(tireNewestData);
            }
        }
        // 车辆断电状态下存在轮位（一个、多个、全部）超过3天（天数可配置）无胎温、胎压数据回传；
        for (Integer tireSiteId : mainTireSiteList) {
            this.saveWarning(vehicle,
                    tireSiteId,
                    tirePressureDataList,
                    vehicleSpec,
                    intervalTime,
                    assetTireList,
                    geoLocation,
                    mainVehicle.getReceiverIdNumber(),
                    batchInsertWarningTraceRecordList,
                    batchInsertWarningDetailList);
        }
        for (Integer tireSiteId : minorTireSiteList) {
            this.saveWarning(vehicle,
                    tireSiteId,
                    tirePressureDataList,
                    vehicleSpec,
                    intervalTime,
                    assetTireList,
                    geoLocation,
                    mainVehicle.getReceiverIdNumber(),
                    batchInsertWarningTraceRecordList,
                    batchInsertWarningDetailList);
        }
    }

    private void saveWarning(VehicleEntity vehicle,
            Integer tireSiteId,
            List<TireNewestData> tirePressureDataDtoList,
            VehicleSpecContextVo vehicleSpec,
            Date intervalTime,
            List<AssetTire> assetTireList,
            NewestGeoLocation geoLocation,
            String receiverIdNumber,
            List<WarningTraceRecord> batchInsertWarningTraceRecordList,
            List<WarningDetail> batchInsertWarningDetailList) {
        TireNewestData tirePressureData = this.getTireNewestData(tireSiteId,
                tirePressureDataDtoList, vehicleSpec);
        // 轮胎数据为null或者轮胎数据的创建时间在当前时间的前三天
        if (tirePressureData == null || tirePressureData.getDeviceTime()
                .before(intervalTime)) {
            String traceNo = "";
            // 根据传感器查询轮胎编号
            String tireCode = this.getTireCode(tirePressureData, assetTireList);
            TireSiteResult tireSiteResult = TireSiteUtil.getTireSiteResult(
                    tireSiteId, vehicleSpec.getSpecType(),
                    vehicleSpec.getWheelCount(), vehicleSpec.getWheelbaseType());
            // 判断告警明细是否存在
            // SQL
            List<WarningDetail> warningDetailList = warningDetailMapper.selectList(
                    new LambdaQueryWrapper<WarningDetail>()
                            .eq(WarningDetail::getLicensePlate, vehicle.getLicensePlate())
//                            .eq(WarningDetail::getTireSite, tireSiteId)
                            .eq(WarningDetail::getWarningType, NO_SIGNAL.getType())
                            .gt(WarningDetail::getCreateTime, intervalTime)
                            .select(WarningDetail::getId)
                            .last("limit 1"));
            if (warningDetailList.isEmpty()) {
                traceNo = String.valueOf(ID_WORKER.nextId());
                // 保存新跟进记录
                WarningTraceRecord warningTraceRecord = this.getWarningTraceRecord(
                        vehicle, tireSiteId, tireSiteResult, traceNo, tireCode, geoLocation);
//                batchInsertWarningTraceRecordList.add(warningTraceRecord);
                this.addBatchInsertWarningTraceRecordList(batchInsertWarningTraceRecordList,
                        warningTraceRecord);
                // 保存告警明细
                WarningDetail warningDetail = this.getWarningDetail(vehicle, tireSiteId,
                        receiverIdNumber, traceNo, tireCode, tireSiteResult);
//                batchInsertWarningDetailList.add(warningDetail);
                this.addBatchInsertWarningDetailList(batchInsertWarningDetailList,
                        warningDetail);
                // 发送报警通知
                List<WarningTypeEnum> warningTypeEnumList = new ArrayList<>();
                warningTypeEnumList.add(NO_SIGNAL);
                this.sendNotice(warningTraceRecord, warningTypeEnumList);
            }
        }
    }

    private void addBatchInsertWarningDetailList(List<WarningDetail> batchInsertWarningDetailList,
            WarningDetail warningDetail) {
        for (WarningDetail detail : batchInsertWarningDetailList) {
            if (detail.getLicensePlate().equals(warningDetail.getLicensePlate())) {
                return;
            }
        }
        batchInsertWarningDetailList.add(warningDetail);
    }

    private TireNewestData getTireNewestData(Integer tireSiteId,
            List<TireNewestData> tirePressureDataDtoList, VehicleSpecContextVo vehicleSpec) {
        TireNewestData tirePressureData = null;
        for (TireNewestData tireNewestData : tirePressureDataDtoList) {
            // 主车或者挂车轮胎数据
            if (vehicleSpec.getSpecType() == tireNewestData.getTireType() + 1
                    && tireSiteId.equals(
                    Integer.valueOf(tireNewestData.getTireSiteId()))) {
                tirePressureData = tireNewestData;
                break;
            }
        }
        return tirePressureData;
    }

    private WarningDetail getWarningDetail(VehicleEntity vehicle, Integer tireSiteId,
            String receiverIdNumber, String traceNo, String tireCode,
            TireSiteResult tireSiteResult) {
        WarningDetail warningDetail = new WarningDetail();
        warningDetail.setReceiverId(receiverIdNumber);
//        warningDetail.setSerialNo();
        warningDetail.setTraceNo(traceNo);
        warningDetail.setClientId(vehicle.getCustomerId());
        warningDetail.setFleetId(vehicle.getFleetId());
        warningDetail.setLicensePlate(vehicle.getLicensePlate());
        warningDetail.setTireCode(tireCode);
        warningDetail.setTireSite(tireSiteId);
        if (tireSiteResult != null) {
            warningDetail.setTireSiteName(
                    tireSiteResult.getTireSiteName());
        }
        warningDetail.setWarningType(NO_SIGNAL.getType());
        return warningDetail;
    }

    private WarningTraceRecord getWarningTraceRecord(VehicleEntity vehicle, Integer tireSiteId,
            TireSiteResult tireSiteResult, String traceNo, String tireCode,
            NewestGeoLocation tirePressureData) {
        WarningTraceRecord warningTraceRecord = new WarningTraceRecord();
        warningTraceRecord.setTraceNo(traceNo);
        warningTraceRecord.setTireCode(tireCode);
        CustomerContextVo fleet = customerContext.getContext(vehicle.getFleetId());
        CustomerContextVo client = customerContext.getContext(vehicle.getCustomerId());
        warningTraceRecord.setFleetId(vehicle.getFleetId());
        if (fleet != null) {
            warningTraceRecord.setFleetName(fleet.getName());
        }
        warningTraceRecord.setClientId(vehicle.getCustomerId());
        if (client != null) {
            warningTraceRecord.setClientName(client.getName());
        }
        warningTraceRecord.setLicensePlate(vehicle.getLicensePlate());
//        warningTraceRecord.setTireSiteId(tireSiteId);
//        if (tireSiteResult != null) {
//            warningTraceRecord.setTireSiteName(
//                    tireSiteResult.getTireSiteName());
//        }
        warningTraceRecord.setWarningType(
                NO_SIGNAL.getDescription());
//        warningTraceRecord.setVoltage();
        // 根据高德坐标获取逆地理编码
        String currentLocation = "";
        if (tirePressureData != null) {
            try {
                // 调用高德地图
                ConvertCoordVo convertCoordVo = amapUtil.convertCoord(
                        tirePressureData.getLng(),
                        tirePressureData.getLat());
                String amapCoord = convertCoordVo.getLocations();
                RegeocodeResultVo regeocodeResult = amapUtil.regeocode(
                        amapCoord);
                RegeocodeVo regeocode = regeocodeResult.getRegeocode();
                AddressComponentVo addressComponent = regeocode.getAddressComponent();
                currentLocation =
                        addressComponent.getProvince()
                                + addressComponent.getCity()
                                + addressComponent.getDistrict();
            } catch (Exception e) {
                log.error("高德地图逆地理编码异常", e);
            }
        }
        warningTraceRecord.setLocation(currentLocation);
        warningTraceRecord.setType(WarningConstant.FOLLOW_UP_TYPE_COMMON);
        return warningTraceRecord;
    }

    private VehicleEntity getMainVehicle(List<VehicleTrailerInfo> vehicleTrailerInfoList,
            VehicleEntity minorVehicle, List<VehicleEntity> vehicleList) {
        for (VehicleTrailerInfo vehicleTrailerInfo : vehicleTrailerInfoList) {
            if (minorVehicle.getLicensePlate()
                    .equals(vehicleTrailerInfo.getMinorLicensePlate())) {
                for (VehicleEntity entity : vehicleList) {
                    if (entity.getLicensePlate()
                            .equals(vehicleTrailerInfo.getMainLicensePlate())) {
                        return entity;
                    }
                }
            }
        }
        return null;
    }

    private VehicleEntity getMinorVehicle(List<VehicleTrailerInfo> vehicleTrailerInfoList,
            VehicleEntity mainVehicle, List<VehicleEntity> vehicleList) {
        for (VehicleTrailerInfo vehicleTrailerInfo : vehicleTrailerInfoList) {
            if (mainVehicle.getLicensePlate().equals(vehicleTrailerInfo.getMainLicensePlate())) {
                for (VehicleEntity entity : vehicleList) {
                    if (entity.getLicensePlate()
                            .equals(vehicleTrailerInfo.getMinorLicensePlate())) {
                        return entity;
                    }
                }
            }
        }
        return null;
    }

    private VehicleSpecContextVo getVehicleSpecList(VehicleEntity vehicle,
            Map<Integer, VehicleSpecContextVo> vehicleSpecContextMap) {
        if (vehicle == null) {
            return null;
        }
        return vehicleSpecContextMap.get(vehicle.getSpecId());

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


    /**
     * 获取车辆驾驶状态
     *
     * @param geoLocation
     * @param vehicle
     * @return
     */
    private Integer getVehicleDrivingStatus(NewestGeoLocation geoLocation, VehicleEntity vehicle,
            List<TireNewestData> tireNewestDataList) {
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
                    if (tireNewestData.getClientId().equals(vehicle.getReceiverIdNumber())) {
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

    private void sendNotice(WarningTraceRecord warningTraceRecord,
            List<WarningTypeEnum> warningTypeEnumList) {
        SysNoticeWarningParam sysNoticeWarningParam = new SysNoticeWarningParam();
        sysNoticeWarningParam.setTargetId(String.valueOf(warningTraceRecord.getId()));
        sysNoticeWarningParam.setType(NoticeConfigConstant.WARNING);
        sysNoticeWarningParam.setMainTitle("【TPMS】");
        String secondTitle =
                warningTraceRecord.getLicensePlate() + "[" + warningTraceRecord.getTireSiteName()
                        + "]" + warningTraceRecord.getWarningType();
        sysNoticeWarningParam.setSecondTitle(secondTitle);
        sysNoticeWarningParam.setContent(secondTitle);
        sysNoticeWarningParam.setSenderId(NoticeConfigConstant.DEFAULT_SENDER_ID);
        sysNoticeWarningParam.setSender(NoticeConfigConstant.DEFAULT_SENDER);
        sysNoticeWarningParam.setFleetId(warningTraceRecord.getFleetId());
        sysNoticeWarningParam.setWarningTypeEnumList(warningTypeEnumList);
        sysNoticeService.sendTireWarningNotice(sysNoticeWarningParam);
    }

    private String getTireCode(TireNewestData data, List<AssetTire> assetTireList) {
        String tireCode = "";
        if (data == null) {
            return tireCode;
        }
        for (AssetTire assetTire : assetTireList) {
            if (assetTire.getSensorId().equals(data.getTireSensorId())) {
                tireCode = assetTire.getCode();
                break;
            }
        }
        return tireCode;
    }

    private void addBatchInsertWarningTraceRecordList(
            List<WarningTraceRecord> batchInsertWarningTraceRecordList,
            WarningTraceRecord warningTraceRecord) {
        for (WarningTraceRecord traceRecord : batchInsertWarningTraceRecordList) {
            if (traceRecord.getLicensePlate().equals(warningTraceRecord.getLicensePlate())) {
                return;
            }
        }
        batchInsertWarningTraceRecordList.add(warningTraceRecord);
    }
}
