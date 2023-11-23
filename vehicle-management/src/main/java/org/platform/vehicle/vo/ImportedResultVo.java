package org.platform.vehicle.vo;

import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/9/21 11:05
 */
@Data
public class ImportedResultVo {

    /**
     * 导入成功条数
     */
    private Integer completeCount;

    /**
     * 重复条数
     */
    private Integer repeatCount;

    /**
     * 错误条数
     */
    private Integer errorCount;

    /**
     * 错误列表
     */
    private List<ImportedDataErrVo> errorList;
}
