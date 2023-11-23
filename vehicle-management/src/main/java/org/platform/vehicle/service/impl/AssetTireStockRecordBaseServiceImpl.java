package org.platform.vehicle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.platform.vehicle.entity.AssetTireStockRecord;
import org.platform.vehicle.mapper.AssetTireStockRecordMapper;
import org.platform.vehicle.service.AssetTireStockRecordBaseService;
import org.springframework.stereotype.Service;

/**
 * @Author gejiawei
 * @Date 2023/9/19 14:52
 */
@Service
public class AssetTireStockRecordBaseServiceImpl extends
        ServiceImpl<AssetTireStockRecordMapper, AssetTireStockRecord> implements
        AssetTireStockRecordBaseService {

}
