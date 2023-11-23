package org.platform.vehicle.vo.context;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/11/2 09:10
 */
@Data
public class CustomerContextVo {

    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 公司名称
     */
    private Integer companyId;

    /**
     * 客户类型:0-面心,1-客户,2-一级车队,3-二级车队
     */
    private Integer type;
}
