package org.platform.vehicle.service.impl;

import static org.platform.vehicle.constant.LocalWarningConstant.VEHICLE_STATUS_PARKING;
import static org.platform.vehicle.constant.LocalWarningConstant.VEHICLE_STATUS_POWER_OFF;
import static org.platform.vehicle.constant.LocalWarningConstant.VEHICLE_STATUS_RUNNING;
import static org.platform.vehicle.constant.LocalWarningConstant.WARNING_TYPE_MAIN_POWER_OFF;
import static org.platform.vehicle.constant.WarningPressureConstant.BINDING_RELAY_KEY_PREFIX;
import static org.platform.vehicle.constant.WarningPressureConstant.SYNC_THRESHOLD_KEY_PREFIX;
import static org.platform.vehicle.constant.WarningPressureConstant.SYNC_WHEEL_KEY_PREFIX;
import static org.platform.vehicle.constant.WarningPressureConstant.VEHICLE_HANG_KEY_PREFIX;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.concurrent.TimeUnit;
import org.platform.vehicle.conf.ExcelCellWidthStyleStrategy;
import org.platform.vehicle.conf.VehicleSpecContext;
import org.platform.vehicle.constant.AssetTireConstant;
import org.platform.vehicle.constant.EsConstant;
import org.platform.vehicle.constant.LocalWarningTypeEnum;
import org.platform.vehicle.constant.VehicleTrailerInfoConstant;
import org.platform.vehicle.constant.WarningConstant;
import org.platform.vehicle.constant.WarningTypeEnum;
import org.platform.vehicle.dto.VehicleTrailerInfoDto;
import org.platform.vehicle.dto.WarningDetailDto;
import org.platform.vehicle.entity.AssetTire;
import org.platform.vehicle.entity.AssetTireFitRecord;
import org.platform.vehicle.entity.JT808.GeoLocationDto;
import org.platform.vehicle.entity.JT808.NewestGeoLocation;
import org.platform.vehicle.entity.JT808.TireNewestData;
import org.platform.vehicle.entity.JT808.TirePressureDataDto;
import org.platform.vehicle.entity.SysCustomer;
import org.platform.vehicle.entity.TireBrandEntity;
import org.platform.vehicle.entity.VehicleEntity;
import org.platform.vehicle.entity.VehicleSpecEntity;
import org.platform.vehicle.entity.VehicleTrailerInfo;
import org.platform.vehicle.entity.WarningDetail;
import org.platform.vehicle.feign.param.SyncIntervalParam;
import org.platform.vehicle.feign.param.TirePressureIntervalParam;
import org.platform.vehicle.mapper.AssetTireFitRecordMapper;
import org.platform.vehicle.mapper.AssetTireMapper;
import org.platform.vehicle.mapper.SysCustomerMapper;
import org.platform.vehicle.mapper.TireBrandMapper;
import org.platform.vehicle.mapper.VehicleMapper;
import org.platform.vehicle.mapper.VehicleSpecMapper;
import org.platform.vehicle.mapper.VehicleTrailerInfoMapper;
import org.platform.vehicle.mapper.WarningDetailMapper;
import org.platform.vehicle.mapper.jt808.GeoLocationRepository;
import org.platform.vehicle.mapper.jt808.NewestGeoLocationMapper;
import org.platform.vehicle.mapper.jt808.TireNewestDataMapper;
import org.platform.vehicle.mapper.jt808.TirePressureDataRepository;
import org.platform.vehicle.mapper.jt808.TirePressureRepository;
import org.platform.vehicle.param.RelayBindParam;
import org.platform.vehicle.param.TireCheckDataDetailParam;
import org.platform.vehicle.param.TireTrackParam;
import org.platform.vehicle.param.TireWarningDataParam;
import org.platform.vehicle.param.TrailerInstallCallbackParam;
import org.platform.vehicle.param.VehicleHangParam;
import org.platform.vehicle.param.WarmPressingConditionQuery;
import org.platform.vehicle.param.WarmPressingExportParam;
import org.platform.vehicle.param.WarningThresholdSyncParam;
import org.platform.vehicle.param.WheelSyncParam;
import org.platform.vehicle.service.Jt808FeignService;
import org.platform.vehicle.service.WarmPressingService;
import org.platform.vehicle.util.AmapUtil;
import org.platform.vehicle.util.DynamicIndex;
import org.platform.vehicle.util.EasyExcelUtils;
import org.platform.vehicle.util.TireSiteUtil;
import org.platform.vehicle.util.WarningHelper;
import org.platform.vehicle.vo.AltitudeTrendVo;
import org.platform.vehicle.vo.SpeedAltitudeExportVo;
import org.platform.vehicle.vo.SpeedTrendVo;
import org.platform.vehicle.vo.TirePressureTrendVo;
import org.platform.vehicle.vo.TireSiteResult;
import org.platform.vehicle.vo.TireStatusVo;
import org.platform.vehicle.vo.TireTemperatureTrendVo;
import org.platform.vehicle.vo.TireTrendDetailDataVo;
import org.platform.vehicle.vo.TireTrendDetailVo;
import org.platform.vehicle.vo.TireTrendParam;
import org.platform.vehicle.vo.TireTrendVo;
import org.platform.vehicle.vo.VehicleTrackVo;
import org.platform.vehicle.vo.WarmPressingDetailVo;
import org.platform.vehicle.vo.WarmPressingPageVo;
import org.platform.vehicle.vo.WarmPressingTireVo;
import org.platform.vehicle.vo.WarningPressingExportVo;
import org.platform.vehicle.vo.amap.AddressComponentVo;
import org.platform.vehicle.vo.amap.AmapWeatherInfoVo;
import org.platform.vehicle.vo.amap.ConvertCoordVo;
import org.platform.vehicle.vo.amap.LiveVo;
import org.platform.vehicle.vo.amap.RegeocodeResultVo;
import org.platform.vehicle.vo.amap.RegeocodeVo;
import org.platform.vehicle.exception.BaseException;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.DateUtils;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @Author gejiawei
 * @Date 2023/10/12 10:35
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WarmPressingServiceImpl implements WarmPressingService {

    private final WarningDetailMapper warningDetailMapper;
    private final VehicleMapper vehicleMapper;
    private final SysCustomerMapper sysCustomerMapper;
    private final AmapUtil amapUtil;
    private final VehicleTrailerInfoMapper vehicleTrailerInfoMapper;
    private final VehicleSpecMapper vehicleSpecMapper;
    private final AssetTireMapper assetTireMapper;
    private final TireBrandMapper tireBrandMapper;
    private final AssetTireFitRecordMapper assetTireFitRecordMapper;
    private final GeoLocationRepository geoLocationRepository;
    private final TirePressureRepository tirePressureRepository;
    private final TirePressureDataRepository tirePressureDataRepository;
    private final VehicleSpecContext vehicleSpecContext;
    private final Jt808FeignService jt808FeignService;
    private final NewestGeoLocationMapper newestGeoLocationMapper;
    private final TireNewestDataMapper tireNewestDataMapper;
    private final DynamicIndex dynamicIndex;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 温压管理-实时温压-车辆条件查询
     *
     * @param param
     * @return return
     */
    @Override
    public BasePageResponse conditionQuery(WarmPressingConditionQuery param) {
        UserVo user = UserContext.getUser();
        param.setCustomerIds(user.getCustomerIds());
        Page<VehicleTrailerInfoDto> page = new Page<>(param.getPageNum(), param.getPageSize());
        Page<VehicleTrailerInfoDto> vehicleTrailerInfoDtoPage = vehicleTrailerInfoMapper.conditionQuery(
                page, param);
        List<VehicleTrailerInfoDto> records = vehicleTrailerInfoDtoPage.getRecords();
        if (records.isEmpty()) {
            return BasePageResponse.ok(new ArrayList<>(), page);
        }
        List<String> receiverIdList = records.stream()
                .map(VehicleTrailerInfoDto::getReceiverIdNumber)
                .collect(Collectors.toList());
        // 查询最新轮胎数据
        List<TireNewestData> tireNewestDataList = tireNewestDataMapper.selectList(
                new LambdaQueryWrapper<TireNewestData>()
                        .in(TireNewestData::getClientId, receiverIdList)
                        .select(TireNewestData::getClientId,
                                TireNewestData::getSerialNo,
                                TireNewestData::getTireType,
                                TireNewestData::getTireSiteId));
        // 查询告警明细
        List<WarningDetail> warningDetailList = warningDetailMapper.selectList(
                new LambdaQueryWrapper<WarningDetail>()
                        .in(WarningDetail::getReceiverId, receiverIdList)
                        .in(WarningDetail::getSerialNo, tireNewestDataList
                                .stream()
                                .map(TireNewestData::getSerialNo)
                                .collect(Collectors.toSet())));
        List<WarmPressingPageVo> warmPressingPageVoList = new ArrayList<>();
        for (VehicleTrailerInfoDto vehicleTrailerInfoDto : records) {
            WarmPressingPageVo warmPressingPageVo = new WarmPressingPageVo();
            warmPressingPageVo.setLicensePlate(vehicleTrailerInfoDto.getLicensePlate());
            warmPressingPageVo.setMainLicensePlate(vehicleTrailerInfoDto.getMainLicensePlate());
            warmPressingPageVo.setMinorLicensePlate(vehicleTrailerInfoDto.getMinorLicensePlate());
            warmPressingPageVo.setMainRelayId(vehicleTrailerInfoDto.getMainRelayId());
            warmPressingPageVo.setMinorRelayId(vehicleTrailerInfoDto.getMinorRelayId());
            warmPressingPageVo.setType(vehicleTrailerInfoDto.getType());
            List<Integer> vehicleWarningTypeList = new ArrayList<>();
            for (WarningDetail warningDetail : warningDetailList) {
                if (warningDetail.getReceiverId()
                        .equals(vehicleTrailerInfoDto.getReceiverIdNumber())) {
                    vehicleWarningTypeList.add(warningDetail.getWarningType());
                }
            }
            warmPressingPageVo.setColor(this.getVehicleWarningColor(vehicleWarningTypeList));
            warmPressingPageVoList.add(warmPressingPageVo);
        }
        return BasePageResponse.ok(warmPressingPageVoList, page);
    }

    /**
     * 温压管理-实时温压-详情
     *
     * @param licensePlate
     * @return return
     */
    @Override
    public BaseResponse<WarmPressingDetailVo> getWarmPressingDetailDetail(String licensePlate) {
        VehicleEntity vehicle = vehicleMapper.selectOne(new LambdaQueryWrapper<VehicleEntity>()
                .eq(VehicleEntity::getLicensePlate, licensePlate)
                .eq(VehicleEntity::getIsDeleted, false));
        if (vehicle == null) {
            return BaseResponse.failure("车辆信息不存在");
        }
        // 查询车辆型号信息
        VehicleSpecEntity vehicleSpec = vehicleSpecMapper.selectOne(
                new LambdaQueryWrapper<VehicleSpecEntity>()
                        .eq(VehicleSpecEntity::getId, vehicle.getSpecId())
                        .eq(VehicleSpecEntity::getIsDeleted, false));
        // 查询最新一条车辆位置信息
        NewestGeoLocation geoLocation = newestGeoLocationMapper.selectOne(
                new LambdaQueryWrapper<NewestGeoLocation>()
                        .eq(NewestGeoLocation::getReceiverId, vehicle.getReceiverIdNumber()));
        // 查询轮胎最新信息
        List<TireNewestData> tireNewestDataList = new ArrayList<>();
        if (vehicleSpec.getSpecType() == 1) {
            if (StringUtils.isNotBlank(vehicle.getReceiverIdNumber())) {
                tireNewestDataList = tireNewestDataMapper.selectList(
                        new LambdaQueryWrapper<TireNewestData>()
                                .eq(TireNewestData::getClientId, vehicle.getReceiverIdNumber()));
            }
        } else if (vehicleSpec.getSpecType() == 2) {
            if (StringUtils.isNotBlank(vehicle.getRepeaterIdNumber())) {
                tireNewestDataList = tireNewestDataMapper.selectList(
                        new LambdaQueryWrapper<TireNewestData>()
                                .eq(TireNewestData::getGuaRepeaterId,
                                        vehicle.getRepeaterIdNumber()));
            }
        }
        SysCustomer fleet = sysCustomerMapper.selectById(vehicle.getFleetId());
        // 查询车辆挂车绑定信息
        List<VehicleTrailerInfo> vehicleTrailerInfoList = vehicleTrailerInfoMapper.selectList(
                new LambdaQueryWrapper<VehicleTrailerInfo>()
                        .eq(VehicleTrailerInfo::getMainLicensePlate, vehicle.getLicensePlate())
                        .eq(VehicleTrailerInfo::getType,
                                VehicleTrailerInfoConstant.TYPE_MAIN_MINOR));
        WarmPressingDetailVo warmPressingDetailVo = new WarmPressingDetailVo();
        warmPressingDetailVo.setLicensePlate(vehicle.getLicensePlate());
        warmPressingDetailVo.setGpsId(vehicle.getReceiverIdNumber());
        warmPressingDetailVo.setMainRepeaterId(vehicle.getRepeaterIdNumber());
        // 设置绑定后的挂车信息
        if (!vehicleTrailerInfoList.isEmpty()) {
            VehicleTrailerInfo vehicleTrailerInfo = vehicleTrailerInfoList.get(0);
            VehicleEntity gua = vehicleMapper.selectOne(new LambdaQueryWrapper<VehicleEntity>()
                    .eq(VehicleEntity::getLicensePlate, vehicleTrailerInfo.getMinorLicensePlate())
                    .eq(VehicleEntity::getIsDeleted, false));
            warmPressingDetailVo.setGuaLicensePlate(gua.getLicensePlate());
            warmPressingDetailVo.setGuaRepeaterId(gua.getRepeaterIdNumber());
            VehicleSpecEntity guaVehicleSpec = vehicleSpecMapper.selectOne(
                    new LambdaQueryWrapper<VehicleSpecEntity>()
                            .eq(VehicleSpecEntity::getId, gua.getSpecId())
                            .eq(VehicleSpecEntity::getIsDeleted, false));
            // 车辆轮位号
            // 设备数据采集,轮胎告警数据
            List<WarmPressingTireVo> minorTireList = this.getWarmPressingTireVoList(
                    vehicle.getReceiverIdNumber(), gua.getLicensePlate(), guaVehicleSpec,
                    tireNewestDataList, 1);
            warmPressingDetailVo.setGuaTireList(minorTireList);
        }
        // 1.若当前车辆的GPS_ID最新一条数据状态是“掉电报警标志”，且10分钟内无温压数据回传，则当前车辆状态为“已断电”
        // 2.若当前有速度海拔数据，或有温压数据，则判断距离系统当前时间5分钟内是否有速度大于0的数据，如果有，则状态为“行驶中”，如果没有则是“已停车”。
        Integer vehicleStatus = this.getVehicleDrivingStatus(geoLocation, vehicle,
                tireNewestDataList);
        warmPressingDetailVo.setVehicleStatus(vehicleStatus);
        String temperature = "";
        String currentLocation = "";
        String locations = "";
        if (geoLocation != null) {
            try {
                ConvertCoordVo convertCoordVo = amapUtil.convertCoord(geoLocation.getLng(),
                        geoLocation.getLat());
                locations = convertCoordVo.getLocations();
                // 根据高德坐标获取逆地理编码
                RegeocodeResultVo regeocodeResult = amapUtil.regeocode(locations);
                RegeocodeVo regeocode = regeocodeResult.getRegeocode();
                AddressComponentVo addressComponent = regeocode.getAddressComponent();
                String adcode = addressComponent.getAdcode();
                // 获取天气信息
                AmapWeatherInfoVo weatherInfo = amapUtil.getWeatherInfo(adcode);
                LiveVo liveVo = weatherInfo.getLives().get(0);
                temperature = liveVo.getTemperature();
                currentLocation = addressComponent.getProvince() + addressComponent.getCity()
                        + addressComponent.getDistrict();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("获取天气信息失败, error:{}", e.getMessage());
            }
        }
        warmPressingDetailVo.setCurrentLocation(currentLocation);
        warmPressingDetailVo.setLocalTemperature(temperature);
        if (StringUtils.isNotBlank(locations)) {
            warmPressingDetailVo.setLongitude(locations.split(",")[0]);
            warmPressingDetailVo.setLatitude(locations.split(",")[1]);
        }
        warmPressingDetailVo.setFleetName(fleet.getName());
        warmPressingDetailVo.setVehicleLine(vehicle.getRunRoute());
        // 车辆轮位号
        // 设备数据采集,轮胎告警数据
        List<WarmPressingTireVo> mainTireList = this.getWarmPressingTireVoList(
                vehicle.getReceiverIdNumber(), vehicle.getLicensePlate(), vehicleSpec,
                // vehicleSpec.getSpecType() - 1 是因为系统车辆规格和设备车辆规格不一致
                tireNewestDataList, vehicleSpec.getSpecType() - 1);
        warmPressingDetailVo.setMainTireList(mainTireList);
        warmPressingDetailVo.setSpecType(vehicleSpec.getSpecType());
        return BaseResponse.ok(warmPressingDetailVo);
    }

    /**
     * 温压管理-实时温压-获取车辆轮胎数据
     *
     * @param receiverId         GPS_ID
     * @param licensePlate       车牌号
     * @param vehicleSpec        车辆规格
     * @param tireNewestDataList 轮胎最新数据
     * @param type               0-主车, 1-挂车
     * @return
     */
    private List<WarmPressingTireVo> getWarmPressingTireVoList(String receiverId,
            String licensePlate, VehicleSpecEntity vehicleSpec,
            List<TireNewestData> tireNewestDataList, Integer type) {
        List<WarmPressingTireVo> tireList = new ArrayList<>();
        List<Integer> tireSiteList = this.getTireSiteIdList(vehicleSpec);
        // 查询轮胎最新告警信息
        List<WarningDetailDto> warningDetailDtoList = warningDetailMapper.selectLatestWarnings(
                licensePlate, tireSiteList);
        for (Integer tireSiteId : tireSiteList) {
            WarmPressingTireVo warmPressingTireVo = new WarmPressingTireVo();
            warmPressingTireVo.setTireSiteId(tireSiteId);
            TireSiteResult tireSiteResult = TireSiteUtil.getTireSiteResult(
                    tireSiteId,
                    vehicleSpec.getSpecType(), vehicleSpec.getWheelCount(),
                    vehicleSpec.getWheelbaseType());
            if (tireSiteResult != null) {
                warmPressingTireVo.setTireSitName(tireSiteResult.getTireSiteName());
            }
            if (!tireNewestDataList.isEmpty()) {
                for (TireNewestData tireNewestData : tireNewestDataList) {
                    if (type.equals(tireNewestData.getTireType())
                            && tireSiteId.equals(
                            Integer.valueOf(tireNewestData.getTireSiteId()))) {
                        warmPressingTireVo.setTirePressure(tireNewestData.getTirePressure());
                        warmPressingTireVo.setTireTemperature(
                                tireNewestData.getTireTemperature());
                        // 查询轮胎告警信息
                        for (WarningDetailDto warningDetailDto : warningDetailDtoList) {
                            if (Integer.valueOf(tireNewestData.getTireSiteId())
                                    .equals(warningDetailDto.getTireSite())) {
                                warmPressingTireVo.setColor(
                                        this.getTireWarningColor(
                                                warningDetailDto.getWarningType()));
                                break;
                            }
                        }
                    }
                }
            }
            tireList.add(warmPressingTireVo);
        }
        return tireList;
    }

    private Integer getTireWarningColor(Integer warningType) {
        if (warningType.equals(WarningTypeEnum.FIRST_HIGH_PRESSURE.getType())
                || warningType.equals(WarningTypeEnum.FIRST_HIGH_TEMPERATURE.getType())
                || warningType.equals(WarningTypeEnum.FIRST_LOW_PRESSURE.getType())
                || warningType.equals(WarningTypeEnum.FAST_LEAK.getType())) {
            return WarningConstant.WARNING_COLOR_RED;
        } else if (warningType.equals(WarningTypeEnum.SECOND_HIGH_PRESSURE.getType())
                || warningType.equals(WarningTypeEnum.SECOND_HIGH_TEMPERATURE.getType())
                || warningType.equals(WarningTypeEnum.SECOND_LOW_PRESSURE.getType())
                || warningType.equals(WarningTypeEnum.LOW_VOLTAGE.getType())) {
            return WarningConstant.WARNING_COLOR_YELLOW;
        } else if (warningType.equals(WarningTypeEnum.NO_SIGNAL.getType())) {
            return WarningConstant.WARNING_COLOR_GRAY;
        } else {
            return WarningConstant.WARNING_COLOR_GREEN;
        }
    }

    private List<Integer> getTireSiteIdList(VehicleSpecEntity vehicleSpec) {
        List<Integer> tireSiteList = new ArrayList<>();
        String wheelArrange = vehicleSpec.getWheelArrange();
        if (StringUtils.isNotBlank(wheelArrange)) {
            for (String axle : wheelArrange.split(";")) {
                for (String tireSite : axle.split(",")) {
                    tireSiteList.add(Integer.valueOf(tireSite));
                }
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
        if (geoLocation == null) {
            return VEHICLE_STATUS_PARKING;
        }
        String warnBinaryStr = this.toBinaryString(geoLocation.getWarnBit());
        // 获取行驶信息告警
        List<LocalWarningTypeEnum> geoLocationWarningType = WarningHelper.getGeoLocationWarningType(
                warnBinaryStr);
        Integer vehicleStatus = null;
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

    /**
     * 温压管理-实时温压-下挂
     *
     * @param param
     * @return
     */
    @Override
    public BaseResponse editTrailerStatus(VehicleHangParam param) {
        // 查询车辆挂车绑定信息
        VehicleTrailerInfo main = vehicleTrailerInfoMapper.selectOne(
                new LambdaQueryWrapper<VehicleTrailerInfo>()
                        .eq(VehicleTrailerInfo::getMainLicensePlate, param.getMainLicensePlate())
                        .eq(VehicleTrailerInfo::getMinorLicensePlate, param.getMinorLicensePlate())
                        .eq(VehicleTrailerInfo::getType,
                                VehicleTrailerInfoConstant.TYPE_MAIN_MINOR));
        VehicleTrailerInfo minor = vehicleTrailerInfoMapper.selectOne(
                new LambdaQueryWrapper<VehicleTrailerInfo>()
                        .eq(VehicleTrailerInfo::getMainLicensePlate, param.getMinorLicensePlate())
                        .eq(VehicleTrailerInfo::getMinorLicensePlate, param.getMainLicensePlate())
                        .eq(VehicleTrailerInfo::getType,
                                VehicleTrailerInfoConstant.TYPE_MINOR_MAIN));
        // 查询主挂车辆信息
        VehicleEntity mainVehicle = vehicleMapper.selectOne(new LambdaQueryWrapper<VehicleEntity>()
                .eq(VehicleEntity::getLicensePlate, param.getMainLicensePlate())
                .eq(VehicleEntity::getIsDeleted, false));
        if (mainVehicle == null) {
            return BaseResponse.failure("主挂车辆信息不存在");
        }
        // 查询挂车车辆信息
        VehicleEntity minorVehicle = vehicleMapper.selectOne(new LambdaQueryWrapper<VehicleEntity>()
                .eq(VehicleEntity::getLicensePlate, param.getMinorLicensePlate())
                .eq(VehicleEntity::getIsDeleted, false));
        if (StringUtils.isBlank(mainVehicle.getReceiverIdNumber())) {
            return BaseResponse.failure("主挂车辆接收器ID为空");
        }
        // redis锁
        String lockKey = VEHICLE_HANG_KEY_PREFIX + mainVehicle.getReceiverIdNumber();
        if (!this.tryLock(lockKey)) {
            return BaseResponse.failure("当前车辆操作中");
        }
        try {
            // 1是上挂,2是下挂
            if (param.getType() == 1) {
                if (StringUtils.isBlank(minorVehicle.getReceiverIdNumber())) {
                    return BaseResponse.failure("挂车车辆接收器ID为空");
                }
                jt808FeignService.trailerHangOn(
                        mainVehicle.getReceiverIdNumber(), minorVehicle.getReceiverIdNumber());
            } else if (param.getType() == 2) {
                jt808FeignService.trailerHangUnder(
                        mainVehicle.getReceiverIdNumber());
            }
        } catch (Exception e) {
            log.error("上下挂失败, error:{}", e.getMessage());
            this.unLock(lockKey);
            return BaseResponse.failure("上下挂失败");
        }
        return BaseResponse.ok();
    }

    /**
     * 温压管理-实时温压-上下挂回调
     *
     * @param param
     */
    @Override
    public void trailerUnInstallCallback(TrailerInstallCallbackParam param) {
        // 查询车辆挂车绑定信息
        VehicleTrailerInfo main = vehicleTrailerInfoMapper.selectOne(
                new LambdaQueryWrapper<VehicleTrailerInfo>()
                        .eq(VehicleTrailerInfo::getMainRelayId, param.getMainRelayId())
                        .eq(VehicleTrailerInfo::getMinorRelayId, param.getMinorRelayId())
                        .eq(VehicleTrailerInfo::getType,
                                VehicleTrailerInfoConstant.TYPE_MAIN_MINOR));
        VehicleTrailerInfo minor = vehicleTrailerInfoMapper.selectOne(
                new LambdaQueryWrapper<VehicleTrailerInfo>()
                        .eq(VehicleTrailerInfo::getMainRelayId, param.getMinorRelayId())
                        .eq(VehicleTrailerInfo::getMinorRelayId, param.getMainRelayId())
                        .eq(VehicleTrailerInfo::getType,
                                VehicleTrailerInfoConstant.TYPE_MINOR_MAIN));
        if (main == null || minor == null) {
            log.error("主挂信息不存在, param:{}", param);
            return;
        }
        vehicleTrailerInfoMapper.deleteById(main.getId());
        vehicleTrailerInfoMapper.deleteById(minor.getId());
        log.info("主挂信息删除成功, param:{}", param);
    }

    /**
     * 温压管理-实时温压-根据车牌获取中继器ID
     *
     * @param licensePlate
     * @return
     */
    @Override
    public BaseResponse<String> getRelayId(String licensePlate) {
        VehicleEntity vehicle = vehicleMapper.selectOne(new LambdaQueryWrapper<VehicleEntity>()
                .eq(VehicleEntity::getLicensePlate, licensePlate)
                .eq(VehicleEntity::getIsDeleted, false));
        if (vehicle == null) {
            return BaseResponse.ok("");
        }
        return BaseResponse.ok(vehicle.getRepeaterIdNumber());
    }

    /**
     * 温压管理-实时温压-绑定中继器
     *
     * @param param
     * @return
     */
    @Override
    public BaseResponse bindRelay(RelayBindParam param) {
        VehicleTrailerInfo main = vehicleTrailerInfoMapper.selectOne(
                new LambdaQueryWrapper<VehicleTrailerInfo>()
                        .eq(VehicleTrailerInfo::getMainLicensePlate, param.getMainLicensePlate())
                        .eq(VehicleTrailerInfo::getMinorLicensePlate, param.getMinorLicensePlate())
                        .eq(VehicleTrailerInfo::getType,
                                VehicleTrailerInfoConstant.TYPE_MAIN_MINOR));
        VehicleEntity vehicle = vehicleMapper.selectOne(
                new LambdaQueryWrapper<VehicleEntity>()
                        .eq(VehicleEntity::getLicensePlate, param.getMainLicensePlate())
                        .eq(VehicleEntity::getIsDeleted, false));
        if (vehicle == null) {
            return BaseResponse.failure("主车辆信息不存在");
        }
        if (StringUtils.isBlank(vehicle.getReceiverIdNumber())) {
            return BaseResponse.failure("主车辆接收器ID为空");
        }
        // 绑定主车挂车中继器,校验挂车是否上挂
        if (param.getType() == 2 && main == null) {
            return BaseResponse.failure("挂车未上挂");
        }
        if (param.getType() == 1 && StringUtils.isBlank(param.getMainRelay())) {
            return BaseResponse.failure("主车或挂车中继器不能为空");
        }
        if (param.getType() == 2) {
            if (StringUtils.isBlank(param.getMainRelay()) || StringUtils.isBlank(
                    param.getMinorRelay())) {
                return BaseResponse.failure("主车或挂车中继器不能为空");
            }
        }
        if (param.getType() == 2) {
            // 保存主车挂车中继器
            VehicleTrailerInfo mainUpdateParam = new VehicleTrailerInfo();
            mainUpdateParam.setId(main.getId());
            mainUpdateParam.setMainRelayId(param.getMainRelay());
            mainUpdateParam.setMinorRelayId(param.getMinorRelay());
            vehicleTrailerInfoMapper.updateById(mainUpdateParam);
            VehicleTrailerInfo minor = vehicleTrailerInfoMapper.selectOne(
                    new LambdaQueryWrapper<VehicleTrailerInfo>()
                            .eq(VehicleTrailerInfo::getMainLicensePlate,
                                    param.getMinorLicensePlate())
                            .eq(VehicleTrailerInfo::getMinorLicensePlate,
                                    param.getMainLicensePlate())
                            .eq(VehicleTrailerInfo::getType,
                                    VehicleTrailerInfoConstant.TYPE_MINOR_MAIN));
            VehicleTrailerInfo minorUpdateParam = new VehicleTrailerInfo();
            minorUpdateParam.setId(minor.getId());
            minorUpdateParam.setMainRelayId(param.getMinorRelay());
            minorUpdateParam.setMinorRelayId(param.getMainRelay());
            vehicleTrailerInfoMapper.updateById(minorUpdateParam);
        }
        // redis锁
        String lockKey = BINDING_RELAY_KEY_PREFIX + vehicle.getReceiverIdNumber();
        if (!this.tryLock(lockKey)) {
            return BaseResponse.failure("当前车辆操作中");
        }
        try {
            // 绑定主车中继器
            if (param.getType() == 1) {
                jt808FeignService.bindRepeater(1, vehicle.getReceiverIdNumber(),
                        param.getMainRelay());
                // 绑定主车挂车中继器
            } else if (param.getType() == 2) {
                jt808FeignService.bindRepeater(1,
                        vehicle.getReceiverIdNumber(),
                        param.getMainRelay());
                jt808FeignService.bindRepeater(1,
                        vehicle.getReceiverIdNumber(),
                        param.getMinorRelay());
            }
        } catch (Exception e) {
            log.error("绑定中继器失败, error:{}", e.getMessage());
            this.unLock(lockKey);
            return BaseResponse.failure("绑定中继器失败");
        }
        return BaseResponse.ok();
    }

    /**
     * 温压管理-实时温压-轮位同步
     *
     * @param param
     * @return
     */
    @Override
    public BaseResponse syncWheel(WheelSyncParam param) {
        VehicleEntity mainVehicle = vehicleMapper.selectOne(
                new LambdaQueryWrapper<VehicleEntity>()
                        .eq(VehicleEntity::getLicensePlate, param.getMainLicensePlate())
                        .eq(VehicleEntity::getIsDeleted, false));
        if (mainVehicle == null) {
            return BaseResponse.failure("主车辆信息不存在");
        }
        if (StringUtils.isBlank(mainVehicle.getReceiverIdNumber())) {
            return BaseResponse.failure("主车辆接收器ID为空");
        }
        // redis锁
        String lockKey = SYNC_WHEEL_KEY_PREFIX + mainVehicle.getReceiverIdNumber();
        if (!this.tryLock(lockKey)) {
            return BaseResponse.failure("当前车辆操作中");
        }
        VehicleSpecEntity mainVehicleSpec = vehicleSpecMapper.selectOne(
                new LambdaQueryWrapper<VehicleSpecEntity>()
                        .eq(VehicleSpecEntity::getId, mainVehicle.getSpecId())
                        .eq(VehicleSpecEntity::getIsDeleted, false));
        if (mainVehicleSpec == null) {
            return BaseResponse.failure("主车辆型号信息不存在");
        }
        // 判断是否是主车
        if (mainVehicleSpec.getSpecType() != 1) {
            return BaseResponse.failure("主车车辆类型不正确");
        }
        // 查询车辆轮胎信息
        List<AssetTire> mainAssetTireList = assetTireMapper.selectList(
                new LambdaQueryWrapper<AssetTire>()
                        .eq(AssetTire::getLicensePlate, param.getMainLicensePlate())
                        .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
        VehicleSpecEntity minorVehicleSpec = null;
        VehicleEntity minorVehicle = null;
        List<AssetTire> minorAssetTireList = new ArrayList<>();
        if (StringUtils.isNotBlank(param.getMinorLicensePlate())) {
            minorVehicle = vehicleMapper.selectOne(
                    new LambdaQueryWrapper<VehicleEntity>()
                            .eq(VehicleEntity::getLicensePlate, param.getMinorLicensePlate())
                            .eq(VehicleEntity::getIsDeleted, false));
        }
        if (minorVehicle != null) {
            minorVehicleSpec = vehicleSpecMapper.selectOne(
                    new LambdaQueryWrapper<VehicleSpecEntity>()
                            .eq(VehicleSpecEntity::getId, minorVehicle.getSpecId())
                            .eq(VehicleSpecEntity::getIsDeleted, false));
            // 查询挂车轮胎信息
            minorAssetTireList = assetTireMapper.selectList(
                    new LambdaQueryWrapper<AssetTire>()
                            .eq(AssetTire::getLicensePlate, minorVehicle.getLicensePlate())
                            .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
        }
        try {
            // 同步主车轮位
            List<Integer> mainTireSiteList = this.getTireSiteIdList(mainVehicleSpec);
            for (Integer tireSiteId : mainTireSiteList) {
                AssetTire assetTire = this.getAssetTire(tireSiteId, mainAssetTireList);
                if (assetTire != null) {
                    // 调用设备接口
                    jt808FeignService.syncWheel(1, tireSiteId, mainVehicle.getReceiverIdNumber(),
                            assetTire.getSensorId());
                }
            }
            if (minorVehicle != null) {
                // 同步挂车轮位
                List<Integer> minorTireSiteList = this.getTireSiteIdList(minorVehicleSpec);
                for (Integer tireSiteId : minorTireSiteList) {
                    AssetTire assetTire = this.getAssetTire(tireSiteId, minorAssetTireList);
                    if (assetTire != null) {
                        // 调用设备接口
                        jt808FeignService.syncWheel(2, tireSiteId,
                                mainVehicle.getReceiverIdNumber(),
                                assetTire.getSensorId());
                    }
                }
            }
        } catch (Exception e) {
            log.error("同步轮位失败, error:{}", e.getMessage());
            this.unLock(lockKey);
            return BaseResponse.failure("同步轮位失败");
        }
        return BaseResponse.ok();
    }

    private AssetTire getAssetTire(Integer tireSiteId, List<AssetTire> assetTireList) {
        AssetTire assetTire = null;
        for (AssetTire temp : assetTireList) {
            if (tireSiteId.equals(temp.getTireSite())) {
                assetTire = temp;
                break;
            }
        }
        return assetTire;
    }

    /**
     * 温压管理-实时温压-阈值同步
     *
     * @param param
     * @return
     */
    @Override
    public BaseResponse syncThreshold(WarningThresholdSyncParam param) {
        VehicleEntity mainVehicle = vehicleMapper.selectOne(
                new LambdaQueryWrapper<VehicleEntity>()
                        .eq(VehicleEntity::getLicensePlate, param.getMainLicensePlate())
                        .eq(VehicleEntity::getIsDeleted, false));
        if (mainVehicle == null) {
            return BaseResponse.failure("主车辆信息不存在");
        }
        if (StringUtils.isBlank(mainVehicle.getReceiverIdNumber())) {
            return BaseResponse.failure("主车辆接收器ID为空");
        }
        // redis锁
        String lockKey = SYNC_THRESHOLD_KEY_PREFIX + mainVehicle.getReceiverIdNumber();
        if (!this.tryLock(lockKey)) {
            return BaseResponse.failure("当前车辆操作中");
        }
        try {
            VehicleSpecEntity mainVehicleSpec = vehicleSpecMapper.selectOne(
                    new LambdaQueryWrapper<VehicleSpecEntity>()
                            .eq(VehicleSpecEntity::getId, mainVehicle.getSpecId())
                            .eq(VehicleSpecEntity::getIsDeleted, false));
            if (mainVehicleSpec == null) {
                return BaseResponse.failure("主车辆型号信息不存在");
            }
            if (mainVehicleSpec.getWheelbaseCount() == null) {
                return BaseResponse.failure("主车辆型号设置异常");
            }
            VehicleSpecEntity minorVehicleSpec = null;
            VehicleEntity minorVehicle = null;
            if (StringUtils.isNotBlank(param.getMinorLicensePlate())) {
                minorVehicle = vehicleMapper.selectOne(
                        new LambdaQueryWrapper<VehicleEntity>()
                                .eq(VehicleEntity::getLicensePlate, param.getMinorLicensePlate())
                                .eq(VehicleEntity::getIsDeleted, false));
            }
            if (minorVehicle != null) {
                minorVehicleSpec = vehicleSpecMapper.selectOne(
                        new LambdaQueryWrapper<VehicleSpecEntity>()
                                .eq(VehicleSpecEntity::getId, minorVehicle.getSpecId())
                                .eq(VehicleSpecEntity::getIsDeleted, false));
            }
            int totalAxleCount = mainVehicleSpec.getWheelbaseCount();
            if (minorVehicleSpec != null) {
                if (minorVehicleSpec.getWheelbaseCount() != null) {
                    totalAxleCount += minorVehicleSpec.getWheelbaseCount();
                }
            }
            SyncIntervalParam syncIntervalParam = new SyncIntervalParam();
            syncIntervalParam.setZhoushu(totalAxleCount);
            syncIntervalParam.setGaowen(
                    Integer.valueOf(mainVehicleSpec.getHighTemperatureAlarmLevel1().toString()));
            List<TirePressureIntervalParam> taiya = new ArrayList<>();
            for (int i = 1; i <= totalAxleCount; i++) {
                TirePressureIntervalParam tirePressureIntervalParam = new TirePressureIntervalParam();
                tirePressureIntervalParam.setIdx(i);
                tirePressureIntervalParam.setGaoya(
                        Double.valueOf(mainVehicleSpec.getHighPressureAlarmLevel1().toString()));
                tirePressureIntervalParam.setDiya(
                        Double.valueOf(mainVehicleSpec.getLowPressureAlarmLevel1().toString()));
                taiya.add(tirePressureIntervalParam);
            }
            syncIntervalParam.setTaiya(taiya);
            jt808FeignService.syncInterval(mainVehicle.getReceiverIdNumber(),
                    JSON.toJSONString(syncIntervalParam));
        } catch (Exception e) {
            log.error("同步阈值失败, error:{}", e.getMessage());
            this.unLock(lockKey);
            return BaseResponse.failure("同步阈值失败");
        }
        return BaseResponse.ok();
    }

    /**
     * 温压管理-实时温压-轮胎状态查询
     *
     * @param licensePlate 车牌
     * @param tireSite     轮位
     * @return
     */
    @Override
    public BaseResponse<TireStatusVo> getTireStatus(String licensePlate, String tireSite) {
        // 根据车牌查询车辆信息
        VehicleEntity vehicle = vehicleMapper.selectOne(new LambdaQueryWrapper<VehicleEntity>()
                .eq(VehicleEntity::getLicensePlate, licensePlate)
                .eq(VehicleEntity::getIsDeleted, false));
        if (vehicle == null) {
            return BaseResponse.failure("车辆信息不存在");
        }
        // 查询车辆型号信息
        VehicleSpecEntity vehicleSpec = vehicleSpecMapper.selectOne(
                new LambdaQueryWrapper<VehicleSpecEntity>()
                        .eq(VehicleSpecEntity::getId, vehicle.getSpecId())
                        .eq(VehicleSpecEntity::getIsDeleted, false));
        if (vehicleSpec == null) {
            return BaseResponse.failure("车辆型号信息不存在");
        }
        AssetTire assetTire = assetTireMapper.selectOne(new LambdaQueryWrapper<AssetTire>()
                .eq(AssetTire::getLicensePlate, licensePlate)
                .eq(AssetTire::getTireSite, Integer.valueOf(tireSite)));
        TireBrandEntity tireBrand = null;
        List<AssetTireFitRecord> assetTireFitRecordList = new ArrayList<>();
        List<TireNewestData> tirePressureDataList = new ArrayList<>();
        if (assetTire != null) {
            tireBrand = tireBrandMapper.selectById(assetTire.getTireBrandId());
            assetTireFitRecordList = assetTireFitRecordMapper.selectList(
                    new LambdaQueryWrapper<AssetTireFitRecord>()
                            .eq(AssetTireFitRecord::getLicensePlate, licensePlate)
                            .eq(AssetTireFitRecord::getTireCode, assetTire.getCode())
                            .eq(AssetTireFitRecord::getType, AssetTireConstant.TIRE_INSTALL)
                            .orderByDesc(AssetTireFitRecord::getCreateTime)
                            .last("limit 1"));
            // 查询轮胎装卸记录
            tirePressureDataList = tireNewestDataMapper.selectList(
                    new LambdaQueryWrapper<TireNewestData>()
                            .eq(TireNewestData::getTireSiteId, Integer.valueOf(tireSite))
                            .eq(TireNewestData::getTireSensorId, assetTire.getSensorId()));
        }
        TireStatusVo tireStatusVo = this.getTireStatusVo(assetTire, tirePressureDataList,
                vehicleSpec, assetTireFitRecordList, tireBrand);
        return BaseResponse.ok(tireStatusVo);
    }

    /**
     * 温压管理-实时温压-查看轮胎温压趋势
     *
     * @param param
     * @return
     */
    @Override
    public BaseResponse<TireTrendVo> getTireTrend(TireTrendParam param) {
        TireTrendVo tireTrendVo = new TireTrendVo();
        List<SpeedTrendVo> speedTrend = new ArrayList<>();
        List<AltitudeTrendVo> altitudeTrend = new ArrayList<>();
        // 查询车辆信息
        List<VehicleEntity> vehicleList = vehicleMapper.selectList(
                new LambdaQueryWrapper<VehicleEntity>()
                        .eq(VehicleEntity::getReceiverIdNumber, param.getGpsId())
                        .eq(VehicleEntity::getIsDeleted, false)
                        .last("limit 1"));
        if (vehicleList.isEmpty()) {
            return BaseResponse.ok(tireTrendVo);
        }
        VehicleEntity mainVehicle = vehicleList.get(0);
        // 查询车型型号信息
        VehicleSpecEntity mainVehicleSpec = vehicleSpecMapper.selectOne(
                new LambdaQueryWrapper<VehicleSpecEntity>()
                        .eq(VehicleSpecEntity::getId, mainVehicle.getSpecId())
                        .eq(VehicleSpecEntity::getIsDeleted, false));
        // 查询挂车信息
        VehicleTrailerInfo vehicleTrailerInfo = vehicleTrailerInfoMapper.selectOne(
                new LambdaQueryWrapper<VehicleTrailerInfo>()
                        .eq(VehicleTrailerInfo::getMainLicensePlate, mainVehicle.getLicensePlate())
                        .eq(VehicleTrailerInfo::getType,
                                VehicleTrailerInfoConstant.TYPE_MAIN_MINOR));
        VehicleEntity minorVehicle = null;
        VehicleSpecEntity minorVehicleSpec = null;
        if (vehicleTrailerInfo != null) {
            minorVehicle = vehicleMapper.selectOne(
                    new LambdaQueryWrapper<VehicleEntity>()
                            .eq(VehicleEntity::getLicensePlate,
                                    vehicleTrailerInfo.getMinorLicensePlate())
                            .eq(VehicleEntity::getIsDeleted, false));
            minorVehicleSpec = vehicleSpecMapper.selectOne(
                    new LambdaQueryWrapper<VehicleSpecEntity>()
                            .eq(VehicleSpecEntity::getId, minorVehicle.getSpecId())
                            .eq(VehicleSpecEntity::getIsDeleted, false));
        }
        List<Integer> tireTypeList = new ArrayList<>();
        List<Integer> totalTireSiteIdList = new ArrayList<>();
        List<String> totalTireSiteIdStrList = new ArrayList<>();
        if (!param.getMainVehicleTireSiteIdList().isEmpty()) {
            tireTypeList.add(0);
            totalTireSiteIdList.addAll(param.getMainVehicleTireSiteIdList());
        }
        if (!param.getMinorVehicleTireSiteIdList().isEmpty()) {
            tireTypeList.add(1);
            totalTireSiteIdList.addAll(param.getMinorVehicleTireSiteIdList());
        }
        if (totalTireSiteIdList.isEmpty()) {
            return BaseResponse.ok(tireTrendVo);
        }
        for (Integer tireSiteId : totalTireSiteIdList) {
            totalTireSiteIdStrList.add(String.valueOf(tireSiteId));
        }
        dynamicIndex.setSuffix(EsConstant.TIRE_DATA_FLEET_INDEX + mainVehicle.getFleetId());
        List<TirePressureDataDto> tirePressureData = new ArrayList<>();
        try {
            tirePressureData = tirePressureDataRepository
                    .findByClientIdAndTireSiteIdInAndTypeInAndDeviceTimeBetweenOrderByDeviceTime(
                            param.getGpsId(),
                            totalTireSiteIdStrList,
                            tireTypeList, param.getStartTime(), param.getEndTime());
        } catch (Exception e) {
            log.error("es查询轮胎数据失败, error:{}", e.getMessage());
        } finally {
            dynamicIndex.remove();
        }
        if (param.getIsShowAltitudeCurve() == 1 || param.getIsShowSpeedCurve() == 1) {
            // 查询车辆位置信息
            dynamicIndex.setSuffix(EsConstant.LOCATION_INDEX + mainVehicle.getFleetId());
            List<GeoLocationDto> geoLocationList = new ArrayList<>();
            try {
                geoLocationList = geoLocationRepository.findByReceiverIdAndDeviceTimeBetweenOrderByDeviceTimeDesc(
                        param.getGpsId(),
                        param.getStartTime(),
                        param.getEndTime()
                );
            } catch (Exception e) {
                log.error("es查询位置信息失败, error:{}", e.getMessage());
            } finally {
                dynamicIndex.remove();
            }
            if (param.getIsShowSpeedCurve() == 1) {
                speedTrend = this.getSpeedTrend(geoLocationList);
            }
            if (param.getIsShowAltitudeCurve() == 1) {
                altitudeTrend = this.getAltitudeTrend(geoLocationList);
            }
        }
        // 轮胎压力趋势
        List<TirePressureTrendVo> tirePressureTrend = this.getTirePressureTrend(tirePressureData,
                mainVehicleSpec, minorVehicleSpec);
        // 轮胎温度趋势
        List<TireTemperatureTrendVo> tireTemperatureTrend = this.getTireTemperatureTrend(
                tirePressureData, mainVehicleSpec, minorVehicleSpec);

        List<TireTrendDetailVo> tireTrendDetailList = this.addTireTrendDetailList(tirePressureData,
                mainVehicleSpec, minorVehicleSpec);

//        tireTrendVo.setTirePressureTrend(tirePressureTrend);
//        tireTrendVo.setTireTemperatureTrend(tireTemperatureTrend);
        tireTrendVo.setTireTrendDetail(tireTrendDetailList);
        tireTrendVo.setSpeedTrend(speedTrend);
        tireTrendVo.setAltitudeTrend(altitudeTrend);
        return BaseResponse.ok(tireTrendVo);
    }

    private List<TireTrendDetailVo> addTireTrendDetailList(
            List<TirePressureDataDto> tirePressureData,
            VehicleSpecEntity mainVehicleSpec, VehicleSpecEntity minorVehicleSpec) {
        List<TireTrendDetailVo> tireTrendDetail = new ArrayList<>();
        if (!tirePressureData.isEmpty()) {
            for (TirePressureDataDto tirePressure : tirePressureData) {
                TireTrendDetailVo tireTrendDetailVo = new TireTrendDetailVo();
                // 主车轮胎数据
                if (tirePressure.getType() == 0) {
                    TireSiteResult tireSiteResult = TireSiteUtil.getTireSiteResult(
                            Integer.valueOf(tirePressure.getTireSiteId()),
                            mainVehicleSpec.getSpecType(), mainVehicleSpec.getWheelCount(),
                            mainVehicleSpec.getWheelbaseType());
                    if (tireSiteResult != null) {
                        tireTrendDetailVo.setTireSiteName(tireSiteResult.getTireSiteName());
                    }
                } else if (tirePressure.getType() == 1) {
                    TireSiteResult tireSiteResult = TireSiteUtil.getTireSiteResult(
                            Integer.valueOf(tirePressure.getTireSiteId()),
                            minorVehicleSpec.getSpecType(), minorVehicleSpec.getWheelCount(),
                            minorVehicleSpec.getWheelbaseType());
                    if (tireSiteResult != null) {
                        tireTrendDetailVo.setTireSiteName(tireSiteResult.getTireSiteName());
                    }
                }
                TireTrendDetailDataVo tireTrendDetailDataVo = new TireTrendDetailDataVo();
                tireTrendDetailDataVo.setPressure(tirePressure.getTirePressure());
                tireTrendDetailDataVo.setTemperature(tirePressure.getTireTemperature());
                tireTrendDetailDataVo.setCreateTime(tirePressure.getDeviceTime());
                this.addTireTrendDetailData(tireTrendDetailVo, tireTrendDetailDataVo,
                        tireTrendDetail);
            }
        }
        return tireTrendDetail;
    }

    private void addTireTrendDetailData(TireTrendDetailVo tireTrendDetailVo,
            TireTrendDetailDataVo tireTrendDetailDataVo, List<TireTrendDetailVo> tireTrendDetail) {
        for (TireTrendDetailVo trendDetailVo : tireTrendDetail) {
            if (trendDetailVo.getTireSiteName().equals(tireTrendDetailVo.getTireSiteName())) {
                trendDetailVo.getTireTrendDetailDataList().add(tireTrendDetailDataVo);
                return;
            }
        }
        tireTrendDetailVo.getTireTrendDetailDataList().add(tireTrendDetailDataVo);
        tireTrendDetail.add(tireTrendDetailVo);
    }

    /**
     * 温压管理-实时温压-查看车辆行驶轨迹
     *
     * @param param
     * @return
     */
    @Override
    public BaseResponse<List<VehicleTrackVo>> getVehicleTrack(TireTrackParam param) {
        VehicleEntity vehicle = vehicleMapper.selectOne(new LambdaQueryWrapper<VehicleEntity>()
                .eq(VehicleEntity::getReceiverIdNumber, param.getGpsId())
                .eq(VehicleEntity::getIsDeleted, false));
        if (vehicle == null) {
            return BaseResponse.ok(new ArrayList<>());
        }
        List<VehicleTrackVo> vehicleTrackVoList = new ArrayList<>();
        // 查询车辆位置
//        dynamicIndex.setSuffix(EsConstant.LOCATION_INDEX + vehicle.getFleetId());
        dynamicIndex.setSuffix(EsConstant.LOCATION_INDEX + 1);
        List<GeoLocationDto> geoLocationList = new ArrayList<>();
        try {
            geoLocationList = geoLocationRepository.findByReceiverIdAndDeviceTimeBetweenOrderByDeviceTimeDesc(
                    param.getGpsId(),
                    param.getStartTime(),
                    param.getEndTime());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("es查询位置信息失败, error:{}", e.getMessage());
        } finally {
            dynamicIndex.remove();
        }
        for (GeoLocationDto geoLocation : geoLocationList) {
            VehicleTrackVo vehicleTrackVo = new VehicleTrackVo();
            // todo 保存车辆电源信息
//            vehicleTrackVo.setPowerSupply();
//            vehicleTrackVo.setVoltage();
            String warnBinaryStr = this.toBinaryString(geoLocation.getWarnBit());
            List<LocalWarningTypeEnum> geoLocationWarningType = WarningHelper.getGeoLocationWarningType(
                    warnBinaryStr);
            Integer powerOffStatus = 0;
            for (LocalWarningTypeEnum localWarningTypeEnum : geoLocationWarningType) {
                if (WARNING_TYPE_MAIN_POWER_OFF == localWarningTypeEnum.getType()) {
                    powerOffStatus = 1;
                    break;
                }
            }
            vehicleTrackVo.setPowerOffStatus(powerOffStatus);
            vehicleTrackVo.setSpeed(geoLocation.getSpeed());
            vehicleTrackVo.setDeviceTime(geoLocation.getDeviceTime());
            vehicleTrackVo.setLngLat(geoLocation.getLng());
            vehicleTrackVo.setLatLat(geoLocation.getLat());
            vehicleTrackVoList.add(vehicleTrackVo);
        }
        return BaseResponse.ok(vehicleTrackVoList);
    }

    /**
     * 温压管理-实时温压-数据导出
     *
     * @param param
     * @param response
     */
    @Override
    public void export(WarmPressingExportParam param, HttpServletResponse response) {
        if (param.getLicensePlateList() == null || param.getLicensePlateList().isEmpty()
                || param.getLicensePlateList().size() > 10) {
            throw new BaseException("99999", "车牌号不能为空且最多只能导出10辆车");
        }
        UserVo user = UserContext.getUser();
        // 根据用户查询车辆
        List<VehicleEntity> vehicleListList = vehicleMapper.selectList(
                new LambdaQueryWrapper<VehicleEntity>()
                        .in(VehicleEntity::getCustomerId, user.getCustomerIds())
                        .in(VehicleEntity::getLicensePlate, param.getLicensePlateList())
                        .eq(VehicleEntity::getIsDeleted, false));
        List<VehicleSpecEntity> vehicleSpecList = vehicleSpecMapper.selectList(
                new LambdaQueryWrapper<VehicleSpecEntity>()
                        .in(VehicleSpecEntity::getId, vehicleListList.stream()
                                .map(VehicleEntity::getSpecId).collect(Collectors.toList()))
                        .eq(VehicleSpecEntity::getIsDeleted, false));
        List<String> gpsIdList = new ArrayList<>();
        List<VehicleEntity> mainVehicleList = new ArrayList<>();
        for (VehicleEntity vehicle : vehicleListList) {
            for (VehicleSpecEntity vehicleSpec : vehicleSpecList) {
                // 查询主车车辆型号
                if (vehicle.getSpecId().equals(vehicleSpec.getId())) {
                    if (vehicleSpec.getSpecType() == 1) {
                        gpsIdList.add(vehicle.getReceiverIdNumber());
                        mainVehicleList.add(vehicle);
                    }
                }
            }
        }
        // 查询轮胎资产数据
        List<AssetTire> assetTireList = assetTireMapper.selectList(
                new LambdaQueryWrapper<AssetTire>()
                        .in(AssetTire::getLicensePlate, param.getLicensePlateList())
                        .eq(AssetTire::getIsDelete, false));

        // 胎温胎压数据
        List<WarningPressingExportVo> warningPressingExportVoList = new ArrayList<>();
        this.getWarningPressingExportVoList(param, mainVehicleList, vehicleSpecList,
                assetTireList, warningPressingExportVoList);
        // 速度海拔数据
        List<SpeedAltitudeExportVo> speedAltitudeList = new ArrayList<>();
        this.getSpeedAltitudeExportVoList(param, mainVehicleList, speedAltitudeList);
        // 导出
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String fileName = URLEncoder.encode("胎温胎压数据导出", "UTF-8")
                    .replaceAll("\\+", "%20");
            response.setHeader("Content-disposition",
                    "attachment;filename*=utf-8''" + fileName + ".xlsx");
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream())
                    .autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(new ExcelCellWidthStyleStrategy())
                    .registerWriteHandler(EasyExcelUtils.getTitleStyle())
                    .build();
            WriteSheet warningPressingDataSheet =
                    EasyExcel.writerSheet(0, "胎温胎压")
                            .head(WarningPressingExportVo.class)
                            .build();
            WriteSheet speedAltitudeDataSheet =
                    EasyExcel.writerSheet(1, "速度海拔")
                            .head(SpeedAltitudeExportVo.class)
                            .build();
            if (!warningPressingExportVoList.isEmpty()) {
                excelWriter.write(warningPressingExportVoList, warningPressingDataSheet);
            }
            if (!speedAltitudeList.isEmpty()) {
                excelWriter.write(speedAltitudeList, speedAltitudeDataSheet);
            }
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>(2);
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            try {
                response.getWriter().println(JSON.toJSONString(map));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private void getSpeedAltitudeExportVoList(WarmPressingExportParam param,
            List<VehicleEntity> mainVehicleList, List<SpeedAltitudeExportVo> speedAltitudeList) {
        if (param.getIsExportSpeedAltitude() == 1) {
            for (VehicleEntity vehicle : mainVehicleList) {
                if (StringUtils.isNotBlank(vehicle.getReceiverIdNumber())) {
                    List<GeoLocationDto> geoLocationList = new ArrayList<>();
                    dynamicIndex.setSuffix(EsConstant.LOCATION_INDEX + vehicle.getFleetId());
                    try {
                        geoLocationList = geoLocationRepository.findByReceiverIdAndDeviceTimeBetweenOrderByDeviceTimeDesc(
                                vehicle.getReceiverIdNumber(),
                                param.getStartTime(),
                                param.getEndTime()
                        );
                    } catch (Exception e) {
                        log.error("es查询位置信息失败, error:{}", e.getMessage());
                    } finally {
                        dynamicIndex.remove();
                    }
                    for (GeoLocationDto geoLocation : geoLocationList) {
                        if (geoLocation.getReceiverId().equals(vehicle.getReceiverIdNumber())) {
                            SpeedAltitudeExportVo speedAltitudeExportVo = new SpeedAltitudeExportVo();
                            speedAltitudeExportVo.setLicensePlate(vehicle.getLicensePlate());
                            speedAltitudeExportVo.setDeviceTime(
                                    DateUtils.dateToStr(geoLocation.getDeviceTime()));
                            speedAltitudeExportVo.setSpeed(geoLocation.getSpeed());
                            speedAltitudeExportVo.setAltitude(geoLocation.getAltitude());
                            speedAltitudeExportVo.setLocation(geoLocation.getLng() + ","
                                    + geoLocation.getLat());
                            speedAltitudeList.add(speedAltitudeExportVo);
                        }
                    }
                }
            }
        }
    }

    private void getWarningPressingExportVoList(WarmPressingExportParam param,
            List<VehicleEntity> mainVehicleList,
            List<VehicleSpecEntity> vehicleSpecList, List<AssetTire> assetTireList,
            List<WarningPressingExportVo> warningPressingExportVoList) {
        for (VehicleEntity mainVehicle : mainVehicleList) {
            // 查询车辆温压数据
            List<TirePressureDataDto> tirePressureDataList = new ArrayList<>();
            if (StringUtils.isNotBlank(mainVehicle.getReceiverIdNumber())) {
                dynamicIndex.setSuffix(EsConstant.TIRE_DATA_FLEET_INDEX + mainVehicle.getFleetId());
                try {
                    tirePressureDataList = tirePressureDataRepository.findByClientIdAndDeviceTimeBetweenOrderByDeviceTime(
                            mainVehicle.getReceiverIdNumber(),
                            param.getStartTime(), param.getEndTime());
                } catch (Exception e) {
                    log.error("es查询轮胎数据失败, error:{}", e.getMessage());
                } finally {
                    dynamicIndex.remove();
                }
            }
            for (TirePressureDataDto tirePressureData : tirePressureDataList) {
                VehicleSpecEntity mainVehicleSpec = this.getVehicleSpecBySpecId(mainVehicle,
                        vehicleSpecList);
                AssetTire assetTire = this.getAssetTire(tirePressureData, assetTireList);
                if (StringUtils.isNotBlank(tirePressureData.getClientId())
                        && tirePressureData.getClientId()
                        .equals(mainVehicle.getReceiverIdNumber())) {
                    WarningPressingExportVo warningPressingExportVo = new WarningPressingExportVo();
                    warningPressingExportVo.setLicensePlate(mainVehicle.getLicensePlate());
                    if (assetTire != null) {
                        warningPressingExportVo.setTireCode(assetTire.getCode());
                    }
                    TireSiteResult tireSiteResult = null;
                    if (tirePressureData.getType() == 0) {
                        tireSiteResult = TireSiteUtil.getTireSiteResult(
                                Integer.valueOf(tirePressureData.getTireSiteId()),
                                mainVehicleSpec.getSpecType(), mainVehicleSpec.getWheelCount(),
                                mainVehicleSpec.getWheelbaseType());
                    } else if (tirePressureData.getType() == 1) {
                        tireSiteResult = TireSiteUtil.getMinorTireSiteResult(
                                Integer.valueOf(tirePressureData.getTireSiteId()));
                    }
                    if (tireSiteResult != null) {
                        warningPressingExportVo.setTireSiteName(tireSiteResult.getTireSiteName());
                    }
                    warningPressingExportVo.setTime(
                            DateUtils.dateToStr(tirePressureData.getCreateTime()));
                    warningPressingExportVo.setPressure(tirePressureData.getTirePressure());
                    warningPressingExportVo.setTemperature(tirePressureData.getTireTemperature());
                    warningPressingExportVo.setBatteryVoltage(tirePressureData.getVoltage());
                    warningPressingExportVoList.add(warningPressingExportVo);
                }
            }
        }
    }

    private AssetTire getAssetTire(TirePressureDataDto tirePressureData,
            List<AssetTire> assetTireList) {
        AssetTire assetTire = null;
        for (AssetTire tire : assetTireList) {
            if (tire.getSensorId().equals(tirePressureData.getTireSensorId())) {
                assetTire = tire;
                break;
            }
        }
        return assetTire;
    }

    private VehicleSpecEntity getVehicleSpecBySpecId(VehicleEntity vehicle,
            List<VehicleSpecEntity> vehicleSpecList) {
        VehicleSpecEntity vehicleSpec = null;
        for (VehicleSpecEntity spec : vehicleSpecList) {
            if (spec.getId().equals(vehicle.getSpecId())) {
                vehicleSpec = spec;
                break;
            }
        }
        return vehicleSpec;
    }

    private List<AltitudeTrendVo> getAltitudeTrend(List<GeoLocationDto> geoLocationList) {
        List<AltitudeTrendVo> altitudeTrendList = new ArrayList<>();
        if (!geoLocationList.isEmpty()) {
            for (GeoLocationDto geoLocation : geoLocationList) {
                AltitudeTrendVo altitudeTrendVo = new AltitudeTrendVo();
                altitudeTrendVo.setAltitude(geoLocation.getAltitude());
                altitudeTrendVo.setTime(geoLocation.getDeviceTime());
                altitudeTrendList.add(altitudeTrendVo);
            }
        }
        return altitudeTrendList;
    }

    private List<SpeedTrendVo> getSpeedTrend(List<GeoLocationDto> geoLocationList) {
        List<SpeedTrendVo> speedTrendList = new ArrayList<>();
        if (!geoLocationList.isEmpty()) {
            for (GeoLocationDto geoLocation : geoLocationList) {
                SpeedTrendVo speedTrendVo = new SpeedTrendVo();
                speedTrendVo.setSpeed(geoLocation.getSpeed());
                speedTrendVo.setTime(geoLocation.getDeviceTime());
                speedTrendList.add(speedTrendVo);
            }
        }
        return speedTrendList;
    }

    private List<TireTemperatureTrendVo> getTireTemperatureTrend(
            List<TirePressureDataDto> tirePressureData, VehicleSpecEntity mainVehicleSpec,
            VehicleSpecEntity minorVehicleSpec) {
        List<TireTemperatureTrendVo> tireTemperatureTrend = new ArrayList<>();
        if (!tirePressureData.isEmpty()) {
            for (TirePressureDataDto tirePressure : tirePressureData) {
                // 主车轮胎数据
                TireTemperatureTrendVo tireTemperatureTrendVo = new TireTemperatureTrendVo();
                tireTemperatureTrendVo.setTireSiteId(Integer.valueOf(tirePressure.getTireSiteId()));
                if (tirePressure.getType() == 0) {
                    TireSiteResult tireSiteResult = TireSiteUtil.getTireSiteResult(
                            tireTemperatureTrendVo.getTireSiteId(),
                            mainVehicleSpec.getSpecType(), mainVehicleSpec.getWheelCount(),
                            mainVehicleSpec.getWheelbaseType());
                    if (tireSiteResult != null) {
                        tireTemperatureTrendVo.setTireSiteName(tireSiteResult.getTireSiteName());
                    }
                } else if (tirePressure.getType() == 1) {
                    TireSiteResult tireSiteResult = TireSiteUtil.getTireSiteResult(
                            tireTemperatureTrendVo.getTireSiteId(),
                            minorVehicleSpec.getSpecType(), minorVehicleSpec.getWheelCount(),
                            minorVehicleSpec.getWheelbaseType());
                    if (tireSiteResult != null) {
                        tireTemperatureTrendVo.setTireSiteName(tireSiteResult.getTireSiteName());
                    }
                }
                tireTemperatureTrendVo.setTemperature(tirePressure.getTireTemperature());
                tireTemperatureTrendVo.setCreateTime(tirePressure.getCreateTime());
                tireTemperatureTrend.add(tireTemperatureTrendVo);
            }
        }
        return tireTemperatureTrend;
    }

    private List<TirePressureTrendVo> getTirePressureTrend(
            List<TirePressureDataDto> tirePressureData, VehicleSpecEntity mainVehicleSpec,
            VehicleSpecEntity minorVehicleSpec) {
        List<TirePressureTrendVo> tirePressureTrend = new ArrayList<>();
        if (!tirePressureData.isEmpty()) {
            for (TirePressureDataDto tirePressure : tirePressureData) {
                // 主车轮胎数据
                TirePressureTrendVo tirePressureTrendVo = new TirePressureTrendVo();
                tirePressureTrendVo.setTireSiteId(Integer.valueOf(tirePressure.getTireSiteId()));
                if (tirePressure.getType() == 0) {
                    TireSiteResult tireSiteResult = TireSiteUtil.getTireSiteResult(
                            tirePressureTrendVo.getTireSiteId(),
                            mainVehicleSpec.getSpecType(), mainVehicleSpec.getWheelCount(),
                            mainVehicleSpec.getWheelbaseType());
                    if (tireSiteResult != null) {
                        tirePressureTrendVo.setTireSiteName(tireSiteResult.getTireSiteName());
                    }
                } else if (tirePressure.getType() == 1) {
                    TireSiteResult tireSiteResult = TireSiteUtil.getTireSiteResult(
                            tirePressureTrendVo.getTireSiteId(),
                            minorVehicleSpec.getSpecType(), minorVehicleSpec.getWheelCount(),
                            minorVehicleSpec.getWheelbaseType());
                    if (tireSiteResult != null) {
                        tirePressureTrendVo.setTireSiteName(tireSiteResult.getTireSiteName());
                    }
                }
                tirePressureTrendVo.setPressure(tirePressure.getTirePressure());
                tirePressureTrendVo.setCreateTime(tirePressure.getCreateTime());
                tirePressureTrend.add(tirePressureTrendVo);
            }
        }
        return tirePressureTrend;
    }

    private TireStatusVo getTireStatusVo(AssetTire assetTire,
            List<TireNewestData> tirePressureDataList, VehicleSpecEntity vehicleSpec,
            List<AssetTireFitRecord> assetTireFitRecordList, TireBrandEntity tireBrand) {
        TireStatusVo tireStatusVo = new TireStatusVo();
        if (assetTire != null) {
            tireStatusVo.setTireSiteName(assetTire.getTireSiteName());
            tireStatusVo.setTireCode(assetTire.getCode());
            tireStatusVo.setSensorId(assetTire.getSensorId());
        }
        if (!tirePressureDataList.isEmpty()) {
            // 轮胎状态数据
            TireNewestData tirePressureData = tirePressureDataList.get(0);
            tireStatusVo.setCurrentTirePressure(tirePressureData.getTirePressure() + "bar");
            tireStatusVo.setCurrentTireTemperature(tirePressureData.getTireTemperature() + "℃");
            tireStatusVo.setBatteryVoltage(tirePressureData.getVoltage() + "V");
            tireStatusVo.setMonitorTime(tirePressureData.getDeviceTime());
            TireCheckDataDetailParam tireCheckDataDetailParam = this.getTireCheckDataDetailParam(
                    tirePressureData);
            List<WarningTypeEnum> warningTypeEnumList = WarningHelper.getTireWarningType(
                    tireCheckDataDetailParam, vehicleSpec);
            List<String> warningTypeList = new ArrayList<>();
            if (!warningTypeEnumList.isEmpty()) {
                for (WarningTypeEnum warningTypeEnum : warningTypeEnumList) {
                    warningTypeList.add(warningTypeEnum.getDescription());
                }
            }
            tireStatusVo.setCurrentStatus(
                    warningTypeList.isEmpty() ? "正常"
                            : StringUtils.join(warningTypeList, ","));
        }
        if (!assetTireFitRecordList.isEmpty()) {
            AssetTireFitRecord assetTireFitRecord = assetTireFitRecordList.get(0);
            tireStatusVo.setInstallTime(assetTireFitRecord.getCreateTime());
        }
        if (tireBrand != null) {
            tireStatusVo.setTireBrand(tireBrand.getBrandName());
        }
        tireStatusVo.setFirstHighPressure(String.valueOf(vehicleSpec.getHighPressureAlarmLevel1()));
        tireStatusVo.setSecondHighPressure(
                String.valueOf(vehicleSpec.getHighPressureAlarmLevel2()));
        tireStatusVo.setFirstLowPressure(String.valueOf(vehicleSpec.getLowPressureAlarmLevel1()));
        tireStatusVo.setSecondLowPressure(String.valueOf(vehicleSpec.getLowPressureAlarmLevel2()));
        tireStatusVo.setFirstHighTemperature(
                String.valueOf(vehicleSpec.getHighTemperatureAlarmLevel1()));
        tireStatusVo.setSecondHighTemperature(
                String.valueOf(vehicleSpec.getHighTemperatureAlarmLevel2()));
        return tireStatusVo;
    }

    private TireCheckDataDetailParam getTireCheckDataDetailParam(
            TireNewestData tirePressureData) {
        TireCheckDataDetailParam tireCheckDataDetailParam = new TireCheckDataDetailParam();
        tireCheckDataDetailParam.setType(tirePressureData.getTireType());
        tireCheckDataDetailParam.setTireSiteId(tirePressureData.getTireSiteId());
        tireCheckDataDetailParam.setTireSensorId(tirePressureData.getTireSensorId());
        tireCheckDataDetailParam.setVoltage(tirePressureData.getVoltage());
        tireCheckDataDetailParam.setTirePressure(tirePressureData.getTirePressure());
        tireCheckDataDetailParam.setTireTemperature(tirePressureData.getTireTemperature());
        // 查询轮胎数据告警信息
        TireWarningDataParam tireWarningDataParam = new TireWarningDataParam();
        tireWarningDataParam.setBatteryVoltageStatus(
                tirePressureData.getBatteryVoltageStatus());
        tireWarningDataParam.setIsTimeout(tirePressureData.getIsTimeout());
        tireWarningDataParam.setScheme(tirePressureData.getScheme());
        tireWarningDataParam.setTirePressureStatus(tirePressureData.getTirePressureStatus());
        tireWarningDataParam.setTireTemperatureStatus(
                tirePressureData.getTireTemperatureStatus());
        tireWarningDataParam.setTireStatus(tirePressureData.getTireStatus());
        tireCheckDataDetailParam.setTireWarningData(tireWarningDataParam);
        return tireCheckDataDetailParam;
    }

    /**
     * 绑定状态颜色设置:告警颜色:1-红:一级高温、一级高压、一级低压报警、急漏气, 1-黄:二级高温、二级高压、二级低压报警、低电压报警, 3-绿:无告警, 4-灰:无信号报警
     *
     * @param warningTypeList
     * @return
     */
    private Integer getVehicleWarningColor(List<Integer> warningTypeList) {
        if (warningTypeList.isEmpty()) {
            return WarningConstant.WARNING_COLOR_GREEN;
        }
        if (warningTypeList.contains(WarningTypeEnum.FIRST_HIGH_PRESSURE.getType())
                || warningTypeList.contains(WarningTypeEnum.FIRST_HIGH_TEMPERATURE.getType())
                || warningTypeList.contains(WarningTypeEnum.FIRST_LOW_PRESSURE.getType())
                || warningTypeList.contains(WarningTypeEnum.FAST_LEAK.getType())) {
            return WarningConstant.WARNING_COLOR_RED;
        } else if (warningTypeList.contains(WarningTypeEnum.SECOND_HIGH_PRESSURE.getType())
                || warningTypeList.contains(WarningTypeEnum.SECOND_HIGH_TEMPERATURE.getType())
                || warningTypeList.contains(WarningTypeEnum.SECOND_LOW_PRESSURE.getType())
                || warningTypeList.contains(WarningTypeEnum.LOW_VOLTAGE.getType())) {
            return WarningConstant.WARNING_COLOR_YELLOW;
        } else if (warningTypeList.contains(WarningTypeEnum.NO_SIGNAL.getType())) {
            return WarningConstant.WARNING_COLOR_GRAY;
        } else {
            return WarningConstant.WARNING_COLOR_GREEN;
        }
    }

    private Boolean tryLock(String key) {
        // redis锁
        Boolean absent = redisTemplate.opsForValue().setIfAbsent(key, "", 1, TimeUnit.HOURS);
        return absent;
    }

    private void unLock(String key) {
        redisTemplate.delete(key);
    }
}
