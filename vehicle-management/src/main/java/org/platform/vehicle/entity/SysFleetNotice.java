package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * sys_fleet_notice
 *
 * @author
 */
@Data
@TableName("sys_fleet_notice")
public class SysFleetNotice implements Serializable {

    private static final long serialVersionUID = 6358603751165587580L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 通知设置ID
     */
    @TableField("notice_config_id")
    private Integer noticeConfigId;

    /**
     * 车队ID
     */
    @TableField("customer_id")
    private Integer customerId;

    /**
     * 提醒类型:1-短信,2-微信,3-短信和微信
     */
    @TableField("remind_type")
    private Integer remindType;

    /**
     * 接收人设置
     */
    @TableField("received_account")
    private String receivedAccount;

}
