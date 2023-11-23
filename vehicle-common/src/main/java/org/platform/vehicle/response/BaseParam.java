package org.platform.vehicle.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BaseParam extends PageParam {

    private static final long serialVersionUID = 2882682028441007909L;

    /**
     * 导出时包含的ID
     */
    protected Long[] exportInc;

    /**
     * 导出时排除的ID
     */
    protected Long[] exportExc;

    /**
     * 排序字段名
     */
    private String sortBy;

    /**
     * 排序方式：正序为ASC，逆序为DESC
     */
    private String sortType;
}
