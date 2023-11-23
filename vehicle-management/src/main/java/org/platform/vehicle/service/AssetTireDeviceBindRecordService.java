package org.platform.vehicle.service;

import org.platform.vehicle.param.AssetTireDeviceBindRecordConditionQueryParam;
import org.platform.vehicle.param.TireDeviceBindRecordAddParam;
import org.platform.vehicle.vo.AssetTireDeviceBindRecordPageVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import java.util.List;

/**
 * (AssetTireDeviceBindRecord)表服务接口
 *
 * @author geforever
 * @since 2023-09-15 15:40:29
 */
public interface AssetTireDeviceBindRecordService {

    /**
     * 资产管理-胎压部件绑定记录-新增
     *
     * @param tireDeviceBindRecordAddParam
     */
    BaseResponse save(TireDeviceBindRecordAddParam tireDeviceBindRecordAddParam);

    /**
     * 资产管理-胎压部件绑定记录-条件查询
     *
     * @Param param
     * @Return
     */
    BasePageResponse<List<AssetTireDeviceBindRecordPageVo>> conditionQuery(
            AssetTireDeviceBindRecordConditionQueryParam param);
}
