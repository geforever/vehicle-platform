package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
@TableName("jtt808_geo_location")
public class GeoLocation implements Serializable {

    private static final long serialVersionUID = 9189492316938921152L;

    /**
     * 流水ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 记录创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 接收器ID
     */
    @TableField("receiver_id")
    private String receiverId;

    /**
     * 消息ID
     */
    @TableField("msg_id")
    private String msgId;

    /**
     * 经度
     */
    @TableField("lng")
    private String lng;

    /**
     * 纬度
     */
    @TableField("lat")
    private String lat;

    /**
     * 海拔高度
     */
    @TableField("altitude")
    private String altitude;

    /**
     * 速度
     */
    @TableField("speed")
    private String speed;

    /**
     * 报警标志
     */
    @TableField("warn_bit")
    private Integer warnBit;

    /**
     * 状态
     */
    @TableField("status_bit")
    private Integer statusBit;

    /**
     * 设备时间
     */
    @TableField("device_time")
    private Date deviceTime;

}
