package org.platform.vehicle.service;

import org.platform.vehicle.param.AssetTireStockRecordConditionQueryParam;
import org.platform.vehicle.vo.AssetTireStockRecordPageVo;
import org.platform.vehicle.response.BasePageResponse;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author gejiawei
 * @Date 2023/9/15 17:19
 */
public interface AssetTireStockRecordService {

    /**
     * 资产管理-轮胎出入库记录-分页查询
     *
     * @param param
     * @return
     */
    BasePageResponse<List<AssetTireStockRecordPageVo>> conditionQuery(
            AssetTireStockRecordConditionQueryParam param);

    /**
     * 资产管理-轮胎出入库记录-导出
     *
     * @param param
     * @return
     */
    void stockInExport(AssetTireStockRecordConditionQueryParam param, HttpServletResponse response);

    /**
     * 资产管理-轮胎出库记录-导出
     *
     * @param param
     * @return
     */
    void stockOutExport(AssetTireStockRecordConditionQueryParam param,
            HttpServletResponse response);
}
