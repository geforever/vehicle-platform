package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/9/6 10:43
 */
@Data
public class SysExportLogVo {

    private Integer id;

    /**
     * 菜单名称
     */
    private String menu;

    /**
     * 列表名
     */
    private String operation;

    /**
     * 导出条数
     */
    private Integer count;

    /**
     * 操作人
     */
    private String createPerson;

    /**
     * 操作时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
