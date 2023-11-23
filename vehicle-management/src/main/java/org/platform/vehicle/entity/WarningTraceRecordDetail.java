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
 * (WarningTraceRecordDetail)实体类
 *
 * @author geforever
 * @since 2023-09-27 16:06:20
 */
@Data
@TableName("warning_trace_record_detail")
public class WarningTraceRecordDetail implements Serializable {

    private static final long serialVersionUID = 374824167630636135L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 跟进记录主键
     */
    @TableField("trace_record_id")
    private Integer traceRecordId;

    /**
     * 司机名称
     */
    @TableField("driver_name")
    private String driverName;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 原因
     */
    @TableField("reason")
    private String reason;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 跟进人
     */
    @TableField("follow_name")
    private String followName;

    /**
     * 跟进人ID
     */
    @TableField("follow_name_id")
    private Integer followNameId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}

