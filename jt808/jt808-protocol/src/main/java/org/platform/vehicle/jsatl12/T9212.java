package org.platform.vehicle.jsatl12;

import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Message;
import org.platform.vehicle.basics.JTMessage;
import org.platform.vehicle.commons.JSATL12;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@Message(JSATL12.文件上传完成消息应答)
public class T9212 extends JTMessage {

    @Field(lengthUnit = 1, desc = "文件名称(文件类型_通道号_报警类型_序号_报警编号.后缀名)")
    private String name;
    @Field(length = 1, desc = "文件类型：0.图片 1.音频 2.视频 3.文本 4.面部特征图片(粤标) 5.其它")
    private int type;
    @Field(length = 1, desc = "上传结果：0.完成 1.需要补传")
    private int result;
    @Field(length = 1, desc = "补传数据包数量")
    private int total;
    @Field(desc = "补传数据包列表[offset,length,offset,length...]")
    private int[] items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int[] getItems() {
        return items;
    }

    public void setItems(int[] items) {
        if (items != null && items.length > 1) {
            this.items = items;
            this.total = items.length / 2;
        }
    }
}
