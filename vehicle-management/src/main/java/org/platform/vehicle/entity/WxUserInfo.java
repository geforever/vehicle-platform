package org.platform.vehicle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * wx_user_info
 *
 * @author
 */
@Data
@TableName("wx_user_info")
public class WxUserInfo implements Serializable {

    private static final long serialVersionUID = -2468296935399263224L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户主键
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 头像地址
     */
    @TableField("avatar_url")
    private String avatarUrl;

    /**
     * 用户昵称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * openid
     */
    @TableField("open_id")
    private String openId;

    /**
     * 公众号openId
     */
    @TableField("official_account_id")
    private String officialAccountId;

    /**
     * union_id
     */
    @TableField("union_id")
    private String unionId;

    /**
     * 省
     */
    @TableField("province")
    private String province;

    /**
     * 市
     */
    @TableField("city")
    private String city;

    /**
     * 区/县
     */
    @TableField("country")
    private String country;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

}
