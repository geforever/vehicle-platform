package org.platform.vehicle.entity.JT808;

import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Author gejiawei
 * @Date 2023/11/9 16:42
 */
@Data
@Document(indexName = "#{@dynamicIndex.getIndex()}", type = "_doc", createIndex = false)
public class TirePressureDataDto {

    /**
     * 流水ID
     */
    @Id
    private Long id;

    /**
     * 车队ID
     */
    @Field(index = false, type = FieldType.Integer)
    private Integer fleetId;

    /**
     * 主表ID
     */
    @Field(index = false, type = FieldType.Long)
    private Long parentCode;

    /**
     * 终端ID号
     */
    @Field(index = false, type = FieldType.Text)
    private String clientId;

    /**
     * 轮胎类型:0-主车,1-挂车
     */
    @Field(index = false, type = FieldType.Integer)
    private Integer type;

    /**
     * 轮胎位置
     */
    @Field(index = false, type = FieldType.Integer)
    private String tireSiteId;

    /**
     * 胎压传感器ID
     */
    @Field(index = false, type = FieldType.Text)
    private String tireSensorId;

    /**
     * 电压
     */
    @Field(index = false, type = FieldType.Text)
    private String voltage;

    /**
     * 胎压
     */
    @Field(index = false, type = FieldType.Text)
    private String tirePressure;

    /**
     * 胎温
     */
    @Field(index = false, type = FieldType.Text)
    private String tireTemperature;

    /**
     * 电池电压状态:0-正常,1-电池电压低
     */
    @Field(index = false, type = FieldType.Integer)
    private Integer batteryVoltageStatus;

    /**
     * 是否超时:0-正常,1-当长时间(60 分钟)没有收到发射器的数据后此位置 1，(此时忽略压力温度 状态字节)
     */
    @Field(index = false, type = FieldType.Integer)
    private Integer isTimeout;

    /**
     * 方案:0:自动定位关闭 1:自动定位开启(自动定位方案)/0:智能甩挂关闭 1:智能甩挂开启(智能甩挂方案)
     */
    @Field(index = false, type = FieldType.Integer)
    private Integer scheme;

    /**
     * 胎压状态:0-正常,1-高压,2-低压
     */
    @Field(index = false, type = FieldType.Integer)
    private Integer tirePressureStatus;

    /**
     * 胎温状态:0-正常,1-异常
     */
    @Field(index = false, type = FieldType.Integer)
    private Integer tireTemperatureStatus;

    /**
     * 轮胎状态:0-正常状态,1-急漏气,2-加气,3-未定义
     */
    @Field(index = false, type = FieldType.Integer)
    private Integer tireStatus;

    /**
     * 记录创建时间
     */
    @Field(index = false, type = FieldType.Date)
    private Date deviceTime;

    /**
     * 创建时间
     */
    @Field(index = false, type = FieldType.Date)
    private Date createTime;

}
