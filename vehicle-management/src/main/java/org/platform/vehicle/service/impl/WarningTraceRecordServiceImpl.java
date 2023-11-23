package org.platform.vehicle.service.impl;

import static org.platform.vehicle.constant.AssetTireConstant.DEVICE_TYPE_RELAY;
import static org.platform.vehicle.constant.AssetTireConstant.DEVICE_TYPE_SENSOR;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.platform.vehicle.conf.CustomerContext;
import org.platform.vehicle.conf.ExcelCellWidthStyleStrategy;
import org.platform.vehicle.conf.VehicleSpecContext;
import org.platform.vehicle.conf.WarningSimpleConfig;
import org.platform.vehicle.constant.AssetTireConstant;
import org.platform.vehicle.constant.NoticeConfigConstant;
import org.platform.vehicle.constant.SysCustomerConstant;
import org.platform.vehicle.constant.WarningConstant;
import org.platform.vehicle.constant.WarningTypeEnum;
import org.platform.vehicle.entity.AssetTire;
import org.platform.vehicle.entity.AssetTireDeviceBindRecord;
import org.platform.vehicle.entity.JT808.TireNewestData;
import org.platform.vehicle.entity.VehicleEntity;
import org.platform.vehicle.entity.WarningDetail;
import org.platform.vehicle.entity.WarningRemarkTemplate;
import org.platform.vehicle.entity.WarningTraceRecord;
import org.platform.vehicle.entity.WarningTraceRecordDetail;
import org.platform.vehicle.helper.jdbc.WarningDetailJdbc;
import org.platform.vehicle.helper.jdbc.WarningTraceRecordJdbc;
import org.platform.vehicle.mapper.AssetTireDeviceBindRecordMapper;
import org.platform.vehicle.mapper.AssetTireMapper;
import org.platform.vehicle.mapper.SysCustomerMapper;
import org.platform.vehicle.mapper.VehicleMapper;
import org.platform.vehicle.mapper.VehicleSpecMapper;
import org.platform.vehicle.mapper.WarningDetailMapper;
import org.platform.vehicle.mapper.WarningRemarkTemplateMapper;
import org.platform.vehicle.mapper.WarningTraceRecordDetailMapper;
import org.platform.vehicle.mapper.WarningTraceRecordMapper;
import org.platform.vehicle.mapper.jt808.TireNewestDataMapper;
import org.platform.vehicle.param.RunningStatusChangeParam;
import org.platform.vehicle.param.SysNoticeWarningParam;
import org.platform.vehicle.param.TireCheckDataDetailParam;
import org.platform.vehicle.param.TireCheckDataParam;
import org.platform.vehicle.param.WarningDetailRecordConditionQueryParam;
import org.platform.vehicle.param.WarningTraceRecordConditionQueryParam;
import org.platform.vehicle.param.WarningTraceRecordFollowUpParam;
import org.platform.vehicle.service.SysNoticeService;
import org.platform.vehicle.service.WarningTraceRecordService;
import org.platform.vehicle.util.AmapUtil;
import org.platform.vehicle.util.EasyExcelUtils;
import org.platform.vehicle.util.TireSiteUtil;
import org.platform.vehicle.util.WarningHelper;
import org.platform.vehicle.vo.CheckWarningDetailRepeat;
import org.platform.vehicle.vo.FollowUpHistoryRecordDetailVo;
import org.platform.vehicle.vo.FollowUpHistoryRecordVo;
import org.platform.vehicle.vo.TirePressureAndTemperatureStatisticVo;
import org.platform.vehicle.vo.TireSiteResult;
import org.platform.vehicle.vo.TrendStatisticVo;
import org.platform.vehicle.vo.UnhandLedWarningCountVo;
import org.platform.vehicle.vo.VehicleAndTireStatisticVo;
import org.platform.vehicle.vo.WarningRemarkTemplateVo;
import org.platform.vehicle.vo.WarningTraceDetailRecordVo;
import org.platform.vehicle.vo.WarningTraceRecordExportVo;
import org.platform.vehicle.vo.WarningTraceRecordVo;
import org.platform.vehicle.vo.amap.AddressComponentVo;
import org.platform.vehicle.vo.amap.ConvertCoordVo;
import org.platform.vehicle.vo.amap.RegeocodeResultVo;
import org.platform.vehicle.vo.amap.RegeocodeVo;
import org.platform.vehicle.vo.context.CustomerContextVo;
import org.platform.vehicle.vo.context.VehicleSpecContextVo;
import org.platform.vehicle.exception.BaseException;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.DateUtil;
import org.platform.vehicle.utils.DateUtils;
import org.platform.vehicle.utils.IdWorker;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (WarningTraceRecord)表服务实现类
 *
 * @author geforever
 * @since 2023-09-27 16:05:55
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WarningTraceRecordServiceImpl implements WarningTraceRecordService {

    private final WarningTraceRecordMapper warningTraceRecordMapper;
    private final WarningDetailMapper warningDetailMapper;
    private final WarningTraceRecordDetailMapper warningTraceDetailRecordMapper;
    private final VehicleMapper vehicleMapper;
    private final AssetTireMapper assetTireMapper;
    private final AssetTireDeviceBindRecordMapper assetTireDeviceBindRecordMapper;
    private final VehicleSpecMapper vehicleSpecMapper;
    private final WarningRemarkTemplateMapper warningRemarkTemplateMapper;
    private final WarningDetailJdbc warningDetailJdbc;
    private final SysCustomerMapper sysCustomerMapper;
    private final AmapUtil amapUtil;
    private final SysNoticeService sysNoticeService;
    private final WarningSimpleConfig warningSimpleConfig;
    private final VehicleSpecContext vehicleSpecDataContext;
    private final CustomerContext customerContext;
    private final IdWorker ID_WORKER = new IdWorker();
    private final WarningTraceRecordJdbc warningTraceRecordJdbc;
    private final TireNewestDataMapper tireNewestDataMapper;

    /**
     * 温压管理-报警记录与跟进-条件查询
     *
     * @param param
     * @return
     */
    @Override
    public BasePageResponse<List<WarningTraceRecordVo>> conditionQuery(
            WarningTraceRecordConditionQueryParam param) {
        UserVo user = UserContext.getUser();
        Page<WarningTraceRecord> page = new Page<>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<WarningTraceRecord> queryWrapper = this.getQueryWrapper(param, user);
        Page<WarningTraceRecord> warningTraceRecordPage = warningTraceRecordMapper
                .selectPage(page, queryWrapper);
        List<WarningTraceRecord> warningTraceRecordList = warningTraceRecordPage.getRecords();
        // 查询更新记录明细
        List<Integer> warningTraceRecordIdList = new ArrayList<>();
        for (WarningTraceRecord warningTraceRecord : warningTraceRecordList) {
            warningTraceRecordIdList.add(warningTraceRecord.getId());
        }
        if (warningTraceRecordIdList.isEmpty()) {
            return BasePageResponse.ok(new ArrayList<>(), page);
        }
        List<WarningTraceRecordDetail> warningTraceRecordDetailList = warningTraceDetailRecordMapper
                .selectList(new LambdaQueryWrapper<WarningTraceRecordDetail>()
                        .in(WarningTraceRecordDetail::getTraceRecordId, warningTraceRecordIdList)
                        .orderByDesc(WarningTraceRecordDetail::getCreateTime));
        List<WarningTraceRecordVo> warningTraceRecordVoList = new ArrayList<>();
        for (WarningTraceRecord record : warningTraceRecordList) {
            WarningTraceRecordVo warningTraceRecordVo = this.getWarningTraceRecordVo(record,
                    warningTraceRecordDetailList);
            warningTraceRecordVoList.add(warningTraceRecordVo);
        }
        return BasePageResponse.ok(warningTraceRecordVoList, page);
    }

    /**
     * 温压管理-报警记录与跟进-查看明细
     *
     * @param param
     * @return
     */
    @Override
    public BasePageResponse<List<WarningTraceDetailRecordVo>> getDetailRecord(
            WarningDetailRecordConditionQueryParam param) {
        Page<WarningDetail> page = new Page<>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<WarningDetail> warningDetailQueryWrapper = this.getWarningDetailQueryWrapper(
                param);
        Page<WarningDetail> warningDetailPage = warningDetailMapper.selectPage(page,
                warningDetailQueryWrapper);
        List<WarningTraceDetailRecordVo> warningTraceDetailRecordVoList = new ArrayList<>();
        for (WarningDetail detail : warningDetailPage.getRecords()) {
            WarningTraceDetailRecordVo warningTraceDetailRecordVo = this
                    .getWarningTraceDetailRecordVo(detail);
            warningTraceDetailRecordVoList.add(warningTraceDetailRecordVo);
        }
        return BasePageResponse.ok(warningTraceDetailRecordVoList, page);
    }

    /**
     * 温压管理-报警记录与跟进-跟进
     *
     * @param param
     * @return
     */
    @Override
    public BaseResponse followUp(WarningTraceRecordFollowUpParam param) {
        UserVo user = UserContext.getUser();
        WarningTraceRecord warningTraceRecord = warningTraceRecordMapper.selectById(param.getId());
        if (warningTraceRecord == null) {
            return BaseResponse.failure("报警记录不存在");
        }
        WarningTraceRecordDetail insertParam = new WarningTraceRecordDetail();
        insertParam.setTraceRecordId(param.getId());
        insertParam.setDriverName(param.getDriverName());
        insertParam.setPhone(param.getPhone());
        insertParam.setReason(param.getReason());
        insertParam.setRemark(param.getRemark());
        insertParam.setFollowName(user.getName());
        insertParam.setFollowNameId(user.getUserId());
        warningTraceDetailRecordMapper.insert(insertParam);
        // 跟进完成,修改改告警通知为已读
        sysNoticeService.updateReadByTargetId(insertParam.getId(), NoticeConfigConstant.WARNING);
        return BaseResponse.ok();
    }

    /**
     * 温压管理-报警记录与跟进-报警跟踪处理（去处理）
     *
     * @param warningTraceRecordId
     * @return
     */
    @Override
    public BaseResponse<FollowUpHistoryRecordVo> followUpRecord(Integer warningTraceRecordId) {
        // 获取跟进记录
        WarningTraceRecord warningTraceRecord = warningTraceRecordMapper
                .selectById(warningTraceRecordId);
        if (warningTraceRecord == null) {
            return BaseResponse.failure("跟进记录不存在");
        }
        // 查询车辆信息
        VehicleEntity vehicle = vehicleMapper.selectOne(new LambdaQueryWrapper<VehicleEntity>()
                .eq(VehicleEntity::getLicensePlate, warningTraceRecord.getLicensePlate()));
        if (vehicle == null) {
            return BaseResponse.failure("车辆不存在");
        }
        // 获取跟进历史记录
        List<WarningTraceRecordDetail> warningTraceRecordDetailList = warningTraceDetailRecordMapper
                .selectList(new LambdaQueryWrapper<WarningTraceRecordDetail>()
                        .eq(WarningTraceRecordDetail::getTraceRecordId, warningTraceRecordId)
                        .orderByDesc(WarningTraceRecordDetail::getCreateTime));
        // 查询司机档案
        FollowUpHistoryRecordVo followUpHistoryRecordVo = new FollowUpHistoryRecordVo();
        followUpHistoryRecordVo.setLicensePlate(warningTraceRecord.getLicensePlate());
        followUpHistoryRecordVo.setTireSiteName(warningTraceRecord.getTireSiteName());
        followUpHistoryRecordVo.setWarningType(warningTraceRecord.getWarningType());
        followUpHistoryRecordVo.setIsRunning(vehicle.getIsRunning());
        followUpHistoryRecordVo.setRunningStartTime(vehicle.getRunningStartTime());
        followUpHistoryRecordVo.setRunningEndTime(vehicle.getRunningEndTime());
//        followUpHistoryRecordVo.setDriverName();
//        followUpHistoryRecordVo.setPhone();
        // 跟进历史记录
        List<FollowUpHistoryRecordDetailVo> followUpHistoryRecordDetailVoList = new ArrayList<>();
        for (WarningTraceRecordDetail warningTraceRecordDetail : warningTraceRecordDetailList) {
            FollowUpHistoryRecordDetailVo followUpHistoryRecordDetailVo = new FollowUpHistoryRecordDetailVo();
            followUpHistoryRecordDetailVo.setReason(warningTraceRecordDetail.getReason());
            followUpHistoryRecordDetailVo.setRemark(warningTraceRecordDetail.getRemark());
            followUpHistoryRecordDetailVo.setFollowName(warningTraceRecordDetail.getFollowName());
            followUpHistoryRecordDetailVo.setCreateTime(warningTraceRecordDetail.getCreateTime());
            followUpHistoryRecordDetailVoList.add(followUpHistoryRecordDetailVo);
        }
        followUpHistoryRecordVo.setFollowUpHistoryRecordDetailVoList(
                followUpHistoryRecordDetailVoList);
        return BaseResponse.ok(followUpHistoryRecordVo);
    }

    /**
     * 温压管理-报警记录与跟进-运营状态变更
     *
     * @param param
     * @return
     */
    @Override
    public BaseResponse changeRunningStatus(RunningStatusChangeParam param) {
        VehicleEntity vehicle = vehicleMapper.selectOne(new LambdaQueryWrapper<VehicleEntity>()
                .eq(VehicleEntity::getLicensePlate, param.getLicensePlate()));
        if (vehicle == null) {
            return BaseResponse.failure("车辆不存在");
        }
        VehicleEntity updateParam = new VehicleEntity();
        updateParam.setId(vehicle.getId());
        updateParam.setIsRunning(param.getIsRunning());
        if (WarningConstant.IS_RUNNING_NO.equals(param.getIsRunning())) {
            // 设置为系统初始时间1970-01-01 00:00:00
            Date date = new Date(0);
            updateParam.setRunningStartTime(date);
            updateParam.setRunningEndTime(date);
        } else {
            updateParam.setRunningStartTime(param.getStartTime());
            updateParam.setRunningEndTime(param.getEndTime());
        }
        // 必填字段更新
        updateParam.setRunRoute(vehicle.getRunRoute());
        updateParam.setRepeaterIdNumber(vehicle.getRepeaterIdNumber());
        updateParam.setTrailerRepeaterIdNumber(vehicle.getTrailerRepeaterIdNumber());
        updateParam.setReceiverIdNumber(vehicle.getReceiverIdNumber());
        vehicleMapper.updateById(updateParam);
        // 设置停运期内有无信号告警则恢复
        this.updateNoSignalWarning(param);
        return BaseResponse.ok();
    }

    private void updateNoSignalWarning(RunningStatusChangeParam param) {
        if (param.getStartTime() == null || param.getEndTime() == null) {
            return;
        }
        List<WarningDetail> warningDetailList = warningDetailMapper.selectList(
                new LambdaQueryWrapper<WarningDetail>()
                        .eq(WarningDetail::getLicensePlate, param.getLicensePlate())
                        .eq(WarningDetail::getWarningType, WarningConstant.WARNING_TYPE_NO_SIGNAL)
                        .eq(WarningDetail::getIsRecovery, WarningConstant.IS_RECOVERY_NO)
                        .ge(WarningDetail::getCreateTime, param.getStartTime())
                        .le(WarningDetail::getCreateTime, param.getEndTime()));
        if (!warningDetailList.isEmpty()) {
            for (WarningDetail warningDetail : warningDetailList) {
                WarningDetail updateParam = new WarningDetail();
                updateParam.setId(warningDetail.getId());
                updateParam.setIsRecovery(WarningConstant.IS_RECOVERY_YES);
                warningDetailMapper.updateById(updateParam);
            }
        }
    }

    /**
     * 温压管理-报警记录与跟进-导出
     *
     * @param param
     * @param response
     * @return
     */
    @Override
    public void exportWarningTraceRecord(WarningTraceRecordConditionQueryParam param,
            HttpServletResponse response) {
        UserVo user = UserContext.getUser();
        LambdaQueryWrapper<WarningTraceRecord> queryWrapper = this.getQueryWrapper(param, user);
        List<WarningTraceRecord> warningTraceRecordList = warningTraceRecordMapper.selectList(
                queryWrapper);
        List<Integer> warningTraceRecordIdList = new ArrayList<>();
        for (WarningTraceRecord warningTraceRecord : warningTraceRecordList) {
            warningTraceRecordIdList.add(warningTraceRecord.getId());
        }
        if (warningTraceRecordIdList.isEmpty()) {
            return;
        }
        List<WarningTraceRecordDetail> warningTraceRecordDetailList = warningTraceDetailRecordMapper
                .selectList(new LambdaQueryWrapper<WarningTraceRecordDetail>()
                        .in(WarningTraceRecordDetail::getTraceRecordId, warningTraceRecordIdList)
                        .orderByDesc(WarningTraceRecordDetail::getCreateTime));
        List<WarningTraceRecordExportVo> exportVoList = new ArrayList<>();
        for (WarningTraceRecord warningTraceRecord : warningTraceRecordList) {
            WarningTraceRecordExportVo warningTraceRecordExportVo = new WarningTraceRecordExportVo();
            warningTraceRecordExportVo.setTireCode(warningTraceRecord.getTireCode());
            warningTraceRecordExportVo.setFleetName(warningTraceRecord.getFleetName());
            warningTraceRecordExportVo.setClientName(warningTraceRecord.getClientName());
            warningTraceRecordExportVo.setLicensePlate(warningTraceRecord.getLicensePlate());
            warningTraceRecordExportVo.setTireSiteName(warningTraceRecord.getTireSiteName());
            warningTraceRecordExportVo.setWarningType(warningTraceRecord.getWarningType());
            warningTraceRecordExportVo.setPressure(warningTraceRecord.getPressure());
            warningTraceRecordExportVo.setTemperature(warningTraceRecord.getTemperature());
            warningTraceRecordExportVo.setVoltage(warningTraceRecord.getVoltage());
            warningTraceRecordExportVo.setLocation(warningTraceRecord.getLocation());
            warningTraceRecordExportVo.setCreateTime(
                    DateUtils.dateToStr(warningTraceRecord.getCreateTime()));
            warningTraceRecordExportVo.setFollowTime(
                    DateUtils.dateToStr(warningTraceRecord.getFollowTime()));
            for (WarningTraceRecordDetail warningTraceRecordDetail : warningTraceRecordDetailList) {
                if (warningTraceRecord.getId()
                        .equals(warningTraceRecordDetail.getTraceRecordId())) {
                    warningTraceRecordExportVo.setReason(warningTraceRecordDetail.getReason());
                    break;
                }
            }
            exportVoList.add(warningTraceRecordExportVo);
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String fileName = URLEncoder.encode("报警记录与跟进导出", "UTF-8")
                    .replaceAll("\\+", "%20");
            response.setHeader("Content-disposition",
                    "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            EasyExcel.write(response.getOutputStream(), WarningTraceRecordExportVo.class)
                    .autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(new ExcelCellWidthStyleStrategy())
                    .registerWriteHandler(EasyExcelUtils.getTitleStyle())
                    .sheet("sheet_1")
                    .doWrite(exportVoList);
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

    /**
     * 温压管理-胎温胎压概览-胎温胎压报警
     *
     * @return
     */
    @Override
    public BaseResponse<TirePressureAndTemperatureStatisticVo> getWarningData() {
        UserVo user = UserContext.getUser();
        // 查询客户车辆列表
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
        // 高温报警数量
        Long highTemperature = this.getHighTemperatureCount(tireNewestDataList);
        // 高压报警数量
        Long highPressureCount = this.getHighPressureCount(tireNewestDataList);
        // 低压报警数量
        Long lowPressureCount = this.getLowPressureCount(tireNewestDataList);
        // 离线报警数量
        Long offlineCount = this.getOfflineCount(user);
        TirePressureAndTemperatureStatisticVo tirePressureAndTemperatureStatisticVo = new TirePressureAndTemperatureStatisticVo();
        tirePressureAndTemperatureStatisticVo.setHighTemperatureWarning(highTemperature);
        tirePressureAndTemperatureStatisticVo.setHighPressureWarning(highPressureCount);
        tirePressureAndTemperatureStatisticVo.setLowPressureWarning(lowPressureCount);
        tirePressureAndTemperatureStatisticVo.setOfflineWarning(offlineCount);
        return BaseResponse.ok(tirePressureAndTemperatureStatisticVo);
    }

    /**
     * 温压管理-胎温胎压概览-车辆轮胎概况
     *
     * @return
     */
    @Override
    public BaseResponse<VehicleAndTireStatisticVo> getVehicleAndTireData() {
        UserVo user = UserContext.getUser();
        // 查询车辆总数
        Long totalVehicleCount = vehicleMapper.selectCount(new LambdaQueryWrapper<VehicleEntity>()
                .eq(VehicleEntity::getFleetId, user.getCustomerIds()));
        // 查询车辆已绑定中继器总数
        Long vehicleBoundCount = vehicleMapper.selectCount(new LambdaQueryWrapper<VehicleEntity>()
                .eq(VehicleEntity::getFleetId, user.getCustomerIds())
                .eq(VehicleEntity::getIsDeleted, false)
                .isNotNull(VehicleEntity::getRepeaterIdNumber));
        // 查询所有轮胎总数
        Long totalTireCount = assetTireMapper.selectCount(new LambdaQueryWrapper<AssetTire>()
                .eq(AssetTire::getFleetId, user.getCustomerIds())
                .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE));
        // 查询轮胎已绑定传感器总数
        Long tireBoundCount = assetTireMapper.selectCount(new LambdaQueryWrapper<AssetTire>()
                .eq(AssetTire::getFleetId, user.getCustomerIds())
                .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE)
                .eq(AssetTire::getHasSensor, AssetTireConstant.IS_BIND_SENSOR));
        VehicleAndTireStatisticVo vehicleAndTireStatisticVo = new VehicleAndTireStatisticVo();
        vehicleAndTireStatisticVo.setTotalVehicleCount(totalVehicleCount);
        vehicleAndTireStatisticVo.setVehicleBoundCount(vehicleBoundCount);
        vehicleAndTireStatisticVo.setVehicleUnboundCount(totalVehicleCount - vehicleBoundCount);
        vehicleAndTireStatisticVo.setTotalTireCount(totalTireCount);
        vehicleAndTireStatisticVo.setTireBoundCount(tireBoundCount);
        vehicleAndTireStatisticVo.setTireUnboundCount(totalTireCount - tireBoundCount);
        return BaseResponse.ok(vehicleAndTireStatisticVo);
    }

    /**
     * 温压管理-胎温胎压概览-趋势图
     *
     * @return
     */
    @Override
    public BaseResponse<TrendStatisticVo> getTrendData() {
        UserVo user = UserContext.getUser();
        // 当前时间
        Date currentDate = new Date();
        Date startDate = this.getStartDate(currentDate);
        List<String> dateList = null;
        try {
            dateList = findDates(DateUtils.dateToStr(startDate),
                    DateUtils.dateToStr(currentDate));
        } catch (ParseException e) {
            throw new BaseException("99999", "日期转换异常" + e.getMessage());
        }
        // 车辆7日绑定新增趋势
        List<Long> vehicleBindTrend = this.getVehicleBindTrend(user, startDate, currentDate,
                dateList);
        // 轮胎7日绑定新增趋势
        List<Long> tireBindTrend = this.getTrieBindTrend(user, startDate, currentDate, dateList);
        // 高温报警7日趋势
        List<Long> highTemperatureWarningTrend = this.getHighTemperatureWarningTrend(user,
                startDate, currentDate, dateList);
        // 高压报警7日趋势
        List<Long> highPressureWarningTrend = this.getHighPressureWarningTrend(user, startDate,
                currentDate, dateList);
        // 低压报警7日趋势
        List<Long> lowPressureWarningTrend = this.getLowPressureWarningTrend(user, startDate,
                currentDate, dateList);
        TrendStatisticVo trendStatisticVo = new TrendStatisticVo();
        trendStatisticVo.setVehicleBindTrend(vehicleBindTrend);
        trendStatisticVo.setTireBindTrend(tireBindTrend);
        trendStatisticVo.setHighTemperatureWarningTrend(highTemperatureWarningTrend);
        trendStatisticVo.setHighPressureWarningTrend(highPressureWarningTrend);
        trendStatisticVo.setLowPressureWarningTrend(lowPressureWarningTrend);
        return BaseResponse.ok(trendStatisticVo);
    }

    /**
     * 温压管理-报警记录与跟进-未处理过的报警数量
     *
     * @return
     */
    @Override
    public BaseResponse<UnhandLedWarningCountVo> unhandledWarningCount() {
        UserVo user = UserContext.getUser();
        // 查询未处理过的报警数量
        Long urgentWarningCount = warningTraceRecordMapper.selectCount(
                new LambdaQueryWrapper<WarningTraceRecord>()
                        .in(WarningTraceRecord::getFleetId, user.getCustomerIds())
                        .eq(WarningTraceRecord::getType, WarningConstant.FOLLOW_UP_TYPE_URGENT)
                        .eq(WarningTraceRecord::getIsFollow, WarningConstant.IS_FOLLOW_UP_NO));
        Long commonWarningCount = warningTraceRecordMapper.selectCount(
                new LambdaQueryWrapper<WarningTraceRecord>()
                        .in(WarningTraceRecord::getFleetId, user.getCustomerIds())
                        .eq(WarningTraceRecord::getType, WarningConstant.FOLLOW_UP_TYPE_COMMON)
                        .eq(WarningTraceRecord::getIsFollow, WarningConstant.IS_FOLLOW_UP_NO));
        UnhandLedWarningCountVo unhandLedWarningCountVo = new UnhandLedWarningCountVo();
        unhandLedWarningCountVo.setUrgentWarningCount(urgentWarningCount);
        unhandLedWarningCountVo.setCommonWarningCount(commonWarningCount);
        return BaseResponse.ok(unhandLedWarningCountVo);
    }

    /**
     * 轮胎异常数据校验
     *
     * @param paramList
     * @return
     */
    @Override
    public BaseResponse checkTireData(List<TireCheckDataParam> paramList) {
        log.info("轮胎异常数据校验,param:{}", JSONObject.toJSON(paramList));
        long startTimestamp = System.currentTimeMillis();
        if (paramList.isEmpty()) {
            return BaseResponse.ok();
        }
        List<String> receiverIdList = new ArrayList<>();
        List<String> guaRepeaterIdList = new ArrayList<>();
        for (TireCheckDataParam tireCheckDataParam : paramList) {
            receiverIdList.add(tireCheckDataParam.getReceiverId());
            guaRepeaterIdList.add(tireCheckDataParam.getGuaRepeaterId());
        }
        List<VehicleEntity> mainVehicleList = vehicleMapper.selectList(
                new LambdaQueryWrapper<VehicleEntity>()
                        .in(VehicleEntity::getReceiverIdNumber, receiverIdList));
        if (mainVehicleList.isEmpty()) {
            log.error("车辆档案组不存在,param:{}", JSONObject.toJSON(paramList));
            return BaseResponse.ok();
        }
        // 挂车信息
        List<VehicleEntity> minorVehicleList = vehicleMapper.selectList(
                new LambdaQueryWrapper<VehicleEntity>()
                        .in(VehicleEntity::getRepeaterIdNumber, guaRepeaterIdList));
        // 查询仓库信息
        List<AssetTire> assetTireList = this.getAssetTireList(paramList);
        Map<Integer, VehicleSpecContextVo> vehicleSpecContextMap = vehicleSpecDataContext.getAllContexts();
        for (TireCheckDataParam param : paramList) {
            // 主车信息
            VehicleEntity mainVehicle = null;
            for (VehicleEntity vehicle : mainVehicleList) {
                if (param.getReceiverId().equals(vehicle.getReceiverIdNumber())) {
                    mainVehicle = vehicle;
                    break;
                }
            }
            // 挂车信息
            VehicleEntity minorVehicle = null;
            for (VehicleEntity vehicle : minorVehicleList) {
                if (param.getGuaRepeaterId().equals(vehicle.getRepeaterIdNumber())) {
                    minorVehicle = vehicle;
                    break;
                }
            }
            // 查询车辆型号
            if (mainVehicle == null) {
                log.error("车辆档案不存在,param:{}", JSONObject.toJSON(param));
                return BaseResponse.ok();
            }
            log.info("轮胎异常数据校验,准备车辆轮胎数据,耗时:{}ms",
                    System.currentTimeMillis() - startTimestamp);
            startTimestamp = System.currentTimeMillis();
            List<WarningDetail> batchInsertWarningDetailList = new ArrayList<>();
            List<WarningTraceRecord> batchSaveUrgencyWarningList = new ArrayList<>();
            for (TireCheckDataDetailParam data : param.getTireCheckDataDetailParamList()) {
                VehicleEntity vehicle;
                if (data.getType() == 0) {
                    vehicle = mainVehicle;
                } else {
                    vehicle = minorVehicle;
                }
                if (vehicle == null) {
                    log.error("车辆档案不存在,param:{}", JSONObject.toJSON(param));
                    break;
                }
                VehicleSpecContextVo vehicleSpec = vehicleSpecContextMap.get(vehicle.getSpecId());
                if (vehicleSpec == null) {
                    log.error("车辆型号不存在,param:{}", JSONObject.toJSON(param));
                    break;
                }
                // 获取轮胎告警类型
                List<WarningTypeEnum> warningTypeEnumList = WarningHelper.getTireWarningType(data,
                        vehicleSpec);
                // 判断异常数据入库
                if (!warningTypeEnumList.isEmpty()) {
                    List<WarningTypeEnum> urgencyWarningTypeList = new ArrayList<>();
                    List<WarningTypeEnum> commonWarningTypeList = new ArrayList<>();
                    // 查询轮胎号
                    String tireCode = this.getTireCode(data, assetTireList);
                    Integer tireSiteId = Integer.valueOf(data.getTireSiteId());
                    TireSiteResult tireSiteResult = TireSiteUtil.getTireSiteResult(tireSiteId,
                            vehicleSpec.getSpecType(), vehicleSpec.getWheelCount(),
                            vehicleSpec.getWheelbaseType());
                    // 判断告警是否是紧急类和常规类
                    this.setWarningType(warningTypeEnumList, urgencyWarningTypeList,
                            commonWarningTypeList);
                    // 保存紧急类告警
                    if (!urgencyWarningTypeList.isEmpty()) {
                        this.saveUrgencyWarning(param, data, tireCode, vehicle,
                                tireSiteResult, urgencyWarningTypeList, tireSiteId,
                                batchSaveUrgencyWarningList,
                                batchInsertWarningDetailList);
                    }
                    // 保存常规类告警
                    if (!commonWarningTypeList.isEmpty()) {
                        this.saveCommonWarning(param, data, tireCode, vehicle,
                                tireSiteResult, commonWarningTypeList, tireSiteId,
                                batchSaveUrgencyWarningList,
                                batchInsertWarningDetailList);
                    }
                }
            }
            log.info("轮胎异常数据校验,轮胎异常数据校验完成,开始入库,耗时:{}ms",
                    System.currentTimeMillis() - startTimestamp);
            startTimestamp = System.currentTimeMillis();
            // 跟进记录批量录入
            if (!batchSaveUrgencyWarningList.isEmpty()) {
                try {
                    warningTraceRecordJdbc.saveBatch(batchSaveUrgencyWarningList);
                } catch (SQLException e) {
                    log.error("跟进记录批量录入异常", e);
                    throw new RuntimeException(e);
                }
            }
            // 报警明细入库
            if (!batchInsertWarningDetailList.isEmpty()) {
                try {
                    warningDetailJdbc.saveBatch(batchInsertWarningDetailList);
                } catch (SQLException e) {
                    log.error("报警明细入库异常", e);
                }
            }
        }
        // 结束时间
        long endTimestamp = System.currentTimeMillis();
        log.info("轮胎异常数据入库结束,耗时:{}ms", endTimestamp - startTimestamp);
        return BaseResponse.success();
    }

    private List<AssetTire> getAssetTireList(List<TireCheckDataParam> paramList) {
        Set<String> sensorIdSet = new HashSet<>();
        for (TireCheckDataParam param : paramList) {
            for (TireCheckDataDetailParam tireCheckDataDetailParam : param.getTireCheckDataDetailParamList()) {
                sensorIdSet.add(tireCheckDataDetailParam.getTireSensorId());
            }
        }
        List<AssetTire> assetTireList = assetTireMapper.selectList(
                new LambdaQueryWrapper<AssetTire>()
                        .in(AssetTire::getSensorId, sensorIdSet)
                        .eq(AssetTire::getIsDelete, AssetTireConstant.NOT_DELETE)
                        .select(AssetTire::getCode, AssetTire::getSensorId));
        return assetTireList;
    }

    private void setWarningType(List<WarningTypeEnum> warningTypeEnumList,
            List<WarningTypeEnum> urgencyWarningTypeList,
            List<WarningTypeEnum> commonWarningTypeList) {
        for (WarningTypeEnum warningTypeEnum : warningTypeEnumList) {
            // 紧急类报警类型包括：高压报警、高温报警、低压报警、电压低报警、二级高压报警、二级低压报警、急漏气报警
            if (WarningTypeEnum.FIRST_HIGH_PRESSURE.equals(warningTypeEnum)
                    || WarningTypeEnum.FIRST_HIGH_TEMPERATURE.equals(warningTypeEnum)
                    || WarningTypeEnum.FIRST_LOW_PRESSURE.equals(warningTypeEnum)
                    || WarningTypeEnum.SECOND_HIGH_PRESSURE.equals(warningTypeEnum)
                    || WarningTypeEnum.SECOND_HIGH_TEMPERATURE.equals(warningTypeEnum)
                    || WarningTypeEnum.SECOND_LOW_PRESSURE.equals(warningTypeEnum)
                    || WarningTypeEnum.FAST_LEAK.equals(warningTypeEnum)) {
                urgencyWarningTypeList.add(warningTypeEnum);
            } else if (WarningTypeEnum.LOW_VOLTAGE.equals(warningTypeEnum)) {
                commonWarningTypeList.add(warningTypeEnum);
            }
        }
    }

    private String getTireCode(TireCheckDataDetailParam data, List<AssetTire> assetTireList) {
        String tireCode = "";
        for (AssetTire assetTire : assetTireList) {
            if (assetTire.getSensorId().equals(data.getTireSensorId())) {
                tireCode = assetTire.getCode();
                break;
            }
        }
        return tireCode;
    }

    private void saveCommonWarning(TireCheckDataParam param, TireCheckDataDetailParam data,
            String tireCode, VehicleEntity vehicle,
            TireSiteResult tireSiteResult, List<WarningTypeEnum> commonWarningTypeList,
            Integer tireSiteId, List<WarningTraceRecord> batchSaveUrgencyWarningList,
            List<WarningDetail> batchInsertWarningDetailList) {
        // 设置查询时间:3天前
        Date threeDaysBeforeDate = this.getThreeDaysBeforeDate(new Date());
        // 校验重复告警
        WarningTraceRecord warningTraceRecord = null;
        CheckWarningDetailRepeat checkWarningDetailRepeat = this.checkWarningDetailRepeat(
                vehicle.getLicensePlate(),
                commonWarningTypeList, tireSiteId, threeDaysBeforeDate);
        String traceNo = "";
        if (!checkWarningDetailRepeat.getNewWarningTypeList().isEmpty()) {
            traceNo = String.valueOf(ID_WORKER.nextId());
            warningTraceRecord = this.saveWarningTraceRecord(traceNo, param, data, tireCode,
                    vehicle, tireSiteResult, commonWarningTypeList, tireSiteId,
                    WarningConstant.FOLLOW_UP_TYPE_COMMON);
            batchSaveUrgencyWarningList.add(warningTraceRecord);
        } else if (!checkWarningDetailRepeat.getOldWarningTypeList().isEmpty()) {
            // 重复告警更新告警记录
            List<WarningTraceRecord> warningTraceRecordList = warningTraceRecordMapper.selectList(
                    new LambdaQueryWrapper<WarningTraceRecord>()
                            .eq(WarningTraceRecord::getLicensePlate, vehicle.getLicensePlate())
                            .eq(WarningTraceRecord::getTireSiteId, tireSiteId)
                            .eq(WarningTraceRecord::getType, WarningConstant.FOLLOW_UP_TYPE_COMMON)
                            .ge(WarningTraceRecord::getCreateTime, threeDaysBeforeDate)
                            .orderByDesc(WarningTraceRecord::getCreateTime)
                            .last("limit 1"));
            if (!warningTraceRecordList.isEmpty()) {
                // 存在常规告警历史记录,不更新
                warningTraceRecord = warningTraceRecordList.get(0);
                traceNo = warningTraceRecord.getTraceNo();
            }
        }
        this.setBatchInsertWarningDetailParam(tireSiteId, param.getReceiverId(),
                String.valueOf(param.getSerialNo()), data.getTirePressure(),
                data.getTireTemperature(),
                tireSiteResult, commonWarningTypeList, traceNo, vehicle, tireCode,
                batchInsertWarningDetailList);
    }

    private void saveUrgencyWarning(TireCheckDataParam param, TireCheckDataDetailParam data,
            String tireCode, VehicleEntity vehicle, TireSiteResult tireSiteResult,
            List<WarningTypeEnum> urgencyWarningTypeList,
            Integer tireSiteId, List<WarningTraceRecord> batchSaveUrgencyWarningList,
            List<WarningDetail> batchInsertWarningDetailList) {
        Date oneHourBeforeDate = this.getOneHourBeforeDate(new Date());
        // 校验重复告警
        CheckWarningDetailRepeat checkWarningDetailRepeat = this.checkWarningDetailRepeat(
                vehicle.getLicensePlate(),
                urgencyWarningTypeList, tireSiteId, oneHourBeforeDate);
        WarningTraceRecord warningTraceRecord = null;
        String traceNo = "";
        if (!checkWarningDetailRepeat.getNewWarningTypeList().isEmpty()) {
            traceNo = String.valueOf(ID_WORKER.nextId());
            // 新告警,保存报警跟进记录
            warningTraceRecord = this.saveWarningTraceRecord(traceNo, param, data, tireCode,
                    vehicle, tireSiteResult, checkWarningDetailRepeat.getNewWarningTypeList(),
                    tireSiteId, WarningConstant.FOLLOW_UP_TYPE_URGENT);
            batchSaveUrgencyWarningList.add(warningTraceRecord);
            // 保存明细记录
            this.setBatchInsertWarningDetailParam(tireSiteId, param.getReceiverId(),
                    String.valueOf(param.getSerialNo()), data.getTirePressure(),
                    data.getTireTemperature(), tireSiteResult, urgencyWarningTypeList, traceNo,
                    vehicle, tireCode, batchInsertWarningDetailList);
        }
    }

    private WarningTraceRecord saveWarningTraceRecord(String traceNo, TireCheckDataParam param,
            TireCheckDataDetailParam data, String tireCode, VehicleEntity vehicle,
            TireSiteResult tireSiteResult, List<WarningTypeEnum> warningTypeEnumList,
            Integer tireSiteId, Integer type) {
        WarningTraceRecord warningTraceRecord = new WarningTraceRecord();
        warningTraceRecord.setTraceNo(traceNo);
        warningTraceRecord.setTireCode(tireCode);
        warningTraceRecord.setFleetId(vehicle.getFleetId());
        CustomerContextVo fleetContext = customerContext.getContext(vehicle.getFleetId());
        if (fleetContext != null) {
            warningTraceRecord.setFleetName(fleetContext.getName());
        }
        warningTraceRecord.setClientId(vehicle.getCustomerId());
        CustomerContextVo clientContext = customerContext.getContext(vehicle.getCustomerId());
        if (clientContext != null) {
            warningTraceRecord.setClientName(clientContext.getName());
        }
        warningTraceRecord.setLicensePlate(vehicle.getLicensePlate());
        warningTraceRecord.setTireSiteId(tireSiteId);
        if (tireSiteResult != null) {
            warningTraceRecord.setTireSiteName(tireSiteResult.getTireSiteName());
        }
        // warningTypeList转换为,号分割的String
        List<String> urgencyWarningTypeStrList = new ArrayList<>();
        for (WarningTypeEnum warningTypeEnum : warningTypeEnumList) {
            urgencyWarningTypeStrList.add(warningTypeEnum.getDescription());
        }
        warningTraceRecord.setWarningType(
                StringUtils.join(urgencyWarningTypeStrList, ","));
        warningTraceRecord.setPressure(data.getTirePressure());
        warningTraceRecord.setTemperature(data.getTireTemperature());
        warningTraceRecord.setVoltage(data.getVoltage());
        // 根据高德坐标获取逆地理编码
        String currentLocation = "";
        try {
            // 调用高德地图
            ConvertCoordVo convertCoordVo = amapUtil.convertCoord(param.getLongitude(),
                    param.getLatitude());
            String amapCoord = convertCoordVo.getLocations();
            RegeocodeResultVo regeocodeResult = amapUtil.regeocode(amapCoord);
            RegeocodeVo regeocode = regeocodeResult.getRegeocode();
            AddressComponentVo addressComponent = regeocode.getAddressComponent();
            currentLocation =
                    addressComponent.getProvince() + addressComponent.getCity()
                            + addressComponent.getDistrict();
        } catch (Exception e) {
            log.error("高德地图逆地理编码异常,param:{}", JSONObject.toJSON(param));
            e.printStackTrace();
        }
        warningTraceRecord.setLocation(currentLocation);
        warningTraceRecord.setType(type);
        // 发送通知
        this.sendNotice(warningTraceRecord, warningTypeEnumList);
        return warningTraceRecord;
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

    /**
     * 校验重复告警
     *
     * @param licensePlate    车牌
     * @param warningTypeList 告警类型
     * @param tireSiteId      轮胎号
     * @param startTime       开始时间
     * @return
     */
    private CheckWarningDetailRepeat checkWarningDetailRepeat(String licensePlate,
            List<WarningTypeEnum> warningTypeList, Integer tireSiteId, Date startTime) {
        CheckWarningDetailRepeat checkWarningDetailRepeat = new CheckWarningDetailRepeat();
        List<WarningTypeEnum> newWarningTypeList = new ArrayList<>();
        List<WarningTypeEnum> oldWarningTypeList = new ArrayList<>();
        // 同一车辆、同一轮位、同一报警类型时间间隔内只报一次（分钟数可配置），不重复报警。同一车辆、同一轮位同时有多重报警类型时归并为一条显示
        for (WarningTypeEnum warningTypeEnum : warningTypeList) {
            List<WarningDetail> warningDetailList = warningDetailMapper.selectList(
                    new LambdaQueryWrapper<WarningDetail>()
                            .eq(WarningDetail::getLicensePlate, licensePlate)
                            .eq(WarningDetail::getTireSite, tireSiteId)
                            .eq(WarningDetail::getWarningType, warningTypeEnum.getType())
                            .gt(WarningDetail::getCreateTime, startTime)
                            .select(WarningDetail::getId)
                            .last("limit 1"));
            // 历史数据差不到则表明新告警
            if (warningDetailList.isEmpty()) {
                newWarningTypeList.add(warningTypeEnum);
            } else {
                oldWarningTypeList.add(warningTypeEnum);
            }
        }
        checkWarningDetailRepeat.setNewWarningTypeList(newWarningTypeList);
        checkWarningDetailRepeat.setOldWarningTypeList(oldWarningTypeList);
        return checkWarningDetailRepeat;
    }

    private Date getOneHourBeforeDate(Date currentDate) {
        // 获取当前时间的 Calendar 实例
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        // 将当前时间减去一个小时
        int interval = warningSimpleConfig.getUrgentInterval();
        calendar.add(Calendar.HOUR, -interval);
        return calendar.getTime();
    }

    private Date getThreeDaysBeforeDate(Date currentDate) {
        // 获取当前时间的 Calendar 实例
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        // 将当前时间减去一个小时
        int interval = warningSimpleConfig.getCommonInterval();
        calendar.add(Calendar.HOUR, -interval);
        return calendar.getTime();
    }

    private void setBatchInsertWarningDetailParam(Integer tireSiteId, String receiverId,
            String serialNo, String tirePressure, String tireTemperature,
            TireSiteResult tireSiteResult, List<WarningTypeEnum> warningTypeEnumList,
            String traceNo, VehicleEntity vehicle, String tireCode,
            List<WarningDetail> batchInsertWarningDetailList) {
        if (StringUtils.isBlank(traceNo)) {
            return;
        }
        for (WarningTypeEnum warningTypeEnum : warningTypeEnumList) {
            WarningDetail warningDetail = new WarningDetail();
            warningDetail.setReceiverId(receiverId);
            warningDetail.setSerialNo(serialNo);
            warningDetail.setTraceNo(traceNo);
            warningDetail.setClientId(vehicle.getCustomerId());
            warningDetail.setFleetId(vehicle.getFleetId());
            warningDetail.setLicensePlate(vehicle.getLicensePlate());
            warningDetail.setTireCode(tireCode);
            warningDetail.setTireSite(tireSiteId);
            if (tireSiteResult != null) {
                warningDetail.setTireSiteName(tireSiteResult.getTireSiteName());
            }
            warningDetail.setWarningType(warningTypeEnum.getType());
            warningDetail.setPressure(tirePressure);
            warningDetail.setTemperature(tireTemperature);
            batchInsertWarningDetailList.add(warningDetail);
        }
    }

    /**
     * 温压管理-报警记录与跟进-获取跟进备注模版
     *
     * @param warningTypeList
     * @return
     */
    @Override
    public BaseResponse<List<WarningRemarkTemplateVo>> followUpTemplate(
            List<Integer> warningTypeList) {
        if (warningTypeList.isEmpty()) {
            return BaseResponse.ok(new ArrayList<>());
        }
        List<WarningRemarkTemplate> warningRemarkTemplateList = warningRemarkTemplateMapper.selectList(
                new LambdaQueryWrapper<WarningRemarkTemplate>()
                        .in(WarningRemarkTemplate::getType, warningTypeList));
        List<WarningRemarkTemplateVo> warningRemarkTemplateVoList = new ArrayList<>();
        for (WarningRemarkTemplate warningRemarkTemplate : warningRemarkTemplateList) {
            WarningRemarkTemplateVo warningRemarkTemplateVo = new WarningRemarkTemplateVo();
            warningRemarkTemplateVo.setId(warningRemarkTemplate.getId());
            warningRemarkTemplateVo.setClientId(warningRemarkTemplate.getClientId());
            warningRemarkTemplateVo.setType(warningRemarkTemplate.getType());
            warningRemarkTemplateVo.setReason(warningRemarkTemplate.getReason());
            warningRemarkTemplateVoList.add(warningRemarkTemplateVo);
        }
        return BaseResponse.ok(warningRemarkTemplateVoList);
    }


    private List<Long> getLowPressureWarningTrend(UserVo user, Date startDate, Date currentDate,
            List<String> dateList) {
        List<Long> lowPressureWarningTrend = new ArrayList<>();
        List<Integer> warningTypeList = new ArrayList<>();
        warningTypeList.add(WarningConstant.WARNING_TYPE_FIRST_LOW_PRESSURE);
        warningTypeList.add(WarningConstant.WARNING_TYPE_SECOND_LOW_PRESSURE);
        List<WarningDetail> warningDetailList = warningDetailMapper.selectList(
                new LambdaQueryWrapper<WarningDetail>()
                        .in(WarningDetail::getFleetId, user.getCustomerIds())
                        .in(WarningDetail::getWarningType, warningTypeList)
                        .between(WarningDetail::getCreateTime, startDate, currentDate)
                        .orderByAsc(WarningDetail::getCreateTime)
                        .select(WarningDetail::getId, WarningDetail::getCreateTime));
        for (String dateStr : dateList) {
            Date startTime = DateUtil.parseDate(dateStr + " 00:00:00",
                    DateUtil.STANDARD_DATE_TIME_PATTERN);
            Date endTime = DateUtil.parseDate(dateStr + " 23:59:59",
                    DateUtil.STANDARD_DATE_TIME_PATTERN);
            long count = 0;
            for (WarningDetail warningDetail : warningDetailList) {
                if (warningDetail.getCreateTime().after(startTime)
                        && warningDetail.getCreateTime().before(endTime)) {
                    count++;
                }
            }
            lowPressureWarningTrend.add(count);
        }
        return lowPressureWarningTrend;
    }

    private List<Long> getHighPressureWarningTrend(UserVo user, Date startDate, Date currentDate,
            List<String> dateList) {
        List<Long> highPressureWarningTrend = new ArrayList<>();
        List<Integer> warningTypeList = new ArrayList<>();
        warningTypeList.add(WarningConstant.WARNING_TYPE_FIRST_HIGH_PRESSURE);
        warningTypeList.add(WarningConstant.WARNING_TYPE_SECOND_HIGH_PRESSURE);
        List<WarningDetail> warningDetailList = warningDetailMapper.selectList(
                new LambdaQueryWrapper<WarningDetail>()
                        .in(WarningDetail::getFleetId, user.getCustomerIds())
                        .in(WarningDetail::getWarningType, warningTypeList)
                        .between(WarningDetail::getCreateTime, startDate, currentDate)
                        .orderByAsc(WarningDetail::getCreateTime)
                        .select(WarningDetail::getId, WarningDetail::getCreateTime));
        for (String dateStr : dateList) {
            Date startTime = DateUtil.parseDate(dateStr + " 00:00:00",
                    DateUtil.STANDARD_DATE_TIME_PATTERN);
            Date endTime = DateUtil.parseDate(dateStr + " 23:59:59",
                    DateUtil.STANDARD_DATE_TIME_PATTERN);
            long count = 0;
            for (WarningDetail warningDetail : warningDetailList) {
                if (warningDetail.getCreateTime().after(startTime)
                        && warningDetail.getCreateTime().before(endTime)) {
                    count++;
                }
            }
            highPressureWarningTrend.add(count);
        }
        return highPressureWarningTrend;
    }

    private List<Long> getHighTemperatureWarningTrend(UserVo user, Date startDate, Date currentDate,
            List<String> dateList) {
        List<Long> highTemperatureWarningTrend = new ArrayList<>();
        List<Integer> warningTypeList = new ArrayList<>();
        warningTypeList.add(WarningConstant.WARNING_TYPE_FIRST_HIGH_TEMPERATURE);
        warningTypeList.add(WarningConstant.WARNING_TYPE_SECOND_HIGH_TEMPERATURE);
        List<WarningDetail> warningDetailList = warningDetailMapper.selectList(
                new LambdaQueryWrapper<WarningDetail>()
                        .in(WarningDetail::getFleetId, user.getCustomerIds())
                        .in(WarningDetail::getWarningType, warningTypeList)
                        .between(WarningDetail::getCreateTime, startDate, currentDate)
                        .orderByAsc(WarningDetail::getCreateTime)
                        .select(WarningDetail::getId, WarningDetail::getCreateTime));
        for (String dateStr : dateList) {
            Date startTime = DateUtil.parseDate(dateStr + " 00:00:00",
                    DateUtil.STANDARD_DATE_TIME_PATTERN);
            Date endTime = DateUtil.parseDate(dateStr + " 23:59:59",
                    DateUtil.STANDARD_DATE_TIME_PATTERN);
            long count = 0;
            for (WarningDetail warningDetail : warningDetailList) {
                if (warningDetail.getCreateTime().after(startTime)
                        && warningDetail.getCreateTime().before(endTime)) {
                    count++;
                }
            }
            highTemperatureWarningTrend.add(count);
        }
        return highTemperatureWarningTrend;
    }

    private List<Long> getTrieBindTrend(UserVo user, Date startDate, Date currentDate,
            List<String> dateList) {
        List<Long> tireBindTrend = new ArrayList<>();
        List<AssetTireDeviceBindRecord> assetTireDeviceBindRecordList = assetTireDeviceBindRecordMapper.selectList(
                new LambdaQueryWrapper<AssetTireDeviceBindRecord>()
                        .in(AssetTireDeviceBindRecord::getFleetId, user.getCustomerIds())
                        .eq(AssetTireDeviceBindRecord::getDeviceType, DEVICE_TYPE_SENSOR)
                        .between(AssetTireDeviceBindRecord::getCreateTime, startDate, currentDate)
                        .orderByAsc(AssetTireDeviceBindRecord::getCreateTime));
        for (String dateStr : dateList) {
            Date startTime = DateUtil.parseDate(dateStr + " 00:00:00",
                    DateUtil.STANDARD_DATE_TIME_PATTERN);
            Date endTime = DateUtil.parseDate(dateStr + " 23:59:59",
                    DateUtil.STANDARD_DATE_TIME_PATTERN);
            long count = 0;
            for (AssetTireDeviceBindRecord tireDeviceBindRecord : assetTireDeviceBindRecordList) {
                if (tireDeviceBindRecord.getCreateTime().after(startTime)
                        && tireDeviceBindRecord.getCreateTime().before(endTime)) {
                    count++;
                }
            }
            tireBindTrend.add(count);
        }
        return tireBindTrend;
    }

    private List<Long> getVehicleBindTrend(UserVo user, Date startDate, Date currentDate,
            List<String> dateList) {
        List<Long> vehicleBindTrend = new ArrayList<>();
        List<AssetTireDeviceBindRecord> assetTireDeviceBindRecordList = assetTireDeviceBindRecordMapper.selectList(
                new LambdaQueryWrapper<AssetTireDeviceBindRecord>()
                        .in(AssetTireDeviceBindRecord::getFleetId, user.getCustomerIds())
                        .eq(AssetTireDeviceBindRecord::getDeviceType, DEVICE_TYPE_RELAY)
                        .between(AssetTireDeviceBindRecord::getCreateTime, startDate, currentDate)
                        .orderByAsc(AssetTireDeviceBindRecord::getCreateTime));
        for (String dateStr : dateList) {
            Date startTime = DateUtil.parseDate(dateStr + " 00:00:00",
                    DateUtil.STANDARD_DATE_TIME_PATTERN);
            Date endTime = DateUtil.parseDate(dateStr + " 23:59:59",
                    DateUtil.STANDARD_DATE_TIME_PATTERN);
            long count = 0;
            for (AssetTireDeviceBindRecord tireDeviceBindRecord : assetTireDeviceBindRecordList) {
                if (tireDeviceBindRecord.getCreateTime().after(startTime)
                        && tireDeviceBindRecord.getCreateTime().before(endTime)) {
                    count++;
                }
            }
            vehicleBindTrend.add(count);
        }
        return vehicleBindTrend;
    }

    public static void main(String[] args) {
        try {
            List<String> dates = findDates("2021-10-01 00:00:00", "2021-10-10 23:59:59");
            for (String date : dates) {
                System.out.println(date);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 时间范围内的每一天
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static List<String> findDates(String startTime, String endTime) throws ParseException {
        //日期工具类准备
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //设置开始时间
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(format.parse(startTime));
        //设置结束时间
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(format.parse(endTime));
        //装返回的日期集合容器
        List<String> dateList = new ArrayList<>();
        //将第一个月添加里面去
        dateList.add(format.format(calBegin.getTime()));
        // 每次循环给calBegin日期加一天，直到calBegin.getTime()时间等于dEnd
        while (format.parse(endTime).after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(format.format(calBegin.getTime()));
        }
        return dateList;
    }

    /**
     * 获取开始时间(当前时间往前推8天)
     *
     * @param currentDate
     * @return
     */
    private Date getStartDate(Date currentDate) {
        // 当前时间往前推8天
        Calendar calendar = Calendar.getInstance();
        // 设置当前时间
        calendar.setTime(currentDate);
        // 往前推8天
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();
        return startDate;
    }

    /**
     * 高温报警数量
     *
     * @return
     */
    private Long getHighTemperatureCount(List<TireNewestData> tireNewestDataList) {
        if (tireNewestDataList.isEmpty()) {
            return 0L;
        }
        List<Integer> warningTypeList = new ArrayList<>();
        warningTypeList.add(WarningConstant.WARNING_TYPE_FIRST_HIGH_TEMPERATURE);
        warningTypeList.add(WarningConstant.WARNING_TYPE_SECOND_HIGH_TEMPERATURE);
        List<WarningDetail> warningDetailList = warningDetailMapper.selectList(
                new LambdaQueryWrapper<WarningDetail>()
                        .in(WarningDetail::getReceiverId, tireNewestDataList.stream()
                                .map(TireNewestData::getClientId).collect(Collectors.toSet()))
                        .in(WarningDetail::getWarningType, warningTypeList)
                        .select(WarningDetail::getReceiverId, WarningDetail::getSerialNo));
        long highTemperature = 0;
        for (WarningDetail warningDetail : warningDetailList) {
            for (TireNewestData tireNewestData : tireNewestDataList) {
                if (warningDetail.getReceiverId().equals(tireNewestData.getClientId())
                        && warningDetail.getSerialNo().equals(tireNewestData.getSerialNo())) {
                    highTemperature++;
                    break;
                }
            }
        }
        return highTemperature;
    }

    /**
     * 高压报警数量
     *
     * @return
     */
    private Long getHighPressureCount(List<TireNewestData> tireNewestDataList) {
        if (tireNewestDataList.isEmpty()) {
            return 0L;
        }
        List<Integer> warningTypeList = new ArrayList<>();
        warningTypeList.add(WarningConstant.WARNING_TYPE_FIRST_HIGH_PRESSURE);
        warningTypeList.add(WarningConstant.WARNING_TYPE_SECOND_HIGH_PRESSURE);
        List<WarningDetail> warningDetailList = warningDetailMapper.selectList(
                new LambdaQueryWrapper<WarningDetail>()
                        .in(WarningDetail::getReceiverId, tireNewestDataList.stream()
                                .map(TireNewestData::getClientId).collect(Collectors.toSet()))
                        .in(WarningDetail::getWarningType, warningTypeList)
                        .select(WarningDetail::getReceiverId, WarningDetail::getSerialNo));
        long highPressure = 0;
        for (WarningDetail warningDetail : warningDetailList) {
            for (TireNewestData tireNewestData : tireNewestDataList) {
                if (warningDetail.getReceiverId().equals(tireNewestData.getClientId())
                        && warningDetail.getSerialNo().equals(tireNewestData.getSerialNo())) {
                    highPressure++;
                    break;
                }
            }
        }
        return highPressure;
    }

    /**
     * 低压报警数量
     *
     * @return
     */
    private Long getLowPressureCount(List<TireNewestData> tireNewestDataList) {
        if (tireNewestDataList.isEmpty()) {
            return 0L;
        }
        List<Integer> warningTypeList = new ArrayList<>();
        warningTypeList.add(WarningConstant.WARNING_TYPE_FIRST_LOW_PRESSURE);
        warningTypeList.add(WarningConstant.WARNING_TYPE_SECOND_LOW_PRESSURE);
        List<WarningDetail> warningDetailList = warningDetailMapper.selectList(
                new LambdaQueryWrapper<WarningDetail>()
                        .in(WarningDetail::getReceiverId, tireNewestDataList.stream()
                                .map(TireNewestData::getClientId).collect(Collectors.toSet()))
                        .in(WarningDetail::getWarningType, warningTypeList)
                        .select(WarningDetail::getReceiverId, WarningDetail::getSerialNo));
        long lowPressure = 0;
        for (WarningDetail warningDetail : warningDetailList) {
            for (TireNewestData tireNewestData : tireNewestDataList) {
                if (warningDetail.getReceiverId().equals(tireNewestData.getClientId())
                        && warningDetail.getSerialNo().equals(tireNewestData.getSerialNo())) {
                    lowPressure++;
                    break;
                }
            }
        }
        return lowPressure;
    }

    /**
     * 离线报警数量
     *
     * @return
     */
    private Long getOfflineCount(UserVo user) {
        Long offline = warningDetailMapper.selectCount(
                new LambdaQueryWrapper<WarningDetail>()
                        .in(WarningDetail::getFleetId, user.getCustomerIds())
                        .eq(WarningDetail::getWarningType, WarningConstant.WARNING_TYPE_NO_SIGNAL)
                        .eq(WarningDetail::getIsRecovery, WarningConstant.IS_RECOVERY_NO));
        return offline;
    }

    private WarningTraceDetailRecordVo getWarningTraceDetailRecordVo(WarningDetail detail) {
        WarningTraceDetailRecordVo warningTraceDetailRecordVo = new WarningTraceDetailRecordVo();
        warningTraceDetailRecordVo.setId(detail.getId());
        warningTraceDetailRecordVo.setTraceNo(detail.getTraceNo());
        warningTraceDetailRecordVo.setLicensePlate(detail.getLicensePlate());
        warningTraceDetailRecordVo.setTireCode(detail.getTireCode());
        warningTraceDetailRecordVo.setTireSite(detail.getTireSite());
        warningTraceDetailRecordVo.setTireSiteName(detail.getTireSiteName());
        warningTraceDetailRecordVo.setWarningType(detail.getWarningType());
        warningTraceDetailRecordVo.setPressure(detail.getPressure());
        warningTraceDetailRecordVo.setTemperature(detail.getTemperature());
        warningTraceDetailRecordVo.setPressureThreshold(detail.getPressureThreshold());
        warningTraceDetailRecordVo.setTemperatureThreshold(detail.getTemperatureThreshold());
        warningTraceDetailRecordVo.setCreateTime(detail.getCreateTime());
        return warningTraceDetailRecordVo;
    }

    private LambdaQueryWrapper<WarningDetail> getWarningDetailQueryWrapper(
            WarningDetailRecordConditionQueryParam param) {
        LambdaQueryWrapper<WarningDetail> queryWrapper = new LambdaQueryWrapper<>();
        if (param.getTraceNo() != null) {
            queryWrapper.eq(WarningDetail::getTraceNo, param.getTraceNo());
        }
        if (param.getStartTime() != null) {
            queryWrapper.ge(WarningDetail::getCreateTime, param.getStartTime());
        }
        if (param.getEndTime() != null) {
            queryWrapper.le(WarningDetail::getCreateTime, param.getEndTime());
        }
        queryWrapper.orderByDesc(WarningDetail::getCreateTime);
        return queryWrapper;
    }

    private WarningTraceRecordVo getWarningTraceRecordVo(WarningTraceRecord record,
            List<WarningTraceRecordDetail> warningTraceRecordDetailList) {
        WarningTraceRecordVo warningTraceRecordVo = new WarningTraceRecordVo();
        warningTraceRecordVo.setId(record.getId());
        warningTraceRecordVo.setTireCode(record.getTireCode());
        warningTraceRecordVo.setFleetId(record.getFleetId());
        warningTraceRecordVo.setFleetName(record.getFleetName());
        warningTraceRecordVo.setClientId(record.getClientId());
        warningTraceRecordVo.setClientName(record.getClientName());
        warningTraceRecordVo.setLicensePlate(record.getLicensePlate());
        warningTraceRecordVo.setTireSiteName(record.getTireSiteName());
        warningTraceRecordVo.setWarningType(record.getWarningType());
        warningTraceRecordVo.setPressure(record.getPressure());
        warningTraceRecordVo.setTemperature(record.getTemperature());
        warningTraceRecordVo.setVoltage(record.getVoltage());
        warningTraceRecordVo.setLocation(record.getLocation());
        warningTraceRecordVo.setType(record.getType());
        warningTraceRecordVo.setCreateTime(record.getCreateTime());
        warningTraceRecordVo.setIsFollow(record.getIsFollow());
        warningTraceRecordVo.setFollowTime(record.getFollowTime());
        for (WarningTraceRecordDetail warningTraceRecordDetail : warningTraceRecordDetailList) {
            if (record.getId().equals(warningTraceRecordDetail.getTraceRecordId())) {
                warningTraceRecordVo.setReason(warningTraceRecordDetail.getReason());
                warningTraceRecordVo.setRemark(warningTraceRecordDetail.getRemark());
                warningTraceRecordVo.setFollowPerson(warningTraceRecordDetail.getFollowName());
                break;
            }
        }
        return warningTraceRecordVo;
    }

    private LambdaQueryWrapper<WarningTraceRecord> getQueryWrapper(
            WarningTraceRecordConditionQueryParam param, UserVo user) {
        LambdaQueryWrapper<WarningTraceRecord> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(param.getLicensePlate())) {
            queryWrapper.like(WarningTraceRecord::getLicensePlate, param.getLicensePlate());
        }
        if (StringUtils.isNotBlank(param.getFleetName())) {
            queryWrapper.like(WarningTraceRecord::getFleetName, param.getFleetName());
        }
        if (param.getType() != null) {
            queryWrapper.eq(WarningTraceRecord::getType, param.getType());
        }
        if (StringUtils.isNotBlank(param.getWarningType())) {
            queryWrapper.like(WarningTraceRecord::getWarningType, param.getWarningType());
        }
        if (param.getIsFollow() != null) {
            queryWrapper.like(WarningTraceRecord::getIsFollow, param.getIsFollow());
        }
        if (param.getStartTime() != null) {
            queryWrapper.ge(WarningTraceRecord::getCreateTime, param.getStartTime());
        }
        if (param.getEndTime() != null) {
            queryWrapper.le(WarningTraceRecord::getCreateTime, param.getEndTime());
        }
        if (user.getCompanyId() == SysCustomerConstant.ADMINISTRATOR_CUSTOMER_ID) {
            queryWrapper.in(WarningTraceRecord::getClientId, user.getCustomerIds());
        } else {
            queryWrapper.eq(WarningTraceRecord::getClientId, user.getCompanyId());
        }
        queryWrapper.orderByDesc(WarningTraceRecord::getFollowTime,
                WarningTraceRecord::getCreateTime);
        return queryWrapper;
    }
}
