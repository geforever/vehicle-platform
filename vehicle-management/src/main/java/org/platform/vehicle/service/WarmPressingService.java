package org.platform.vehicle.service;

import org.platform.vehicle.param.BindRelayCallbackParam;
import org.platform.vehicle.param.RelayBindParam;
import org.platform.vehicle.param.SyncWheelCallbackParam;
import org.platform.vehicle.param.TireTrackParam;
import org.platform.vehicle.param.TrailerInstallCallbackParam;
import org.platform.vehicle.param.VehicleHangParam;
import org.platform.vehicle.param.WarmPressingConditionQuery;
import org.platform.vehicle.param.WarmPressingExportParam;
import org.platform.vehicle.param.WarningThresholdSyncCallbackParam;
import org.platform.vehicle.param.WarningThresholdSyncParam;
import org.platform.vehicle.param.WheelSyncParam;
import org.platform.vehicle.vo.TireStatusVo;
import org.platform.vehicle.vo.TireTrendParam;
import org.platform.vehicle.vo.TireTrendVo;
import org.platform.vehicle.vo.VehicleTrackVo;
import org.platform.vehicle.vo.WarmPressingDetailVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author gejiawei
 * @Date 2023/10/12 10:35
 */
public interface WarmPressingService {

    /**
     * 温压管理-实时温压-车辆条件查询
     *
     * @param param
     * @return return
     */
    BasePageResponse conditionQuery(WarmPressingConditionQuery param);

    /**
     * 温压管理-实时温压-详情
     *
     * @param licensePlate
     * @return return
     */
    BaseResponse<WarmPressingDetailVo> getWarmPressingDetailDetail(String licensePlate);

    /**
     * 温压管理-实时温压-下挂
     *
     * @param param
     * @return
     */
    BaseResponse editTrailerStatus(VehicleHangParam param);

    /**
     * 温压管理-实时温压-上下挂回调
     *
     * @param param
     */
    void trailerUnInstallCallback(TrailerInstallCallbackParam param);

    /**
     * 温压管理-实时温压-根据车牌获取中继器ID
     *
     * @param licensePlate
     * @return
     */
    BaseResponse<String> getRelayId(String licensePlate);

    /**
     * 温压管理-实时温压-绑定中继器
     *
     * @param param
     * @return
     */
    BaseResponse bindRelay(RelayBindParam param);

    /**
     * 温压管理-实时温压-轮位同步
     *
     * @param param
     * @return
     */
    BaseResponse syncWheel(WheelSyncParam param);

    /**
     * 温压管理-实时温压-阈值同步
     *
     * @param param
     * @return
     */
    BaseResponse syncThreshold(WarningThresholdSyncParam param);

    /**
     * 温压管理-实时温压-轮胎状态查询
     *
     * @param licensePlate 车牌
     * @param tireSite     轮位
     * @return
     */
    BaseResponse<TireStatusVo> getTireStatus(String licensePlate, String tireSite);

    /**
     * 温压管理-实时温压-查看轮胎温压趋势
     *
     * @param param
     * @return
     */
    BaseResponse<TireTrendVo> getTireTrend(TireTrendParam param);

    /**
     * 温压管理-实时温压-查看车辆行驶轨迹
     *
     * @param param
     * @return
     */
    BaseResponse<List<VehicleTrackVo>> getVehicleTrack(TireTrackParam param);

    /**
     * 温压管理-实时温压-数据导出
     *
     * @param param
     * @param response
     */
    void export(WarmPressingExportParam param, HttpServletResponse response);


    void bindRelayCallback(BindRelayCallbackParam param);

    void syncWheelCallback(SyncWheelCallbackParam param);

    void syncThresholdCallback(WarningThresholdSyncCallbackParam param);

    void trailerInstallCallback(TrailerInstallCallbackParam param);
}
