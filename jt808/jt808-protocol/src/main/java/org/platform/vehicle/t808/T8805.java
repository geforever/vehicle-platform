package org.platform.vehicle.t808;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Message;
import org.platform.vehicle.basics.JTMessage;
import org.platform.vehicle.commons.JT808;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@Message(JT808.单条存储多媒体数据检索上传命令)
public class T8805 extends JTMessage {

    @Field(length = 4, desc = "多媒体ID(大于0)")
    private int id;
    @Field(length = 1, desc = "删除标志：0.保留 1.删除 ")
    private int delete;

    public T8805() {
    }

    public T8805(int id, int delete) {
        this.id = id;
        this.delete = delete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDelete() {
        return delete;
    }

    public void setDelete(int delete) {
        this.delete = delete;
    }
}
