package org.platform.vehicle.util.resp;

import lombok.Data;

/**
 * @author wangchengcheng
 * @date 2022/1/6 - 9:56
 */
@Data
public class VideoMessage extends BaseMessage {

    // 视频
    private Video Video;
}
