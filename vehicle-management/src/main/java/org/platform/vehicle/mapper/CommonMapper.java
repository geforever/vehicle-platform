package org.platform.vehicle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @Author gejiawei
 * @Date 2023/9/22 17:41
 */
@Repository
public interface CommonMapper<T> extends BaseMapper<T> {

    /**
     * 全量插入,等价于insert
     *
     * @param entityList
     * @return
     */
    int insertBatchSomeColumn(List<T> entityList);
}
