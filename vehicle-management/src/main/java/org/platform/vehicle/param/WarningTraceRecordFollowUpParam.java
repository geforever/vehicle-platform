package org.platform.vehicle.param;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/9 10:44
 */
@Data
public class WarningTraceRecordFollowUpParam {

    /**
     * 跟进记录主键ID
     */
    private Integer id;

    /**
     * 跟进记录主键
     */
    private Integer traceRecordId;

    /**
     * 司机名称
     */
    private String driverName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 原因
     */
    private String reason;

    /**
     * 备注
     */
    private String remark;


}
