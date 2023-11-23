package org.platform.vehicle.base;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.platform.vehicle.entity.VehicleEntity;
import org.platform.vehicle.utils.UserContext;


/**
 * @company:上海面心科技
 * @project:路安车辆维管系统
 * @module:后台数据管理
 * @version:V1.0
 *
 * @author Sunnykid
 * @date 2023年9月11日 上午9:52:45
 * @description: 功模块Service继承的基类Service接口实现
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

	@Override
	public int add(T t) {
		return this.getMapper().insert(t);
	}

	@Override
	public int update(T t) {
		return this.getMapper().updateById(t);
	}

	@Override
	public List<T> query(QueryWrapper<T> queryWrapper) {
		return this.getMapper().selectList(queryWrapper);
	}

	@Override
	public Page<T> query(QueryWrapper<T> queryWrapper, Integer pageNum, Integer pageSize) {
		Page<T> page = new Page<>(pageNum.intValue(), pageSize.intValue());
		return this.getMapper().selectPage(page, queryWrapper);
	}

	@Override
	public T get(Serializable id) {
		return this.getMapper().selectById(id);
	}

	@Override
	public int delete(Serializable id) {
		return this.getMapper().deleteById(id);
	}

	@Override
	public int markDelete(Serializable id) {
		T t = this.getMapper().selectById(id);

		return this.getMapper().updateById(t);
	}

	@Override
	public boolean checkDuplicate(Integer id, String uniqueFiledName, String uniqueFiledValue, Integer customerId,
			String... extraFiledNames) {
		QueryWrapper<T> q = new QueryWrapper<T>();
		if(id != null) {
			q.ne("id", id);
		}
		if(customerId != null) {
			q.eq("customer_id", customerId);
		}
		q.and(new Consumer<QueryWrapper<T>>() {
			@Override
			public void accept(QueryWrapper<T> t) {
				t.eq(uniqueFiledName, uniqueFiledValue);
				for(String extraFiledName : extraFiledNames) {
					t.or().eq(extraFiledName, uniqueFiledValue);
				}
			}
		});
		q.eq("is_deleted", Boolean.FALSE);
		return this.getMapper().selectCount(q).intValue() == 0;
	}

	@Override
	public int deleteByQueryWrapper(QueryWrapper<T> queryWrapper) {
		return this.getMapper().delete(queryWrapper);
	}

}
