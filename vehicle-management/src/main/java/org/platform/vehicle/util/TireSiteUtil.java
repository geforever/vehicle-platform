package org.platform.vehicle.util;

import static org.platform.vehicle.constants.BaseConstant.WHEEL_TYPE_MAPPING;
import static org.platform.vehicle.enums.VehicleSpeciesTypeEnum.GUA_CHE;
import static org.platform.vehicle.enums.VehicleSpeciesTypeEnum.ZHU_CHE;
import static org.platform.vehicle.enums.VehicleSpeciesTypeEnum.ZHU_GUA_YI_TI;

import com.alibaba.fastjson.JSON;
import org.platform.vehicle.vo.TireSiteResult;
import org.platform.vehicle.enums.WheelBaseTypeEnum;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author gejiawei
 * @Date 2023/9/25 16:18
 */
public class TireSiteUtil {

    private final static List<List<Map<String, List<Integer>>>> MAIN_VEHICLE_WHEEL_LIST = generateVehicleWheelList(
            1, new String[]{"2", "4", "4", "4", "4", "4", "4", "4"});
    private final static List<List<Map<String, List<Integer>>>> MINOR_VEHICLE_WHEEL_LIST = generateVehicleWheelList(
            2, new String[]{"4", "4", "4", "4", "4", "4", "4", "4"});
    private final static List<List<Map<String, List<Integer>>>> MAIN_MINOR_VEHICLE_WHEEL_LIST = generateVehicleWheelList(
            3, new String[]{"2", "4", "4", "4", "4", "4", "4", "4"});

    public static TireSiteResult getMinorTireSiteResult(Integer tireSiteId) {
        return getTireSiteResult(tireSiteId, 2, "4,4,4,4,4,4,4,4", null);
    }

    /**
     * 根据轮位获取轮位详情
     *
     * @param tireSiteId      轮位号
     * @param vehicleType     车辆类型:1:主车,2-挂车,3-主挂一体
     * @param wheelNumSetting 轮位设置
     * @return
     */
    public static TireSiteResult getTireSiteResult(Integer tireSiteId, Integer vehicleType,
            String wheelNumSetting, String wheelbaseType) {
        if (StringUtils.isBlank(wheelNumSetting)) {
            return null;
        }
        // 获取轮位图
        List<List<Map<String, List<Integer>>>> vehicleWheelList = getVehicleWheelList(vehicleType);
        if (vehicleWheelList != null) {
            TireSiteResult tireSiteResult = getTireSiteResult(tireSiteId, vehicleType,
                    wheelNumSetting, wheelbaseType, vehicleWheelList);
            return tireSiteResult;
        }
        return null;
    }

    private static List<List<Map<String, List<Integer>>>> getVehicleWheelList(Integer vehicleType) {
        switch (vehicleType) {
            case 1:
                return MAIN_VEHICLE_WHEEL_LIST;
            case 2:
                return MINOR_VEHICLE_WHEEL_LIST;
            case 3:
                return MAIN_MINOR_VEHICLE_WHEEL_LIST;
            default:
                return null;
        }
    }

