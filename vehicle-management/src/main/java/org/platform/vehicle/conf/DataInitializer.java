package org.platform.vehicle.conf;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.platform.vehicle.entity.SysCustomer;
import org.platform.vehicle.entity.VehicleEntity;
import org.platform.vehicle.entity.VehicleSpecEntity;
import org.platform.vehicle.mapper.SysCustomerMapper;
import org.platform.vehicle.mapper.VehicleMapper;
import org.platform.vehicle.mapper.VehicleSpecMapper;
import org.platform.vehicle.vo.context.CustomerContextVo;
import org.platform.vehicle.vo.context.VehicleContextVo;
import org.platform.vehicle.vo.context.VehicleSpecContextVo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/10/31 14:23
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DataInitializer implements ApplicationRunner {

    private final VehicleMapper vehicleMapper;
    private final VehicleSpecMapper vehicleSpecMapper;
    private final SysCustomerMapper sysCustomerMapper;

    private final VehicleContext vehicleContext;
    private final VehicleSpecContext vehicleSpecContext;
    private final CustomerContext customerContext;
    private final TrailerVehicleContext trailerVehicleContext;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.initVehicleData();
    }

    public void initVehicleData() {
        log.info("初始化信息");
        // 开始时间
        long start = System.currentTimeMillis();
        this.initVehicle();
        this.initVehicleSpec();
        this.initCustomer();
        long end = System.currentTimeMillis();
        log.info("初始化信息完成, 耗时: {} ms", (end - start));
    }

    private void initVehicle() {
        vehicleContext.clean();
        trailerVehicleContext.clean();
        Long totalVehicleCount = vehicleMapper.selectCount(
                new LambdaQueryWrapper<VehicleEntity>()
                        .eq(VehicleEntity::getIsDeleted, false));
        int pageSize = 1000;
        int totalPage = (int) Math.ceil(totalVehicleCount / (double) pageSize);
        for (int i = 0; i < totalPage; i++) {
            List<VehicleEntity> vehicleList = vehicleMapper.selectList(
                    new LambdaQueryWrapper<VehicleEntity>()
                            .eq(VehicleEntity::getIsDeleted, false)
                            .select(VehicleEntity::getLicensePlate,
                                    VehicleEntity::getReceiverIdNumber,
                                    VehicleEntity::getRepeaterIdNumber,
                                    VehicleEntity::getFleetId,
                                    VehicleEntity::getSpecId)
                            .last("limit " + i * pageSize + "," + pageSize));
            this.initMainVehicle(vehicleList);
            this.initMinorVehicle(vehicleList);
        }
    }

    private void initMinorVehicle(List<VehicleEntity> vehicleList) {
        Map<String, VehicleContextVo> models = new HashMap<>();
        for (VehicleEntity vehicle : vehicleList) {
            if (StringUtils.isNotBlank(vehicle.getRepeaterIdNumber())
                    && vehicle.getFleetId() != null) {
                VehicleContextVo vehicleContextVo = new VehicleContextVo();
                vehicleContextVo.setLicensePlate(vehicle.getLicensePlate());
                vehicleContextVo.setFleetId(vehicle.getFleetId());
                vehicleContextVo.setSpecId(vehicle.getSpecId());
                models.put(vehicle.getRepeaterIdNumber(), vehicleContextVo);
            }
        }
        trailerVehicleContext.init(models);
    }

    private void initMainVehicle(List<VehicleEntity> vehicleList) {
        Map<String, VehicleContextVo> models = new HashMap<>();
        for (VehicleEntity vehicle : vehicleList) {
            if (StringUtils.isNotBlank(vehicle.getReceiverIdNumber())
                    && vehicle.getFleetId() != null) {
                VehicleContextVo vehicleContextVo = new VehicleContextVo();
                vehicleContextVo.setLicensePlate(vehicle.getLicensePlate());
                vehicleContextVo.setFleetId(vehicle.getFleetId());
                vehicleContextVo.setSpecId(vehicle.getSpecId());
                models.put(vehicle.getReceiverIdNumber(), vehicleContextVo);
            }
        }
        vehicleContext.init(models);
    }

    private void initCustomer() {
        List<SysCustomer> sysCustomerList = sysCustomerMapper.selectList(
                new LambdaQueryWrapper<SysCustomer>()
                        .eq(SysCustomer::getIsDelete, false));
        Map<String, CustomerContextVo> customerModels = new HashMap<>();
        for (SysCustomer sysCustomer : sysCustomerList) {
            CustomerContextVo customerContextVo = new CustomerContextVo();
            customerContextVo.setId(sysCustomer.getId());
            customerContextVo.setName(sysCustomer.getName());
            customerContextVo.setCompanyId(sysCustomer.getCompanyId());
            customerContextVo.setType(sysCustomer.getType());
            customerModels.put(String.valueOf(sysCustomer.getId()), customerContextVo);
        }
        customerContext.init(customerModels);
    }

    private void initVehicleSpec() {
        List<VehicleSpecEntity> vehicleSpecList = vehicleSpecMapper.selectList(
                new LambdaQueryWrapper<VehicleSpecEntity>()
                        .eq(VehicleSpecEntity::getIsDeleted, false));
        Map<String, VehicleSpecContextVo> vehicleModels = new HashMap<>();
        for (VehicleSpecEntity vehicleSpec : vehicleSpecList) {
            VehicleSpecContextVo vehicleSpecContextVo = new VehicleSpecContextVo();
            vehicleSpecContextVo.setSpecId(vehicleSpec.getId());
            vehicleSpecContextVo.setSpecType(vehicleSpec.getSpecType());
            vehicleSpecContextVo.setWheelCount(vehicleSpec.getWheelCount());
            vehicleSpecContextVo.setWheelbaseType(vehicleSpec.getWheelbaseType());
            vehicleSpecContextVo.setWheelArrange(vehicleSpec.getWheelArrange());
            vehicleSpecContextVo.setLowPressureAlarmLevel1(vehicleSpec.getLowPressureAlarmLevel1());
            vehicleSpecContextVo.setHighPressureAlarmLevel1(
                    vehicleSpec.getHighPressureAlarmLevel1());
            vehicleSpecContextVo.setHighTemperatureAlarmLevel1(
                    vehicleSpec.getHighTemperatureAlarmLevel1());
            vehicleSpecContextVo.setLowPressureAlarmLevel2(vehicleSpec.getLowPressureAlarmLevel2());
            vehicleSpecContextVo.setHighPressureAlarmLevel2(
                    vehicleSpec.getHighPressureAlarmLevel2());
            vehicleSpecContextVo.setHighTemperatureAlarmLevel2(
                    vehicleSpec.getHighTemperatureAlarmLevel2());
            vehicleSpecContextVo.setEnableLev2Alarm(vehicleSpec.getEnableLev2Alarm());
            vehicleModels.put(String.valueOf(vehicleSpec.getId()), vehicleSpecContextVo);
        }
        vehicleSpecContext.init(vehicleModels);
    }

}
