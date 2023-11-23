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
import org.platform.vehicle.entity.TireSpecEntity;
import org.platform.vehicle.param.TireSpecQueryCondition;
import org.platform.vehicle.service.SysClientService;
import org.platform.vehicle.service.TireSpecService;
import org.platform.vehicle.util.OperateLogAsync;
import org.platform.vehicle.constants.BaseConstant;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 档案管理-轮胎规格
 *
 * @Author Sunnykid
 */
@Slf4j
@RestController
@RequestMapping("/tireSpec")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TireSpecController {

	private final TireSpecService tireSpecService;
	private final OperateLogAsync operateLogAsync;
	private final HttpServletRequest request;
	private final SysClientService sysClientService;

	/**
	 * 轮胎规格-条件查询
	 *
	 * @param param
	 * @return
	 */
	@PostMapping("/query")
	public BaseResponse<TireSpecEntity> query(@RequestBody TireSpecQueryCondition param) {
		log.info("轮胎规格-条件查询, url:/tireSpec/query, param:{}", JSON.toJSONString(param));
		QueryWrapper<TireSpecEntity> q = param.wrapper();
		Set<Integer> customerIdScope = this.sysClientService.getAllRelatedIds();
		if(!customerIdScope.isEmpty()) {
			q.in("customer_id", customerIdScope);
		}
		Page<TireSpecEntity> page = this.tireSpecService.query(q, param.getPageNum(), param.getPageSize());
		for(TireSpecEntity vbe : page.getRecords()) {
			vbe.setCustomerName(this.sysClientService.getCustomerInfo(vbe.getCustomerId()).getName());
		}
		return BasePageResponse.ok(page);
	}

	/**
	 * 轮胎规格-查看详情
	 *
	 * @param id 轮胎规格ID
	 * @return
	 */
	@GetMapping("/detail/{id}")
	public BaseResponse<TireSpecEntity> detail(@PathVariable("id") Integer id) {
		log.info("轮胎规格-查看详情, url:/tireSpec/{}", id);
		TireSpecEntity vbe = this.tireSpecService.get(id);
		vbe.setCustomerName(this.sysClientService.getCustomerInfo(vbe.getCustomerId()).getName());
		return BaseResponse.ok(vbe);
	}

	/**
	 * 轮胎规格-新增
	 *
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RepeatSubmit()
	@PostMapping("/add")
	public BaseResponse add(@RequestBody TireSpecEntity entity) {
		log.info("轮胎规格-新增, url:/tireSpec/add, param:{}", JSON.toJSONString(entity));
		if(!this.tireSpecService.checkDuplicate(null, "spec_name", entity.getSpecName(), entity.getCustomerId())) {
			return BaseResponse.failure("规格名称已存在");
		}
		// 获取当前用户
		UserVo userVo = UserContext.getUser();
		entity.setUpdateTime(new Date());
		entity.setCreatePerson(userVo.getName());
		entity.setUpdatePerson(userVo.getName());
		entity.setIsDeleted(Boolean.FALSE);
		this.tireSpecService.add(entity);
		// 获取当前用户
		this.operateLogAsync.pushAddLog(userVo, OperateModuleEnum.ARCHIVE_TIRE_BRAND_ADD,
				String.valueOf(entity.getId()), userVo, this.request);
		return BaseResponse.ok();
	}

	/**
	 * 轮胎规格-编辑
	 *
	 * @param entity
	 * @return
	 */
	@SuppressWarnings({"boxing", "rawtypes"})
	@RepeatSubmit(lockField = "id")
	@PostMapping("/edit")
	public BaseResponse edit(@RequestBody TireSpecEntity entity) {
		log.info("轮胎规格-编辑, url:/tireSpec/edit, param:{}", JSON.toJSONString(entity));
		if(!this.tireSpecService.checkDuplicate(entity.getId(), "spec_name", entity.getSpecName(),
				entity.getCustomerId())) {
			return BaseResponse.failure("规格名称已存在");
		}
		TireSpecEntity oe = this.tireSpecService.get(entity.getId());
		oe.setSpecName(entity.getSpecName());
		oe.setCustomerId(entity.getCustomerId());
		oe.setDescription(entity.getDescription());
		// 获取当前用户
		UserVo userVo = UserContext.getUser();
		entity.setUpdatePerson(userVo.getName());

		this.operateLogAsync.pushAddLog(userVo, OperateModuleEnum.ARCHIVE_TIRE_BRAND_EDIT,
				String.valueOf(entity.getId()), userVo, this.request);
		return BaseResponse.ok(this.tireSpecService.update(oe));
	}

	/**
	 * 轮胎规格-删除
	 *
	 * @param id 轮胎规格ID
	 * @param phone 用户电话
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "boxing"})
	@PutMapping("/delete")
	public BaseResponse delete(Integer id, String phone) {
		log.info("轮胎规格-删除", "url:/tireSpec/delete, id:{}, phone:{}", id, phone);
		// 获取当前用户
		UserVo userVo = UserContext.getUser();
		// 校验手机号
		if(!userVo.getPhone().equals(phone)) {
			return BaseResponse.failure("手机号错误，删除操作未执行");
		}
		// 保存日志
		this.operateLogAsync.pushDeleteLog(OperateModuleEnum.ARCHIVE_TIRE_BRAND_DELETE, String.valueOf(id), userVo,
				this.request, null);
		tireSpecService.delete(id);
		return BaseResponse.ok();
	}
}
