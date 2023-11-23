package org.platform.vehicle.param;

import org.platform.vehicle.response.PageParam;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author gejiawei
 * @Date 2023/8/23 17:16
 */

@Getter
@Setter
public class SysUserConditionQueryParam extends PageParam {

    /**
     * 账号
     */
    private String account;

    /**
     * 姓名
     */
    private String name;

    /**
     * 客户ID
     */
    private List<Integer> customerIdList;

}
