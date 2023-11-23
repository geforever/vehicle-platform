package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.platform.vehicle.aspect.FieldName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * sys_role
 *
 * @author
 */
@Data
@TableName("sys_role")
public class SysRole implements Serializable {

    private static final long serialVersionUID = -2411545130713870938L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 客户ID
     */
    @TableField("customer_id")
    @FieldName(value = "角色归属")
    private Integer customerId;

    /**
     * 角色名
     */
    @TableField("name")
    @FieldName(value = "角色名")
    private String name;

    /**
     * 角色名称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 角色等级,1-默认角色,2-可维护角色
     */
    @TableField("level")
    private Integer level;

    /**
     * 描述
     */
    @TableField("description")
    @FieldName(value = "角色描述")
    private String description;

    /**
     * 禁用状态：1-启用，0-禁用
     */
    @TableField("status")
    @FieldName(value = "角色状态")
    private Integer status;

    /**
     * 删除状态：1-删除，0-未删除
     */
    @TableField("is_delete")
    @FieldName(value = "删除状态")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

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
     * 前端缓存
     */
    @TableField("temp")
    private String temp;

}
