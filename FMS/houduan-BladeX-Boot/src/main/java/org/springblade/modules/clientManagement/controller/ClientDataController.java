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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.clientManagement.service.impl.ClientDataServiceImpl;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.clientManagement.entity.ClientData;
import org.springblade.modules.clientManagement.vo.ClientDataVO;
import org.springblade.modules.clientManagement.wrapper.ClientDataWrapper;
import org.springblade.modules.clientManagement.service.IClientDataService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 客户资料 控制器
 *
 * @author BladeX
 * @since 2019-08-20
 */
@RestController
@AllArgsConstructor
@RequestMapping("client/clientdata")
@Api(value = "客户资料", tags = "客户资料接口")
public class ClientDataController extends BladeController {

	private ClientDataServiceImpl clientDataService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入clientData")
	public R<ClientDataVO> detail(ClientData clientData) {
		clientData.setCreateUser(getUser().getUserId());
		ClientData detail = clientDataService.getOne(Condition.getQueryWrapper(clientData));
		return R.data(ClientDataWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 客户资料
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入clientData")
	public R<IPage<ClientDataVO>> list(ClientData clientData, Query query) {
		BladeUser user = getUser();
		clientData.setCreateUser(user.getUserId());
		IPage<ClientData> pages = clientDataService.page(Condition.getPage(query), Condition.getQueryWrapper(clientData));
		return R.data(ClientDataWrapper.build().pageVO(pages));
	}

	/**
	 * 自定义分页 客户资料
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入clientData")
	public R<IPage<ClientDataVO>> page(ClientDataVO clientData, Query query) {
		clientData.setCreateUser(getUser().getUserId());
		IPage<ClientDataVO> pages = clientDataService.selectClientDataPage(Condition.getPage(query), clientData);
		return R.data(pages);
	}

	/**
	 * 新增 客户资料
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入clientData")
	public R save(@Valid @RequestBody ClientData clientData) {
		return R.status(clientDataService.save(clientData));
	}

	/**
	 * 修改 客户资料
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入clientData")
	public R update(@Valid @RequestBody ClientData clientData) {
		return R.status(clientDataService.updateById(clientData));
	}

	/**
	 * 新增或修改 客户资料
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入clientData")
	public R submit(@Valid @RequestBody ClientData clientData) {
		return R.status(clientDataService.saveOrUpdate(clientData));
	}


	/**
	 * 删除 客户资料
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(clientDataService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 批量导入
	 */
	@PostMapping("/batchImport")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "批量导入", notes = "传入excel文件")
	public R batchImport(MultipartFile file) {
		return R.status(clientDataService.batchImport(file, getUser()));
	}

	/**
	 * 批量导出
	 *
	 * @param response
	 */
	@GetMapping("/batchExport")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "批量导出", notes = "导出excel文件")
	public void batchExport(HttpServletResponse response) {
		clientDataService.batchExport(response);
	}

	/**
	 * 导入模板
	 */
	@GetMapping("/importTemplate")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "导入模板", notes = "导入模板")
	public void importTemplate(HttpServletResponse response) {
		ClassPathResource resource = new ClassPathResource("/static/client_import_template.xlsx");
		try {
			Workbook workbook = new XSSFWorkbook(resource.getFile());
			CommonUtil.excelExport(workbook,response,"client");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
