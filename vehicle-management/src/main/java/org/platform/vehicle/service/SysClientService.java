package org.platform.vehicle.service;

import org.platform.vehicle.entity.SysCustomer;
import org.platform.vehicle.param.ClientAddParam;
import org.platform.vehicle.param.ClientConditionQuery;
import org.platform.vehicle.param.ClientEditParam;
import org.platform.vehicle.vo.ClientDetailVo;
import org.platform.vehicle.vo.ClientNameVo;
import org.platform.vehicle.vo.SimpleCustomerVo;
import org.platform.vehicle.vo.SimpleFleetVo;
import org.platform.vehicle.vo.SysClientVo;
import org.platform.vehicle.vo.SysCustomerTreeVo;
import org.platform.vehicle.response.BasePageResponse;
import org.platform.vehicle.response.BaseResponse;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author gejiawei
 * @Date 2023/8/28 15:58
 */
public interface SysClientService {

	/**
	 * 客户档案-条件查询
	 *
	 * @param param
	 * @return
	 */
	BasePageResponse<List<SysClientVo>> conditionQuery(ClientConditionQuery param);

	/**
	 * 客户档案-新增客户
	 *
	 * @param param
	 * @param request
	 * @return
	 */
	BaseResponse addClient(ClientAddParam param, HttpServletRequest request);

	/**
	 * 客户档案-修改客户
	 *
	 * @param param
	 * @param request
	 * @return
	 */
	BaseResponse editClient(ClientEditParam param, HttpServletRequest request);

	/**
	 * 客户档案-查看客户详情
	 *
	 * @param clientId
	 * @return
	 */
	BaseResponse<ClientDetailVo> getClientDetail(Integer clientId);

	/**
	 * 客户档案-删除客户
	 *
	 * @param clientId
	 * @param phone
	 * @param request
	 * @return
	 */
	BaseResponse deleteClient(Integer clientId, String phone, HttpServletRequest request);

	/**
	 * 客户档案-获取所有客户名称
	 *
	 * @return
	 */
	BaseResponse<List<ClientNameVo>> getAllClientName();

	/**
	 * 客户档案-查询当前用户归属
	 */
	BaseResponse<List<SysCustomerTreeVo>> getCustomerTree();

	/**
	 * 客户档案-查询当前登录用户所有相关的customerId列表
	 *
	 * @return
	 */
	Set<Integer> getAllRelatedIds();

	/**
	 * 客户档案-查询当前登录用户所属的客户信息和公司信息
	 *
	 * @return
	 */
	SimpleCustomerVo getCustomerInfo();

	/**
	 * 客户档案-查询指定用户所属的客户信息和公司信息
	 *
	 * @return
	 */
	SimpleCustomerVo getCustomerInfo(Integer customerId);

	/**
	 * 客户档案-根据ID查询
	 *
	 * @return
	 */
	SysCustomer getById(Integer customerId);

	/**
	 * 客户档案-简洁的客户信息列表
	 */
	List<SimpleCustomerVo> getCustomersLite();

	/**
	 * 客户档案-简洁的车队信息列表
	 */
	List<SimpleFleetVo> getFleetsLite();
}
