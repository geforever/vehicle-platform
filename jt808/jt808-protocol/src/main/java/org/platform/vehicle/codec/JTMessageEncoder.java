package org.platform.vehicle.codec;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.SchemaManager;
import io.github.yezhihao.protostar.schema.RuntimeSchema;
import io.github.yezhihao.protostar.util.ArrayMap;
import io.github.yezhihao.protostar.util.Explain;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;
import java.util.LinkedList;
import org.platform.vehicle.basics.JTMessage;
import org.platform.vehicle.commons.JTUtils;

/**
 * JT协议编码器
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class JTMessageEncoder {

    private static final ByteBufAllocator ALLOC = PooledByteBufAllocator.DEFAULT;

    private final SchemaManager schemaManager;

    private final ArrayMap<RuntimeSchema> headerSchemaMap;

    public JTMessageEncoder(String... basePackages) {
        this.schemaManager = new SchemaManager(basePackages);
        this.headerSchemaMap = schemaManager.getRuntimeSchema(JTMessage.class);
    }

    public JTMessageEncoder(SchemaManager schemaManager) {
        this.schemaManager = schemaManager;
        this.headerSchemaMap = schemaManager.getRuntimeSchema(JTMessage.class);
    }

    public ByteBuf encode(JTMessage message) {
        return encode(message, null);
    }

    public ByteBuf encode(JTMessage message, Explain explain) {
        int version = message.getProtocolVersion();
        int headLength = JTUtils.headerLength(version, false);
        int bodyLength = 0;

        Schema headSchema = headerSchemaMap.get(version);
        Schema bodySchema = schemaManager.getRuntimeSchema(message.getMessageId(), version);

        ByteBuf output;
        if (bodySchema != null) {
            output = ALLOC.buffer(headLength + bodySchema.length());
            output.writerIndex(headLength);
            bodySchema.writeTo(output, message, explain);
            bodyLength = output.writerIndex() - headLength;
        } else {
            output = ALLOC.buffer(headLength, 21);
        }

        if (bodyLength <= 1023) {
            message.setBodyLength(bodyLength);

            int writerIndex = output.writerIndex();
            if (writerIndex > 0) {
                output.writerIndex(0);
                headSchema.writeTo(output, message, explain);
                output.writerIndex(writerIndex);
            } else {
                headSchema.writeTo(output, message, explain);
            }

            output = sign(output);
            output = escape(output);

        } else {

            ByteBuf[] slices = slices(output, headLength, 1023);
            int total = slices.length;

            CompositeByteBuf _allBuf = new CompositeByteBuf(ALLOC, false, total);
            output = _allBuf;

            message.setSubpackage(true);
            message.setPackageTotal(total);

            headLength = JTUtils.headerLength(version, true);
            for (int i = 0; i < total; i++) {
                ByteBuf slice = slices[i];

                message.setPackageNo(i + 1);
                message.setBodyLength(slice.readableBytes());

                ByteBuf headBuf = ALLOC.buffer(headLength, headLength);
                headSchema.writeTo(headBuf, message, explain);
                ByteBuf msgBuf = new CompositeByteBuf(ALLOC, false, 2)
                        .addComponent(true, 0, headBuf)
                        .addComponent(true, 1, slice);
                msgBuf = sign(msgBuf);
                msgBuf = escape(msgBuf);
                _allBuf.addComponent(true, i, msgBuf);
            }
        }
        return output;
    }

    public static ByteBuf[] slices(ByteBuf output, int start, int unitSize) {
        int totalSize = output.writerIndex() - start;
        int tailIndex = (totalSize - 1) / unitSize;

        ByteBuf[] slices = new ByteBuf[tailIndex + 1];
        output.skipBytes(start);
        for (int i = 0; i < tailIndex; i++) {
            slices[i] = output.readSlice(unitSize);
        }
        slices[tailIndex] = output.readSlice(output.readableBytes());
        output.retain(tailIndex);
        return slices;
    }

    /** 签名 */
    public static ByteBuf sign(ByteBuf buf) {
        byte checkCode = JTUtils.bcc(buf, 0);
        buf.writeByte(checkCode);
        return buf;
    }

    private static final ByteProcessor searcher = value -> !(value == 0x7d || value == 0x7e);

    /** 转义处理 */
    public static ByteBuf escape(ByteBuf source) {
        int low = source.readerIndex();
        int high = source.writerIndex();

        LinkedList<ByteBuf> bufList = new LinkedList();
        int mark, len;
        while ((mark = source.forEachByte(low, high - low, searcher)) > 0) {

            len = mark + 1 - low;
            ByteBuf[] slice = slice(source, low, len);
            bufList.add(slice[0]);
            bufList.add(slice[1]);
            low += len;
        }

        if (bufList.size() > 0) {
            bufList.add(source.slice(low, high - low));
        } else {
            bufList.add(source);
        }

        ByteBuf delimiter = Unpooled.buffer(1, 1).writeByte(0x7e).retain();
        bufList.addFirst(delimiter);
        bufList.addLast(delimiter);

        CompositeByteBuf byteBufs = new CompositeByteBuf(ALLOC, false, bufList.size());
        byteBufs.addComponents(true, bufList);
        return byteBufs;
    }

    /** 截断转义前报文，并转义 */
    protected static ByteBuf[] slice(ByteBuf byteBuf, int index, int length) {
        byte first = byteBuf.getByte(index + length - 1);

        ByteBuf[] bufs = new ByteBuf[2];
        bufs[0] = byteBuf.retainedSlice(index, length);

        if (first == 0x7d) {
            bufs[1] = Unpooled.buffer(1, 1).writeByte(0x01);
        } else {
            byteBuf.setByte(index + length - 1, 0x7d);
            bufs[1] = Unpooled.buffer(1, 1).writeByte(0x02);
        }
        return bufs;
    }
}
