package org.platform.vehicle.base;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @company:上海面心科技
 * @project:路安车辆维管系统
 * @module:后台数据管理
 * @version:V1.0
 *
 * @author Sunnykid
 * @date 2023年9月11日 上午9:52:45
 * @description: 功模块Service继承的基类Service接口
 */
public interface BaseService<T> {

	public abstract BaseMapper<T> getMapper();

	public abstract int add(T t);

	public abstract int update(T t);

	public abstract Page<T> query(QueryWrapper<T> queryWrapper, Integer pageNum, Integer pageSize);

	public abstract List<T> query(QueryWrapper<T> queryWrapper);

	public abstract T get(Serializable id);

	public abstract int delete(Serializable id);

	public abstract int deleteByQueryWrapper(QueryWrapper<T> queryWrapper);

	public abstract int markDelete(Serializable id);

	public abstract boolean checkDuplicate(Integer id, String uniqueFiledName, String uniqueFiledValue,
			Integer customerId, String... extraFiledNames);

}
