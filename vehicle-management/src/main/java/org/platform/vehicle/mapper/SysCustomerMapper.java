package org.platform.vehicle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.platform.vehicle.entity.SysCustomer;
import org.platform.vehicle.vo.SimpleCustomerVo;
import org.platform.vehicle.vo.SimpleFleetVo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface SysCustomerMapper extends BaseMapper<SysCustomer> {

	@Select("SELECT c.id FROM sys_customer c WHERE c.is_delete=0 AND c.`status`=1 AND c.parent_id IN (${customerIds})")
	public List<Integer> getChildCustomerId(@Param("customerIds") String customerIds);

	@Select("SELECT c.id FROM sys_customer c WHERE c.is_delete=0 AND c.`status`=1")
	public List<Integer> getAllCustomerId();

	@Select("SELECT c.id, c.`name` FROM sys_customer c WHERE c.is_delete=0 AND c.`status`=1 AND C.type=#{cusType}")
	public List<SimpleCustomerVo> getAllCustomerByType(@Param("cusType") int cusType);

	@Select("SELECT c.id, c.`name` FROM sys_customer c WHERE c.is_delete=0 AND c.`status`=1")
	public List<SimpleCustomerVo> getAllCustomer();

	@Select("SELECT c.id, c.`name` FROM sys_customer c WHERE c.is_delete=0 AND c.`status`=1 AND c.company_id=#{companyId}")
	public List<SimpleCustomerVo> getAllCustomerByCompanyId(@Param("companyId") Integer companyId);

	@Select("SELECT c.id, c.`name` FROM sys_customer c WHERE c.is_delete=0 AND c.`status`=1 AND c.parent_id=#{parentId} AND c.type=#{type}")
	public List<SimpleFleetVo> getFleetVoByTypeAndParent(@Param("parentId") Integer parentId, @Param("type") int type);
}
