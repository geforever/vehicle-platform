package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * sys_notice_config
 *
 * @author
 */
@Data
@TableName("sys_notice_config")
public class SysNoticeConfig implements Serializable {

    private static final long serialVersionUID = 7422559628084261054L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 通知名称
     */
    @TableField("name")
    private String name;

    /**
     * 通知模版
     */
    @TableField("model")
    private String model;

    /**
     * 通知类型:1-温压报警,2-维保状态变更
     */
    @TableField("type")
    private Integer type;

    /**
     * 重要等级:1-一般,2-重要,3-非常重要
     */
    @TableField("level")
    private Integer level;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 创建人
     */
    @TableField("create_person")
    private String createPerson;

    /**
     * 更新人
     */
    @TableField("update_person")
    private String updatePerson;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;
}
