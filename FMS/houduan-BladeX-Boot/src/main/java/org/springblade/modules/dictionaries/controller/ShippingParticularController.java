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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import liquibase.util.StringUtils;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.dictionaries.entity.ShippingParticular;
import org.springblade.modules.dictionaries.vo.ShippingParticularVO;
import org.springblade.modules.dictionaries.wrapper.ShippingParticularWrapper;
import org.springblade.modules.dictionaries.service.IShippingParticularService;
import org.springblade.core.boot.ctrl.BladeController;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 *  控制器
 *
 * @author BladeX
 * @since 2019-09-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("dictionaries/shippingparticular")
@Api(value = "", tags = "接口")
public class ShippingParticularController extends BladeController {

	private IShippingParticularService shippingParticularService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入shippingParticular")
	public R<ShippingParticularVO> detail(ShippingParticular shippingParticular) {
		ShippingParticular detail = shippingParticularService.getOne(Condition.getQueryWrapper(shippingParticular));
		return R.data(ShippingParticularWrapper.build().entityVO(detail));
	}

	/**
	* 分页
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入shippingParticular")
	public R<IPage<ShippingParticularVO>> list(ShippingParticular shippingParticular, Query query) {
		if(StringUtils.isEmpty(shippingParticular.getLineId())){
			System.out.println(shippingParticular.getLineId());
			return R.data(ShippingParticularWrapper.build().pageVO(new Page<>()));
		}
		IPage<ShippingParticular> pages = shippingParticularService.page(Condition.getPage(query), Condition.getQueryWrapper(shippingParticular));
		return R.data(ShippingParticularWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入shippingParticular")
	public R<IPage<ShippingParticularVO>> page(ShippingParticularVO shippingParticular, Query query) {
		IPage<ShippingParticularVO> pages = shippingParticularService.selectShippingParticularPage(Condition.getPage(query), shippingParticular);
		return R.data(pages);
	}

	/**
	* 新增
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入shippingParticular")
	public R save(@Valid @RequestBody ShippingParticular shippingParticular) {
		return R.status(shippingParticularService.save(shippingParticular));
	}

	/**
	* 修改
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入shippingParticular")
	public R update(@Valid @RequestBody ShippingParticular shippingParticular) {
		return R.status(shippingParticularService.updateById(shippingParticular));
	}

	/**
	* 新增或修改
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入shippingParticular")
	public R submit(@Valid @RequestBody ShippingParticular shippingParticular) {
		return R.status(shippingParticularService.saveOrUpdate(shippingParticular));
	}


	/**
	* 删除
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(shippingParticularService.removeByIds(Func.toLongList(ids)));
	}


	//下载模板
	@GetMapping("/download")
	@ApiOperation(value = "下载模板", notes = "导出storage", position = 10)
	public void download(HttpServletResponse response) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet("Sheet1");
		//产生表格标题行
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("起运港");
		row.createCell(1).setCellValue("目的港");
		row.createCell(2).setCellValue("截关/开船");
		row.createCell(3).setCellValue("船程");
		row.createCell(4).setCellValue("中转");
		row.createCell(5).setCellValue("GP20");
		row.createCell(6).setCellValue("GP40");
		row.createCell(7).setCellValue("HC40");
		row.createCell(8).setCellValue("HC45");
		row.createCell(9).setCellValue("运价备注");
		row.createCell(10).setCellValue("有效期");
		HSSFRow row1 = sheet.createRow(1);
		row1.createCell(0).setCellValue("CAN");
		row1.createCell(1).setCellValue("CAN");
		row1.createCell(2).setCellValue("1/3");
		row1.createCell(3).setCellValue("5天");
		row1.createCell(4).setCellValue("CAN");
		row1.createCell(5).setCellValue(60.0);
		row1.createCell(6).setCellValue(60.0);
		row1.createCell(7).setCellValue(60.0);
		row1.createCell(8).setCellValue(60.0);
		//row1.createCell(9).setCellValue();
		row1.createCell(10).setCellValue("2019.1.2");
		//浏览器下载excel
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment;filename=CMA_达飞_CAN.xls");
		response.flushBuffer();
		OutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		outputStream.flush();
		outputStream.close();


	}




}
