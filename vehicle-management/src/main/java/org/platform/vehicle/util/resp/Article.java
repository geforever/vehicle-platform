package org.platform.vehicle.util.resp;

import lombok.Data;

/**
 * 图文model
 *
 * @author wangchengcheng
 * @date 2022/1/6 - 9:44
 */
@Data
public class Article {

    // 图文消息名称
    private String Title;
    // 图文消息描述
    private String Description;
    // 图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80
    private String PicUrl;
    // 点击图文消息跳转链接
    private String Url;
}
