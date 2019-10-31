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
import org.springblade.modules.dictionaries.entity.SerialNum;
import org.springblade.modules.dictionaries.vo.SerialNumVO;
import org.springblade.modules.dictionaries.wrapper.SerialNumWrapper;
import org.springblade.modules.dictionaries.service.ISerialNumService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 流水号表 控制器
 *
 * @author BladeX
 * @since 2019-08-26
 */
@RestController
@AllArgsConstructor
@RequestMapping("dictionaries/serialnum")
@Api(value = "流水号表", tags = "流水号表接口")
public class SerialNumController extends BladeController {

	private ISerialNumService serialNumService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入serialNum")
	public R<SerialNumVO> detail(SerialNum serialNum) {
		SerialNum detail = serialNumService.getOne(Condition.getQueryWrapper(serialNum));
		return R.data(SerialNumWrapper.build().entityVO(detail));
	}

	/**
	* 分页 流水号表
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入serialNum")
	public R<IPage<SerialNumVO>> list(SerialNum serialNum, Query query) {
		IPage<SerialNum> pages = serialNumService.page(Condition.getPage(query), Condition.getQueryWrapper(serialNum));
		return R.data(SerialNumWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 流水号表
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入serialNum")
	public R<IPage<SerialNumVO>> page(SerialNumVO serialNum, Query query) {
		IPage<SerialNumVO> pages = serialNumService.selectSerialNumPage(Condition.getPage(query), serialNum);
		return R.data(pages);
	}

	/**
	* 新增 流水号表
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入serialNum")
	public R save(@Valid @RequestBody SerialNum serialNum) {
		return R.status(serialNumService.save(serialNum));
	}

	/**
	* 修改 流水号表
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入serialNum")
	public R update(@Valid @RequestBody SerialNum serialNum) {
		return R.status(serialNumService.updateById(serialNum));
	}

	/**
	* 新增或修改 流水号表
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入serialNum")
	public R submit(@Valid @RequestBody SerialNum serialNum) {
		return R.status(serialNumService.saveOrUpdate(serialNum));
	}

	
	/**
	* 删除 流水号表
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(serialNumService.removeByIds(Func.toLongList(ids)));
	}

	
}
