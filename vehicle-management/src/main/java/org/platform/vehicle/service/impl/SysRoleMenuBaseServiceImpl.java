package org.platform.vehicle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.platform.vehicle.entity.SysRoleMenu;
import org.platform.vehicle.mapper.SysRoleMenuMapper;
import org.platform.vehicle.service.SysRoleMenuBaseService;
import org.springframework.stereotype.Service;

/**
 * @Author gejiawei
 * @Date 2023/8/25 14:26
 */
@Service
public class SysRoleMenuBaseServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuBaseService {
}
