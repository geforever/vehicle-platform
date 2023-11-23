package org.platform.vehicle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.platform.vehicle.entity.AssetTireDeviceBindRecord;
import org.platform.vehicle.mapper.AssetTireDeviceBindRecordMapper;
import org.platform.vehicle.param.AssetTireDeviceBindRecordConditionQueryParam;
import org.platform.vehicle.param.TireDeviceBindRecordAddParam;
import org.platform.vehicle.service.AssetTireDeviceBindRecordService;
import org.platform.vehicle.vo.AssetTireDeviceBindRecordPageVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.utils.phone.util.StringUtils;
import org.platform.vehicle.vo.UserVo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (AssetTireDeviceBindRecord)表服务实现类
 *
 * @author geforever
 * @since 2023-09-15 15:40:29
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssetTireDeviceBindRecordServiceImpl implements AssetTireDeviceBindRecordService {

    @Resource
    private AssetTireDeviceBindRecordMapper assetTireDeviceBindRecordMapper;

    /**
     * 资产管理-胎压部件绑定记录-新增
     *
     * @param param
     */
    @Override
    public BaseResponse save(TireDeviceBindRecordAddParam param) {
        UserVo user = UserContext.getUser();
        AssetTireDeviceBindRecord assetTireDeviceBindRecord = new AssetTireDeviceBindRecord();
        assetTireDeviceBindRecord.setCode(param.getCode());
        assetTireDeviceBindRecord.setClientId(param.getClientId());
        assetTireDeviceBindRecord.setFleetId(param.getFleetId());
        assetTireDeviceBindRecord.setDeviceType(param.getDeviceType());
        assetTireDeviceBindRecord.setLicensePlate(param.getLicensePlate());
        assetTireDeviceBindRecord.setTireSiteName(param.getTireSiteName());
        assetTireDeviceBindRecord.setTireCode(param.getTireCode());
        assetTireDeviceBindRecord.setCreatePerson(user.getName());
        assetTireDeviceBindRecordMapper.insert(assetTireDeviceBindRecord);
        return BaseResponse.ok();
    }

    /**
     * 资产管理-胎压部件绑定记录-条件查询
     *
     * @Param param
     * @Return
     */
    @Override
    public BasePageResponse<List<AssetTireDeviceBindRecordPageVo>> conditionQuery(
            AssetTireDeviceBindRecordConditionQueryParam param) {
        Page<AssetTireDeviceBindRecord> page = new Page<>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<AssetTireDeviceBindRecord> wrapper = this.getAssetTireDeviceBindWrapper(
                param);
        Page<AssetTireDeviceBindRecord> assetTireDeviceBindRecordPage = assetTireDeviceBindRecordMapper.selectPage(
                page, wrapper);
        List<AssetTireDeviceBindRecord> records = assetTireDeviceBindRecordPage.getRecords();
        List<AssetTireDeviceBindRecordPageVo> assetTireDeviceBindRecordPageVos = new ArrayList<>();
        for (AssetTireDeviceBindRecord record : records) {
            AssetTireDeviceBindRecordPageVo assetTireDeviceBindRecordPageVo = getAssetTireDeviceBindRecordPageVo(
                    record);
            assetTireDeviceBindRecordPageVos.add(assetTireDeviceBindRecordPageVo);
        }
        return BasePageResponse.ok(assetTireDeviceBindRecordPageVos, page);
    }

    private static AssetTireDeviceBindRecordPageVo getAssetTireDeviceBindRecordPageVo(
            AssetTireDeviceBindRecord record) {
        AssetTireDeviceBindRecordPageVo assetTireDeviceBindRecordPageVo = new AssetTireDeviceBindRecordPageVo();
        assetTireDeviceBindRecordPageVo.setId(record.getId());
        assetTireDeviceBindRecordPageVo.setCode(record.getCode());
        assetTireDeviceBindRecordPageVo.setDeviceType(record.getDeviceType());
        assetTireDeviceBindRecordPageVo.setLicensePlate(record.getLicensePlate());
        assetTireDeviceBindRecordPageVo.setTireSiteName(record.getTireSiteName());
        assetTireDeviceBindRecordPageVo.setTireCode(record.getTireCode());
        assetTireDeviceBindRecordPageVo.setCreatePerson(record.getCreatePerson());
        assetTireDeviceBindRecordPageVo.setCreateTime(record.getCreateTime());
        return assetTireDeviceBindRecordPageVo;
    }

    private LambdaQueryWrapper<AssetTireDeviceBindRecord> getAssetTireDeviceBindWrapper(
            AssetTireDeviceBindRecordConditionQueryParam param) {
        LambdaQueryWrapper<AssetTireDeviceBindRecord> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(param.getCode())) {
            wrapper.like(AssetTireDeviceBindRecord::getCode, param.getCode());
        }
        if (StringUtils.isNotBlank(param.getDeviceType())) {
            wrapper.eq(AssetTireDeviceBindRecord::getDeviceType, param.getDeviceType());
        }
        if (StringUtils.isNotBlank(param.getLicensePlate())) {
            wrapper.like(AssetTireDeviceBindRecord::getLicensePlate, param.getLicensePlate());
        }

        wrapper.orderByDesc(AssetTireDeviceBindRecord::getCreateTime);
        return wrapper;
    }
}
