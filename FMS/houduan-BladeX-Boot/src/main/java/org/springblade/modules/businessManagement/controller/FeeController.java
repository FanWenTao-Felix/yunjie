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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.businessManagement.entity.Task;
import org.springblade.modules.businessManagement.mapper.FeeMapper;
import org.springblade.modules.businessManagement.service.impl.AirBusinessServiceImpl;
import org.springblade.modules.businessManagement.service.impl.FeeServiceImpl;
import org.springblade.modules.businessManagement.wrapper.TaskWrapper;
import org.springblade.modules.ocean.service.impl.SeaBusinessServiceImpl;
import org.springblade.modules.system.service.impl.RoleServiceImpl;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.businessManagement.entity.Fee;
import org.springblade.modules.businessManagement.vo.FeeVO;
import org.springblade.modules.businessManagement.wrapper.FeeWrapper;
import org.springblade.modules.businessManagement.service.IFeeService;
import org.springblade.core.boot.ctrl.BladeController;

import java.util.List;

/**
 * 费用表 控制器
 *
 * @author BladeX
 * @since 2019-09-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("business/fee")
@Api(value = "费用表", tags = "费用表接口")
public class FeeController extends BladeController {

	private FeeServiceImpl feeService;
	private AirBusinessServiceImpl airBusinessService;
	private SeaBusinessServiceImpl seaBusinessServices;
	private RoleServiceImpl roleService;
	private FeeMapper feeMapper;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入fee")
	public R<FeeVO> detail(Fee fee) {
		Fee detail = feeService.getOne(Condition.getQueryWrapper(fee));
		return R.data(FeeWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 费用表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入fee")
	public R<IPage<FeeVO>> list(Fee fee, Query query) {
		return R.data(FeeWrapper.build().pageVO(feeService.getFeePageAfterCount(fee, query)));
	}

	/**
	 * 自定义分页 费用表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入fee")
	public R<IPage<FeeVO>> page(FeeVO fee, Query query) {
		IPage<FeeVO> pages = feeService.selectFeePage(Condition.getPage(query), fee);
		return R.data(pages);
	}

	/**
	 * 新增 费用表
	 */
	@PostMapping("/seaSave")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入fee")
	public R seaSave(@Valid @RequestBody Fee fee) {
		System.out.println(fee);
		fee.setAmount((double) fee.getQuantity() * fee.getUnitPrice());
		return R.status(feeService.save(fee));
	}

	/**
	 * 修改 费用表
	 */
	@PostMapping("/seaUpdate")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入fee")
	public R seaUpdate(@Valid @RequestBody Fee fee) {
		System.out.println(fee);
		//CommonUtil.airFeeSaveOrUpdateJudge(fee, airBusinessService, feeService, roleService, getUser());
		fee.setAmount((double) fee.getQuantity() * fee.getUnitPrice());
		return R.status(feeService.updateById(fee));
	}

	/**
	 * 新增 费用表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入fee")
	public R save(@Valid @RequestBody Fee fee) {
		CommonUtil.airFeeSaveOrUpdateJudge(fee, airBusinessService, feeService, roleService, getUser());
		fee.setAmount((double) fee.getQuantity() * fee.getUnitPrice());
		return R.status(feeService.save(fee));
	}


	/**
	 * 修改 费用表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入fee")
	public R update(@Valid @RequestBody Fee fee) {
		CommonUtil.airFeeSaveOrUpdateJudge(fee, airBusinessService, feeService, roleService, getUser());
		fee.setAmount((double) fee.getQuantity() * fee.getUnitPrice());
		return R.status(feeService.updateById(fee));
	}

	/**
	 * 新增或修改 费用表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入fee")
	public R submit(@Valid @RequestBody Fee fee) {
		String ids = fee.getId() == null ? null : fee.getId().toString();
		CommonUtil.airFeeSaveOrUpdateJudge(fee, airBusinessService, feeService, roleService, getUser());
		fee.setAmount((double) fee.getQuantity() * fee.getUnitPrice());
		return R.status(feeService.saveOrUpdate(fee));
	}


	/**
	 * 删除 费用表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		CommonUtil.airFeeJudgeWithIds(true, ids, airBusinessService, feeService, roleService, getUser());
		return R.status(feeService.customRemoveByIds(ids));
	}

	/**
	 * 删除 费用表
	 */
	@PostMapping("/seaRemove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R seaRemove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(feeService.removeByIds(Func.toLongList(ids)));
	}


	/**
	 * 生成应付
	 */
	@PostMapping("/payable")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "生成应付", notes = "传入ids")
	public R createPayable(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		CommonUtil.airFeeJudgeWithIds(false, ids, airBusinessService, feeService, roleService, getUser());
		return R.status(feeService.createPayable(Func.toLongList(ids)));
	}

	/**
	 * 生成应付
	 */
	@PostMapping("/seaPayable")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "生成应付", notes = "传入ids")
	public R createSeaPayable(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		//CommonUtil.airFeeJudgeWithIds(false, ids, airBusinessService, feeService, roleService, getUser());
		return R.status(feeService.createPayable(Func.toLongList(ids)));
	}

	/**
	 * 获取账单时的费用
	 */
	@GetMapping("/billfee")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "获取账单时的费用", notes = "传入fee")
	public R<List<Fee>> billfee(Fee fee, Query query) {
		if (fee == null) throw new RuntimeException("fee不能为空");
		if (fee.getSettlementUnit() == null) throw new RuntimeException("结算单位不能为空");
		if (fee.getType() == null) throw new RuntimeException("结算类型不能为空");
		List<Fee> feeList = feeMapper.getUnSettledFee(fee);
		feeList.forEach(i -> i.setCurrentBillAmount(i.getAmount() - i.getBillCreatedAmount()));
		return R.data(feeList);
	}
}
