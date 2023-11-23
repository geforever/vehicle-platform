package org.platform.vehicle.controller;

import java.io.File;
import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.platform.vehicle.aspect.RepeatSubmit;
import org.platform.vehicle.constant.OperateModuleEnum;
import org.platform.vehicle.entity.VehicleBrandEntity;
import org.platform.vehicle.entity.VehicleSpecEntity;
import org.platform.vehicle.param.VehicleBrandQueryCondition;
import org.platform.vehicle.service.SysClientService;
import org.platform.vehicle.service.VehicleBrandService;
import org.platform.vehicle.service.VehicleSpecService;
import org.platform.vehicle.util.OperateLogAsync;
import org.platform.vehicle.constants.BaseConstant;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 档案管理-车辆品牌
 *
 * @Author Sunnykid
 */
@Slf4j
@RestController
@RequestMapping("/vehicleBrand")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VehicleBrandController {

	private final VehicleBrandService vehicleBrandService;
	private final VehicleSpecService vehicleSpecService;
	private final OperateLogAsync operateLogAsync;
	private final HttpServletRequest request;
	private final SysClientService sysClientService;

	/**
	 * 车辆品牌-条件查询
	 *
	 * @param param
	 * @return
	 */
	@PostMapping("/query")
	public BaseResponse<VehicleBrandEntity> query(@RequestBody VehicleBrandQueryCondition param) {
		log.info("车辆品牌-条件查询, url:/vehicleBrand/query, param:{}", JSON.toJSONString(param));
		QueryWrapper<VehicleBrandEntity> q = param.wrapper();
		Set<Integer> customerIdScope = this.sysClientService.getAllRelatedIds();
		if(!customerIdScope.isEmpty()) {
			q.in("customer_id", customerIdScope);
		}
		Page<VehicleBrandEntity> page = this.vehicleBrandService.query(q, param.getPageNum(), param.getPageSize());
		for(VehicleBrandEntity vbe : page.getRecords()) {
			vbe.setCustomerName(this.sysClientService.getCustomerInfo(vbe.getCustomerId()).getName());
		}
		return BasePageResponse.ok(page);
	}

	/**
	 * 车辆品牌-查看详情
	 *
	 * @param id 车辆品牌ID
	 * @return
	 */
	@GetMapping("/detail/{id}")
	public BaseResponse<VehicleBrandEntity> detail(@PathVariable("id") Integer id) {
		log.info("车辆品牌-查看详情, url:/vehicleBrand/{}", id);
		VehicleBrandEntity vbe = this.vehicleBrandService.get(id);
		vbe.setCustomerName(this.sysClientService.getCustomerInfo(vbe.getCustomerId()).getName());
		return BaseResponse.ok(vbe);
	}

	/**
	 * 车辆品牌-新增
	 *
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RepeatSubmit()
	@PostMapping("/add")
	public BaseResponse add(@RequestBody VehicleBrandEntity entity) {
		log.info("车辆品牌-新增, url:/vehicleBrand/add, param:{}", JSON.toJSONString(entity));
		if(!this.vehicleBrandService.checkDuplicate(null, "brand_name", entity.getBrandName(), entity.getCustomerId())) {
			return BaseResponse.failure("品牌名称已存在");
		}
		// 获取当前用户
		UserVo userVo = UserContext.getUser();
		entity.setUpdateTime(new Date());
		entity.setCreatePerson(userVo.getName());
		entity.setUpdatePerson(userVo.getName());
		entity.setIsDeleted(Boolean.FALSE);
		this.vehicleBrandService.add(entity);
		this.operateLogAsync.pushAddLog(userVo, OperateModuleEnum.ARCHIVE_TIRE_BRAND_ADD, String.valueOf(entity.getId()), userVo,
				this.request);
		return BaseResponse.ok();
	}

	/**
	 * 车辆品牌-编辑
	 *
	 * @param entity
	 * @return
	 */
	@SuppressWarnings({"boxing", "rawtypes"})
	@PostMapping("/edit")
	public BaseResponse edit(@RequestBody VehicleBrandEntity entity) {
		log.info("车辆品牌-编辑, url:/vehicleBrand/edit, param:{}", JSON.toJSONString(entity));
		if(!this.vehicleBrandService.checkDuplicate(entity.getId(), "brand_name", entity.getBrandName(), entity.getCustomerId())) {
			return BaseResponse.failure("品牌名称已存在");
		}
		// 获取当前用户
		UserVo userVo = UserContext.getUser();
		VehicleBrandEntity oe = this.vehicleBrandService.get(entity.getId());
		oe.setUpdatePerson(userVo.getName());
		oe.setBrandName(entity.getBrandName());
		oe.setDescription(entity.getDescription());
		oe.setCustomerId(entity.getCustomerId());
		this.operateLogAsync.pushAddLog(userVo, OperateModuleEnum.ARCHIVE_TIRE_BRAND_EDIT,
				String.valueOf(entity.getId()), userVo, this.request);
		return BaseResponse.ok(this.vehicleBrandService.update(oe));
	}

	/**
	 * 车辆品牌-删除
	 *
	 * @param id 车辆品牌ID
	 * @param phone 用户电话
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "boxing"})
	@PutMapping("/delete")
	public BaseResponse delete(Integer id, String phone) {
		log.info("车辆品牌-删除", "url:/vehicleBrand/delete, id:{}, phone:{}", id, phone);
		// 获取当前用户
		UserVo userVo = UserContext.getUser();
		// 校验手机号
		if(!userVo.getPhone().equals(phone)) {
			return BaseResponse.failure("手机号错误，删除操作未执行");
		}

		VehicleBrandEntity vehicleBrandEntity = this.vehicleBrandService.get(id);
		QueryWrapper<VehicleSpecEntity> q = new QueryWrapper<VehicleSpecEntity>();
		q.eq("customer_id", vehicleBrandEntity.getCustomerId()).eq("brand_id", vehicleBrandEntity.getId());
		if(!this.vehicleSpecService.query(q).isEmpty()) {
			return BaseResponse.failure("品牌已在型号中关联，无法删除");
		}
		// 保存日志
		this.operateLogAsync.pushDeleteLog(OperateModuleEnum.ARCHIVE_TIRE_BRAND_DELETE, String.valueOf(id), userVo,
				this.request, null);
		vehicleBrandService.delete(id);
		return BaseResponse.ok();
	}

}
