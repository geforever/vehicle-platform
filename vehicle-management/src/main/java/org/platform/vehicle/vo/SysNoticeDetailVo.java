package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/9/6 11:28
 */
@Data
public class SysNoticeDetailVo {

    private Integer id;

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
     * 发送人ID
     */
    private Integer senderId;

    /**
     * 发送人
     */
    private String sender;

    /**
     * 接收人ID
     */
    private Integer receiverId;

    /**
     * 接收人
     */
    private String receiver;

    /**
     * 是否已读:0-未读,1-已读
     */
    private Integer isRead;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
