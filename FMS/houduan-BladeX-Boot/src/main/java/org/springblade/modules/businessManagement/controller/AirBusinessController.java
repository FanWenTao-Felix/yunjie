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
package org.springblade.modules.businessManagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springblade.common.constant.CommonConstant;
import org.springblade.common.enums.UserRole;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.businessManagement.entity.Task;
import org.springblade.modules.businessManagement.service.impl.AirBusinessServiceImpl;
import org.springblade.modules.businessManagement.service.impl.TaskServiceImpl;
import org.springblade.modules.system.service.impl.RoleServiceImpl;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.businessManagement.entity.AirBusiness;
import org.springblade.modules.businessManagement.vo.AirBusinessVO;
import org.springblade.modules.businessManagement.wrapper.AirBusinessWrapper;
import org.springblade.modules.businessManagement.service.IAirBusinessService;
import org.springblade.core.boot.ctrl.BladeController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 空运业务 控制器
 *
 * @author BladeX
 * @since 2019-09-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("business/airbusiness")
@Api(value = "空运业务", tags = "空运业务接口")
public class AirBusinessController extends BladeController {
	private RoleServiceImpl roleService;
	private AirBusinessServiceImpl airBusinessService;
	private TaskServiceImpl taskService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入airBusiness")
	public R<AirBusinessVO> detail(AirBusiness airBusiness) {
		AirBusiness detail = airBusinessService.getOne(Condition.getQueryWrapper(airBusiness));
		return R.data(AirBusinessWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 空运业务
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入airBusiness")
	public R<IPage<AirBusinessVO>> list(AirBusiness airBusiness, Query query) {
		BladeUser user = getUser();
		UserRole userRole = CommonUtil.judgeUserRole(user, roleService);
		if (userRole == UserRole.SALESMAN) airBusiness.setSalesman(user.getUserId());
		if (userRole == UserRole.OPERATOR) airBusiness.setOperator(user.getUserId());
		String task = airBusiness.getTask();
		LocalDateTime taskDate = airBusiness.getTaskTime();
		QueryWrapper<Task> task_qw = new QueryWrapper<>();
		Set<String> internal_order_no_set = new HashSet<>();
		if (!StringUtils.isEmpty(task)) task_qw.like("task_name", task);
		if (taskDate != null) {
			task_qw.le("begin_time", taskDate);
			task_qw.ge("finish_time", taskDate);
		}
		boolean taskSearch = !(StringUtils.isEmpty(task) && taskDate == null);
		if (taskSearch) {
			List<Task> taskList = taskService.list(task_qw);
			taskList.forEach(item -> internal_order_no_set.add(item.getInternalOrderNo()));
		}
		if(taskSearch&&internal_order_no_set.size()==0)return R.data(AirBusinessWrapper.build().pageVO(new Page<>()));
		QueryWrapper<AirBusiness> qw = Condition.getQueryWrapper(airBusiness);
		qw.orderByDesc("create_time");
		if(taskSearch)qw.in("internal_order_no",internal_order_no_set);
		IPage<AirBusiness> pages = airBusinessService.page(Condition.getPage(query), qw);
		return R.data(AirBusinessWrapper.build().pageVO(pages));
	}

	/**
	 * 自定义分页 空运业务
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入airBusiness")
	public R<IPage<AirBusinessVO>> page(AirBusinessVO airBusiness, Query query) {
		IPage<AirBusinessVO> pages = airBusinessService.selectAirBusinessPage(Condition.getPage(query), airBusiness);
		return R.data(pages);
	}

	/**
	 * 新增 空运业务
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入airBusiness")
	public R save(@Valid @RequestBody AirBusiness airBusiness) {
		airBusiness.setMainOrderNo(CommonUtil.airMainOrderJudge(airBusiness.getMainOrderNo()));
		BladeUser user = getUser();
		if (user.getRoleId().contains(CommonConstant.SALESMAN_ROLE_ID)) airBusiness.setSalesman(user.getUserId());
		return R.status(airBusinessService.saveData(airBusiness));
	}

	/**
	 * 修改 空运业务
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入airBusiness")
	public R update(@Valid @RequestBody AirBusiness airBusiness) {
		airBusiness.setMainOrderNo(CommonUtil.airMainOrderJudge(airBusiness.getMainOrderNo()));
		CommonUtil.airBusinessLockedJudge(airBusiness.getId().toString(), airBusinessService, getUser(), roleService);
		return R.status(airBusinessService.updateById(airBusiness));
	}

	/**
	 * 新增或修改 空运业务
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入airBusiness")
	public R submit(@Valid @RequestBody AirBusiness airBusiness) {
		airBusiness.setMainOrderNo(CommonUtil.airMainOrderJudge(airBusiness.getMainOrderNo()));
		CommonUtil.airBusinessLockedJudge(airBusiness.getId().toString(), airBusinessService, getUser(), roleService);
		return R.status(airBusinessService.saveOrUpdate(airBusiness));
	}


	/**
	 * 删除 空运业务
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		CommonUtil.airBusinessLockedJudge(ids, airBusinessService, getUser(), roleService);
		return R.status(airBusinessService.customRemoveById(ids));
	}

	/**
	 * 下载提单,打印用
	 */
	@GetMapping("/waybill")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "下载提单", notes = "传入airBusiness")
	public void waybill(AirBusiness airBusiness, HttpServletResponse response, String printer) {
		airBusinessService.getWaybill(airBusiness, response, printer);
	}

	/**
	 * 下载提单,展示用
	 */
	@GetMapping("/waybillshow")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "下载提单", notes = "传入airBusiness")
	public void waybillShow(AirBusiness airBusiness, HttpServletResponse response) {
		airBusinessService.getWaybillShow(airBusiness, response);
	}


	/**
	 * 下载交接单
	 */
	@GetMapping("/deliveryReceipt")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "下载交接单", notes = "传入airBusiness")
	public void deliveryReceipt(AirBusiness airBusiness, HttpServletResponse response) {
		airBusinessService.getDeliveryReceipt(airBusiness, response);
	}

	/**
	 * 下载对账单
	 */
	@GetMapping("/statement")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "下载对账单", notes = "传入airBusiness")
	public void statement(String bankAccountIds, AirBusiness airBusiness, HttpServletResponse response) {
		airBusinessService.getStatement(Func.toLongList(bankAccountIds), airBusiness, response);
	}

	/**
	 *
	 */
	@PostMapping("/verifyAllFee")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "审核全部费用", notes = "传入airBusiness")
	public R verifyAllFee(String internalOrderNo) {
		return R.status(airBusinessService.verifyAllFee(internalOrderNo));
	}
}
