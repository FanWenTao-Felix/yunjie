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
import org.springblade.modules.businessManagement.entity.TaskName;
import org.springblade.modules.businessManagement.vo.TaskNameVO;
import org.springblade.modules.businessManagement.wrapper.TaskNameWrapper;
import org.springblade.modules.businessManagement.service.ITaskNameService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 作业项目名管理 控制器
 *
 * @author BladeX
 * @since 2019-10-25
 */
@RestController
@AllArgsConstructor
@RequestMapping("business/taskname")
@Api(value = "作业项目名管理", tags = "作业项目名管理接口")
public class TaskNameController extends BladeController {

	private ITaskNameService taskNameService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入taskName")
	public R<TaskNameVO> detail(TaskName taskName) {
		TaskName detail = taskNameService.getOne(Condition.getQueryWrapper(taskName));
		return R.data(TaskNameWrapper.build().entityVO(detail));
	}

	/**
	* 分页 作业项目名管理
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入taskName")
	public R<IPage<TaskNameVO>> list(TaskName taskName, Query query) {
		IPage<TaskName> pages = taskNameService.page(Condition.getPage(query), Condition.getQueryWrapper(taskName));
		return R.data(TaskNameWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 作业项目名管理
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入taskName")
	public R<IPage<TaskNameVO>> page(TaskNameVO taskName, Query query) {
		IPage<TaskNameVO> pages = taskNameService.selectTaskNamePage(Condition.getPage(query), taskName);
		return R.data(pages);
	}

	/**
	* 新增 作业项目名管理
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入taskName")
	public R save(@Valid @RequestBody TaskName taskName) {
		return R.status(taskNameService.save(taskName));
	}

	/**
	* 修改 作业项目名管理
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入taskName")
	public R update(@Valid @RequestBody TaskName taskName) {
		return R.status(taskNameService.updateById(taskName));
	}

	/**
	* 新增或修改 作业项目名管理
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入taskName")
	public R submit(@Valid @RequestBody TaskName taskName) {
		return R.status(taskNameService.saveOrUpdate(taskName));
	}

	
	/**
	* 删除 作业项目名管理
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(taskNameService.removeByIds(Func.toLongList(ids)));
	}

	
}
