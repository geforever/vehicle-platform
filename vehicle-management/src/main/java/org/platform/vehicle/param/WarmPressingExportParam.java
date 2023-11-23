package org.platform.vehicle.param;

import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/11/3 10:56
 */
@Data
public class WarmPressingExportParam {

    /**
     * 开始时间(yyyy-MM-dd HH:mm:ss)
     *
     * @required
     */
    private String startTime;

    /**
     * 结束时间(yyyy-MM-dd HH:mm:ss)
     *
     * @required
     */
    private String endTime;

    /**
     * 是否导出速度海拔数据:0-否, 1-是
     */
    private Integer isExportSpeedAltitude;

    /**
     * 车牌号列表(最多10个车辆)
     *
     * @required
     */
    private List<String> licensePlateList;
}
