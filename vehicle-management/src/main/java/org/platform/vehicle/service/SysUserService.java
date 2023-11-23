package org.platform.vehicle.service;

import org.platform.vehicle.entity.SysUser;
import org.platform.vehicle.param.SysUserAddParam;
import org.platform.vehicle.param.SysUserConditionQueryParam;
import org.platform.vehicle.param.SysUserEditParam;
import org.platform.vehicle.vo.SysUserDetailVo;
import org.platform.vehicle.vo.SysUserVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author gejiawei
 * @Date 2023/8/23 17:20
 */
public interface SysUserService {

    /**
     * 系统设置-用户设置-条件查询
     *
     * @param param
     * @return
     */
    BasePageResponse<List<SysUserVo>> conditionQuery(SysUserConditionQueryParam param);

    /**
     * 系统设置-用户设置-新增账号
     *
     * @param param
     * @param request
     * @return
     */
    BaseResponse add(SysUserAddParam param, HttpServletRequest request);

    /**
     * 系统设置-用户设置-修改账号
     *
     * @param param
     * @param request
     * @return
     */
    BaseResponse edit(SysUserEditParam param, HttpServletRequest request);

    /**
     * 系统设置-用户设置-删除账号
     *
     * @param id
     * @param phone
     * @param request
     * @return
     */
    BaseResponse delete(Integer id, String phone, HttpServletRequest request);

    /**
     * 系统设置-用户设置-删除账号
     *
     * @param account
     * @param request
     * @return
     */
    int delete(String account, HttpServletRequest request);

    /**
     * 系统设置-用户设置-根据账号查
     *
     * @param account
     * @return
     */
    SysUser getByAccount(String account);

    /**
     * 根据主键更新用户信息
     *
     * @param sysUser
     * @return
     */
    int updateById(SysUser sysUser);

    /**
     * 系统设置-用户设置-详情
     *
     * @param id
     * @return
     */
    BaseResponse<SysUserDetailVo> getSysUserDetail(Integer id);

    /**
     * 系统设置-用户设置-重置密码
     *
     * @param id
     * @return
     */
    BaseResponse resetPassword(Integer id, HttpServletRequest request);
}
