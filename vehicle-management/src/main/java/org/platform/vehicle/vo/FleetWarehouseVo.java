package org.platform.vehicle.vo;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/25 10:37
 */
@Data
public class FleetWarehouseVo {

    private Integer id;

    /**
     * 客户/车队名称
     */
    private String name;


    private Integer parentId;

    /**
     * 客户类型:0-面心,1-客户,2-一级车队,3-二级车队
     */
    private Integer type;

    /**
     * 仓库名称
     */
    private List<WarehouseSimpleVo> warehouseSimpleList;

    /**
     * 下级车队仓库
     */
    private List<FleetWarehouseVo> children = new ArrayList<>();

}
