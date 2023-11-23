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
 * sys_operate_log
 *
 * @author
 */
@Data
@TableName("sys_operate_log")
public class SysOperateLog implements Serializable {

    private static final long serialVersionUID = 2374645851123203035L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 操作路径
     */
    @TableField("url")
    private String url;

    /**
     * 编号:其他表主键ID
     */
    @TableField("code")
    private String code;

    /**
     * 请求方式
     */
    @TableField("method")
    private String method;

    /**
     * 模块
     */
    @TableField("module")
    private String module;

    /**
     * 操作
     */
    @TableField("operation")
    private String operation;

    /**
     * 前值
     */
    @TableField("old_value")
    private String oldValue;

    /**
     * 后值
     */
    @TableField("new_value")
    private String newValue;

    /**
     * 操作内容
     */
    @TableField("message")
    private String message;

    /**
     * 客户类型:0-面心, 1-客户, 2-一级车队, 3-二级车队
     */
    @TableField("source")
    private Integer source;

    /**
     * 类型:1-新增,2-修改,3-删除
     */
    @TableField("type")
    private Integer type;

    /**
     * 创建人
     */
    @TableField("create_person")
    private String createPerson;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
