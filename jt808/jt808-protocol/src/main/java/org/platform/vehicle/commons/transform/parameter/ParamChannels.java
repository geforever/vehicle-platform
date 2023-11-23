package org.platform.vehicle.commons.transform.parameter;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;

/**
 * 音视频通道列表设置
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class ParamChannels {

    public static final Integer key = 0x0076;

    public static final Schema<ParamChannels> SCHEMA = new ParamChannelsSchema();

    @Field(desc = "音视频通道总数")
    private byte audioVideoChannels;
    @Field(desc = "音频通道总数")
    private byte audioChannels;
    @Field(desc = "视频通道总数")
    private byte videoChannels;
    @Field(desc = "音视频通道对照表")
    private List<ChannelInfo> channels;

    public ParamChannels() {
    }

    public byte getAudioVideoChannels() {
        return audioVideoChannels;
    }

    public void setAudioVideoChannels(byte audioVideoChannels) {
        this.audioVideoChannels = audioVideoChannels;
    }

    public byte getAudioChannels() {
        return audioChannels;
    }

    public void setAudioChannels(byte audioChannels) {
        this.audioChannels = audioChannels;
    }

    public byte getVideoChannels() {
        return videoChannels;
    }

    public void setVideoChannels(byte videoChannels) {
        this.videoChannels = videoChannels;
    }

    public List<ChannelInfo> getChannels() {
        return channels;
    }

    public void setChannels(List<ChannelInfo> channels) {
        this.channels = channels;
    }

    private static class ChannelInfo {
        @Field(desc = "物理通道号(从1开始)")
        private byte channelId;
        @Field(desc = "逻辑通道号(按照JT/T 1076-2016 中的表2)")
        private byte channelNo;
        @Field(desc = "通道类型：0.音视频 1.音频 2.视频")
        private byte channelType;
        @Field(desc = "是否连接云台(类型为0和2时,此字段有效)：0.未连接 1.连接")
        private boolean hasPanTilt;

        public ChannelInfo() {
        }

        public ChannelInfo(byte channelId, byte channelNo, byte channelType, boolean hasPanTilt) {
            this.channelId = channelId;
            this.channelNo = channelNo;
            this.channelType = channelType;
            this.hasPanTilt = hasPanTilt;
        }

        public byte getChannelId() {
            return channelId;
        }

        public void setChannelId(byte channelId) {
            this.channelId = channelId;
        }

        public byte getChannelNo() {
            return channelNo;
        }

        public void setChannelNo(byte channelNo) {
            this.channelNo = channelNo;
        }

        public byte getChannelType() {
            return channelType;
        }

        public void setChannelType(byte channelType) {
            this.channelType = channelType;
        }

        public boolean isHasPanTilt() {
            return hasPanTilt;
        }

        public void setHasPanTilt(boolean hasPanTilt) {
            this.hasPanTilt = hasPanTilt;
        }
    }

    private static class ParamChannelsSchema implements Schema<ParamChannels> {

        private ParamChannelsSchema() {
        }

        @Override
        public ParamChannels readFrom(ByteBuf input) {
            ParamChannels message = new ParamChannels();
            message.audioVideoChannels = input.readByte();
            message.audioChannels = input.readByte();
            message.videoChannels = input.readByte();

            List<ChannelInfo> channels = new ArrayList<>(4);
            while (input.isReadable()) {
                byte channelId = input.readByte();
                byte channelNo = input.readByte();
                byte channelType = input.readByte();
                boolean hasPanTilt = input.readBoolean();
                channels.add(new ChannelInfo(channelId, channelNo, channelType, hasPanTilt));
            }
            message.setChannels(channels);
            return message;
        }

        @Override
        public void writeTo(ByteBuf output, ParamChannels message) {
            output.writeByte(message.audioVideoChannels);
            output.writeByte(message.audioChannels);
            output.writeByte(message.videoChannels);

            List<ChannelInfo> channelInfos = message.getChannels();
            for (ChannelInfo channelInfo : channelInfos) {
                output.writeByte(channelInfo.channelId);
                output.writeByte(channelInfo.channelNo);
                output.writeByte(channelInfo.channelType);
                output.writeBoolean(channelInfo.hasPanTilt);
            }
        }
    }
}
