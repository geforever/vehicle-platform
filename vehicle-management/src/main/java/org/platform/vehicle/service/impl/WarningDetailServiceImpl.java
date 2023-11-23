package org.platform.vehicle.service.impl;

import org.platform.vehicle.mapper.WarningDetailMapper;
import org.platform.vehicle.service.WarningDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (WarningDetail)表服务实现类
 *
 * @author geforever
 * @since 2023-09-27 16:05:00
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WarningDetailServiceImpl implements WarningDetailService {

    private final WarningDetailMapper warningDetailMapper;

}
