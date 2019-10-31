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
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.financialManagement.entity.BankAccount;
import org.springblade.modules.financialManagement.vo.BankAccountVO;
import org.springblade.modules.financialManagement.wrapper.BankAccountWrapper;
import org.springblade.modules.financialManagement.service.IBankAccountService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 银行账号 控制器
 *
 * @author BladeX
 * @since 2019-09-23
 */
@RestController
@AllArgsConstructor
@RequestMapping("financial/bankaccount")
@Api(value = "银行账号", tags = "银行账号接口")
public class BankAccountController extends BladeController {

	private IBankAccountService bankAccountService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入bankAccount")
	public R<BankAccountVO> detail(BankAccount bankAccount) {
		BankAccount detail = bankAccountService.getOne(Condition.getQueryWrapper(bankAccount));
		return R.data(BankAccountWrapper.build().entityVO(detail));
	}

	/**
	* 分页 银行账号
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入bankAccount")
	public R<IPage<BankAccountVO>> list(BankAccount bankAccount, Query query) {
		IPage<BankAccount> pages = bankAccountService.page(Condition.getPage(query), Condition.getQueryWrapper(bankAccount));
		return R.data(BankAccountWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 银行账号
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入bankAccount")
	public R<IPage<BankAccountVO>> page(BankAccountVO bankAccount, Query query) {
		IPage<BankAccountVO> pages = bankAccountService.selectBankAccountPage(Condition.getPage(query), bankAccount);
		return R.data(pages);
	}

	/**
	* 新增 银行账号
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入bankAccount")
	public R save(@Valid @RequestBody BankAccount bankAccount) {
		return R.status(bankAccountService.save(bankAccount));
	}

	/**
	* 修改 银行账号
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入bankAccount")
	public R update(@Valid @RequestBody BankAccount bankAccount) {
		return R.status(bankAccountService.updateById(bankAccount));
	}

	/**
	* 新增或修改 银行账号
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入bankAccount")
	public R submit(@Valid @RequestBody BankAccount bankAccount) {
		return R.status(bankAccountService.saveOrUpdate(bankAccount));
	}

	
	/**
	* 删除 银行账号
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(bankAccountService.removeByIds(Func.toLongList(ids)));
	}

	
}
