package org.platform.vehicle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.platform.vehicle.dto.WarmPressingDto;
import org.platform.vehicle.dto.WarningDetailDto;
import org.platform.vehicle.entity.WarningDetail;
import org.platform.vehicle.param.WarmPressingConditionQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author gejiawei
 * @Date 2023/9/27 16:20
 */
@Repository
public interface WarningDetailMapper extends BaseMapper<WarningDetail> {

    Page<WarmPressingDto> conditionQuery(Page<WarmPressingDto> page,
            WarmPressingConditionQuery param);

    List<WarningDetailDto> selectLatestWarnings(
            @Param("licensePlate") String licensePlate,
            @Param("tireSiteList") List<Integer> tireSiteList
    );
}
