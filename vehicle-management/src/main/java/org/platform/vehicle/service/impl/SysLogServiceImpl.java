package org.platform.vehicle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.platform.vehicle.entity.SysExportLog;
import org.platform.vehicle.entity.SysOperateLog;
import org.platform.vehicle.mapper.SysExportLogMapper;
import org.platform.vehicle.mapper.SysOperateLogMapper;
import org.platform.vehicle.param.ExportLogConditionQuery;
import org.platform.vehicle.param.OperateLogConditionQueryParam;
import org.platform.vehicle.service.SysLogService;
import org.platform.vehicle.vo.SysExportLogVo;
import org.platform.vehicle.vo.SysOperateLogVo;
import org.platform.vehicle.response.BasePageResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author gejiawei
 * @Date 2023/9/1 10:07
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysLogServiceImpl implements SysLogService {

    private final SysOperateLogMapper sysOperateLogMapper;
    private final SysExportLogMapper sysExportLogMapper;

    /**
     * 系统设置-操作日志-条件查询
     *
     * @param param
     * @return
     */
    @Override
    public BasePageResponse operateLogConditionQuery(OperateLogConditionQueryParam param) {
        Integer type = param.getType();
        if (type == null) {
            return BasePageResponse.failure();
        }
        Page<SysOperateLog> page = new Page<SysOperateLog>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<SysOperateLog> wrapper = new LambdaQueryWrapper<>();
        if (param.getEndTime() != null) {
            wrapper.le(SysOperateLog::getCreateTime, param.getEndTime());
        }
        if (param.getStartTime() != null) {
            wrapper.ge(SysOperateLog::getCreateTime, param.getStartTime());
        }
        wrapper.eq(SysOperateLog::getType, type);
        wrapper.orderByDesc(SysOperateLog::getCreateTime);
        Page<SysOperateLog> sysOperateLogPage = sysOperateLogMapper.selectPage(page, wrapper);
        List<SysOperateLog> sysOperateLogList = sysOperateLogPage.getRecords();
        List<SysOperateLogVo> sysOperateLogVoList = new ArrayList<>();
        for (SysOperateLog sysOperateLog : sysOperateLogList) {
            SysOperateLogVo sysOperateLogVo = new SysOperateLogVo();
            sysOperateLogVo.setId(sysOperateLog.getId());
            sysOperateLogVo.setMenuName(sysOperateLog.getModule());
            sysOperateLogVo.setOperation(sysOperateLog.getOperation());
            sysOperateLogVo.setOperatePerson(sysOperateLog.getCreatePerson());
            sysOperateLogVo.setCreateTime(sysOperateLog.getCreateTime());
            sysOperateLogVo.setMessage(sysOperateLog.getMessage());
            sysOperateLogVoList.add(sysOperateLogVo);
        }
        return BasePageResponse.ok(sysOperateLogVoList, page);
    }

    /**
     * 系统设置-操作日志-条件查询(导出日志)
     *
     * @param param
     * @return
     */
    @Override
    public BasePageResponse<List<SysExportLogVo>> exportLogConditionQuery(
            ExportLogConditionQuery param) {
        Page<SysExportLog> page = new Page<SysExportLog>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<SysExportLog> wrapper = new LambdaQueryWrapper<>();
        if (param.getEndTime() != null) {
            wrapper.le(SysExportLog::getCreateTime, param.getEndTime());
        }
        if (param.getStartTime() != null) {
            wrapper.ge(SysExportLog::getCreateTime, param.getStartTime());
        }
        wrapper.orderByDesc(SysExportLog::getCreateTime);
        Page<SysExportLog> sysOperateLogPage = sysExportLogMapper.selectPage(page, wrapper);
        List<SysExportLog> sysExportLogList = sysOperateLogPage.getRecords();
        List<SysExportLogVo> sysExportLogVoList = new ArrayList<>();
        for (SysExportLog sysExportLog : sysExportLogList) {
            SysExportLogVo sysExportLogVo = new SysExportLogVo();
            sysExportLogVo.setId(sysExportLog.getId());
            sysExportLogVo.setMenu(sysExportLog.getMenu());
            sysExportLogVo.setOperation(sysExportLog.getOperation());
            sysExportLogVo.setCount(sysExportLog.getCount());
            sysExportLogVo.setCreatePerson(sysExportLog.getCreatePerson());
            sysExportLogVo.setCreateTime(sysExportLog.getCreateTime());
            sysExportLogVoList.add(sysExportLogVo);
        }
        return BasePageResponse.ok(sysExportLogVoList, page);
    }
}
