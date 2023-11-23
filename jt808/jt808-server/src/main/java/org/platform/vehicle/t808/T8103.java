package org.platform.vehicle.t808;

import io.github.yezhihao.netmc.util.AdapterMap;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.annotation.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import org.platform.vehicle.basics.JTMessage;
import org.platform.vehicle.commons.JT808;
import org.platform.vehicle.commons.transform.ParameterConverter;
import org.platform.vehicle.commons.transform.parameter.*;

/**
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
@Message(JT808.设置终端参数)
public class T8103 extends JTMessage {

    @Field(length = 1, desc = "参数总数")
    private int total;
    @Field(desc = "参数项列表", converter = ParameterConverter.class)
    private Map<Integer, Object> parameters;

    public T8103() {
    }

    public T8103(Map<Integer, Object> parameters) {
        this.parameters = parameters;
        this.total = parameters.size();
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Map<Integer, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<Integer, Object> parameters) {
        this.parameters = parameters;
        this.total = parameters.size();
    }

    public T8103 addParameter(Integer key, Object value) {
        if (parameters == null)
            parameters = new TreeMap();
        parameters.put(key, value);
        total = parameters.size();
        return this;
    }

    @Schema(description = "数值型参数列表(BYTE、WORD)")
    private Map<Integer, Integer> parametersInt;
    @Schema(description = "数值型参数列表(DWORD、QWORD)")
    private Map<Integer, String> parametersLong;
    @Schema(description = "字符型参数列表")
    private Map<Integer, String> parametersStr;
    @Schema(description = "图像分析报警参数设置(1078)")
    private ParamImageIdentifyAlarm paramImageIdentifyAlarm;
    @Schema(description = "特殊报警录像参数设置(1078)")
    private ParamVideoSpecialAlarm paramVideoSpecialAlarm;
    @Schema(description = "音视频通道列表设置(1078)")
    private ParamChannels paramChannels;
    @Schema(description = "终端休眠唤醒模式设置数据格式(1078)")
    private ParamSleepWake paramSleepWake;
    @Schema(description = "音视频参数设置(1078)")
    private ParamVideo paramVideo;
    @Schema(description = "单独视频通道参数设置(1078)")
    private ParamVideoSingle paramVideoSingle;
    @Schema(description = "盲区监测系统参数(苏标)")
    private ParamBSD paramBSD;
    @Schema(description = "胎压监测系统参数(苏标)")
    private ParamTPMS paramTPMS;
    @Schema(description = "驾驶员状态监测系统参数(苏标)")
    private ParamDSM paramDSM;
    @Schema(description = "高级驾驶辅助系统参数(苏标)")
    private ParamADAS paramADAS;

    public Map<Integer, Integer> getParametersInt() {
        return parametersInt;
    }

    public void setParametersInt(Map<Integer, Integer> parametersInt) {
        this.parametersInt = parametersInt;
    }

    public Map<Integer, String> getParametersLong() {
        return parametersLong;
    }

    public void setParametersLong(Map<Integer, String> parametersLong) {
        this.parametersLong = parametersLong;
    }

    public Map<Integer, String> getParametersStr() {
        return parametersStr;
    }

    public void setParametersStr(Map<Integer, String> parametersStr) {
        this.parametersStr = parametersStr;
    }

    public ParamADAS getParamADAS() {
        return paramADAS;
    }

    public void setParamADAS(ParamADAS paramADAS) {
        this.paramADAS = paramADAS;
    }

    public ParamBSD getParamBSD() {
        return paramBSD;
    }

    public void setParamBSD(ParamBSD paramBSD) {
        this.paramBSD = paramBSD;
    }

    public ParamChannels getParamChannels() {
        return paramChannels;
    }

    public void setParamChannels(ParamChannels paramChannels) {
        this.paramChannels = paramChannels;
    }

    public ParamDSM getParamDSM() {
        return paramDSM;
    }

    public void setParamDSM(ParamDSM paramDSM) {
        this.paramDSM = paramDSM;
    }

    public ParamImageIdentifyAlarm getParamImageIdentifyAlarm() {
        return paramImageIdentifyAlarm;
    }

    public void setParamImageIdentifyAlarm(ParamImageIdentifyAlarm paramImageIdentifyAlarm) {
        this.paramImageIdentifyAlarm = paramImageIdentifyAlarm;
    }

    public ParamSleepWake getParamSleepWake() {
        return paramSleepWake;
    }

    public void setParamSleepWake(ParamSleepWake paramSleepWake) {
        this.paramSleepWake = paramSleepWake;
    }

    public ParamTPMS getParamTPMS() {
        return paramTPMS;
    }

    public void setParamTPMS(ParamTPMS paramTPMS) {
        this.paramTPMS = paramTPMS;
    }

    public ParamVideo getParamVideo() {
        return paramVideo;
    }

    public void setParamVideo(ParamVideo paramVideo) {
        this.paramVideo = paramVideo;
    }

    public ParamVideoSingle getParamVideoSingle() {
        return paramVideoSingle;
    }

    public void setParamVideoSingle(ParamVideoSingle paramVideoSingle) {
        this.paramVideoSingle = paramVideoSingle;
    }

    public ParamVideoSpecialAlarm getParamVideoSpecialAlarm() {
        return paramVideoSpecialAlarm;
    }

    public void setParamVideoSpecialAlarm(ParamVideoSpecialAlarm paramVideoSpecialAlarm) {
        this.paramVideoSpecialAlarm = paramVideoSpecialAlarm;
    }

    public T8103 build() {
        Map<Integer, Object> map = new TreeMap<>();

        if (parametersInt != null && !parametersInt.isEmpty())
            map.putAll(parametersInt);

        if (parametersLong != null && !parametersLong.isEmpty())
            map.putAll(new AdapterMap(parametersLong, (Function<String, Long>) Long::parseLong));

        if (parametersStr != null && !parametersStr.isEmpty())
            map.putAll(parametersStr);

        if (paramADAS != null)
            map.put(paramADAS.key, paramADAS);
        if (paramBSD != null)
            map.put(paramBSD.key, paramBSD);
        if (paramChannels != null)
            map.put(paramChannels.key, paramChannels);
        if (paramDSM != null)
            map.put(paramDSM.key, paramDSM);
        if (paramImageIdentifyAlarm != null)
            map.put(paramImageIdentifyAlarm.key, paramImageIdentifyAlarm);
        if (paramSleepWake != null)
            map.put(paramSleepWake.key, paramSleepWake);
        if (paramTPMS != null)
            map.put(paramTPMS.key, paramTPMS);
        if (paramVideo != null)
            map.put(paramVideo.key, paramVideo);
        if (paramVideoSingle != null)
            map.put(paramVideoSingle.key, paramVideoSingle);
        if (paramVideoSpecialAlarm != null)
            map.put(paramVideoSpecialAlarm.key, paramVideoSpecialAlarm);

        this.total = map.size();
        this.parameters = map;
        return this;
    }
}
