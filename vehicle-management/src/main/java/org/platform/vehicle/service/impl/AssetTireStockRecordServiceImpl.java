package org.platform.vehicle.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.platform.vehicle.conf.ExcelCellWidthStyleStrategy;
import org.platform.vehicle.entity.AssetTireStockRecord;
import org.platform.vehicle.mapper.AssetTireStockRecordMapper;
import org.platform.vehicle.param.AssetTireStockRecordConditionQueryParam;
import org.platform.vehicle.service.AssetTireStockRecordService;
import org.platform.vehicle.util.EasyExcelUtils;
import org.platform.vehicle.vo.AssetTireStockInRecordExportVo;
import org.platform.vehicle.vo.AssetTireStockOutRecordExportVo;
import org.platform.vehicle.vo.AssetTireStockRecordPageVo;
import org.platform.vehicle.exception.BaseException;
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
 * (AssetTire)表服务实现类
 *
 * @author geforever
 * @since 2023-09-14 14:42:31
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssetTireStockRecordServiceImpl implements AssetTireStockRecordService {

    private final AssetTireStockRecordMapper assetTireStockRecordMapper;

    /**
     * 资产管理-轮胎出入库记录-分页查询
     *
     * @param param
     * @return
     */
    @Override
    public BasePageResponse<List<AssetTireStockRecordPageVo>> conditionQuery(
            AssetTireStockRecordConditionQueryParam param) {
        if (param.getType() == null) {
            throw new BaseException("99999", "type不能为空");
        }
        Page<AssetTireStockRecord> page = new Page<>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<AssetTireStockRecord> wrapper = this.getAssetTireStockRecordWrapper(
                param);
        Page<AssetTireStockRecord> assetTireStockRecordPage = assetTireStockRecordMapper.selectPage(
                page, wrapper);
        List<AssetTireStockRecord> records = assetTireStockRecordPage.getRecords();
        List<AssetTireStockRecordPageVo> assetTireStockRecordPageVoList = new ArrayList<>();
        for (AssetTireStockRecord record : records) {
            AssetTireStockRecordPageVo assetTireStockRecordPageVo = this.getAssetTireStockRecordPageVo(
                    record);
            assetTireStockRecordPageVoList.add(assetTireStockRecordPageVo);
        }
        return BasePageResponse.ok(assetTireStockRecordPageVoList, page);
    }

    /**
     * 资产管理-轮胎出入库记录-导出
     *
     * @param param
     * @return
     */
    @Override
    public void stockInExport(AssetTireStockRecordConditionQueryParam param,
            HttpServletResponse response) {
        LambdaQueryWrapper<AssetTireStockRecord> wrapper = this.getAssetTireStockRecordWrapper(
                param);
        List<AssetTireStockRecord> assetTireStockRecordList = assetTireStockRecordMapper.selectList(
                wrapper);
        List<AssetTireStockInRecordExportVo> exportVoList = new ArrayList<>();
        for (AssetTireStockRecord assetTireStockRecord : assetTireStockRecordList) {
            AssetTireStockInRecordExportVo assetTireStockInRecordExportVo = new AssetTireStockInRecordExportVo();
            assetTireStockInRecordExportVo.setTireCode(assetTireStockRecord.getTireCode());
            assetTireStockInRecordExportVo.setCustomerName(assetTireStockRecord.getClientName());
            assetTireStockInRecordExportVo.setFleetName(assetTireStockRecord.getFleetName());
            assetTireStockInRecordExportVo.setWarehouseName(
                    assetTireStockRecord.getWarehouseName());
            String stockTypeStr = this.getStockInTypeStr(assetTireStockRecord.getStockType());
            assetTireStockInRecordExportVo.setStockTypeStr(stockTypeStr);
            assetTireStockInRecordExportVo.setTarget(assetTireStockRecord.getTarget());
            assetTireStockInRecordExportVo.setTireBrand(assetTireStockRecord.getTireBrand());
            assetTireStockInRecordExportVo.setTireSpec(assetTireStockRecord.getTireSpec());
            assetTireStockInRecordExportVo.setDegree(assetTireStockRecord.getDegree());
            assetTireStockInRecordExportVo.setMileage(
                    String.valueOf(assetTireStockRecord.getMileage()));
            assetTireStockInRecordExportVo.setCreatePerson(assetTireStockRecord.getCreatePerson());
            assetTireStockInRecordExportVo.setCreateTime(
                    DateUtils.dateToStr(assetTireStockRecord.getCreateTime()));
            exportVoList.add(assetTireStockInRecordExportVo);
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String fileName = URLEncoder.encode("轮胎入库记录", "UTF-8")
                    .replaceAll("\\+", "%20");
            response.setHeader("Content-disposition",
                    "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            EasyExcel.write(response.getOutputStream(), AssetTireStockInRecordExportVo.class)
                    .autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(new ExcelCellWidthStyleStrategy())
                    .registerWriteHandler(EasyExcelUtils.getTitleStyle())
                    .sheet("sheet_1")
                    .doWrite(exportVoList);
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

    /**
     * 资产管理-轮胎出库记录-导出
     *
     * @param param
     * @return
     */
    @Override
    public void stockOutExport(AssetTireStockRecordConditionQueryParam param,
            HttpServletResponse response) {
        LambdaQueryWrapper<AssetTireStockRecord> wrapper = this.getAssetTireStockRecordWrapper(
                param);
        List<AssetTireStockRecord> assetTireStockRecordList = assetTireStockRecordMapper.selectList(
                wrapper);
        List<AssetTireStockOutRecordExportVo> exportVoList = new ArrayList<>();
        for (AssetTireStockRecord assetTireStockRecord : assetTireStockRecordList) {
            AssetTireStockOutRecordExportVo assetTireStockOutRecordExportVo = new AssetTireStockOutRecordExportVo();
            assetTireStockOutRecordExportVo.setTireCode(assetTireStockRecord.getTireCode());
            assetTireStockOutRecordExportVo.setCustomerName(assetTireStockRecord.getClientName());
            assetTireStockOutRecordExportVo.setFleetName(assetTireStockRecord.getFleetName());
            assetTireStockOutRecordExportVo.setWarehouseName(
                    assetTireStockRecord.getWarehouseName());
            String stockTypeStr = this.getStockOutTypeStr(assetTireStockRecord.getStockType());
            assetTireStockOutRecordExportVo.setStockTypeStr(stockTypeStr);
            assetTireStockOutRecordExportVo.setTireBrand(assetTireStockRecord.getTireBrand());
            assetTireStockOutRecordExportVo.setTireSpec(assetTireStockRecord.getTireSpec());
            assetTireStockOutRecordExportVo.setDegree(assetTireStockRecord.getDegree());
            assetTireStockOutRecordExportVo.setMileage(
                    String.valueOf(assetTireStockRecord.getMileage()));
            assetTireStockOutRecordExportVo.setTarget(assetTireStockRecord.getTarget());
            assetTireStockOutRecordExportVo.setCreatePerson(assetTireStockRecord.getCreatePerson());
            assetTireStockOutRecordExportVo.setCreateTime(
                    DateUtils.dateToStr(assetTireStockRecord.getCreateTime()));
            exportVoList.add(assetTireStockOutRecordExportVo);
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String fileName = URLEncoder.encode("轮胎出库记录", "UTF-8")
                    .replaceAll("\\+", "%20");
            response.setHeader("Content-disposition",
                    "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            EasyExcel.write(response.getOutputStream(), AssetTireStockOutRecordExportVo.class)
                    .autoCloseStream(Boolean.FALSE)
                    .registerWriteHandler(new ExcelCellWidthStyleStrategy())
                    .registerWriteHandler(EasyExcelUtils.getTitleStyle())
                    .sheet("sheet_1")
                    .doWrite(exportVoList);
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

    /**
     * 入库类型:1:采集,2赔付,3:拆卸入库,4-调拨入库
     *
     * @param stockType
     * @return
     */
    private String getStockInTypeStr(Integer stockType) {
        String stockTypeStr = "";
        switch (stockType) {
            case 1:
                stockTypeStr = "采集";
                break;
            case 2:
                stockTypeStr = "赔付";
                break;
            case 3:
                stockTypeStr = "拆卸入库";
                break;
            case 4:
                stockTypeStr = "调拨入库";
                break;
            default:
                break;
        }
        return stockTypeStr;
    }

    /**
     * 出库类型:1:安装出库,2-调拨出库,3-变卖出库
     *
     * @param stockType
     * @return
     */
    private String getStockOutTypeStr(Integer stockType) {
        String stockTypeStr = "";
        switch (stockType) {
            case 1:
                stockTypeStr = "安装出库";
                break;
            case 2:
                stockTypeStr = "调拨出库";
                break;
            case 3:
                stockTypeStr = "变卖出库";
                break;
            default:
                break;
        }
        return stockTypeStr;
    }

    private AssetTireStockRecordPageVo getAssetTireStockRecordPageVo(AssetTireStockRecord record) {
        AssetTireStockRecordPageVo assetTireStockRecordPageVo = new AssetTireStockRecordPageVo();
        assetTireStockRecordPageVo.setId(record.getId());
        assetTireStockRecordPageVo.setTireCode(record.getTireCode());
        assetTireStockRecordPageVo.setClientName(record.getClientName());
        assetTireStockRecordPageVo.setFleetName(record.getFleetName());
        assetTireStockRecordPageVo.setWarehouseName(record.getWarehouseName());
        assetTireStockRecordPageVo.setType(record.getType());
        assetTireStockRecordPageVo.setStockType(record.getStockType());
        assetTireStockRecordPageVo.setTarget(record.getTarget());
        assetTireStockRecordPageVo.setTireBrand(record.getTireBrand());
        assetTireStockRecordPageVo.setTireSpec(record.getTireSpec());
        assetTireStockRecordPageVo.setDegree(record.getDegree());
        assetTireStockRecordPageVo.setMileage(record.getMileage());
        assetTireStockRecordPageVo.setRemark(record.getRemark());
        assetTireStockRecordPageVo.setCreatePerson(record.getCreatePerson());
        assetTireStockRecordPageVo.setCreateTime(record.getCreateTime());
        return assetTireStockRecordPageVo;
    }

    private LambdaQueryWrapper<AssetTireStockRecord> getAssetTireStockRecordWrapper(
            AssetTireStockRecordConditionQueryParam param) {
        UserVo user = UserContext.getUser();
        LambdaQueryWrapper<AssetTireStockRecord> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(param.getTireCode())) {
            wrapper.like(AssetTireStockRecord::getTireCode, param.getTireCode());
        }
        if (StringUtils.isNotBlank(param.getFleetName())) {
            wrapper.like(AssetTireStockRecord::getFleetName, param.getFleetName());
        }
        if (param.getStockType() != null) {
            wrapper.eq(AssetTireStockRecord::getStockType, param.getStockType());
        }
        wrapper.eq(AssetTireStockRecord::getType, param.getType());
        wrapper.in(AssetTireStockRecord::getFleetId, user.getCustomerIds());
        wrapper.orderByDesc(AssetTireStockRecord::getCreateTime);
        return wrapper;
    }
}
