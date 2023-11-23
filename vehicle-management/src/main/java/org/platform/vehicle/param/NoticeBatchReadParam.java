package org.platform.vehicle.param;

import java.util.List;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/30 10:30
 */
@Data
public class NoticeBatchReadParam {

    /**
     * 主键ID
     */
    private List<Integer> idList;
}
