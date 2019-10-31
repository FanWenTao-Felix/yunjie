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

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.businessManagement.entity.Fee;
import org.springblade.modules.dictionaries.entity.Parities;
import org.springblade.modules.dictionaries.service.IParitiesService;
import org.springblade.modules.dictionaries.service.impl.ParitiesServiceImpl;
import org.springblade.modules.dictionaries.vo.ParitiesVO;
import org.springblade.modules.dictionaries.wrapper.ParitiesWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 控制器
 *
 * @author BladeX1
 * @since 2019-08-13
 */
@RestController
@AllArgsConstructor
@RequestMapping("dictionaries/parities")
@Api(value = "", tags = "接口")
public class ParitiesController extends BladeController {

	private IParitiesService paritiesService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入parities")
	public R<ParitiesVO> detail(Parities parities) {
		Parities detail = paritiesService.getOne(Condition.getQueryWrapper(parities));
		return R.data(ParitiesWrapper.build().entityVO(detail));
	}

	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入parities")
	public R<IPage<ParitiesVO>> list(Parities parities, Query query) {
		//parities.setParitiesType("标准汇率");
		IPage<Parities> pages = paritiesService.page(Condition.getPage(query), Condition.getQueryWrapper(parities));
		return R.data(ParitiesWrapper.build().pageVO(pages));
	}

	/**
	 * 自定义分页
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入parities")
	public R<IPage<ParitiesVO>> page(ParitiesVO parities, Query query) {
		//parities.setParitiesType("标准汇率");
		IPage<ParitiesVO> pages = paritiesService.selectParitiesPage(Condition.getPage(query), parities);
		return R.data(pages);
	}

	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入parities")
	public R save(@Valid @RequestBody Parities parities) {
		return R.status(paritiesService.save(parities));
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入parities")
	public R update(@Valid @RequestBody Parities parities) {
		return R.status(paritiesService.updateById(parities));
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入parities")
	public R submit(@Valid @RequestBody Parities parities) {
		return R.status(paritiesService.saveOrUpdate(parities));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(paritiesService.removeByIds(Func.toLongList(ids)));
	}

}
