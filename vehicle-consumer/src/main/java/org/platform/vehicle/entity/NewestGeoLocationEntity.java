package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/11/6 16:33
 */
@Data
@TableName("jtt808_newest_geo_location")
public class NewestGeoLocationEntity implements Serializable {

    private static final long serialVersionUID = 5726023715004375803L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
     * 速度
     */
    @TableField("speed")
    private String speed;

    /**
     * 海拔高度
     */
    @TableField("altitude")
    private String altitude;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deviceTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NewestGeoLocationEntity that = (NewestGeoLocationEntity) o;
        return Objects.equals(receiverId, that.receiverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiverId);
    }
}
