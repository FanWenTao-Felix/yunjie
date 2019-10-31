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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.businessManagement.entity.AirBusiness;
import org.springblade.modules.businessManagement.entity.Fee;
import org.springblade.modules.financialManagement.enums.BillStatus;
import org.springblade.modules.financialManagement.service.impl.BillServiceImpl;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.financialManagement.entity.Bill;
import org.springblade.modules.financialManagement.vo.BillVO;
import org.springblade.modules.financialManagement.wrapper.BillWrapper;
import org.springblade.modules.financialManagement.service.IBillService;
import org.springblade.core.boot.ctrl.BladeController;

import java.util.List;

/**
 * 账单 控制器
 *
 * @author BladeX
 * @since 2019-10-16
 */
@RestController
@AllArgsConstructor
@RequestMapping("reconciliationstatement/bill")
@Api(value = "账单", tags = "账单接口")
public class BillController extends BladeController {

	private BillServiceImpl billService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入bill")
	public R<BillVO> detail(Bill bill) {
		Bill detail = billService.getOne(Condition.getQueryWrapper(bill));
		return R.data(BillWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 账单
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入bill")
	public R<IPage<BillVO>> list(Bill bill, Query query) {
		IPage<Bill> pages = billService.page(Condition.getPage(query), Condition.getQueryWrapper(bill));
		return R.data(BillWrapper.build().pageVO(pages));
	}

	/**
	 * 自定义分页 账单
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入bill")
	public R<IPage<BillVO>> page(BillVO bill, Query query) {
		IPage<BillVO> pages = billService.selectBillPage(Condition.getPage(query), bill);
		return R.data(pages);
	}

	/**
	 * 新增 账单
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入bill")
	public R<String> save(@RequestBody Bill bill, @RequestParam String feeIds) {
		billService.createOrderInserToBill(bill, feeIds);
		return R.data(bill.getId().toString());
	}

	/**
	 * 加入账单
	 */

	@PostMapping("/join")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "加入", notes = "传入bill")
	public R join(String billId, String feeIds) {
		return R.status(billService.insertToBill(billId, feeIds));
	}


	/**
	 * 修改 账单
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入bill")
	public R update(@Valid @RequestBody Bill bill) {
		return R.status(billService.updateById(bill));
	}

	/**
	 * 新增或修改 账单
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入bill")
	public R submit(@Valid @RequestBody Bill bill) {
		return R.status(billService.saveOrUpdate(bill));
	}


	/**
	 * 删除 账单
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		List<Long> idList = Func.toLongList(ids);
		if (idList.size() != 1) throw new RuntimeException("每次只能删除一个账单");
		return R.status(billService.customRemoveBill(Func.toLongList(ids).get(0)));
	}

	/**
	 * 归档
	 */
	@PostMapping("archive")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "归档", notes = "传入ids")
	public R archive(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		List<Long> id = Func.toLongList(ids);
		if (id.size() != 1) throw new RuntimeException("一次只能选择一个账单归档");
		return R.status(billService.archive(id.get(0)));
	}

	/**
	 * 账单结算
	 */
	@PostMapping("settle")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "结算账单", notes = "传入ids")
	public R settle(@RequestBody Bill bill) {
		if (bill.getBillStatus() == BillStatus.ARCHIVED) throw new RuntimeException("账单已归档");
		bill.setBillStatus(BillStatus.SETTLED);
		return R.status(billService.updateById(bill));
	}

	/**
	 * 添加结算
	 */
	@PostMapping("addToBill")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "结算账单", notes = "传入ids")
	public R addToBill(@RequestBody List<Fee> feeList, Bill bill) {
		return R.status(billService.addToBill(feeList, bill, getUser()));
	}

	/**
	 * 下载对账单
	 */
	@GetMapping("/statement")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "下载对账单", notes = "传入airBusiness")
	public void statement(String bankAccountIds, Bill bill, HttpServletResponse response) {
		billService.getStatement(Func.toLongList(bankAccountIds), bill, response);
	}

}
