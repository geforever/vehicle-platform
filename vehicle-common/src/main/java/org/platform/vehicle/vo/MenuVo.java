package org.platform.vehicle.vo;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import lombok.Data;

/**
 * @Author gejiawei
 * @Date 2023/8/25 11:13
 */
@Data
public class MenuVo implements Comparable<MenuVo> {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 父级id
     */
    private Integer parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单编码
     */
    private String code;

    /**
     * 类型为页面时，代表前端路由地址，类型为按钮时，代表后端接口地址
     */
    private String url;

    /**
     * 权限类型，页面-1，按钮-2
     */
    private Integer type;

    /**
     * 排序
     */
    private Integer sort;

    private Set<MenuVo> children = new TreeSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(MenuVo sysMenu) {
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
        MenuVo that = (MenuVo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(url, that.url);
    }

}
