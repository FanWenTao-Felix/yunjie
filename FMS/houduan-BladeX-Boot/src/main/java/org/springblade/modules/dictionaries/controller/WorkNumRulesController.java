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

import com.alibaba.fastjson.JSONObject;
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
import org.springblade.modules.dictionaries.service.impl.WorkNumRulesServiceImpl;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.dictionaries.entity.WorkNumRules;
import org.springblade.modules.dictionaries.vo.WorkNumRulesVO;
import org.springblade.modules.dictionaries.wrapper.WorkNumRulesWrapper;
import org.springblade.modules.dictionaries.service.IWorkNumRulesService;
import org.springblade.core.boot.ctrl.BladeController;

import java.util.List;

/**
 * 工作号规则 控制器
 *
 * @author BladeX
 * @since 2019-09-03
 */
@RestController
@AllArgsConstructor
@RequestMapping("dictionaries/worknumrules")
@Api(value = "工作号规则", tags = "工作号规则接口")
public class WorkNumRulesController extends BladeController {

	private WorkNumRulesServiceImpl workNumRulesService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入workNumRules")
	public R<WorkNumRulesVO> detail(WorkNumRules workNumRules) {
		WorkNumRules detail = workNumRulesService.getOne(Condition.getQueryWrapper(workNumRules));
		return R.data(WorkNumRulesWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 工作号规则
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入workNumRules")
	public R<IPage<WorkNumRulesVO>> list(WorkNumRules workNumRules, Query query) {
		IPage<WorkNumRules> pages = workNumRulesService.page(Condition.getPage(query), Condition.getQueryWrapper(workNumRules));
		return R.data(WorkNumRulesWrapper.build().pageVO(pages));
	}

	/**
	 * 自定义分页 工作号规则
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入workNumRules")
	public R<IPage<WorkNumRulesVO>> page(WorkNumRulesVO workNumRules, Query query) {
		IPage<WorkNumRulesVO> pages = workNumRulesService.selectWorkNumRulesPage(Condition.getPage(query), workNumRules);
		return R.data(pages);
	}

	/**
	 * 新增 工作号规则
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入workNumRules")
	public R save(@Valid @RequestBody WorkNumRules workNumRules) {
		return R.status(workNumRulesService.save(workNumRules));
	}

	/**
	 * 修改 工作号规则
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入workNumRules")
	public R update(@Valid @RequestBody WorkNumRules workNumRules) {
		return R.status(workNumRulesService.updateById(workNumRules));
	}

	/**
	 * 新增或修改 工作号规则
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入workNumRules")
	public R submit(@Valid @RequestBody WorkNumRules workNumRules) {
		System.out.println(JSONObject.toJSONString(workNumRules)+"=========================");
		return R.status(workNumRulesService.saveOrUpdate(workNumRules));
	}


	/**
	 * 删除 工作号规则
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(workNumRulesService.removeByIds(Func.toLongList(ids)));
	}


}
