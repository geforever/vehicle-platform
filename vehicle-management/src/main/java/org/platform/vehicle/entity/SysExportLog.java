package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.platform.vehicle.aspect.FieldName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * sys_export_log
 *
 * @author
 */
@Data
@TableName("sys_export_log")
public class SysExportLog implements Serializable {

    private static final long serialVersionUID = -3589061872815183361L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单名称
     */
    @FieldName(value = "菜单名称")
    private String menu;

    /**
     * 操作名称
     */
    @FieldName(value = "操作名称")
    private String operation;

    /**
     * 导出条数
     */
    @FieldName(value = "导出条数")
    private Integer count;

    /**
     * 操作人
     */
    @FieldName(value = "操作人")
    private String createPerson;

    /**
     * 操作时间
     */
    @FieldName(value = "操作时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
