package org.platform.vehicle.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.ArrayList;
import org.platform.vehicle.conf.TrailerVehicleContext;
import org.platform.vehicle.conf.VehicleContext;
import org.platform.vehicle.constant.OperateModuleEnum;
import org.platform.vehicle.entity.VehicleEntity;
import org.platform.vehicle.entity.VehicleImageEntity;
import org.platform.vehicle.param.VehicleQueryCondition;
import org.platform.vehicle.service.SysClientService;
import org.platform.vehicle.service.VehicleBrandService;
import org.platform.vehicle.service.VehicleImageService;
import org.platform.vehicle.service.VehicleService;
import org.platform.vehicle.service.VehicleSpecService;
import org.platform.vehicle.util.OperateLogAsync;
import org.platform.vehicle.constants.BaseConstant;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.utils.phone.util.StringUtils;
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
 * 档案管理-车辆档案
 *
 * @Author Sunnykid
 */
@Slf4j
@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleImageService vehicleImageService;
    private final OperateLogAsync operateLogAsync;
    private final HttpServletRequest request;
    private final SysClientService sysClientService;
    private final VehicleBrandService vehicleBrandService;
    private final VehicleSpecService vehicleSpecService;
    private final VehicleContext vehicleContext;
    private final TrailerVehicleContext trailerVehicleContext;

    /**
     * 车辆档案-条件查询
     *
     * @param param
     * @return
     */
    @PostMapping("/query")
    public BaseResponse<VehicleEntity> query(@RequestBody VehicleQueryCondition param) {
        log.info("车辆档案-条件查询, url:/vehicle/query, param:{}", JSON.toJSONString(param));
        QueryWrapper<VehicleEntity> q = param.wrapper();
        Set<Integer> customerIdScope = this.sysClientService.getAllRelatedIds();
        if (!customerIdScope.isEmpty()) {
            q.in("customer_id", customerIdScope);
        }
        Page<VehicleEntity> page = this.vehicleService.query(q, param.getPageNum(),
                param.getPageSize());
        for (VehicleEntity vbe : page.getRecords()) {
            vbe.setCustomerName(this.sysClientService.getById(vbe.getCustomerId()).getName());
            vbe.setFleetName(this.sysClientService.getById(vbe.getFleetId()).getName());
            vbe.setBrandName(this.vehicleBrandService.get(vbe.getBrandId()).getBrandName());
            vbe.setSpecName(this.vehicleSpecService.get(vbe.getSpecId()).getSpecName());
            List<String> imageList = new ArrayList<>();
            List<VehicleImageEntity> vehicleImageList = this.vehicleImageService
                    .query(new QueryWrapper<VehicleImageEntity>().eq("vehicle_id",
                            vbe.getId()));
            for (VehicleImageEntity vehicleImageEntity : vehicleImageList) {
                imageList.add(vehicleImageEntity.getImageFile());
            }
            vbe.setImageList(imageList);
        }
        return BasePageResponse.ok(page);
    }

    /**
     * 车辆档案-查看详情
     *
     * @param id 车辆ID
     * @return
     */
    @GetMapping("/detail/{id}")
    public BaseResponse<VehicleEntity> detail(@PathVariable("id") Integer id) {
        log.info("车辆档案-查看详情, url:/vehicle/{}", id);
        VehicleEntity vbe = this.vehicleService.get(id);
        vbe.setCustomerName(this.sysClientService.getById(vbe.getCustomerId()).getName());
        vbe.setFleetName(this.sysClientService.getById(vbe.getFleetId()).getName());
        vbe.setBrandName(this.vehicleBrandService.get(vbe.getBrandId()).getBrandName());
        vbe.setSpecName(this.vehicleSpecService.get(vbe.getSpecId()).getSpecName());
        List<String> imageList = new ArrayList<>();
        List<VehicleImageEntity> vehicleImageList = this.vehicleImageService
                .query(new QueryWrapper<VehicleImageEntity>().eq("vehicle_id",
                        vbe.getId()));
        for (VehicleImageEntity vehicleImageEntity : vehicleImageList) {
            imageList.add(vehicleImageEntity.getImageFile());
        }
        vbe.setImageList(imageList);
        return BaseResponse.ok(vbe);
    }

    /**
     * 车辆档案-新增
     *
     * @param entity
     * @return
     */
    @SuppressWarnings("rawtypes")
    //@RepeatSubmit()
    @PostMapping("/add")
    public BaseResponse add(@RequestBody VehicleEntity entity) {
        log.info("车辆档案-新增, url:/vehicle/add, param:{}", JSON.toJSONString(entity));
        if (!this.vehicleService.checkDuplicate(null, "vehicle_id_number",
                entity.getVehicleIdNumber(),
                entity.getCustomerId())) {
            return BaseResponse.failure("车辆识别号已存在");
        }
        if (!this.vehicleService.checkDuplicate(null, "engine_number", entity.getEngineNumber(),
                entity.getCustomerId())) {
            return BaseResponse.failure("发动机号已存在");
        }
        if (!this.vehicleService.checkDuplicate(null, "license_plate", entity.getLicensePlate(),
                entity.getCustomerId(),
                "gua_license_plate")) {
            return BaseResponse.failure("车牌号已存在");
        }
        if (StringUtils.isNotBlank(entity.getGuaLicensePlate())) {
            if (!this.vehicleService.checkDuplicate(null, "gua_license_plate",
                    entity.getGuaLicensePlate(),
                    entity.getCustomerId(), "license_plate")) {
                return BaseResponse.failure("挂车车牌号已存在");
            }
        }
        if (!this.vehicleService.checkDuplicate(null, "repeater_id_number",
                entity.getRepeaterIdNumber(),
                entity.getCustomerId(), "trailer_repeater_id_number")) {
            return BaseResponse.failure("中继器ID已存在");
        }

        if (StringUtils.isNotBlank(entity.getTrailerRepeaterIdNumber())) {
            if (!this.vehicleService.checkDuplicate(null, "trailer_repeater_id_number",
                    entity.getTrailerRepeaterIdNumber(), entity.getCustomerId(),
                    "repeater_id_number")) {
                return BaseResponse.failure("挂车中继器ID已存在");
            }
        }
        if (!this.vehicleService.checkDuplicate(null, "receiver_id_number",
                entity.getRepeaterIdNumber(),
                entity.getCustomerId())) {
            return BaseResponse.failure("接收器ID已存在");
        }
        List<String> images = entity.getImageList();

        // 获取当前用户
        UserVo userVo = UserContext.getUser();
        entity.setUpdateTime(new Date());
        entity.setCreatePerson(userVo.getName());
        entity.setUpdatePerson(userVo.getName());
        entity.setIsDeleted(Boolean.FALSE);
        entity.setIsEnabled(Boolean.TRUE);
        entity.setCustomerId(this.sysClientService.getById(entity.getFleetId()).getCompanyId());
        this.vehicleService.add(entity);
        if (images != null) {
            for (String image : images) {
                VehicleImageEntity vie = new VehicleImageEntity(entity.getId(), image);
                this.vehicleImageService.add(vie);
            }
        }
        // 获取当前用户
        this.operateLogAsync.pushAddLog(userVo, OperateModuleEnum.ARCHIVE_TIRE_BRAND_ADD,
                String.valueOf(entity.getId()), userVo, this.request);
        // 更新缓存
        vehicleContext.add(entity);
        trailerVehicleContext.add(entity);
        return BaseResponse.ok();
    }

    /**
     * 车辆档案-编辑
     *
     * @param entity
     * @return
     */
    @SuppressWarnings({"boxing", "rawtypes"})
    //@RepeatSubmit(lockField = "id")
    @PostMapping("/edit")
    public BaseResponse edit(@RequestBody VehicleEntity entity) {

        System.out.println("==========================");

        System.out.println(entity);

        System.out.println("==========================");

        log.info("车辆档案-编辑, url:/vehicle/edit, param:{}", JSON.toJSONString(entity));
        VehicleEntity vehicleHistory = vehicleService.get(entity.getId());
        if (!this.vehicleService.checkDuplicate(entity.getId(), "vehicle_id_number",
                entity.getVehicleIdNumber(),
                entity.getCustomerId())) {
            return BaseResponse.failure("车辆识别号已存在");
        }
        if (!this.vehicleService.checkDuplicate(entity.getId(), "engine_number",
                entity.getEngineNumber(),
                entity.getCustomerId())) {
            return BaseResponse.failure("发动机号已存在");
        }
        if (!this.vehicleService.checkDuplicate(entity.getId(), "license_plate",
                entity.getLicensePlate(),
                entity.getCustomerId(), "gua_license_plate")) {
            return BaseResponse.failure("车牌号已存在");
        }
        if (StringUtils.isNotBlank(entity.getGuaLicensePlate())) {
            if (!this.vehicleService.checkDuplicate(entity.getId(), "gua_license_plate",
                    entity.getGuaLicensePlate(),
                    entity.getCustomerId(), "license_plate")) {
                return BaseResponse.failure("挂车车牌号已存在");
            }
        }
        if (!this.vehicleService.checkDuplicate(entity.getId(), "repeater_id_number",
                entity.getRepeaterIdNumber(),
                entity.getCustomerId(), "trailer_repeater_id_number")) {
            return BaseResponse.failure("中继器ID已存在");
        }

        if (StringUtils.isNotBlank(entity.getTrailerRepeaterIdNumber())) {
            if (!this.vehicleService.checkDuplicate(entity.getId(), "trailer_repeater_id_number",
                    entity.getTrailerRepeaterIdNumber(), entity.getCustomerId(),
                    "repeater_id_number")) {
                return BaseResponse.failure("挂车中继器ID已存在");
            }
        }
        if (!this.vehicleService.checkDuplicate(entity.getId(), "receiver_id_number",
                entity.getRepeaterIdNumber(),
                entity.getCustomerId())) {
            return BaseResponse.failure("接收器ID已存在");
        }
        this.vehicleImageService
                .deleteByQueryWrapper(
                        new QueryWrapper<VehicleImageEntity>().eq("vehicle_id", entity.getId()));
        List<String> images = entity.getImageList();
        if (images != null) {
            for (String image : images) {
                VehicleImageEntity vie = new VehicleImageEntity(entity.getId(), image);
                this.vehicleImageService.add(vie);
            }
        }

        VehicleEntity oe = this.vehicleService.get(entity.getId());
        entity.setCreatePerson(oe.getCreatePerson());
        entity.setCreateTime(oe.getCreateTime());
        entity.setIsDeleted(oe.getIsDeleted());
        entity.setIsEnabled(oe.getIsEnabled());
        entity.setCustomerId(this.sysClientService.getById(entity.getFleetId()).getCompanyId());
//        // 获取当前用户
//        UserVo userVo = UserContext.getUser();
//        entity.setUpdatePerson(userVo.getName());
//        int x = this.vehicleService.update(entity);
//
//        this.operateLogAsync.pushAddLog(userVo, OperateModuleEnum.ARCHIVE_TIRE_BRAND_EDIT,
//                String.valueOf(entity.getId()), userVo, this.request);
//        return BaseResponse.ok(x);

        // 更新缓存
        vehicleContext.edit(vehicleHistory, entity);
        trailerVehicleContext.edit(vehicleHistory, entity);
        return BaseResponse.ok(this.vehicleService.update(entity));
    }

    /**
     * 车辆档案-删除
     *
     * @param id    车辆ID
     * @param phone 用户电话
     * @return
     */
    @SuppressWarnings({"rawtypes", "boxing"})
    @PutMapping("/delete")
    public BaseResponse delete(Integer id, String phone) {
        log.info("车辆档案-删除", "url:/vehicle/delete, id:{}, phone:{}", id, phone);
        // 获取当前用户
        UserVo userVo = UserContext.getUser();
        // 校验手机号
        if (!userVo.getPhone().equals(phone)) {
            return BaseResponse.failure("手机号错误，删除操作未执行");
        }
        VehicleEntity vehicle = this.vehicleService.get(id);
        // 保存日志
        this.operateLogAsync.pushDeleteLog(OperateModuleEnum.ARCHIVE_TIRE_BRAND_DELETE,
                String.valueOf(id), userVo,
                this.request, null);
        this.vehicleImageService.deleteByQueryWrapper(
                new QueryWrapper<VehicleImageEntity>().eq("vehicle_id", id));
        if (vehicle != null) {
            if (StringUtils.isNotBlank(vehicle.getReceiverIdNumber())) {
                vehicleContext.remove(vehicle.getReceiverIdNumber());
            }
            if (StringUtils.isNotBlank(vehicle.getRepeaterIdNumber())) {
                trailerVehicleContext.remove(vehicle.getRepeaterIdNumber());
            }
        }
        vehicleService.delete(id);
        return BaseResponse.ok();
    }

}
