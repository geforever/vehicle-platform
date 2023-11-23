package org.platform.vehicle.param;

import org.platform.vehicle.response.PageParam;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author gejiawei
 * @Date 2023/9/6 11:18
 */
@Getter
@Setter
public class SysNoticeConditionQueryParam extends PageParam {

    /**
     * 标题
     */
    private String title;

}
