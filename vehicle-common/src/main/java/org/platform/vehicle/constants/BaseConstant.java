package org.platform.vehicle.constants;

import static org.platform.vehicle.enums.VehicleSpeciesTypeEnum.GUA_CHE;
import static org.platform.vehicle.enums.VehicleSpeciesTypeEnum.ZHU_CHE;
import static org.platform.vehicle.enums.VehicleSpeciesTypeEnum.ZHU_GUA_YI_TI;
import static org.platform.vehicle.enums.WheelBaseTypeEnum.DAO_XIANG_ZHOU;
import static org.platform.vehicle.enums.WheelBaseTypeEnum.FU_QIAO_ZHOU;
import static org.platform.vehicle.enums.WheelBaseTypeEnum.GUA_CHE_ZHOU;
import static org.platform.vehicle.enums.WheelBaseTypeEnum.QU_DONG_ZHOU;
import static org.platform.vehicle.enums.WheelTypeEnum.CHENG_ZHONG_LUN;
import static org.platform.vehicle.enums.WheelTypeEnum.DAO_XIANG_LUN;
import static org.platform.vehicle.enums.WheelTypeEnum.GUA_CHE_LUN;
import static org.platform.vehicle.enums.WheelTypeEnum.QU_DONG_LUN;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.platform.vehicle.enums.VehicleSpeciesTypeEnum;
import org.platform.vehicle.enums.WheelBaseTypeEnum;
import org.platform.vehicle.enums.WheelTypeEnum;

public final class BaseConstant {

    public static final String OPTIONS_METHOD = "OPTIONS";

    // 支持上传的文件后缀
    public static final List<String> UPLOAD_SUFFIX_FORMAT = Arrays.asList(".png", ".jpeg", ".jpg",
            ".bmp", ".doc",
            ".docx", ".pdf", ".xls", ".xlsx", ".3gp", ".mp4", ".mpeg", ".avi");
    // 支持上传的图上文件后缀
    public static final String IMG_SUFFIX_FORMAT = ".png,.jpeg,.jpg,.bmp,.webp";
    public static final String VIDEO_SUFFIX_FORMAT = ".3gp, .mp4, .mpeg, .avi";
    public static final String EXCEL_SUFFIX_FORMAT = ".xls,.xlsx";

    /**
     * 车轴-车轮对应关系映射表
     */
    public static final Map<VehicleSpeciesTypeEnum, Map<WheelBaseTypeEnum, WheelTypeEnum>> WHEEL_TYPE_MAPPING = new HashMap<VehicleSpeciesTypeEnum, Map<WheelBaseTypeEnum, WheelTypeEnum>>() {
        private static final long serialVersionUID = 4343087782728728437L;

        {
            this.put(ZHU_CHE, new HashMap<WheelBaseTypeEnum, WheelTypeEnum>() {// 主车车型的车轴-车轮对应关系
                private static final long serialVersionUID = 9068137399890080181L;

                {
                    this.put(DAO_XIANG_ZHOU, DAO_XIANG_LUN);// 导向轴-导向轮
                    this.put(QU_DONG_ZHOU, QU_DONG_LUN);// 驱动轴-驱动轮
                }
            });
            this.put(GUA_CHE, new HashMap<WheelBaseTypeEnum, WheelTypeEnum>() {// 挂车车型的车轴-车轮对应关系
                private static final long serialVersionUID = 3814191251668018764L;

                {
                    this.put(GUA_CHE_ZHOU, GUA_CHE_LUN);// 挂车轴-挂车轮
                }
            });
            this.put(ZHU_GUA_YI_TI,
                    new HashMap<WheelBaseTypeEnum, WheelTypeEnum>() {// 主挂一体车型的车轴-车轮对应关系
                        private static final long serialVersionUID = 4565310726139199044L;

                        {
                            this.put(DAO_XIANG_ZHOU, DAO_XIANG_LUN);// 导向轴-导向轮
                            this.put(QU_DONG_ZHOU, QU_DONG_LUN);// 驱动轴-驱动轮
                            this.put(FU_QIAO_ZHOU, CHENG_ZHONG_LUN);// 浮桥轴-承重轮
                        }
                    });
        }
    };
}
