package org.platform.vehicle.service;

import org.platform.vehicle.param.FleetConditionQuery;
import org.platform.vehicle.param.FleetWxUserInfo;
import org.platform.vehicle.param.SysFleetAddParam;
import org.platform.vehicle.param.SysFleetEditParam;
import org.platform.vehicle.vo.SysFleetDetailVo;
import org.platform.vehicle.vo.SysFleetVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author gejiawei
 * @Date 2023/8/30 10:04
 */
public interface SysFleetService {

    /**
     * 车队档案-条件查询
     *
     * @param param
     * @return
     */
    BasePageResponse<List<SysFleetVo>> conditionQuery(FleetConditionQuery param);

    /**
     * 车队档案-新增
     *
     * @param param
     * @param request
     * @return
     */
    BaseResponse addFleet(SysFleetAddParam param, HttpServletRequest request);

    /**
     * 车队档案-修改
     *
     * @param param
     * @param request
     * @return
     */
    BaseResponse editFleet(SysFleetEditParam param, HttpServletRequest request);

    /**
     * 车队档案-删除
     *
     * @param id
     * @param phone
     * @param request
     * @return
     */
    BaseResponse deleteFleet(Integer id, String phone, HttpServletRequest request);

    /**
     * 车队档案-查看车队详情
     *
     * @param fleetId
     * @return
     */
    BaseResponse<SysFleetDetailVo> getFleetDetail(Integer fleetId);

    /**
     * 获取当前登录人所属客户列表
     *
     * @return
     */
    BaseResponse<List<SysFleetVo>> getClientList();

    /**
     * 获取当前登录人上级车队列表
     *
     * @return
     */
    BaseResponse<List<SysFleetVo>> getParentFleetList(Integer clientId);

    /**
     * 获取当前用户及下属已绑定微信的用户(通知接收人)
     *
     * @return
     */
    BaseResponse<List<FleetWxUserInfo>> getwxUser();
}
