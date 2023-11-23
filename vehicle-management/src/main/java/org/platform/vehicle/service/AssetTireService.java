package org.platform.vehicle.service;

import org.platform.vehicle.param.AssetTireConditionQueryParam;
import org.platform.vehicle.param.AssetTireFitConditionQuery;
import org.platform.vehicle.param.AssetTireSensorBindParam;
import org.platform.vehicle.param.AssetTireStockOutParam;
import org.platform.vehicle.param.InstallTireParam;
import org.platform.vehicle.param.UninstallTireParam;
import org.platform.vehicle.vo.AssertTireDetailVo;
import org.platform.vehicle.vo.AssetTireEditParam;
import org.platform.vehicle.vo.AssetTireFitDetailVo;
import org.platform.vehicle.vo.AssetTireFitPageVo;
import org.platform.vehicle.vo.AssetTirePageVo;
import org.platform.vehicle.vo.AssetTireStockInParam;
import org.platform.vehicle.vo.FleetWarehouseVo;
import org.platform.vehicle.vo.TireSiteSimpleVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * (AssetTire)表服务接口
 *
 * @author geforever
 * @since 2023-09-14 14:42:31
 */
public interface AssetTireService {


    /**
     * 资产管理-轮胎列表-条件查询
     *
     * @param param
     * @return
     */
    BasePageResponse<List<AssetTirePageVo>> conditionQuery(AssetTireConditionQueryParam param);

    /**
     * 资产管理-轮胎列表-轮胎入库
     *
     * @param param
     * @param request
     * @return
     */
    BaseResponse stockIn(AssetTireStockInParam param, HttpServletRequest request);

    /**
     * 资产管理-轮胎列表-获取轮胎编码(轮胎入库是获取轮胎编码用)
     *
     * @return
     */
    BaseResponse getTireCode();

    /**
     * 资产管理-轮胎列表-获取明细
     *
     * @param id
     * @return
     */
    BaseResponse<AssertTireDetailVo> getAssetTireDetail(Integer id);

    /**
     * 资产管理-轮胎列表-轮胎编辑
     *
     * @param param
     * @param request
     * @return
     */
    BaseResponse edit(AssetTireEditParam param, HttpServletRequest request);

    /**
     * 资产管理-轮胎列表-根据轮胎编号获取传感器ID
     *
     * @param tireCode
     * @return
     */
    BaseResponse getSensorId(String tireCode);

    /**
     * 资产管理-轮胎列表-传感器解绑捆绑
     *
     * @param param
     * @return
     */
    BaseResponse editSensorBind(AssetTireSensorBindParam param);

    /**
     * 资产管理-轮胎列表-轮胎出库
     *
     * @param param
     * @return
     */
    BaseResponse stockOut(AssetTireStockOutParam param, HttpServletRequest request);

    /**
     * 资产管理-轮胎列表-删除
     *
     * @param id
     * @param phone
     * @return
     */
    BaseResponse delete(Integer id, String phone, HttpServletRequest request);

    /**
     * 资产管理-轮胎拆装-车辆分页查询
     *
     * @param param
     * @return
     */
    BasePageResponse<List<AssetTireFitPageVo>> fitConditionQuery(AssetTireFitConditionQuery param);

    /**
     * 资产管理-轮胎拆装-安装轮胎
     *
     * @param param
     * @return
     */
    BaseResponse installTire(InstallTireParam param);

    /**
     * 资产管理-轮胎列表-轮胎号模糊查询
     *
     * @param tireCode
     * @return
     */
    BaseResponse<List<String>> getTireCodeLikeTireCode(String tireCode);

    /**
     * 资产管理-轮胎拆装-明细
     *
     * @param licensePlate
     * @return
     */
    BaseResponse<List<AssetTireFitDetailVo>> getFitDetail(String licensePlate);

    /**
     * 资产管理-轮胎拆装-拆卸轮胎
     *
     * @param param
     * @return
     */
    BaseResponse uninstallTire(UninstallTireParam param);

    /**
     * 资产管理-轮胎列表-导入模版下载
     *
     * @param type     :1-轮胎导入,2-轮胎安装
     * @param response
     * @retirm
     */
    void downloadTemplate(Integer type, HttpServletResponse response);

    /**
     * 资产管理-轮胎列表-批量入库
     *
     * @param file
     * @return
     */
    BaseResponse batchStockIn(MultipartFile file);

    /**
     * 资产管理-轮胎列表-批量安装
     *
     * @param file
     * @return
     */
    BaseResponse batchInstall(MultipartFile file);

    /**
     * 资产管理-轮胎列表-导出
     *
     * @param param
     * @return
     */
    void export(AssetTireConditionQueryParam param, HttpServletResponse response);

    /**
     * 资产管理-轮胎列表-选择轮位
     *
     * @param licensePlate
     * @return
     */
    BaseResponse<List<TireSiteSimpleVo>> getTireSite(String licensePlate);

    /**
     * 资产管理-轮胎列表-选择调入仓库
     *
     * @return
     */
    BaseResponse<FleetWarehouseVo> getFleetWarehouse();

    /**
     * 资产管理-轮胎列表-通过车队ID选择调入仓库
     *
     * @param fleetId
     * @return
     */
    BaseResponse<List<FleetWarehouseVo>> getWarehouseByFleetId(Integer fleetId);
}
