package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/8/30 10:40
 */
@Data
public class SysFleetVo {

    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 车队名称
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
     * 所属客户ID
     */
    private Integer parentClientId;

    /**
     * 所属客户名称
     */
    private String parentClientName;

    /**
     * 所属车队ID
     */
    private Integer parentFleetId;

    /**
     * 所属车队名称
     */
    private String parentFleetName;

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
     * 更新人
     */
    private String updatePerson;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
