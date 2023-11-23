package org.platform.vehicle.service;

import org.platform.vehicle.param.WarehouseSettingAddParam;
import org.platform.vehicle.param.WarehouseSettingConditionQueryParam;
import org.platform.vehicle.param.WarehouseSettingEditParam;
import org.platform.vehicle.vo.WarehouseSettingVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * (WarehouseSetting)表服务接口
 *
 * @author geforever
 * @since 2023-09-13 10:20:17
 */
public interface WarehouseSettingService {

    /**
     * 资产管理-仓库管理-条件查询
     *
     * @param param
     * @return
     */
    BasePageResponse<List<WarehouseSettingVo>> conditionQuery(
            WarehouseSettingConditionQueryParam param);

    /**
     * 资产管理-仓库管理-新增
     *
     * @param param
     * @param request
     * @return
     */
    BaseResponse add(WarehouseSettingAddParam param, HttpServletRequest request);

    /**
     * 资产管理-仓库管理-修改
     *
     * @param param
     * @param request
     * @return
     */
    BaseResponse edit(WarehouseSettingEditParam param, HttpServletRequest request);

    /**
     * 资产管理-仓库管理-删除
     *
     * @param id
     * @param phone
     * @return
     */
    BaseResponse delete(Integer id, String phone, HttpServletRequest request);

    /**
     * 资产管理-仓库管理-查看仓库详情
     *
     * @param id
     * @return
     */
    BaseResponse<WarehouseSettingVo> getWarehouseDetail(Integer id);
}
