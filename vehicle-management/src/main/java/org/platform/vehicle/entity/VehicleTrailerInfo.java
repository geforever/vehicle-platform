package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.platform.vehicle.aspect.FieldName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/10/27 10:31
 */
@Data
@TableName("vehicle_trailer_info")
public class VehicleTrailerInfo implements Serializable {

    private static final long serialVersionUID = 2121662999591719287L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 主车车牌
     */
    @FieldName(value = "main_license_plate")
    private String mainLicensePlate;

    /**
     * 副车车牌
     */
    @FieldName(value = "minor_license_plate")
    private String minorLicensePlate;

    /**
     * 主车中继器ID
     */
    @FieldName(value = "main_relay_id")
    private String mainRelayId;

    /**
     * 副车中继器ID
     */
    @FieldName(value = "minor_relay_id")
    private String minorRelayId;

    /**
     * 主车挂车绑定类型:1-主车挂车,2-挂车主车
     */
    @FieldName(value = "type")
    private Integer type;


    /**
     * 更新时间
     */
    @FieldName(value = "update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
