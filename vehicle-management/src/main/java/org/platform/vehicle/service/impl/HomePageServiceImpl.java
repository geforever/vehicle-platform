package org.platform.vehicle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.platform.vehicle.conf.DataInitializer;
import org.platform.vehicle.constant.WarningConstant;
import org.platform.vehicle.entity.JT808.TireNewestData;
import org.platform.vehicle.entity.VehicleEntity;
import org.platform.vehicle.entity.WarningDetail;
import org.platform.vehicle.mapper.VehicleMapper;
import org.platform.vehicle.mapper.WarningDetailMapper;
import org.platform.vehicle.mapper.jt808.TireNewestDataMapper;
import org.platform.vehicle.service.HomePageService;
import org.platform.vehicle.vo.MaintenanceOverviewVo;
import org.platform.vehicle.vo.TirePressureAndTemperatureStatisticVo;
import org.platform.vehicle.vo.VehicleOverviewVo;
import org.platform.vehicle.vo.WarmPressingOverviewVo;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author gejiawei
 * @Date 2023/10/13 09:38
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class HomePageServiceImpl implements HomePageService {


    private final VehicleMapper vehicleMapper;
    private final WarningDetailMapper warningDetailMapper;
    private final DataInitializer dataInitializer;
    private final TireNewestDataMapper tireNewestDataMapper;

    /**
     * 首页-车辆概况
     */
    @Override
    public BaseResponse<VehicleOverviewVo> getVehicleOverview() {
        UserVo user = UserContext.getUser();
        Date now = new Date();
        // 查询车辆总数
        Long totalVehicleCount = vehicleMapper.selectCount(new LambdaQueryWrapper<VehicleEntity>()
                .in(VehicleEntity::getFleetId, user.getCustomerIds())
                .eq(VehicleEntity::getIsDeleted, false));
        // todayIncreaseCount
        Date todayStartTime = this.getTodayStartTime(now);
        Date todayEndTime = getTodayEndTime(now);
        Long todayVehicleCount = vehicleMapper.selectCount(new LambdaQueryWrapper<VehicleEntity>()
                .in(VehicleEntity::getFleetId, user.getCustomerIds())
                .between(VehicleEntity::getCreateTime, todayStartTime, todayEndTime)
                .eq(VehicleEntity::getIsDeleted, false));
        // 本周新增
        Date weekStartTime = this.getWeekStartTime(now);
        Date weekEndTime = this.getWeekEndTime(now);
        Long weekVehicleCount = vehicleMapper.selectCount(new LambdaQueryWrapper<VehicleEntity>()
                .in(VehicleEntity::getFleetId, user.getCustomerIds())
                .between(VehicleEntity::getCreateTime, weekStartTime, weekEndTime)
                .eq(VehicleEntity::getIsDeleted, false));
        // 本月新增
        Date monthStartTime = this.getMonthStartTime(now);
        Date monthEndTime = this.getMonthEndTime(now);
        Long monthVehicleCount = vehicleMapper.selectCount(new LambdaQueryWrapper<VehicleEntity>()
                .in(VehicleEntity::getFleetId, user.getCustomerIds())
                .between(VehicleEntity::getCreateTime, monthStartTime, monthEndTime)
                .eq(VehicleEntity::getIsDeleted, false));
        VehicleOverviewVo vehicleOverviewVo = new VehicleOverviewVo();
        vehicleOverviewVo.setTotalVehicleCount(totalVehicleCount);
        vehicleOverviewVo.setTodayReceiveCount(0L);
        vehicleOverviewVo.setTodayDeliverCount(0L);
        vehicleOverviewVo.setTodayIncreaseCount(todayVehicleCount);
        vehicleOverviewVo.setWeekIncreaseCount(weekVehicleCount);
        vehicleOverviewVo.setMonthIncreaseCount(monthVehicleCount);
        return BaseResponse.ok(vehicleOverviewVo);
    }

    /**
     * 温压概况
     */
    @Override
    public BaseResponse<WarmPressingOverviewVo> getWarmPressingOverview() {
        UserVo user = UserContext.getUser();
        List<VehicleEntity> vehicleList = vehicleMapper.selectList(
                new LambdaQueryWrapper<VehicleEntity>()
                        .in(VehicleEntity::getFleetId, user.getCustomerIds()));
        if (vehicleList.isEmpty()) {
            TirePressureAndTemperatureStatisticVo tirePressureAndTemperatureStatisticVo = new TirePressureAndTemperatureStatisticVo();
            tirePressureAndTemperatureStatisticVo.setHighTemperatureWarning(0L);
            tirePressureAndTemperatureStatisticVo.setHighPressureWarning(0L);
            tirePressureAndTemperatureStatisticVo.setLowPressureWarning(0L);
            tirePressureAndTemperatureStatisticVo.setOfflineWarning(0L);
            return BaseResponse.ok(tirePressureAndTemperatureStatisticVo);
        }
        // 查询车辆最新轮胎数据
        List<TireNewestData> tireNewestDataList = tireNewestDataMapper.selectList(
                new LambdaQueryWrapper<TireNewestData>()
                        .in(TireNewestData::getClientId, vehicleList.stream()
                                .map(VehicleEntity::getReceiverIdNumber)
                                .filter(StringUtils::isNotBlank)
                                .collect(Collectors.toList()))
                        .groupBy(TireNewestData::getClientId, TireNewestData::getSerialNo)
                        .select(TireNewestData::getClientId,
                                TireNewestData::getSerialNo));
        // 查询温压总数
        Long totalCount = warningDetailMapper.selectCount(new LambdaQueryWrapper<WarningDetail>()
                .in(WarningDetail::getFleetId, user.getCustomerIds())
                .ne(WarningDetail::getWarningType, WarningConstant.WARNING_TYPE_NO_SIGNAL));
        // 高温报警数
        List<Integer> warningTypeList = new ArrayList<>();
        Long highTemperatureCount = this.getHighTemperatureCount(tireNewestDataList,
                warningTypeList);
        // 高压报警数
        Long highPressureCount = this.getHighPressureCount(warningTypeList, tireNewestDataList);
        // 低压报警数
        Long lowPressureCount = this.getLowPressureCount(warningTypeList, tireNewestDataList);
        // 低电压报警数
        Long lowElectricityCount = this.getLowElectricityCount(tireNewestDataList);
        // 离线车辆数
        Long vehicleNoSignalCount = this.getVehicleNoSignalCount(user);
        WarmPressingOverviewVo warmPressingOverviewVo = new WarmPressingOverviewVo();
        warmPressingOverviewVo.setTotalCount(totalCount);
        warmPressingOverviewVo.setHighTemperatureCount(highTemperatureCount);
        warmPressingOverviewVo.setHighPressureCount(highPressureCount);
        warmPressingOverviewVo.setLowPressureCount(lowPressureCount);
        warmPressingOverviewVo.setLowElectricityCount(lowElectricityCount);
        warmPressingOverviewVo.setVehicleOfflineCount(vehicleNoSignalCount);
        return BaseResponse.ok(warmPressingOverviewVo);
    }

    private Long getVehicleNoSignalCount(UserVo user) {
        Long vehicleNoSignalCount = warningDetailMapper.selectCount(
                new QueryWrapper<WarningDetail>()
                        .select("distinct license_plate")
                        .lambda()
                        .in(WarningDetail::getFleetId, user.getCustomerIds())
                        .eq(WarningDetail::getWarningType, WarningConstant.WARNING_TYPE_NO_SIGNAL)
                        .eq(WarningDetail::getIsRecovery, WarningConstant.IS_RECOVERY_NO));
        return vehicleNoSignalCount;
    }

    private Long getLowElectricityCount(List<TireNewestData> tireNewestDataList) {
        Long lowElectricityCount = 0L;
        List<WarningDetail> warningDetailList = warningDetailMapper.selectList(
                new LambdaQueryWrapper<WarningDetail>()
                        .in(WarningDetail::getReceiverId, tireNewestDataList.stream()
                                .map(TireNewestData::getClientId).collect(Collectors.toSet()))
                        .eq(WarningDetail::getWarningType,
                                WarningConstant.WARNING_TYPE_LOW_VOLTAGE)
                        .select(WarningDetail::getReceiverId, WarningDetail::getSerialNo));
        for (WarningDetail warningDetail : warningDetailList) {
            for (TireNewestData tireNewestData : tireNewestDataList) {
                if (warningDetail.getReceiverId().equals(tireNewestData.getClientId())
                        && warningDetail.getSerialNo().equals(tireNewestData.getSerialNo())) {
                    lowElectricityCount++;
                    break;
                }
            }
        }
        return lowElectricityCount;
    }

    private Long getLowPressureCount(List<Integer> warningTypeList,
            List<TireNewestData> tireNewestDataList) {
        Long lowPressureCount = 0L;
        warningTypeList.add(WarningConstant.WARNING_TYPE_FIRST_LOW_PRESSURE);
        warningTypeList.add(WarningConstant.WARNING_TYPE_SECOND_LOW_PRESSURE);
        List<WarningDetail> warningDetailList = warningDetailMapper.selectList(
                new LambdaQueryWrapper<WarningDetail>()
                        .in(WarningDetail::getReceiverId, tireNewestDataList.stream()
                                .map(TireNewestData::getClientId).collect(Collectors.toSet()))
                        .in(WarningDetail::getWarningType, warningTypeList)
                        .select(WarningDetail::getReceiverId, WarningDetail::getSerialNo));
        for (WarningDetail warningDetail : warningDetailList) {
            for (TireNewestData tireNewestData : tireNewestDataList) {
                if (warningDetail.getReceiverId().equals(tireNewestData.getClientId())
                        && warningDetail.getSerialNo().equals(tireNewestData.getSerialNo())) {
                    lowPressureCount++;
                    break;
                }
            }
        }
        return lowPressureCount;
    }

    private Long getHighPressureCount(List<Integer> warningTypeList,
            List<TireNewestData> tireNewestDataList) {
        Long highPressureCount = 0L;
        warningTypeList.add(WarningConstant.WARNING_TYPE_FIRST_HIGH_PRESSURE);
        warningTypeList.add(WarningConstant.WARNING_TYPE_SECOND_HIGH_PRESSURE);
        List<WarningDetail> warningDetailList = warningDetailMapper.selectList(
                new LambdaQueryWrapper<WarningDetail>()
                        .in(WarningDetail::getReceiverId, tireNewestDataList.stream()
                                .map(TireNewestData::getClientId).collect(Collectors.toSet()))
                        .in(WarningDetail::getWarningType, warningTypeList)
                        .select(WarningDetail::getReceiverId, WarningDetail::getSerialNo));
        for (WarningDetail warningDetail : warningDetailList) {
            for (TireNewestData tireNewestData : tireNewestDataList) {
                if (warningDetail.getReceiverId().equals(tireNewestData.getClientId())
                        && warningDetail.getSerialNo().equals(tireNewestData.getSerialNo())) {
                    highPressureCount++;
                    break;
                }
            }
        }
        warningTypeList.clear();
        return highPressureCount;
    }

    private Long getHighTemperatureCount(List<TireNewestData> tireNewestDataList,
            List<Integer> warningTypeList) {
        warningTypeList.add(WarningConstant.WARNING_TYPE_FIRST_HIGH_TEMPERATURE);
        warningTypeList.add(WarningConstant.WARNING_TYPE_SECOND_HIGH_TEMPERATURE);
        Long highTemperatureCount = 0L;
        List<WarningDetail> warningDetailList = warningDetailMapper.selectList(
                new LambdaQueryWrapper<WarningDetail>()
                        .in(WarningDetail::getReceiverId, tireNewestDataList.stream()
                                .map(TireNewestData::getClientId).collect(Collectors.toSet()))
                        .in(WarningDetail::getWarningType, warningTypeList)
                        .select(WarningDetail::getReceiverId, WarningDetail::getSerialNo));
        for (WarningDetail warningDetail : warningDetailList) {
            for (TireNewestData tireNewestData : tireNewestDataList) {
                if (warningDetail.getReceiverId().equals(tireNewestData.getClientId())
                        && warningDetail.getSerialNo().equals(tireNewestData.getSerialNo())) {
                    highTemperatureCount++;
                    break;
                }
            }
        }
        warningTypeList.clear();
        return highTemperatureCount;
    }

    /**
     * 维保概况
     */
    @Override
    public BaseResponse<MaintenanceOverviewVo> getMaintenanceOverview() {
        MaintenanceOverviewVo maintenanceOverviewVo = new MaintenanceOverviewVo();
        maintenanceOverviewVo.setTotalMaintenanceOrderCount(0L);
        maintenanceOverviewVo.setTotalMaintenanceOrderAmount(new BigDecimal(0));
        maintenanceOverviewVo.setTodayMaintenanceOrderCount(0L);
        maintenanceOverviewVo.setWeekMaintenanceOrderCount(0L);
        maintenanceOverviewVo.setMonthMaintenanceOrderCount(0L);
        return BaseResponse.ok(maintenanceOverviewVo);
    }

    /**
     * 初始化数据接口
     *
     * @return
     */
    @Override
    public BaseResponse<String> initData() {
        log.info("开始手动初始化数据");
        // 开始时间
        long start = System.currentTimeMillis();
        dataInitializer.initVehicleData();
        return BaseResponse.ok(
                "手动初始化数据成功,耗时:" + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * 获取今天开始时间00:00:00
     *
     * @param currentDate
     * @return
     */
    private Date getTodayStartTime(Date currentDate) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(currentDate);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        return calendarStart.getTime();
    }

    /**
     * 获取今天结束时间23:59:59
     *
     * @param currentDate
     * @return
     */
    private Date getTodayEndTime(Date currentDate) {
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(currentDate);
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        return calendarEnd.getTime();
    }

    /**
     * 获取本周开始时间00:00:00
     *
     * @param currentDate
     * @return
     */
    private Date getWeekStartTime(Date currentDate) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(currentDate);
        calendarStart.set(Calendar.DAY_OF_WEEK, 1);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        return calendarStart.getTime();
    }

    /**
     * 获取本周结束时间23:59:59
     *
     * @param currentDate
     * @return
     */
    private Date getWeekEndTime(Date currentDate) {
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(currentDate);
        calendarEnd.set(Calendar.DAY_OF_WEEK, 7);
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        return calendarEnd.getTime();
    }

    /**
     * 获取本月开始时间
     *
     * @param currentDate
     * @return
     */
    private Date getMonthStartTime(Date currentDate) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(currentDate);
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        return calendarStart.getTime();
    }

    /**
     * 获取本月结束时间
     *
     * @param currentDate
     * @return
     */
    private Date getMonthEndTime(Date currentDate) {
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(currentDate);
        calendarEnd.set(Calendar.DAY_OF_MONTH, calendarEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23);
        calendarEnd.set(Calendar.MINUTE, 59);
        calendarEnd.set(Calendar.SECOND, 59);
        return calendarEnd.getTime();
    }
}
