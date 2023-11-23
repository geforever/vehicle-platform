package org.platform.vehicle.vo;

import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/21 11:04
 */
@Data
public class ImportedDataErrVo {

    /**
     * 轮胎号
     */
    private String tireCode;

    /**
     * 错误内容
     */
    private String msg;

    public ImportedDataErrVo() {
    }

    public ImportedDataErrVo(String tireCode, String msg) {
        this.tireCode = tireCode;
        this.msg = msg;
    }
}
