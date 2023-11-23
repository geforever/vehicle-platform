package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author gejiawei
 * @Date 2023/8/25 09:19
 */
@Data
public class SysRoleVo {

    private Integer id;

    /**
     * 客户ID
     */
    private Integer customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色名称
     */
    private String nickname;

    /**
     * 角色等级,1-默认角色,2-可维护角色
     */
    private Integer level;

    /**
     * 描述
     */
    private String description;

    /**
     * 禁用状态：1-启用，0-禁用
     */
    private Integer status;

    /**
     * 删除状态：1-删除，0-未删除
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 更新人
     */
    private String updatePerson;

    /**
     * 前端缓存
     */
    private String temp;
}
