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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.businessManagement.service.impl.BusinessFileServiceImpl;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.businessManagement.entity.BusinessFile;
import org.springblade.modules.businessManagement.vo.BusinessFileVO;
import org.springblade.modules.businessManagement.wrapper.BusinessFileWrapper;
import org.springblade.modules.businessManagement.service.IBusinessFileService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 文件管理 控制器
 *
 * @author BladeX
 * @since 2019-09-12
 */
@RestController
@AllArgsConstructor
@RequestMapping("business/businessfile")
@Api(value = "文件管理", tags = "文件管理接口")
public class BusinessFileController extends BladeController {

	private BusinessFileServiceImpl businessFileService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入businessFile")
	public R<BusinessFileVO> detail(BusinessFile businessFile) {
		BusinessFile detail = businessFileService.getOne(Condition.getQueryWrapper(businessFile));
		return R.data(BusinessFileWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 文件管理
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入businessFile")
	public R<IPage<BusinessFileVO>> list(BusinessFile businessFile, Query query) {
		IPage<BusinessFile> pages = businessFileService.page(Condition.getPage(query), Condition.getQueryWrapper(businessFile));
		return R.data(BusinessFileWrapper.build().pageVO(pages));
	}

	/**
	 * 自定义分页 文件管理
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入businessFile")
	public R<IPage<BusinessFileVO>> page(BusinessFileVO businessFile, Query query) {
		IPage<BusinessFileVO> pages = businessFileService.selectBusinessFilePage(Condition.getPage(query), businessFile);
		return R.data(pages);
	}

	/**
	 * 新增 文件管理
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入businessFile")
	public R save(BusinessFile businessFile) {
		return R.status(businessFileService.saveFile(businessFile));
	}

	/**
	 * 修改 文件管理
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入businessFile")
	public R update(@Valid @RequestBody BusinessFile businessFile) {
		return R.status(businessFileService.updateById(businessFile));
	}

	/**
	 * 新增或修改 文件管理
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入businessFile")
	public R submit(@Valid @RequestBody BusinessFile businessFile) {
		return R.status(businessFileService.saveOrUpdate(businessFile));
	}


	/**
	 * 删除 文件管理
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(businessFileService.removeFiles(ids));
	}


	/**
	 * 下载文件
	 */
	@GetMapping("/getfile")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入businessFile")
	public void getFile(BusinessFile businessFile, HttpServletResponse response) {
		businessFileService.downloadFile(businessFile, response);
	}


}
