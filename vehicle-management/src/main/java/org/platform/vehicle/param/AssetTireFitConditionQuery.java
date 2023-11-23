package org.platform.vehicle.param;

import org.platform.vehicle.response.PageParam;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author gejiawei
 * @Date 2023/9/18 11:52
 */
@Setter
@Getter
public class AssetTireFitConditionQuery extends PageParam {

    /**
     * 车辆归属
     */
    private Integer customerId;

    /**
     * 车辆归属批量查询
     */
    private List<Integer> customerIds;

    /**
     * 车牌号
     */
    private String licensePlate;

    /**
     * 绑定状态:1.车轮已绑定传感器, 2.车辆未设置轮位, 3.存在车轮未绑传感器, 4.存在轮位未装车轮
     */
    private Integer bindType;
}
