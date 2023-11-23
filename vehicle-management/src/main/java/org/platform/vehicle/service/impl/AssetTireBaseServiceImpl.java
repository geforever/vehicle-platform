package org.platform.vehicle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.platform.vehicle.entity.AssetTire;
import org.platform.vehicle.mapper.AssetTireMapper;
import org.platform.vehicle.service.AssetTireBaseService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author gejiawei
 * @Date 2023/9/19 14:52
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssetTireBaseServiceImpl extends ServiceImpl<AssetTireMapper, AssetTire> implements
        AssetTireBaseService {

    private final AssetTireMapper assetTireMapper;

    /**
     * @param entityList
     * @param batchSize
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveBatch(Collection<AssetTire> entityList, int batchSize) {
        try {
            int size = entityList.size();
            int idxLimit = Math.min(batchSize, size);
            int i = 1;
            //保存单批提交的数据集合
            List<AssetTire> oneBatchList = new ArrayList<>();
            for (Iterator<AssetTire> var7 = entityList.iterator(); var7.hasNext(); ++i) {
                AssetTire element = var7.next();
                oneBatchList.add(element);
                if (i == idxLimit) {
                    assetTireMapper.insertBatchSomeColumn(oneBatchList);
                    //每次提交后需要清空集合数据
                    oneBatchList.clear();
                    idxLimit = Math.min(idxLimit + batchSize, size);
                }
            }
        } catch (Exception e) {
            log.error("saveBatch fail", e);
            return false;
        }
        return true;
    }
}
