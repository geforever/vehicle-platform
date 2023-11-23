package org.platform.vehicle.web.util;

import io.github.yezhihao.protostar.util.KeyValuePair;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.platform.vehicle.t808.T0900;
import org.platform.vehicle.web.domain.TirePressure;
import org.platform.vehicle.web.domain.TirePressureData;
import org.platform.vehicle.web.domain.TireWarningData;

/**
 * @Author gejiawei
 * @Date 2023/10/23 14:58
 */
public class TirePressureConvert {

    /**
     * 轮胎数量位置
     */
    private static final int TIRE_COUNT_INDEX = 3;
    /**
     * 轮胎数据起始位置
     */
    private static final int TIRE_START_INDEX = 4;
    /**
     * 轮胎数据长度
     */
    private static final int TIRE_DATA_LENGTH = 8;


    public static TirePressure convert(T0900 message) {
        KeyValuePair<Integer, Object> keyValuePair = message.getMessage();
        TirePressure tirePressure = new TirePressure();
        tirePressure.setClientId(message.getClientId());
        tirePressure.setSerialNo(message.getSerialNo());
        byte[] bytes = (byte[]) keyValuePair.getValue();
        int[] values = new int[bytes.length];
        for (byte i = 0; i < bytes.length; i++) {
            String hex = String.format("%02X", bytes[i]);
            int intValue = Integer.parseInt(hex, 16);
            // 存入 value
            values[i] = intValue;
        }
        // 取第三个值,减3因为轮胎数量包含:1条接收器信息,1车头中继器信息,1挂车中继器信息
        int totalTireCount = values[TIRE_COUNT_INDEX] - 3;
        // 轮胎数据范围=轮胎数量*轮胎数据长度+轮胎数据起始位置
        int tireDataEndIndex = totalTireCount * TIRE_DATA_LENGTH + TIRE_START_INDEX;
        // 轮胎数据
        int[] tireDataArrays = Arrays.copyOfRange(values, TIRE_START_INDEX, tireDataEndIndex);
        List<TirePressureData> dataList = new ArrayList<>();
        // 轮胎数据,8个为一批
        for (int i = 0; i < tireDataArrays.length; i += TIRE_DATA_LENGTH) {
            int[] oneTireDataArray = Arrays.copyOfRange(tireDataArrays, i, i + 8);
            TirePressureData tirePressureData = new TirePressureData();
            // 轮胎类型:0-主车,1-挂车
            tirePressureData.setType(getTireType(oneTireDataArray));
            // 轮胎位置
            tirePressureData.setTireSiteId(getTireSiteId(oneTireDataArray));
            // 胎压传感器ID
            tirePressureData.setTireSensorId(getSensorId(oneTireDataArray));
            // 电池电压
            tirePressureData.setVoltage(getTireBatteryVoltage(oneTireDataArray));
            // 胎压
            tirePressureData.setTirePressure(getTirePressure(oneTireDataArray));
            // 胎温
            tirePressureData.setTireTemperature(getTireTemperature(oneTireDataArray));
            // 轮胎报警数据
            tirePressureData.setTireWarningData(getTireWarningData(oneTireDataArray));
            dataList.add(tirePressureData);
        }
        int index = tireDataEndIndex + 8;
        int nextIndex = index;
        // 接收器信息
        int[] receiverDataArrays = Arrays.copyOfRange(values, tireDataEndIndex, index);
        // 车头中继器信息
        nextIndex += 8;
        int[] trailerRelayDataArrays = Arrays.copyOfRange(values, index, nextIndex);
        // 获取上下挂状态
        Integer trailerStatus = getTrailerStatus(trailerRelayDataArrays);
        tirePressure.setTrailerStatus(trailerStatus);
        String guaRepeaterId = getGuaRepeaterId(trailerRelayDataArrays);
        tirePressure.setGuaRepeaterId(guaRepeaterId);
        // 挂车中继器信息
        index += 8;
        nextIndex += 8;
        // 前桥中继器信息
        int[] frontAxleRelayDataArrays = Arrays.copyOfRange(values, index, nextIndex);
        // 经度起始位置
        int longitudeIdx = values[TIRE_COUNT_INDEX] * TIRE_DATA_LENGTH + TIRE_START_INDEX;
        // 纬度
        String latitude = getLongitudeAndLatitude(values, longitudeIdx, longitudeIdx + 4);
        // 经度
        String longitude= getLongitudeAndLatitude(values, longitudeIdx + 4, longitudeIdx + 8);
        // 日期起始位置
        int dateStartIdx = longitudeIdx + 8;
        int[] dateArray = Arrays.copyOfRange(values, dateStartIdx, dateStartIdx + 3);
        String date = "20" + toHexString(dateArray[0]) + "-" + toHexString(dateArray[1]) + "-" + toHexString(dateArray[2]);
        // 时间起始位置
        int timeStartIdx = dateStartIdx + 3;
        int[] timeArray = Arrays.copyOfRange(values, timeStartIdx, timeStartIdx + 3);
        String time = toHexString(timeArray[0]) + ":" + toHexString(timeArray[1]) + ":" + toHexString(timeArray[2]);
        // 时间转换
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date deviceTime = new Date();
        try {
            deviceTime = formatter.parse(date + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tirePressure.setDataList(dataList);
//        tirePressure.setReceiverId();
//        tirePressure.setUpTrailerInfo();
//        tirePressure.setFrontAxleRelayInfo();
        tirePressure.setLongitude(longitude);
        tirePressure.setLatitude(latitude);
        tirePressure.setDeviceTime(deviceTime);
        return tirePressure;
    }

    private static String getGuaRepeaterId(int[] trailerRelayDataArrays) {
        String guaRepeaterId;
        guaRepeaterId = toHexString(trailerRelayDataArrays[1]) + toHexString(trailerRelayDataArrays[2]) + toHexString(
                trailerRelayDataArrays[3]);
        return guaRepeaterId;
    }

    private static Integer getTrailerStatus(int[] trailerRelayDataArrays) {
        String trailerStatusHex = toHexString(trailerRelayDataArrays[0]);
        // 挂车状态:1-上挂,2-下挂
        Integer trailerStatus = null;
        String guaRepeaterId = "";
        if ("e0".equals(trailerStatusHex)) {
            trailerStatus = 1;
        } else {
            trailerStatus = 2;
        }
        return trailerStatus;
    }


    private static String getLongitudeAndLatitude(int[] values, int startIdx, int endIdx) {
        int[] longitudeDataArrays = Arrays.copyOfRange(values, startIdx, endIdx);
        StringBuilder longitudeHex = new StringBuilder();
        for (int longitudeDataArray : longitudeDataArrays) {
            // 转化为16进制
            String hex = toHexString(longitudeDataArray);
            longitudeHex.append(hex);
        }
        // longitudeHex 转化为10进制 再除100000 string
        return String.valueOf(Integer.parseInt(longitudeHex.toString(), 16) / 1000000.0);
    }


    /**
     * 获取轮胎类型:0-主车,1-挂车
     *
     * @param oneTireDataArray
     * @return
     */
    private static Integer getTireType(int[] oneTireDataArray) {
        int tireSiteId = oneTireDataArray[0];
        // 转换为二进制
        String binaryString = toBinaryString(tireSiteId);
        // 7~5bit 为轮胎类型,000主车,001挂车
        String tireTypeBinary = binaryString.substring(0, 3);
        return Integer.parseInt(tireTypeBinary, 2);
    }

    /**
     * 获取轮胎号
     *
     * @param oneTireDataArray
     * @return
     */
    private static String getTireSiteId(int[] oneTireDataArray) {
        int tireSiteId = oneTireDataArray[0];
        // 转换为二进制
        String binaryString = toBinaryString(tireSiteId);
        // 4~0bit 为轮胎号
        String tireSiteIdBinary = binaryString.substring(3, 8);
        return String.valueOf(Integer.parseInt(tireSiteIdBinary, 2));
    }

    /**
     * 获取传感器ID
     *
     * @param oneTireDataArray
     * @return
     */
    public static String getSensorId(int[] oneTireDataArray) {
        int sensorFirst = oneTireDataArray[1];
        int sensorSecond = oneTireDataArray[2];
        int sensorThird = oneTireDataArray[3];
        return toHexString(sensorFirst) + toHexString(sensorSecond) + toHexString(sensorThird);

    }

    /**
     * 获取电池电压
     *
     * @param oneTireDataArray
     * @return
     */
    public static String getTireBatteryVoltage(int[] oneTireDataArray) {
        String binaryString = toBinaryString(oneTireDataArray[4]);
        // 电压数据取bit7~bit4
        String voltageBinary = binaryString.substring(0, 4);
        int voltage = Integer.parseInt(voltageBinary, 2);
        // 传感器电池电压为:V= voltage/10+1.9 4舍5入保留一位小数
        return String.valueOf(voltage * 0.1 + 1.9);
//        return String.format("%.1f", voltage * 0.1 + 1.9);
    }

    /**
     * 获取胎压
     *
     * @param oneTireDataArray
     * @return
     */
    public static String getTirePressure(int[] oneTireDataArray) {
        // byte4转换为二进制
        String byte4Binary = toBinaryString(oneTireDataArray[4]);
        String byte5Binary = toBinaryString(oneTireDataArray[5]);
        // byte4取低 2bit(即 bit1~bit0 )
        String byte4BinarySub = byte4Binary.substring(6, 8);
        // byte4 的低 2bit 为 bit9~ 8
        String pressureBinary = byte4BinarySub + byte5Binary;
        //  P = (byte4 byte5) * 0.025 Bar
        double pressure = Integer.parseInt(pressureBinary, 2) * 0.025;
        return String.format("%.2f", pressure);
    }

    /**
     * 获取胎温
     *
     * @param oneTireDataArray
     * @return
     */
    public static String getTireTemperature(int[] oneTireDataArray) {
        // 转换公式为 T = byte6-50 °C
        int temperature = oneTireDataArray[6] - 50;
        return temperature + "";
    }

    public static TireWarningData getTireWarningData(int[] oneTireDataArray) {
        TireWarningData tireWarningData = new TireWarningData();
        String byte7Binary = toBinaryString(oneTireDataArray[7]);
        // bit7 发射器电池电压状态，0-表示电池电压正常，1-表示电池电压低
        Integer batteryVoltageStatus = Integer.valueOf(byte7Binary.substring(0, 1));
        // Bit6 当长时间(60 分钟)没有收到发射器的数据后此位置 1，(此时忽略压力温度 状态字节)
        Integer isTimeout = Integer.valueOf(byte7Binary.substring(1, 2));
        // Bit5 0-自动定位关闭, 1-自动定位开启(自动定位方案);0-智能甩挂关闭, 1-智能甩挂开启(智能甩挂方案)
        Integer scheme = Integer.valueOf(byte7Binary.substring(2, 3));
        // Bit4 1-为气压高, 0-为气压正常
        int tireHighPressureStatus = Integer.parseInt(byte7Binary.substring(3, 4));
        // Bit3 1 为气压低, 0-为气压正常
        int tireLowPressureStatus = Integer.parseInt(byte7Binary.substring(4, 5));
        int tirePressureStatus = 0;
        if (tireHighPressureStatus == 1) {
            tirePressureStatus = 1;
        }
        if (tireLowPressureStatus == 1) {
            tirePressureStatus = 2;
        }
        // Bit2 1 为温度高，0 为温度正常
        int tireHighTemperatureStatus = Integer.parseInt(byte7Binary.substring(5, 6));
        // Bit1~0 00正常状态，10急漏气，11加气 01未定义
        String tireStatusBinary = byte7Binary.substring(6, 8);
        int tireStatus = 0;
        if ("10".equals(tireStatusBinary)) {
            tireStatus = 1;
        }
        if ("11".equals(tireStatusBinary)) {
            tireStatus = 2;
        }
        if ("01".equals(tireStatusBinary)) {
            tireStatus = 3;
        }
        tireWarningData.setBatteryVoltageStatus(batteryVoltageStatus);
        tireWarningData.setIsTimeout(isTimeout);
        tireWarningData.setScheme(scheme);
        tireWarningData.setTirePressureStatus(tirePressureStatus);
        tireWarningData.setTireTemperatureStatus(tireHighTemperatureStatus);
        tireWarningData.setTireStatus(tireStatus);
        return tireWarningData;
    }

    /**
     * 转换位二进制
     *
     * @param param
     * @return
     */
    public static String toBinaryString(int param) {
        StringBuilder stringBuilder = new StringBuilder(Integer.toBinaryString(param));
        while (stringBuilder.length() < 8) {
            stringBuilder.insert(0, "0");
        }
        return stringBuilder.toString();
    }

    /**
     * 十进制转换为16进制,如果不足两位,则补0
     *
     * @param param
     * @return
     */
    public static String toHexString(int param) {
        String hex = Integer.toHexString(param);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }


    public static void main(String[] args) {
        T0900 message = new T0900();
        byte[] tireDataArrays = {1,1,1,9,1,49,-43,59,-63,-90,94,32,4,45,-119,82,-63,-97,95,32,3,21,18,-112,-79,-101,95,32,34,100,-63,34,-63,-86,89,0,39,50,77,-107,-63,-101,93,0,37,99,-103,44,-79,-77,90,0,-12,-109,0,-26,26,87,0,0,-32,85,119,-112,27,0,3,0,-31,85,119,114,27,1,20,16,1,-87,35,-6,7,51,-29,65,35,7,23,9,49,86};
        KeyValuePair<Integer, Object> keyValuePair = new KeyValuePair<>();
        keyValuePair.setKey(1);
        keyValuePair.setValue(tireDataArrays);
        message.setMessage(keyValuePair);

        TirePressure p = TirePressureConvert.convert(message);

        System.out.println(p.getClientId());
        System.out.println(p.getSerialNo());

        for(TirePressureData pd : p.getDataList()) {
        	System.out.println(pd.getTireWarningData());
        }
    }
}
