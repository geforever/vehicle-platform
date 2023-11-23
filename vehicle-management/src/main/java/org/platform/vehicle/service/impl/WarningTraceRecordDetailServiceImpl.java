package org.platform.vehicle.service.impl;

import org.platform.vehicle.mapper.WarningTraceRecordDetailMapper;
import org.platform.vehicle.service.WarningTraceRecordDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (WarningTraceRecordDetail)表服务实现类
 *
 * @author geforever
 * @since 2023-09-27 16:06:20
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WarningTraceRecordDetailServiceImpl implements WarningTraceRecordDetailService {

    private final WarningTraceRecordDetailMapper warningTraceRecordDetailMapper;


}
