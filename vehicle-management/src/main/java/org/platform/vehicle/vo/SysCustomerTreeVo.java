package org.platform.vehicle.vo;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/31 11:41
 */

@Data
public class SysCustomerTreeVo {

    /**
     * 客户id
     */
    private Integer id;

    /**
     * 上级客户id
     */
    private Integer parentId;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 客户类型: 1-客户，2-一级车队, 3-二级车队
     */
    private Integer type;

    /**
     * 子客户
     */
    private List<SysCustomerTreeVo> children = new ArrayList<>();

}
