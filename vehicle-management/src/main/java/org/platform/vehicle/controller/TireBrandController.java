package org.platform.vehicle.controller;

import java.io.File;
import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import org.platform.vehicle.entity.TireBrandEntity;
import org.platform.vehicle.param.TireBrandQueryCondition;
import org.platform.vehicle.service.SysClientService;
import org.platform.vehicle.service.TireBrandService;
import org.platform.vehicle.util.OperateLogAsync;
import org.platform.vehicle.constants.BaseConstant;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 档案管理-轮胎品牌
 *
 * @Author Sunnykid
 */
@Slf4j
@RestController
@RequestMapping("/tireBrand")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TireBrandController {

	private final TireBrandService tireBrandService;
	private final OperateLogAsync operateLogAsync;
	private final HttpServletRequest request;
	private final SysClientService sysClientService;

	/**
	 * 轮胎品牌-条件查询
	 *
	 * @param param
	 * @return
	 */
	@PostMapping("/query")
	public BaseResponse<TireBrandEntity> query(@RequestBody TireBrandQueryCondition param) {
		log.info("轮胎品牌-条件查询, url:/tireBrand/query, param:{}", JSON.toJSONString(param));
		QueryWrapper<TireBrandEntity> q = param.wrapper();
		Set<Integer> customerIdScope = this.sysClientService.getAllRelatedIds();
		if(!customerIdScope.isEmpty()) {
			q.in("customer_id", customerIdScope);
		}
		Page<TireBrandEntity> page = this.tireBrandService.query(q, param.getPageNum(), param.getPageSize());
		for(TireBrandEntity vbe : page.getRecords()) {
			vbe.setCustomerName(this.sysClientService.getCustomerInfo(vbe.getCustomerId()).getName());
		}
		return BasePageResponse.ok(page);
	}

	/**
	 * 轮胎品牌-查看详情
	 *
	 * @param id 轮胎品牌ID
	 * @return
	 */
	@GetMapping("/detail/{id}")
	public BaseResponse<TireBrandEntity> detail(@PathVariable("id") Integer id) {
		log.info("轮胎品牌-查看详情, url:/tireBrand/{}", id);
		TireBrandEntity vbe = this.tireBrandService.get(id);
		vbe.setCustomerName(this.sysClientService.getCustomerInfo(vbe.getCustomerId()).getName());
		return BaseResponse.ok(vbe);
	}

	/**
	 * 轮胎品牌-新增
	 *
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RepeatSubmit()
	@PostMapping("/add")
	public BaseResponse add(@RequestBody TireBrandEntity entity) {
		log.info("轮胎品牌-新增, url:/tireBrand/add, param:{}", JSON.toJSONString(entity));
		if(!this.tireBrandService.checkDuplicate(null, "brand_name", entity.getBrandName(), entity.getCustomerId())) {
			return BaseResponse.failure("品牌名称已存在");
		}
		// 获取当前用户
		UserVo userVo = UserContext.getUser();
		entity.setUpdateTime(new Date());
		entity.setCreatePerson(userVo.getName());
		entity.setUpdatePerson(userVo.getName());
		entity.setIsDeleted(Boolean.FALSE);
		this.tireBrandService.add(entity);
		// 获取当前用户
		this.operateLogAsync.pushAddLog(userVo, OperateModuleEnum.ARCHIVE_TIRE_BRAND_ADD,
				String.valueOf(entity.getId()), userVo, this.request);
		return BaseResponse.ok();
	}

	/**
	 * 轮胎品牌-删除
	 *
	 * @param id 轮胎品牌ID
	 * @param phone 用户电话
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "boxing"})
	@PutMapping("/delete")
	public BaseResponse delete(Integer id, String phone) {
		log.info("轮胎品牌-删除", "url:/tireBrand/delete, id:{}, phone:{}", id, phone);
		// 获取当前用户
		UserVo userVo = UserContext.getUser();
		// 校验手机号
		if(!userVo.getPhone().equals(phone)) {
			return BaseResponse.failure("手机号错误，删除操作未执行");
		}
		// 保存日志
		this.operateLogAsync.pushDeleteLog(OperateModuleEnum.ARCHIVE_TIRE_BRAND_DELETE, String.valueOf(id), userVo,
				this.request, null);
		tireBrandService.delete(id);
		return BaseResponse.ok();
	}

	/**
	 * 轮胎品牌-编辑
	 *
	 * @param entity
	 * @return
	 */
	@SuppressWarnings({"boxing", "rawtypes"})
	@RepeatSubmit(lockField = "id")
	@PostMapping("/edit")
	public BaseResponse edit(@RequestBody TireBrandEntity entity) {
		log.info("轮胎品牌-编辑, url:/tireBrand/edit, param:{}", JSON.toJSONString(entity));
		if(!this.tireBrandService.checkDuplicate(entity.getId(), "brand_name", entity.getBrandName(),
				entity.getCustomerId())) {
			return BaseResponse.failure("品牌名称已存在");
		}
		TireBrandEntity oe = this.tireBrandService.get(entity.getId());
		oe.setBrandName(entity.getBrandName());
		oe.setCustomerId(entity.getCustomerId());
		oe.setDescription(entity.getDescription());
		// 获取当前用户
		UserVo userVo = UserContext.getUser();
		entity.setUpdatePerson(userVo.getName());
		this.operateLogAsync.pushAddLog(userVo, OperateModuleEnum.ARCHIVE_TIRE_BRAND_EDIT,
				String.valueOf(entity.getId()), userVo, this.request);
		return BaseResponse.ok(this.tireBrandService.update(oe));
	}
}
