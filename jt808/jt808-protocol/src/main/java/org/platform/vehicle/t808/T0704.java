package org.platform.vehicle.t808;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Message;
import java.util.List;
import org.platform.vehicle.basics.JTMessage;
import org.platform.vehicle.commons.JT808;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@Message(JT808.定位数据批量上传)
public class T0704 extends JTMessage {

    @Field(length = 2, desc = "数据项个数")
    private int total;
    @Field(length = 1, desc = "位置数据类型：0.正常位置批量汇报 1.盲区补报")
    private int type;
    @Field(lengthUnit = 2, desc = "位置汇报数据项")
    private List<T0200> items;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<T0200> getItems() {
        return items;
    }

    public void setItems(List<T0200> items) {
        this.items = items;
        this.total = items.size();
    }
}
