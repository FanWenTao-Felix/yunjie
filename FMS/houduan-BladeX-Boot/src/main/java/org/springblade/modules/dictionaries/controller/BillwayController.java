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
package org.springblade.modules.dictionaries.controller;

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
import org.springblade.modules.dictionaries.entity.Billway;
import org.springblade.modules.dictionaries.vo.BillwayVO;
import org.springblade.modules.dictionaries.wrapper.BillwayWrapper;
import org.springblade.modules.dictionaries.service.IBillwayService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 *  控制器
 *
 * @author BladeX
 * @since 2019-08-20
 */
@RestController
@AllArgsConstructor
@RequestMapping("dictionaries/billway")
@Api(value = "", tags = "接口")
public class BillwayController extends BladeController {

	private IBillwayService billwayService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入billway")
	public R<BillwayVO> detail(Billway billway) {
		Billway detail = billwayService.getOne(Condition.getQueryWrapper(billway));
		return R.data(BillwayWrapper.build().entityVO(detail));
	}

	/**
	* 分页 
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入billway")
	public R<IPage<BillwayVO>> list(Billway billway, Query query) {
		IPage<Billway> pages = billwayService.page(Condition.getPage(query), Condition.getQueryWrapper(billway));
		return R.data(BillwayWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入billway")
	public R<IPage<BillwayVO>> page(BillwayVO billway, Query query) {
		IPage<BillwayVO> pages = billwayService.selectBillwayPage(Condition.getPage(query), billway);
		return R.data(pages);
	}

	/**
	* 新增 
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入billway")
	public R save(@Valid @RequestBody Billway billway) {
		return R.status(billwayService.save(billway));
	}

	/**
	* 修改 
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入billway")
	public R update(@Valid @RequestBody Billway billway) {
		return R.status(billwayService.updateById(billway));
	}

	/**
	* 新增或修改 
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入billway")
	public R submit(@Valid @RequestBody Billway billway) {
		return R.status(billwayService.saveOrUpdate(billway));
	}

	
	/**
	* 删除 
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(billwayService.removeByIds(Func.toLongList(ids)));
	}

	
}
