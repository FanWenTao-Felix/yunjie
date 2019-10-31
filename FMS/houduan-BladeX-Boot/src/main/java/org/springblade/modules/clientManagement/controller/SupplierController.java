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
package org.springblade.modules.clientManagement.controller;

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
import org.springblade.modules.clientManagement.service.impl.SupplierServiceImpl;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.clientManagement.entity.Supplier;
import org.springblade.modules.clientManagement.vo.SupplierVO;
import org.springblade.modules.clientManagement.wrapper.SupplierWrapper;
import org.springblade.modules.clientManagement.service.ISupplierService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 供应商资料 控制器
 *
 * @author BladeX
 * @since 2019-08-28
 */
@RestController
@AllArgsConstructor
@RequestMapping("client/supplier")
@Api(value = "供应商资料", tags = "供应商资料接口")
public class SupplierController extends BladeController {

	private SupplierServiceImpl supplierService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入supplier")
	public R<SupplierVO> detail(Supplier supplier) {
		Supplier detail = supplierService.getOne(Condition.getQueryWrapper(supplier));
		return R.data(SupplierWrapper.build().entityVO(detail));
	}

	/**
	* 分页 供应商资料
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入supplier")
	public R<IPage<SupplierVO>> list(Supplier supplier, Query query) {
		IPage<Supplier> pages = supplierService.page(Condition.getPage(query), Condition.getQueryWrapper(supplier));
		return R.data(SupplierWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 供应商资料
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入supplier")
	public R<IPage<SupplierVO>> page(SupplierVO supplier, Query query) {
		IPage<SupplierVO> pages = supplierService.selectSupplierPage(Condition.getPage(query), supplier);
		return R.data(pages);
	}

	/**
	* 新增 供应商资料
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入supplier")
	public R save(@Valid @RequestBody Supplier supplier) {
		return R.status(supplierService.save(supplier));
	}

	/**
	* 修改 供应商资料
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入supplier")
	public R update(@Valid @RequestBody Supplier supplier) {
		return R.status(supplierService.updateById(supplier));
	}

	/**
	* 新增或修改 供应商资料
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入supplier")
	public R submit(@Valid @RequestBody Supplier supplier) {
		return R.status(supplierService.saveOrUpdate(supplier));
	}


	/**
	* 删除 供应商资料
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(supplierService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 批量导入
	 */
	@PostMapping("/batchImport")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "批量导入", notes = "传入excel文件")
	public R batchImport(MultipartFile file) {
		return R.status(supplierService.batchImport(file, getUser()));
	}

	/**
	 * 批量导出
	 * @param response
	 */
	@GetMapping("/batchExport")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "批量导出", notes = "导出excel文件")
	public void batchExport(HttpServletResponse response) {
		supplierService.batchExport(response);
	}



}
