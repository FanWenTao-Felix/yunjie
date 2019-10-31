/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.springblade.modules.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springblade.common.constant.CommonConstant;
import org.springblade.common.enums.UserRole;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tenant.TenantId;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.system.entity.Dept;
import org.springblade.modules.system.entity.Role;
import org.springblade.modules.system.entity.RoleMenu;
import org.springblade.modules.system.entity.Tenant;
import org.springblade.modules.system.mapper.DeptMapper;
import org.springblade.modules.system.mapper.RoleMapper;
import org.springblade.modules.system.mapper.RoleMenuMapper;
import org.springblade.modules.system.mapper.TenantMapper;
import org.springblade.modules.system.service.ITenantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
@AllArgsConstructor
public class TenantServiceImpl extends BaseServiceImpl<TenantMapper, Tenant> implements ITenantService {

	private final TenantId tenantId;
	private final RoleMapper roleMapper;
	private final DeptMapper deptMapper;
	private final RoleMenuServiceImpl roleMenuService;

	@Override
	public IPage<Tenant> selectTenantPage(IPage<Tenant> page, Tenant tenant) {
		return page.setRecords(baseMapper.selectTenantPage(page, tenant));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveTenant(Tenant tenant) {
		if (Func.isEmpty(tenant.getId())) {
			List<Tenant> tenants = baseMapper.selectList(Wrappers.<Tenant>query().lambda().eq(Tenant::getIsDeleted, BladeConstant.DB_NOT_DELETED));
			List<String> codes = tenants.stream().map(Tenant::getTenantId).collect(Collectors.toList());
			String tenantId = getTenantId(codes);
			tenant.setTenantId(tenantId);

			/**获取操作员权限**/
			QueryWrapper<RoleMenu> rm_qw_operator = new QueryWrapper<>();
			rm_qw_operator.eq("role_id", UserRole.OPERATOR.getTemplateRoleIdToString());
			List<RoleMenu> operator_menu = roleMenuService.list(rm_qw_operator);
			/**获取业务员权限**/
			QueryWrapper<RoleMenu> rm_qw_salesman = new QueryWrapper<>();
			rm_qw_salesman.eq("role_id", UserRole.SALESMAN.getTemplateRoleIdToString());
			List<RoleMenu> salesman_menu = roleMenuService.list(rm_qw_salesman);
			/**获取admin权限**/
			QueryWrapper<RoleMenu> rm_qw_admin = new QueryWrapper<>();
			rm_qw_admin.eq("role_id", UserRole.ADMIN.getTemplateRoleIdToString());
			List<RoleMenu> admin_menu = roleMenuService.list(rm_qw_admin);
            /**获取财务权限**/
            QueryWrapper<RoleMenu> rm_qw_finance=new QueryWrapper<>();
            rm_qw_finance.eq("role_id",UserRole.FINANCE.getTemplateRoleIdToString());
            List<RoleMenu> finance_menu=roleMenuService.list(rm_qw_finance);

			// 新建租户对应的默认角色  admin
			Role role = new Role();
			role.setTenantId(tenantId);
			role.setParentId(BladeConstant.TOP_PARENT_ID);
			role.setRoleName(tenant.getTenantName()+UserRole.ADMIN.getDescription());
			role.setRoleAlias(UserRole.ADMIN.toString());
			role.setSort(2);
			role.setIsDeleted(0);
			roleMapper.insert(role);
			List<RoleMenu> a_r_m_l = new ArrayList<>(admin_menu.size());
			for (RoleMenu item : admin_menu) {
				RoleMenu d = new RoleMenu();
				d.setMenuId(item.getMenuId());
				d.setRoleId(role.getId());
				a_r_m_l.add(d);
			}

			//业务员
			Role salesman = new Role();
			salesman.setTenantId(tenantId);
			salesman.setParentId(BladeConstant.TOP_PARENT_ID);
			salesman.setRoleName(tenant.getTenantName()+UserRole.SALESMAN.getDescription());
			salesman.setRoleAlias(UserRole.SALESMAN.toString());
			salesman.setSort(2);
			salesman.setIsDeleted(0);
			roleMapper.insert(salesman);
			List<RoleMenu> s_r_m_l = new ArrayList<>(salesman_menu.size());
			for (RoleMenu item : salesman_menu) {
				RoleMenu d = new RoleMenu();
				d.setRoleId(salesman.getId());
				d.setMenuId(item.getMenuId());
				s_r_m_l.add(d);
			}
			//操作员
			Role operator = new Role();
			operator.setTenantId(tenantId);
			operator.setParentId(BladeConstant.TOP_PARENT_ID);
			operator.setRoleName(tenant.getTenantName()+UserRole.OPERATOR.getDescription());
			operator.setRoleAlias(UserRole.OPERATOR.toString());
			operator.setSort(3);
			operator.setIsDeleted(0);
			roleMapper.insert(operator);
			List<RoleMenu> o_r_m_l = new ArrayList<>(operator_menu.size());
			for (RoleMenu item : operator_menu) {
				RoleMenu d = new RoleMenu();
				d.setMenuId(item.getMenuId());
				d.setRoleId(Long.valueOf(operator.getId()));
				o_r_m_l.add(d);
			}
			//财务
			Role finance=new Role();
			finance.setTenantId(tenantId);
			finance.setParentId(BladeConstant.TOP_PARENT_ID);
			finance.setRoleName(tenant.getTenantName()+UserRole.FINANCE.getDescription());
			finance.setRoleAlias(UserRole.FINANCE.toString());
			finance.setSort(4);
			finance.setIsDeleted(0);
			roleMapper.insert(finance);
			List<RoleMenu> f_r_m_l=new ArrayList<>(finance_menu.size());
			for(RoleMenu item:finance_menu){
				RoleMenu d=new RoleMenu();
				d.setMenuId(item.getMenuId());
				d.setRoleId(finance.getId());
				f_r_m_l.add(d);
			}
			List<RoleMenu> roleMenus = new ArrayList<>(s_r_m_l.size() + o_r_m_l.size() + a_r_m_l.size()+f_r_m_l.size());
			roleMenus.addAll(o_r_m_l);
			roleMenus.addAll(s_r_m_l);
			roleMenus.addAll(a_r_m_l);
			roleMenus.addAll(f_r_m_l);
			if (!roleMenuService.saveBatch(roleMenus)) throw new RuntimeException("角色权限创建失败");
			// 新建租户对应的默认部门
			Dept dept = new Dept();
			dept.setTenantId(tenantId);
			dept.setParentId(BladeConstant.TOP_PARENT_ID);
			dept.setDeptName(tenant.getTenantName());
			dept.setFullName(tenant.getTenantName());
			dept.setSort(2);
			dept.setDeptCategory(1);
			dept.setIsDeleted(0);
			deptMapper.insert(dept);
		}
		return super.saveOrUpdate(tenant);
	}

	private String getTenantId(List<String> codes) {
		String code = tenantId.generate();
		if (codes.contains(code)) {
			return getTenantId(codes);
		}
		return code;
	}

}
