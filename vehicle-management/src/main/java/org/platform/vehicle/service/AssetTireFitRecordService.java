package org.platform.vehicle.service;

import org.platform.vehicle.param.AssetTireFitRecordConditionQueryParam;
import org.platform.vehicle.response.BasePageResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * (AssetTireFitRecord)表服务接口
 *
 * @author geforever
 * @since 2023-09-19 15:26:22
 */
public interface AssetTireFitRecordService {

    /**
     * 资产管理-轮胎装卸记录-条件查询
     *
     * @param param
     * @return
     */
    BasePageResponse conditionQuery(AssetTireFitRecordConditionQueryParam param);

    /**
     * 资产管理-轮胎装卸记录-导出
     *
     * @param param
     */
    void export(AssetTireFitRecordConditionQueryParam param, HttpServletResponse response);
}
