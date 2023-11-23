package org.platform.vehicle.util;

import org.platform.vehicle.entity.AssetTireStockRecord;
import org.platform.vehicle.helper.jdbc.AssetTireJdbc;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2021/9/24 4:43 下午
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssetTireStockRecordAsync {

    private final AssetTireJdbc assetTireJdbc;

    @Async("jdbcTaskExecutor")
    public void batchSave(List<AssetTireStockRecord> insertStockRecordParamList) {
        long startTime = System.currentTimeMillis();
        try {
            assetTireJdbc.saveBatchTireStockRecord(insertStockRecordParamList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();
        log.info("轮胎出入库记录数据批量保存耗时:{}", endTime - startTime);
    }
}
