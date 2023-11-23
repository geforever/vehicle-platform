package org.platform.vehicle.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.platform.vehicle.constant.OperateModuleEnum;
import org.platform.vehicle.constant.SysRoleConstant;
import org.platform.vehicle.entity.DriverEntity;
import org.platform.vehicle.entity.SysUser;
import org.platform.vehicle.param.DriverQueryCondition;
import org.platform.vehicle.param.SysUserAddParam;
import org.platform.vehicle.service.DriverService;
import org.platform.vehicle.service.SysClientService;
import org.platform.vehicle.service.SysUserService;
import org.platform.vehicle.util.OperateLogAsync;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 档案管理-司机档案
 *
 * @Author Sunnykid
 */
@Slf4j
@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DriverController {

	private final DriverService driverService;
	private final OperateLogAsync operateLogAsync;
	private final HttpServletRequest request;
	private final SysClientService sysClientService;
	private final SysUserService sysUserService;

	/**
	 * 司机档案-条件查询
	 *
	 * @param param
	 * @return
	 */
	@PostMapping("/query")
	public BaseResponse<DriverEntity> query(@RequestBody DriverQueryCondition param) {
		log.info("司机档案-条件查询, url:/driver/query, param:{}", JSON.toJSONString(param));
		QueryWrapper<DriverEntity> q = param.wrapper();
		Set<Integer> customerIdScope = this.sysClientService.getAllRelatedIds();
		if(!customerIdScope.isEmpty()) {
			q.in("customer_id", customerIdScope);
		}
		Page<DriverEntity> page = this.driverService.query(q, param.getPageNum(), param.getPageSize());
		for(DriverEntity vbe : page.getRecords()) {
			vbe.setFleetName(this.sysClientService.getById(vbe.getCustomerId()).getName());
			vbe.setCustomerName(this.sysClientService.getCustomerInfo(vbe.getCustomerId()).getName());
		}
		return BasePageResponse.ok(page);
	}

	/**
	 * 司机档案-查看详情
	 *
	 * @param id 司机ID
	 * @return
	 */
	@GetMapping("/detail/{id}")
	public BaseResponse<DriverEntity> detail(@PathVariable("id") Integer id) {
		log.info("司机档案-查看详情, url:/driver/{}", id);
		DriverEntity vbe = this.driverService.get(id);
		vbe.setFleetName(this.sysClientService.getById(vbe.getCustomerId()).getName());
		vbe.setCustomerName(this.sysClientService.getCustomerInfo(vbe.getCustomerId()).getName());
		return BaseResponse.ok(vbe);
	}

	/**
	 * 司机档案-新增
	 *
	 * @param entity 司机实体
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/add")
	public BaseResponse add(@RequestBody DriverEntity entity) {
		log.info("司机档案-新增, url:/driver/add, param:{}", JSON.toJSONString(entity));
		if(!this.driverService.checkDuplicate(null, "mobile", entity.getMobile(), entity.getCustomerId())) {
			return BaseResponse.failure("手机号已存在");
		}
		// 获取当前用户
		UserVo userVo = UserContext.getUser();
		entity.setUpdateTime(new Date());
		entity.setCreatePerson(userVo.getName());
		entity.setUpdatePerson(userVo.getName());
		entity.setIsDeleted(Boolean.FALSE);
		entity.setIsEnabled(Boolean.TRUE);

		this.driverService.add(entity);
		this.sysUserService.add(new SysUserAddParam(entity.getCustomerId(), entity.getMobile(), entity.getMobile(),
				entity.getDriverName(), Integer.valueOf(SysRoleConstant.DRIVER_DEFAULT_ROLE_ID)), this.request);
		// 获取当前用户
		this.operateLogAsync.pushAddLog(userVo, OperateModuleEnum.ARCHIVE_DRIVER_ADD, String.valueOf(entity.getId()),
				userVo, this.request);
		return BaseResponse.ok();
	}

	/**
	 * 司机档案-编辑
	 *
	 * @param entity 司机实体
	 * @return
	 */
	@SuppressWarnings({"boxing", "rawtypes"})
	@PostMapping("/edit")
	public BaseResponse edit(@RequestBody DriverEntity entity) {
		log.info("司机档案-编辑, url:/driver/edit, param:{}", JSON.toJSONString(entity));
		if(!this.driverService.checkDuplicate(entity.getId(), "mobile", entity.getMobile(), entity.getCustomerId())) {
			return BaseResponse.failure("手机号已存在");
		}
		DriverEntity oe = this.driverService.get(entity.getId());
		String oMobile = oe.getMobile();
		oe.setDriverName(entity.getDriverName());
		oe.setApprovedDrivingType(entity.getApprovedDrivingType());
		oe.setBornDate(entity.getBornDate());
		oe.setCertificateDate(entity.getCertificateDate());
		oe.setContactAddr(entity.getContactAddr());
		oe.setCustomerId(entity.getCustomerId());
		oe.setIsEnabled(entity.getIsEnabled());
		oe.setJoinDate(entity.getJoinDate());
		oe.setLicenseNumber(entity.getLicenseNumber());
		oe.setMobile(entity.getMobile());
		oe.setSex(entity.getSex());
		oe.setLicenseImage(entity.getLicenseImage());

		// 获取当前用户
		UserVo userVo = UserContext.getUser();
		oe.setUpdatePerson(userVo.getName());
		oe.setUpdateTime(new Date());
		int x = this.driverService.update(oe);

		if(!entity.getMobile().equals(oMobile)) {
			SysUser sysUser = this.sysUserService.getByAccount(oMobile);
			sysUser.setAccount(entity.getMobile());
			sysUser.setPhone(entity.getMobile());
			sysUser.setUpdatePerson(userVo.getName());
			this.sysUserService.updateById(sysUser);
		}

		this.operateLogAsync.pushAddLog(userVo, OperateModuleEnum.ARCHIVE_DRIVER_EDIT, String.valueOf(entity.getId()),
				userVo, this.request);
		return BaseResponse.ok(x);
	}

	/**
	 * 司机档案-删除
	 *
	 * @param id 司机ID
	 * @param phone 用户电话
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "boxing"})
	@PutMapping("/delete")
	public BaseResponse delete(Integer id, String phone) {
		log.info("司机档案-删除", "url:/driver/delete, id:{}, phone:{}", id, phone);
		// 获取当前用户
		UserVo userVo = UserContext.getUser();
		// 校验手机号
		if(!userVo.getPhone().equals(phone)) {
			return BaseResponse.failure("手机号错误，删除操作未执行");
		}
		this.sysUserService.delete(this.driverService.get(id).getMobile(), this.request);
		// 保存日志
		this.operateLogAsync.pushDeleteLog(OperateModuleEnum.ARCHIVE_DRIVER_DELETE, String.valueOf(id), userVo,
				this.request, null);
		driverService.delete(id);
		return BaseResponse.ok();
	}
}
