package org.platform.vehicle.codec;

import io.github.yezhihao.netmc.util.ByteBufUtils;
import io.github.yezhihao.protostar.SchemaManager;
import io.github.yezhihao.protostar.schema.RuntimeSchema;
import io.netty.buffer.ByteBuf;
import org.platform.vehicle.basics.JTMessage;
import org.platform.vehicle.jsatl12.DataPacket;

/**
 * 数据帧解码器
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class DataFrameMessageDecoder extends JTMessageDecoder {

    private final RuntimeSchema<DataPacket> dataFrameSchema;
    private final byte[] dataFramePrefix;

    public DataFrameMessageDecoder(SchemaManager schemaManager, byte[] dataFramePrefix) {
        super(schemaManager);
        this.dataFramePrefix = dataFramePrefix;
        this.dataFrameSchema = schemaManager.getRuntimeSchema(DataPacket.class, 0);
    }

    @Override
    public JTMessage decode(ByteBuf input) {
        if (ByteBufUtils.startsWith(input, dataFramePrefix)) {
            DataPacket message = new DataPacket();
            message.setPayload(input);
            dataFrameSchema.mergeFrom(input, message);
            return message;
        }
        return super.decode(input);
    }
}
