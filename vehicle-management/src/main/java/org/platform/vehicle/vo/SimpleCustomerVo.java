package org.platform.vehicle.vo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author Sunnykid
 * @company:上海面心科技
 * @project:路安车辆维管系统
 * @module:后台数据管理
 * @version:V1.0
 * @date 2023年9月13日 下午5:37:43
 * @description: 简单的客户信息封装
 */
@Data
public class SimpleCustomerVo implements Serializable {

    private static final long serialVersionUID = -2387458730604179812L;

    /**
     * 名称
     */
    protected Integer id;

    /**
     * 名称
     */
    protected String name;

    public SimpleCustomerVo(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }
}
