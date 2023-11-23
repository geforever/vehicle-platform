package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/8/30 14:45
 */
@Data
public class SysNoticeConfigVo {

    private Integer id;

    /**
     * 通知名称
     */
    private String name;

    /**
     * 通知模版
     */
    private String model;

    /**
     * 通知类型:1-温压报警,2-维保状态变更
     */
    private Integer type;

    /**
     * 重要等级:1-一般,2-重要,3-非常重要
     */
    private Integer level;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 更新人
     */
    private String updatePerson;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