    private static TireSiteResult getTireSiteResult(Integer tireSiteId, Integer vehicleType,
            String wheelNumSetting, String wheelbaseType,
            List<List<Map<String, List<Integer>>>> vehicleWheelList) {
        for (int i = 0; i < wheelNumSetting.split(",").length; i++) {
            String actualAxle = wheelNumSetting.split(",")[i];
            int axle = i + 1;
            Integer axleType = null;
            if (StringUtils.isNotBlank(wheelbaseType)) {
                axleType = Integer.parseInt(wheelbaseType.split(",")[i]);
            }
            List<Map<String, List<Integer>>> axleList = vehicleWheelList.get(i);
            for (Map<String, List<Integer>> map : axleList) {
                for (Map.Entry<String, List<Integer>> entry : map.entrySet()) {
                    String key = entry.getKey();
                    List<Integer> value = entry.getValue();
                    for (Integer wheels : value) {
                        if (wheels.equals(tireSiteId)) {
                            String vehicleTypeName = getVehicleTypeName(vehicleType);
                            String axleName = getAxleName(axle);
                            String tireTypeName = getTireTypeName(axleType, vehicleType);
                            StringBuilder tireSiteName = new StringBuilder();
//                            tireSiteName.append(vehicleTypeName);
                            tireSiteName.append(axleName);
                            if ("right".equals(key)) {
                                tireSiteName.append("右");
                            } else {
                                tireSiteName.append("左");
                            }
                            // 判断是否是前轮
                            if (vehicleType == 1 && axle == 1) {
                                tireSiteName.append("前");
                            } else if ("2".equals(actualAxle)) {
                                tireSiteName.append("轮");
                            } else {
                                if ("right".equals(key)) {
                                    if (value.indexOf(wheels) == 0) {
                                        tireSiteName.append("外");
                                    } else {
                                        tireSiteName.append("内");
                                    }
                                } else {
                                    if (value.indexOf(wheels) == 0) {
                                        tireSiteName.append("内");
                                    } else {
                                        tireSiteName.append("外");
                                    }
                                }

                            }
                            TireSiteResult tireSiteResult = new TireSiteResult();
                            tireSiteResult.setTireSiteName(tireSiteName.toString());
                            tireSiteResult.setTireSiteType(axle);
                            tireSiteResult.setTireSiteTypeName(tireTypeName);
                            return tireSiteResult;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static String getTireTypeName(Integer axleType, Integer vehicleType) {
        if (axleType == null) {
            return "";
        }
        WheelBaseTypeEnum wheelEnum = getWheelBaseTypeEnum(axleType);
        switch (vehicleType) {
            case 1:
                return WHEEL_TYPE_MAPPING.get(ZHU_CHE).get(wheelEnum).getText();
            case 2:
                return WHEEL_TYPE_MAPPING.get(GUA_CHE).get(wheelEnum).getText();
            case 3:
                return WHEEL_TYPE_MAPPING.get(ZHU_GUA_YI_TI).get(wheelEnum).getText();
            default:
                return "";
        }
    }

    private static WheelBaseTypeEnum getWheelBaseTypeEnum(int axleType) {
        switch (axleType) {
            case 1:
                return WheelBaseTypeEnum.DAO_XIANG_ZHOU;
            case 2:
                return WheelBaseTypeEnum.QU_DONG_ZHOU;
            case 3:
                return WheelBaseTypeEnum.GUA_CHE_ZHOU;
            case 4:
                return WheelBaseTypeEnum.FU_QIAO_ZHOU;
            default:
                return null;
        }
    }

    private static String getVehicleTypeName(Integer vehicleType) {
        switch (vehicleType) {
            case 1:
                return "主车";
            case 2:
                return "挂车";
            case 3:
                return "主挂车";
            default:
                return "";
        }
    }

    private static List<List<Map<String, List<Integer>>>> generateVehicleWheelList(
            Integer vehicleType,
            String[] wheelArray) {
        // key:轴,value:轮位
        List<List<Map<String, List<Integer>>>> vehicleWheelList = new ArrayList<>();
        switch (vehicleType) {
            // 主车
            case 1:
                // 设置一轴轮胎位置
                setFirstAxleWheel(vehicleWheelList);
                // 从二轴开始
                setWheelList(wheelArray, 1, 3, vehicleWheelList);
                break;
            case 2:
                // 挂车
                setWheelList(wheelArray, 0, 1, vehicleWheelList);
                break;
            case 3:
                // 设置一轴轮胎位置
                setFirstAxleWheel(vehicleWheelList);
                // 从二轴开始
                setWheelList(wheelArray, 1, 3, vehicleWheelList);
                break;
            default:
                break;
        }
        return vehicleWheelList;
    }

    private static void setFirstAxleWheel(List<List<Map<String, List<Integer>>>> vehicleWheelList) {
        List<Map<String, List<Integer>>> firstAxle = new ArrayList<>();
        Map<String, List<Integer>> firstRightMap = new HashMap<>();
        List<Integer> firstRightWheelList = new ArrayList<>();
        firstRightWheelList.add(1);
        firstRightMap.put("right", firstRightWheelList);
        Map<String, List<Integer>> firstLeftMap = new HashMap<>();
        List<Integer> firstLeftWheelList = new ArrayList<>();
        firstLeftWheelList.add(2);
        firstLeftMap.put("left", firstLeftWheelList);
        firstAxle.add(firstRightMap);
        firstAxle.add(firstLeftMap);
        vehicleWheelList.add(firstAxle);
    }

    /**
     * @param wheelArray       轮胎数量数组
     * @param initAxleSite     轴位初始位置(从0开始)
     * @param initTireSite     轮胎初始位置
     * @param vehicleWheelList 轮位列表
     */
    private static void setWheelList(String[] wheelArray, int initAxleSite, int initTireSite,
            List<List<Map<String, List<Integer>>>> vehicleWheelList) {
        for (int i = initAxleSite; i < wheelArray.length; i++) {
            List<Map<String, List<Integer>>> axleList = new ArrayList<>(2);
            // 轮胎数量(2,4,4)
            int wheelCount = Integer.parseInt(wheelArray[i]);
            Map<String, List<Integer>> rightMap = new HashMap<>(1);
            Map<String, List<Integer>> leftMap = new HashMap<>(1);
            List<Integer> leftWheelList = new ArrayList<>();
            List<Integer> rightWheelList = new ArrayList<>();
            for (int count = 0; count < wheelCount; count++) {
                // 每轴轮胎数量分两种情况,2个轮胎,和4个轮胎,2个轮胎只取内胎
                if (wheelCount == 2) {
                    if (count < wheelCount / 2) {
                        initTireSite++;
                        rightWheelList.add(initTireSite);
                        initTireSite++;
                    } else {
                        leftWheelList.add(initTireSite);
                        initTireSite += 2;
                    }
                } else if (wheelCount == 4) {
                    if (count < wheelCount / 2) {
                        rightWheelList.add(initTireSite);
                        initTireSite++;
                    } else {
                        leftWheelList.add(initTireSite);
                        initTireSite++;
                    }
                }
                rightMap.put("right", rightWheelList);
                leftMap.put("left", leftWheelList);
            }
            axleList.add(rightMap);
            axleList.add(leftMap);
            vehicleWheelList.add(axleList);
        }
    }

    private static String getAxleName(Integer axle) {
        switch (axle) {
            case 1:
                return "一轴";
            case 2:
                return "二轴";
            case 3:
                return "三轴";
            case 4:
                return "四轴";
            case 5:
                return "五轴";
            case 6:
                return "六轴";
            case 7:
                return "七轴";
            case 8:
                return "八轴";
            default:
                return "";
        }
    }

    public static void main(String[] args) {
//        List<List<Map<String, List<Integer>>>> vehicleWheelList = getVehicleWheelList(1,
//                new String[]{"2", "4", "4"});
//        System.out.println(JSON.toJSONString(vehicleWheelList));
//        Map<WheelBaseTypeEnum, WheelTypeEnum> wheelBaseTypeEnumWheelTypeEnumMap = WHEEL_TYPE_MAPPING.get(
//                ZHU_CHE);
//        System.out.println(JSON.toJSONString(wheelBaseTypeEnumWheelTypeEnumMap));
        TireSiteResult tireSiteResult = getTireSiteResult(5, 1, "2,4,4", "1,2,2");
//        TireSiteResult tireSiteResult = getTireSiteResult(10, 2, "4,4,4", "3,3,3");
        System.out.println(JSON.toJSONString(tireSiteResult));
    }

}
