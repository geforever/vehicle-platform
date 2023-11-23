package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/10/30 10:23
 */

@Data
public class NoticeSimpleVo {

    private Integer id;

    /**
     * 消息类型主键ID
     */
    private String targetId;

    /**
     * 主标题
     */
    private String mainTitle;

    /**
     * 副标题
     */
    private String secondTitle;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
