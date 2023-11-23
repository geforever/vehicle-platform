package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * sys_notice
 *
 * @author
 */
@Data
@TableName("sys_notice")
public class SysNotice implements Serializable {

    private static final long serialVersionUID = -8832321413258501428L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 消息类型主键ID
     */
    @TableField("target_id")
    private String targetId;

    /**
     * 消息类型:1-告警,2-维保
     */
    @TableField("type")
    private Integer type;

    /**
     * 主标题
     */
    @TableField("main_title")
    private String mainTitle;

    /**
     * 副标题
     */
    @TableField("second_title")
    private String secondTitle;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 发送人ID,1-系统
     */
    @TableField("sender_id")
    private Integer senderId;

    /**
     * 发送人
     */
    @TableField("sender")
    private String sender;

    /**
     * 接收人ID
     */
    @TableField("receiver_id")
    private Integer receiverId;

    /**
     * 接收人
     */
    @TableField("receiver")
    private String receiver;

    /**
     * 是否已读:0-未读,1-已读
     */
    @TableField("is_read")
    private Integer isRead;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
