package org.platform.vehicle.util.resp;

import java.util.List;
import lombok.Data;

/**
 * 图文消息
 *
 * @author wangchengcheng
 * @date 2022/1/6 - 9:45
 */
@Data
public class NewsMessage extends BaseMessage {

    // 图文消息个数，限制为10条以内
    private int ArticleCount;
    // 多条图文消息信息，默认第一个item为大图
    private List<Article> Articles;
}
