package org.platform.vehicle.util;

import org.platform.vehicle.entity.SysExportLog;
import org.platform.vehicle.mapper.SysExportLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/9/6 10:47
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ExportLogAsync {

    private final SysExportLogMapper sysExportLogMapper;

    /**
     * 保存导出日志
     *
     * @param menu         菜单名
     * @param operation    列表名
     * @param count        导出数据条数
     * @param createPerson 操作人
     */
    @Async("asyncTaskExecutor")
    public void pushExportLog(String menu, String operation, Integer count, String createPerson) {
        SysExportLog sysExportLog = new SysExportLog();
        sysExportLog.setMenu(menu);
        sysExportLog.setOperation(operation);
        sysExportLog.setCount(count);
        sysExportLog.setCreatePerson(createPerson);
        sysExportLogMapper.insert(sysExportLog);
    }
}
