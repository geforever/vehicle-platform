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
public class TirePressureDto {

    /**
     * 流水ID
     */
    @Id
    private Long id;

    /**
     * 唯一编号
     */
    @Field(index = false, type = FieldType.Long)
    private Long code;

    /**
     * 车队ID
     */
    @Field(index = false, type = FieldType.Integer)
    private Integer fleetId;

    /**
     * 终端ID号
     */
    @Field(index = false, type = FieldType.Text)
    private String clientId;

    /**
     * 流水号
     */
    @Field(index = false, type = FieldType.Integer)
    private int serialNo;

    /**
     * 接收器ID
     */
    @Field(index = false, type = FieldType.Text)
    private String receiverId;

    /**
     * 上挂信息
     */
    @Field(index = false, type = FieldType.Text)
    private String upTrailerInfo;

    /**
     * 挂车状态:1-上挂,2-下挂
     */
    @Field(index = false, type = FieldType.Integer)
    private Integer trailerStatus;

    /**
     * 挂车中继器ID
     */
    @Field(index = false, type = FieldType.Text)
    private String guaRepeaterId;

    /**
     * 前桥中继器信息
     */
    @Field(index = false, type = FieldType.Text)
    private String frontAxleRelayInfo;

    /**
     * 经度
     */
    @Field(index = false, type = FieldType.Text)
    private String longitude;

    /**
     * 纬度
     */
    @Field(index = false, type = FieldType.Text)
    private String latitude;

    /**
     * 设备时间
     */
    @Field(index = false, type = FieldType.Date)
    private Date deviceTime;

    /**
     * 插入时间
     */
    @Field(index = false, type = FieldType.Date)
    private Date createTime;
}
