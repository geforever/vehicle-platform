package org.platform.vehicle.vo.amap;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/11 16:34
 */
@Data
public class AmapBaseVo {

    /**
     * 返回状态:1：成功；0：失败
     */
    private String status;

    /**
     * 返回的状态信息
     */
    private String info;

    /**
     * 返回状态说明,10000代表正确
     */
    private String infocode;
}
