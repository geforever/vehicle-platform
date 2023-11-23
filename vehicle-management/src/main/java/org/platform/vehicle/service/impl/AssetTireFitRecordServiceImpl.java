package org.platform.vehicle.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.platform.vehicle.conf.ExcelCellWidthStyleStrategy;
import org.platform.vehicle.constant.AssetTireConstant;
import org.platform.vehicle.entity.AssetTireFitRecord;
import org.platform.vehicle.mapper.AssetTireFitRecordMapper;
import org.platform.vehicle.param.AssetTireFitRecordConditionQueryParam;
import org.platform.vehicle.service.AssetTireFitRecordService;
import org.platform.vehicle.util.EasyExcelUtils;
import org.platform.vehicle.vo.AssetTireFitRecordExportVo;
import org.platform.vehicle.vo.AssetTireFitRecordVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.utils.DateUtils;
import org.platform.vehicle.utils.UserContext;
import org.platform.vehicle.vo.UserVo;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (AssetTireFitRecord)表服务实现类
 *
 * @author geforever
 * @since 2023-09-19 15:26:22
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssetTireFitRecordServiceImpl implements AssetTireFitRecordService {

    private final AssetTireFitRecordMapper assetTireFitRecordMapper;

    /**
     * 资产管理-轮胎装卸记录-条件查询
     *
     * @param param
     * @return
     */
    @Override
    public BasePageResponse conditionQuery(AssetTireFitRecordConditionQueryParam param) {
        Page<AssetTireFitRecord> page = new Page<>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<AssetTireFitRecord> queryWrapper = this.getAssetTireFitRecordLambdaQueryWrapper(
                param);
        Page<AssetTireFitRecord> assetTireFitRecordPage = assetTireFitRecordMapper.selectPage(page,
                queryWrapper);
        List<AssetTireFitRecord> records = assetTireFitRecordPage.getRecords();
        List<AssetTireFitRecordVo> assetTireFitRecordVoList = new ArrayList<>();
        for (AssetTireFitRecord record : records) {
            AssetTireFitRecordVo assetTireFitRecordVo = this.getAssetTireFitRecordVo(record);
            assetTireFitRecordVoList.add(assetTireFitRecordVo);
        }
        return BasePageResponse.ok(assetTireFitRecordVoList, page);
    }

    /**
     * 资产管理-轮胎装卸记录-导出
     *
     * @param param
     */
    @Override
    public void export(AssetTireFitRecordConditionQueryParam param, HttpServletResponse response) {
        LambdaQueryWrapper<AssetTireFitRecord> queryWrapper = this.getAssetTireFitRecordLambdaQueryWrapper(
                param);
        List<AssetTireFitRecord> assetTireFitRecordList = assetTireFitRecordMapper.selectList(
                queryWrapper);
        List<AssetTireFitRecordExportVo> assetTireFitRecordExportVoList = new ArrayList<>();
        for (AssetTireFitRecord assetTireFitRecord : assetTireFitRecordList) {
            AssetTireFitRecordExportVo assetTireFitRecordExportVo = new AssetTireFitRecordExportVo();
            assetTireFitRecordExportVo.setClientName(assetTireFitRecord.getClientName());
            assetTireFitRecordExportVo.setFleetName(assetTireFitRecord.getFleetName());
            assetTireFitRecordExportVo.setLicensePlate(assetTireFitRecord.getLicensePlate());
            assetTireFitRecordExportVo.setTireCode(assetTireFitRecord.getTireCode());
            assetTireFitRecordExportVo.setTireSiteName(assetTireFitRecord.getTireSiteName());
            assetTireFitRecordExportVo.setBrandName(assetTireFitRecord.getBrandName());
            // 类型:1-安装,2-拆卸
            String typeStr = "";
            if (assetTireFitRecord.getType() == AssetTireConstant.TIRE_INSTALL) {
                typeStr = "安装";
            } else if (assetTireFitRecord.getType() == AssetTireConstant.TIRE_UNINSTALL) {
                typeStr = "拆卸";
            }
            assetTireFitRecordExportVo.setTypeStr(typeStr);
            assetTireFitRecordExportVo.setCreatePerson(assetTireFitRecord.getCreatePerson());
            assetTireFitRecordExportVo.setCreateTime(
                    DateUtils.dateToStr(assetTireFitRecord.getCreateTime()));
            assetTireFitRecordExportVoList.add(assetTireFitRecordExportVo);
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String fileName = URLEncoder.encode("轮胎装卸记录", "UTF-8")
                    .replaceAll("\\+", "%20");
            response.setHeader("Content-disposition",
                    "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            EasyExcel.write(response.getOutputStream(), AssetTireFitRecordExportVo.class)
                    .autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(new ExcelCellWidthStyleStrategy())
                    .registerWriteHandler(EasyExcelUtils.getTitleStyle())
                    .sheet("sheet_1")
                    .doWrite(assetTireFitRecordExportVoList);
        } catch (Exception e) {
            e.printStackTrace();
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>(2);
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            try {
                response.getWriter().println(JSON.toJSONString(map));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private AssetTireFitRecordVo getAssetTireFitRecordVo(AssetTireFitRecord record) {
        AssetTireFitRecordVo assetTireFitRecordVo = new AssetTireFitRecordVo();
        assetTireFitRecordVo.setId(record.getId());
        assetTireFitRecordVo.setClientName(record.getClientName());
        assetTireFitRecordVo.setFleetName(record.getFleetName());
        assetTireFitRecordVo.setLicensePlate(record.getLicensePlate());
        assetTireFitRecordVo.setTireCode(record.getTireCode());
        assetTireFitRecordVo.setTireSiteName(record.getTireSiteName());
        assetTireFitRecordVo.setBrandName(record.getBrandName());
        assetTireFitRecordVo.setType(record.getType());
        assetTireFitRecordVo.setCreatePerson(record.getCreatePerson());
        assetTireFitRecordVo.setCreateTime(record.getCreateTime());
        return assetTireFitRecordVo;
    }

    private LambdaQueryWrapper<AssetTireFitRecord> getAssetTireFitRecordLambdaQueryWrapper(
            AssetTireFitRecordConditionQueryParam param) {
        UserVo user = UserContext.getUser();
        LambdaQueryWrapper<AssetTireFitRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AssetTireFitRecord::getFleetId, user.getCustomerIds());
        if (StringUtils.isNotBlank(param.getFleetName())) {
            queryWrapper.like(AssetTireFitRecord::getFleetName, param.getFleetName());
        }
        if (StringUtils.isNotBlank(param.getLicensePlate())) {
            queryWrapper.like(AssetTireFitRecord::getLicensePlate, param.getLicensePlate());
        }
        if (StringUtils.isNotBlank(param.getTireCode())) {
            queryWrapper.like(AssetTireFitRecord::getTireCode, param.getTireCode());
        }
        if (param.getType() != null) {
            queryWrapper.eq(AssetTireFitRecord::getType, param.getType());
        }
        if (param.getCreateStartTime() != null) {
            queryWrapper.ge(AssetTireFitRecord::getCreateTime, param.getCreateStartTime());
        }
        if (param.getCreateEndTime() != null) {
            queryWrapper.le(AssetTireFitRecord::getCreateTime, param.getCreateEndTime());
        }
        queryWrapper.orderByDesc(AssetTireFitRecord::getCreateTime);
        return queryWrapper;
    }
}
