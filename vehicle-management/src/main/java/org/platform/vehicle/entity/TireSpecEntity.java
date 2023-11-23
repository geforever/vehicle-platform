package org.platform.vehicle.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.platform.vehicle.utils.UserContext;

import lombok.Data;

@Data
@TableName("t_tire_spec")
public class TireSpecEntity implements Serializable {

	private static final long serialVersionUID = -4097381358348453333L;

	/**
	 * 流水ID
	 */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

	/**
	 * 客户ID
	 */
	@TableField("customer_id")
	private Integer customerId;

	/**
	 * 是否删除
	 */
	@TableField("is_deleted")
	private Boolean isDeleted;

	/**
	 * 创建人
	 */
	@TableField("create_person")
	private String createPerson;

	/**
	 * 更新人
	 */
	@TableField("update_person")
	private String updatePerson;

	/**
	 * 创建时间
	 */
	@TableField("create_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@TableField("update_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	/**
	 * 规格名称
	 */
	@TableField("spec_name")
	private String specName;

	/**
	 * 描述
	 */
	@TableField("description")
	private String description;

    /**
     * 所属客户名称
     */
	@Transient
	@TableField(exist = false)
    private String customerName;

	public TireSpecEntity(Integer customerId, String specName, String description, String createPerson,
			String updatePerson) {
		this();
		this.customerId = customerId;
		this.specName = specName;
		this.description = description;
		this.createPerson = createPerson;
		this.updatePerson = updatePerson;
	}

	public TireSpecEntity(Integer customerId, String specName, String description) {
		this(customerId, specName, description, UserContext.getUser().getName(), UserContext.getUser().getName());
	}

	public TireSpecEntity() {
		this.isDeleted = Boolean.FALSE;
		this.createTime = new Date();
		this.updateTime = new Date();
	}

}
