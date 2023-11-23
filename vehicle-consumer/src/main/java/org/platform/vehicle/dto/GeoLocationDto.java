package org.platform.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Document(indexName = "#{@dynamicIndex.getIndex()}", type = "_doc", createIndex = false)
public class GeoLocationDto implements Serializable {

    private static final long serialVersionUID = 9189492316938921152L;

    @Id
    private Long id;

    /**
     * 车队ID
     */
    @Field(index = false, type = FieldType.Integer)
    private Integer fleetId;

    /**
     * 接收器ID
     */
    @Field(index = false, type = FieldType.Text)
    private String receiverId;

    /**
     * 消息ID
     */
    @Field(index = false, type = FieldType.Text)
    private String msgId;

    /**
     * 经度
     */
    @Field(index = false, type = FieldType.Text)
    private String lng;

    /**
     * 纬度
     */
    @Field(index = false, type = FieldType.Text)
    private String lat;

    /**
     * 海拔高度
     */
    @Field(index = false, type = FieldType.Text)
    private String altitude;

    /**
     * 速度
     */
    @Field(index = false, type = FieldType.Text)
    private String speed;

    /**
     * 报警标志
     */
    @Field(index = false, type = FieldType.Integer)
    private Integer warnBit;

    /**
     * 状态
     */
    @Field(index = false, type = FieldType.Integer)
    private Integer statusBit;

    /**
     * 设备时间
     */
    @Field(index = false, type = FieldType.Date, format = DateFormat.custom,
            pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deviceTime;

    /**
     * 插入时间
     */
    @Field(index = false, type = FieldType.Date, format = DateFormat.custom,
            pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
