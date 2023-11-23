package org.platform.vehicle.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.platform.vehicle.aspect.RepeatSubmit;
import org.platform.vehicle.conf.VehicleSpecContext;
import org.platform.vehicle.constant.OperateModuleEnum;
import org.platform.vehicle.entity.VehicleSpecEntity;
import org.platform.vehicle.entity.VehicleSpecMaintenaceEntity;
import org.platform.vehicle.param.VehicleSpecQueryCondition;
import org.platform.vehicle.service.SysClientService;
import org.platform.vehicle.service.VehicleBrandService;
import org.platform.vehicle.service.VehicleSpecMaintenaceService;
import org.platform.vehicle.service.VehicleSpecService;
import org.platform.vehicle.util.OperateLogAsync;
import org.platform.vehicle.vo.context.VehicleSpecContextVo;
import org.platform.vehicle.constants.BaseConstant;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * 档案管理-车辆型号
 *
 * @Author Sunnykid
 */
@Slf4j
@RestController
@RequestMapping("/vehicleSpec")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VehicleSpecController {

    private final VehicleBrandService vehicleBrandService;
    private final VehicleSpecService vehicleSpecService;
    private final VehicleSpecMaintenaceService vehicleSpecMaintenaceService;
    private final OperateLogAsync operateLogAsync;
    private final HttpServletRequest request;
    private final SysClientService sysClientService;
    private final VehicleSpecContext vehicleSpecContext;


    /**
     * 车辆型号-条件查询
     *
     * @param param
     * @return
     */
    @PostMapping("/query")
    public BaseResponse<VehicleSpecEntity> query(@RequestBody VehicleSpecQueryCondition param) {
        log.info("车辆型号-条件查询, url:/vehicleSpec/query, param:{}", JSON.toJSONString(param));
        QueryWrapper<VehicleSpecEntity> q = param.wrapper();
        Set<Integer> customerIdScope = this.sysClientService.getAllRelatedIds();
        if (!customerIdScope.isEmpty()) {
            q.in("customer_id", customerIdScope);
        }
        Page<VehicleSpecEntity> page = this.vehicleSpecService.query(q, param.getPageNum(),
                param.getPageSize());
        for (VehicleSpecEntity vbe : page.getRecords()) {
            vbe.setCustomerName(
                    this.sysClientService.getCustomerInfo(vbe.getCustomerId()).getName());
            vbe.setBrandName(this.vehicleBrandService.get(vbe.getBrandId()).getBrandName());
            vbe.setMaintenaceList(this.vehicleSpecMaintenaceService
                    .query(new QueryWrapper<VehicleSpecMaintenaceEntity>().eq("spec_id",
                            vbe.getId())));
        }
        return BasePageResponse.ok(page);
    }

    /**
     * 车辆型号-查看详情
     *
     * @param id 车辆型号ID
     * @return
     */
    @GetMapping("/detail/{id}")
    public BaseResponse<VehicleSpecEntity> detail(@PathVariable("id") Integer id) {
        log.info("车辆型号-查看详情, url:/vehicleSpec/{}", id);
        VehicleSpecEntity vbe = this.vehicleSpecService.get(id);
        vbe.setCustomerName(this.sysClientService.getCustomerInfo(vbe.getCustomerId()).getName());
        vbe.setBrandName(this.vehicleBrandService.get(vbe.getBrandId()).getBrandName());
        vbe.setMaintenaceList(this.vehicleSpecMaintenaceService
                .query(new QueryWrapper<VehicleSpecMaintenaceEntity>().eq("spec_id", vbe.getId())));
        return BaseResponse.ok(vbe);
    }

    /**
     * 车辆型号-新增
     *
     * @param entity
     * @return
     */
    @SuppressWarnings("rawtypes")
    @RepeatSubmit()
    @PostMapping("/add")
    public BaseResponse add(@RequestBody VehicleSpecEntity entity) {
        log.info("车辆型号-新增, url:/vehicleSpec/add, param:{}", JSON.toJSONString(entity));
        if (!this.vehicleSpecService.checkDuplicate(null, "spec_name", entity.getSpecName(),
                entity.getCustomerId())) {
            return BaseResponse.failure("车辆型号已存在");
        }
        List<VehicleSpecMaintenaceEntity> vsmeList = entity.getMaintenaceList();

        // 获取当前用户
        UserVo userVo = UserContext.getUser();
        entity.setUpdateTime(new Date());
        entity.setCreatePerson(userVo.getName());
        entity.setUpdatePerson(userVo.getName());
        entity.setIsDeleted(Boolean.FALSE);

        this.vehicleSpecService.add(entity);
        if (vsmeList != null) {
            for (VehicleSpecMaintenaceEntity vsme : vsmeList) {
                vsme.setSpecId(entity.getId());
                this.vehicleSpecMaintenaceService.add(vsme);
            }
        }
        // 获取当前用户
        this.operateLogAsync.pushAddLog(userVo, OperateModuleEnum.ARCHIVE_TIRE_BRAND_ADD,
                String.valueOf(entity.getId()), userVo, this.request);

        // 更新车辆型号缓存
        this.updateVehicleSpecContext(entity);
        return BaseResponse.ok();
    }

    private void updateVehicleSpecContext(VehicleSpecEntity entity) {
        vehicleSpecContext.edit(entity);
    }

    /**
     * 车辆型号-编辑
     *
     * @param entity
     * @return
     */
    @SuppressWarnings({"boxing", "rawtypes"})
    @RepeatSubmit(lockField = "id")
    @PostMapping("/edit")
    public BaseResponse edit(@RequestBody VehicleSpecEntity entity) {
        log.info("车辆型号-编辑, url:/vehicleSpec/edit, param:{}", JSON.toJSONString(entity));
        if (!this.vehicleSpecService.checkDuplicate(entity.getId(), "spec_name",
                entity.getSpecName(),
                entity.getCustomerId())) {
            return BaseResponse.failure("车辆型号已存在");
        }
        this.vehicleSpecMaintenaceService
                .deleteByQueryWrapper(new QueryWrapper<VehicleSpecMaintenaceEntity>().eq("spec_id",
                        entity.getId()));
        List<VehicleSpecMaintenaceEntity> vsmeList = entity.getMaintenaceList();
        if (vsmeList != null) {
            for (VehicleSpecMaintenaceEntity vsme : vsmeList) {
                vsme.setSpecId(entity.getId());
                this.vehicleSpecMaintenaceService.add(vsme);
            }
        }

        VehicleSpecEntity oe = this.vehicleSpecService.get(entity.getId());
        entity.setCreatePerson(oe.getCreatePerson());
        entity.setCreateTime(oe.getCreateTime());
        entity.setIsDeleted(oe.getIsDeleted());
        // 获取当前用户
        UserVo userVo = UserContext.getUser();
        entity.setUpdatePerson(userVo.getName());

        // 获取当前用户
        this.operateLogAsync.pushAddLog(userVo, OperateModuleEnum.ARCHIVE_TIRE_BRAND_EDIT,
                String.valueOf(entity.getId()), userVo, this.request);

        // 更新车辆型号缓存
        this.updateVehicleSpecContext(entity);
        return BaseResponse.ok(this.vehicleSpecService.update(entity));
    }

    /**
     * 车辆型号-删除
     *
     * @param id    车辆型号ID
     * @param phone 用户电话
     * @return
     */
    @SuppressWarnings({"rawtypes", "boxing"})
    @PutMapping("/delete")
    public BaseResponse delete(Integer id, String phone) {
        log.info("车辆型号-删除", "url:/vehicleSpec/delete, id:{}, phone:{}", id, phone);
        // 获取当前用户
        UserVo userVo = UserContext.getUser();
        // 校验手机号
        if (!userVo.getPhone().equals(phone)) {
            return BaseResponse.failure("手机号错误，删除操作未执行");
        }
        // 保存日志
        this.operateLogAsync.pushDeleteLog(OperateModuleEnum.ARCHIVE_TIRE_BRAND_DELETE,
                String.valueOf(id), userVo,
                this.request, null);
        this.vehicleSpecMaintenaceService
                .deleteByQueryWrapper(
                        new QueryWrapper<VehicleSpecMaintenaceEntity>().eq("spec_id", id));
        this.vehicleSpecService.delete(id);
        return BaseResponse.ok();
    }

}
