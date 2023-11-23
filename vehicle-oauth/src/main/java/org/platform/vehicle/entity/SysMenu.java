package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 权限表(SysMenu)实体类
 *
 * @author gejiawei
 */

@Data
@TableName("sys_menu")
public class SysMenu implements Serializable, Comparable<SysMenu> {

    private static final long serialVersionUID = -3435549319502191535L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父级id
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 菜单名称
     */
    @TableField("name")
    private String name;

    @TableField("code")
    private String code;

    /**
     * 类型为页面时，代表前端路由地址，类型为按钮时，代表后端接口地址
     */
    @TableField("url")
    private String url;

    /**
     * 权限类型，页面-1，按钮-2
     */
    @TableField("type")
    private Integer type;

    /**
     * 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 状态：1-启用，0-禁用
     */
    @TableField("status")
    private Integer status;

    /**
     * 删除状态：1-删除，0-未删除
     */
    @TableField("is_delete")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
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

    @TableField(exist = false)
    private Set<SysMenu> children = new TreeSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(SysMenu sysMenu) {
        //根据sort排序
        if (!this.sort.equals(sysMenu.sort)) {
            return Integer.compare(this.sort, sysMenu.sort);
        } else {
            return Integer.compare(this.id, sysMenu.id);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SysMenu that = (SysMenu) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(url, that.url);
    }
}

