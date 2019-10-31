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
import org.springblade.modules.businessManagement.entity.Task;
import org.springblade.modules.businessManagement.vo.TaskVO;
import org.springblade.modules.businessManagement.wrapper.TaskWrapper;
import org.springblade.modules.businessManagement.service.ITaskService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 作业项目 控制器
 *
 * @author BladeX
 * @since 2019-10-25
 */
@RestController
@AllArgsConstructor
@RequestMapping("business/task")
@Api(value = "作业项目", tags = "作业项目接口")
public class TaskController extends BladeController {

	private ITaskService taskService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入task")
	public R<TaskVO> detail(Task task) {
		Task detail = taskService.getOne(Condition.getQueryWrapper(task));
		return R.data(TaskWrapper.build().entityVO(detail));
	}

	/**
	* 分页 作业项目
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入task")
	public R<IPage<TaskVO>> list(Task task, Query query) {
		IPage<Task> pages = taskService.page(Condition.getPage(query), Condition.getQueryWrapper(task));
		return R.data(TaskWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 作业项目
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入task")
	public R<IPage<TaskVO>> page(TaskVO task, Query query) {
		IPage<TaskVO> pages = taskService.selectTaskPage(Condition.getPage(query), task);
		return R.data(pages);
	}

	/**
	* 新增 作业项目
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入task")
	public R save(@Valid @RequestBody Task task) {
		return R.status(taskService.save(task));
	}

	/**
	* 修改 作业项目
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入task")
	public R update(@Valid @RequestBody Task task) {
		return R.status(taskService.updateById(task));
	}

	/**
	* 新增或修改 作业项目
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入task")
	public R submit(@Valid @RequestBody Task task) {
		return R.status(taskService.saveOrUpdate(task));
	}

	
	/**
	* 删除 作业项目
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(taskService.removeByIds(Func.toLongList(ids)));
	}

	
}
