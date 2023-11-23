package org.platform.vehicle.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.platform.vehicle.param.ExportLogConditionQuery;
import org.platform.vehicle.param.OperateLogConditionQueryParam;
import org.platform.vehicle.service.SysLogService;
import org.platform.vehicle.vo.SysExportLogVo;
import org.platform.vehicle.vo.SysOperateLogVo;
import org.platform.vehicle.response.BasePageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统设置-操作日志
 *
 * @Author gejiawei
 * @Date 2023/9/1 10:04
 */
@Slf4j
@RestController
@RequestMapping("/log")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysLogController {

    private final SysLogService sysLogService;

    /**
     * 系统设置-操作日志-条件查询(编辑日志,删除日志)
     *
     * @param param
     * @return
     */
    @PostMapping("/operateLog/conditionQuery")
    public BasePageResponse<List<SysOperateLogVo>> operateLogConditionQuery(
            @RequestBody OperateLogConditionQueryParam param) {
        log.info("系统设置-操作日志-条件查询(编辑日志,删除日志), url:/operateLog/conditionQuery");
        return sysLogService.operateLogConditionQuery(param);
    }

    /**
     * 系统设置-操作日志-条件查询(导出日志)
     *
     * @param param
     * @return
     */
    @PostMapping("/exportLog/conditionQuery")
    public BasePageResponse<List<SysExportLogVo>> exportLogConditionQuery(
            @RequestBody ExportLogConditionQuery param) {
        log.info("系统设置-操作日志-条件查询(导出日志), url:/exportLog/conditionQuery");
        return sysLogService.exportLogConditionQuery(param);
    }
}
