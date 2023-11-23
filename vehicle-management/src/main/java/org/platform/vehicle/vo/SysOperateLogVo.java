package org.platform.vehicle.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author gejiawei
 * @Date 2023/9/1 10:22
 */
@Data
public class SysOperateLogVo {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 菜单名
     */
    private String menuName;

    /**
     * 列表名
     */
    private String operation;

    /**
     * 操作人
     */
    private String operatePerson;

    /**
     * 操作时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 操作内容(数据行标识)
     */
    private String message;
}
