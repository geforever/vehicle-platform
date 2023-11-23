package org.platform.vehicle.vo;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/19 16:39
 */
@Data
public class UnhandLedWarningCountVo {

    /**
     * 紧急报警数量
     */
    private Long urgentWarningCount;

    /**
     * 常规类型报警数量
     */
    private Long commonWarningCount;

}
