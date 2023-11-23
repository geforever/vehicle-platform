package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/10/25 12:05
 */
@Data
@TableName("warning_remark_template")
public class WarningRemarkTemplate implements Serializable {

    private static final long serialVersionUID = 1241291096515603728L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 客户ID:0-默认模版
     */
    @TableField("client_id")
    private Integer clientId;

    /**
     * 告警类型:1-一级高压报警,2-一级高温报警,3-一级低压报警,4-二级高压报警,5-二级高温报警,6-二级低压报警,7-低电压报警,8-急漏气报警,9-慢漏气报警,10-无信号报警
     */
    @TableField("type")
    private Integer type;

    /**
     * 故障原因
     */
    @TableField("reason")
    private String reason;
}
