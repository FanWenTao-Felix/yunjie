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
package org.springblade.modules.ocean.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import javax.validation.Valid;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.dictionaries.entity.WorkNumRules;
import org.springblade.modules.dictionaries.service.impl.WorkNumRulesServiceImpl;
import org.springblade.modules.ocean.service.impl.SeaSpellServiceImpl;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.ocean.entity.SeaSpell;
import org.springblade.modules.ocean.vo.SeaSpellVO;
import org.springblade.modules.ocean.wrapper.SeaSpellWrapper;
import org.springblade.modules.ocean.service.ISeaSpellService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 *  控制器
 *
 * @author BladeX
 * @since 2019-09-17
 */
@RestController
@AllArgsConstructor
@RequestMapping("ocean/seaspell")
@Api(value = "", tags = "接口")
public class SeaSpellController extends BladeController {

	private SeaSpellServiceImpl seaSpellService;


	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入seaSpell")
	public R<SeaSpellVO> detail(SeaSpell seaSpell) {
		SeaSpell detail = seaSpellService.getOne(Condition.getQueryWrapper(seaSpell));
		return R.data(SeaSpellWrapper.build().entityVO(detail));
	}

	/**
	* 分页
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入seaSpell")
	public R<IPage<SeaSpellVO>> list(SeaSpell seaSpell, Query query) {
		IPage<SeaSpell> pages = seaSpellService.page(Condition.getPage(query), Condition.getQueryWrapper(seaSpell));
		return R.data(SeaSpellWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入seaSpell")
	public R<IPage<SeaSpellVO>> page(SeaSpellVO seaSpell, Query query) {
		IPage<SeaSpellVO> pages = seaSpellService.selectSeaSpellPage(Condition.getPage(query), seaSpell);
		return R.data(pages);
	}

	/**
	* 新增
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入seaSpell")
	public R save(@Valid @RequestBody SeaSpell seaSpell) {
		return R.status(seaSpellService.saveData(seaSpell));
	}

	/**
	* 修改
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入seaSpell")
	public R update(@Valid @RequestBody SeaSpell seaSpell) {
		return R.status(seaSpellService.updateById(seaSpell));
	}

	/**
	* 新增或修改
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入seaSpell")
	public R submit(@Valid @RequestBody SeaSpell seaSpell) {
		return R.status(seaSpellService.saveOrUpdate(seaSpell));
	}


	/**
	* 删除
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(seaSpellService.allRemoveById(ids));
	}


}
