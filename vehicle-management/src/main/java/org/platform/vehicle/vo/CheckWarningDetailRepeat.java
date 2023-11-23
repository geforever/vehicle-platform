package org.platform.vehicle.vo;

import org.platform.vehicle.constant.WarningTypeEnum;
import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/26 11:17
 */
@Data
public class CheckWarningDetailRepeat {

    /**
     * 新告警类型
     */
    private List<WarningTypeEnum> newWarningTypeList;

    /**
     * 旧告警类型
     */
    private List<WarningTypeEnum> oldWarningTypeList;

}
