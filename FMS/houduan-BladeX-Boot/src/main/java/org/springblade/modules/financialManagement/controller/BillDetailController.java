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
package org.springblade.modules.financialManagement.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

import javax.validation.Valid;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.financialManagement.service.impl.BillDetailServiceImpl;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.financialManagement.entity.BillDetail;
import org.springblade.modules.financialManagement.vo.BillDetailVO;
import org.springblade.modules.financialManagement.wrapper.BillDetailWrapper;
import org.springblade.modules.financialManagement.service.IBillDetailService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 账单明细 控制器
 *
 * @author BladeX
 * @since 2019-10-16
 */
@RestController
@AllArgsConstructor
@RequestMapping("reconciliationstatement/billdetail")
@Api(value = "账单明细", tags = "账单明细接口")
public class BillDetailController extends BladeController {

	private BillDetailServiceImpl billDetailService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入billDetail")
	public R<BillDetailVO> detail(BillDetail billDetail) {
		BillDetail detail = billDetailService.getOne(Condition.getQueryWrapper(billDetail));
		return R.data(BillDetailWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 账单明细
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入billDetail")
	public R<IPage<BillDetailVO>> list(BillDetail billDetail, Query query) {
		IPage<BillDetail> pages = billDetailService.page(Condition.getPage(query), Condition.getQueryWrapper(billDetail));
		return R.data(BillDetailWrapper.build().pageVO(pages));
	}

	/**
	 * 自定义分页 账单明细
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入billDetail")
	public R<IPage<BillDetailVO>> page(BillDetailVO billDetail, Query query) {
		IPage<BillDetailVO> pages = billDetailService.selectBillDetailPage(Condition.getPage(query), billDetail);
		return R.data(pages);
	}

	/**
	 * 新增或修改 账单明细
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入billDetail")
	public R submit(@Valid @RequestBody BillDetail billDetail) {
		return R.status(billDetailService.customSaveOrderUpdate(billDetail));
	}


	/**
	 * 删除 账单明细
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(billDetailService.customRemoveByIds(ids));
	}


}
