package org.platform.vehicle.service;

import org.platform.vehicle.param.ExportLogConditionQuery;
import org.platform.vehicle.param.OperateLogConditionQueryParam;
import org.platform.vehicle.vo.SysExportLogVo;
import org.platform.vehicle.vo.SysOperateLogVo;
import org.platform.vehicle.response.BasePageResponse;
import java.util.List;

/**
 * @Author gejiawei
 * @Date 2023/9/1 10:06
 */
public interface SysLogService {

    /**
     * 系统设置-操作日志-条件查询
     *
     * @param param
     * @return
     */
    BasePageResponse<List<SysOperateLogVo>> operateLogConditionQuery(
            OperateLogConditionQueryParam param);

    /**
     * 系统设置-操作日志-条件查询(导出日志)
     *
     * @param param
     * @return
     */
    BasePageResponse<List<SysExportLogVo>> exportLogConditionQuery(ExportLogConditionQuery param);
}
