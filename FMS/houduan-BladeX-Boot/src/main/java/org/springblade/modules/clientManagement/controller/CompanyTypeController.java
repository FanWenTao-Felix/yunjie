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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.clientManagement.enums.CompanyTypeEnum;
import org.springblade.modules.clientManagement.service.impl.CompanyTypeServiceImpl;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.clientManagement.entity.CompanyType;
import org.springblade.modules.clientManagement.vo.CompanyTypeVO;
import org.springblade.modules.clientManagement.wrapper.CompanyTypeWrapper;
import org.springblade.modules.clientManagement.service.ICompanyTypeService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 公司类型 控制器
 *
 * @author BladeX
 * @since 2019-08-20
 */
@RestController
@AllArgsConstructor
@RequestMapping("client/companytype")
@Api(value = "公司类型", tags = "公司类型接口")
public class CompanyTypeController extends BladeController {

	private CompanyTypeServiceImpl companyTypeService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入companyType")
	public R<CompanyTypeVO> detail(CompanyType companyType) {
		CompanyType detail = companyTypeService.getOne(Condition.getQueryWrapper(companyType));
		return R.data(CompanyTypeWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 公司类型
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入companyType")
	public R<IPage<CompanyTypeVO>> list(CompanyType companyType, Query query) {
		IPage<CompanyType> pages = new Page<>();//companyTypeService.page(Condition.getPage(query), Condition.getQueryWrapper(companyType));
		List<CompanyType> companyTypeList = new ArrayList<>();
		CompanyTypeEnum[] companyTypeEnums = CompanyTypeEnum.values();
		for (int i = 0; i < companyTypeEnums.length; i++) {
			CompanyType ct = new CompanyType();
			ct.setCode(companyTypeEnums[i].getValue());
			ct.setName(companyTypeEnums[i].getName());
			companyTypeList.add(ct);
		}
		pages.setRecords(companyTypeList);
		return R.data(CompanyTypeWrapper.build().pageVO(pages));
	}

	/**
	 * 自定义分页 公司类型
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入companyType")
	public R<IPage<CompanyTypeVO>> page(CompanyTypeVO companyType, Query query) {
		IPage<CompanyTypeVO> pages = companyTypeService.selectCompanyTypePage(Condition.getPage(query), companyType);
		return R.data(pages);
	}

	/**
	 * 新增 公司类型
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入companyType")
	public R save(@Valid @RequestBody CompanyType companyType) {
		return R.status(companyTypeService.save(companyType));
	}

	/**
	 * 修改 公司类型
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入companyType")
	public R update(@Valid @RequestBody CompanyType companyType) {
		return R.status(companyTypeService.updateById(companyType));
	}

	/**
	 * 新增或修改 公司类型
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入companyType")
	public R submit(@Valid @RequestBody CompanyType companyType) {
		return R.status(companyTypeService.saveOrUpdate(companyType));
	}


	/**
	 * 删除 公司类型
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(companyTypeService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 批量导入
	 */
	@PostMapping("/batchImport")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "批量导入", notes = "传入excel文件")
	public R batchImport(MultipartFile file) {
		return R.status(companyTypeService.batchImport(file, getUser()));
	}

}
