package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author gejiawei
 * @Date 2023/8/28 16:11
 */
@Data
public class SysClientVo {

    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 名称
     */
    private String name;

    /**
     * 状态:0-停用,1-启用
     */
    private Integer status;

    /**
     * 公司ID
     */
    private Integer companyId;

    /**
     * 管理员ID
     */
    private Integer adminUserId;

    /**
     * 管理员账号
     */
    private String adminUserAccount;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人
     */
    private String contactPhone;

    /**
     * 客户类型:0-面心,1-客户,2-一级车队,3-二级车队
     */
    private Integer type;

    /**
     * 最大车辆数
     */
    private Integer maxVehicle;

    /**
     * 系统有效期限
     */
    private String invalidTime;

    /**
     * 更新人
     */
    private String updatePerson;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 是否删除:0-未删除，1-已删除
     */
    private Integer isDelete;

    /**
     * 父ID
     */
    private Integer parentId;
}
