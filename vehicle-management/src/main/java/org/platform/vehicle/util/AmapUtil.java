package org.platform.vehicle.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import org.platform.vehicle.vo.amap.AmapWeatherInfoVo;
import org.platform.vehicle.vo.amap.ConvertCoordVo;
import org.platform.vehicle.vo.amap.RegeocodeResultVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/10/11 15:58
 */
@Component
public class AmapUtil {

    /**
     * 高德天气接口
     */
    private final static String WEATHER_INFO_URL = "https://restapi.amap.com/v3/weather/weatherInfo";

    /**
     * 高德坐标转换接口
     */
    private final static String CONVERT_COORD_URL = "https://restapi.amap.com/v3/assistant/coordinate/convert";

    /**
     * 高德逆地理编码接口
     */
    private final static String REVERSE_GEOCODE_URL = "https://restapi.amap.com/v3/geocode/regeo";

    @Value("${amap.key}")
    private String weatherInfoKey;

    /**
     * 坐标转换
     *
     * @param longitude
     * @param latitude
     * @return ConvertCoordVo
     */
    public ConvertCoordVo convertCoord(String longitude, String latitude) {
        String location = longitude + "," + latitude;
//        String weatherInfoKey = "fff8c38bd13a6e75faa0d93d823daa67";
        // 请求高德坐标转换接口
        String url =
                CONVERT_COORD_URL + "?locations=" + location + "&coordsys=gps&key="
                        + weatherInfoKey;
        String result = HttpUtil.get(url);
        ConvertCoordVo convertCoordVo = JSON.parseObject(result, ConvertCoordVo.class);
        return convertCoordVo;
    }

    /**
     * 逆地理编码
     *
     * @param location
     * @return
     */
    public RegeocodeResultVo regeocode(String location) {
//        String weatherInfoKey = "fff8c38bd13a6e75faa0d93d823daa67";
        String url = REVERSE_GEOCODE_URL + "?location=" + location + "&key=" + weatherInfoKey;
        String result = HttpUtil.get(url);
        RegeocodeResultVo regeocodeVo = JSON.parseObject(result, RegeocodeResultVo.class);
        return regeocodeVo;
    }


    /**
     * 获取天气信息
     *
     * @param city
     * @return
     */
    public AmapWeatherInfoVo getWeatherInfo(String city) {
//        String weatherInfoKey = "fff8c38bd13a6e75faa0d93d823daa67";
        // 请求高德天气接口
        String url = WEATHER_INFO_URL + "?city=" + city + "&key=" + weatherInfoKey;
        String result = HttpUtil.get(url);
        AmapWeatherInfoVo amapWeatherInfoVo = JSON.parseObject(result, AmapWeatherInfoVo.class);
        return amapWeatherInfoVo;
    }

    public static void main(String[] args) {
        AmapUtil amapUtil = new AmapUtil();
//        ConvertCoordVo convertCoordVo = amapUtil.convertCoord("113.193620,28.197771");
//        System.out.println(JSON.toJSONString(convertCoordVo));
//        RegeocodeResultVo regeocodeVo = amapUtil.regeocode("113.199375,28.194478352865");
//        System.out.println(JSON.toJSONString(regeocodeVo));
        AmapWeatherInfoVo weatherInfo = amapUtil.getWeatherInfo("430121");
        System.out.println(JSON.toJSONString(weatherInfo));
    }

}
