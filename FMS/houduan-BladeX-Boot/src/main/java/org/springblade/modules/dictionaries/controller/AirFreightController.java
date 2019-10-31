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
import org.springblade.modules.dictionaries.entity.AirFreightInfo;
import org.springblade.modules.dictionaries.service.IAirFreightInfoService;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.dictionaries.entity.AirFreight;
import org.springblade.modules.dictionaries.vo.AirFreightVO;
import org.springblade.modules.dictionaries.wrapper.AirFreightWrapper;
import org.springblade.modules.dictionaries.service.IAirFreightService;
import org.springblade.core.boot.ctrl.BladeController;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 *  控制器
 *
 * @author BladeX
 * @since 2019-09-05
 */
@RestController
@AllArgsConstructor
@RequestMapping("dictionaries/airfreight")
@Api(value = "", tags = "接口")
public class AirFreightController extends BladeController {

	private IAirFreightService airFreightService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入airFreight")
	public R<AirFreightVO> detail(AirFreight airFreight) {
		AirFreight detail = airFreightService.getOne(Condition.getQueryWrapper(airFreight));
		return R.data(AirFreightWrapper.build().entityVO(detail));
	}

	/**
	* 分页
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入airFreight")
	public R<IPage<AirFreightVO>> list(AirFreight airFreight, Query query) {
		//System.out.println("222222222222222222222222");
		if (StringUtils.isEmpty(airFreight.getFreigthInfoId()))
			return R.data(AirFreightWrapper.build().pageVO(new Page<>()));
		IPage<AirFreight> pages = airFreightService.page(Condition.getPage(query), Condition.getQueryWrapper(airFreight));
		return R.data(AirFreightWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入airFreight")
	public R<IPage<AirFreightVO>> page(AirFreightVO airFreight, Query query) {
		IPage<AirFreightVO> pages = airFreightService.selectAirFreightPage(Condition.getPage(query), airFreight);
		return R.data(pages);
	}

	/**
	* 新增
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入airFreight")
	public R save(@Valid @RequestBody AirFreight airFreight) {
		return R.status(airFreightService.save(airFreight));
	}

	/**
	* 修改
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入airFreight")
	public R update(@Valid @RequestBody AirFreight airFreight) {
		return R.status(airFreightService.updateById(airFreight));
	}

	/**
	* 新增或修改
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入airFreight")
	public R submit(@Valid @RequestBody AirFreight airFreight) {
		return R.status(airFreightService.saveOrUpdate(airFreight));
	}


	/**
	* 删除
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {

		return R.status(airFreightService.removeByIds(Func.toLongList(ids)));
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
		row.createCell(0).setCellValue("地区");
		row.createCell(1).setCellValue("起运港口");
		row.createCell(2).setCellValue("到达港口");
		row.createCell(3).setCellValue("最大值");
		row.createCell(4).setCellValue("最小值");
		row.createCell(5).setCellValue("+45KG");
		row.createCell(6).setCellValue("+100KG");
		row.createCell(7).setCellValue("+300KG");
		row.createCell(8).setCellValue("+500KG");
		row.createCell(9).setCellValue("+1000KG");
		row.createCell(10).setCellValue("ROUTING");
		row.createCell(11).setCellValue("2nd");
		row.createCell(12).setCellValue("3th");
		row.createCell(13).setCellValue("时效");
		row.createCell(14).setCellValue("备注");
		HSSFRow row1 = sheet.createRow(1);
		//row1.createCell(0).setCellValue("");
		row1.createCell(1).setCellValue("CAN");
		row1.createCell(2).setCellValue("TLL");
	//	row1.createCell(3).setCellValue("");
		//row1.createCell(4).setCellValue("");
		row1.createCell(5).setCellValue(60.0);
		row1.createCell(6).setCellValue(60.0);
		row1.createCell(7).setCellValue(60.0);
		row1.createCell(8).setCellValue(60.0);
		row1.createCell(9).setCellValue(59.0);
		//row1.createCell(10).setCellValue("");
		//row1.createCell(11).setCellValue("");
		//row1.createCell(12).setCellValue("");
		//row1.createCell(13).setCellValue("");
		//row1.createCell(14).setCellValue("");
		//浏览器下载excel
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment;filename=071_ET_CAN.xls");
		response.flushBuffer();
		OutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		outputStream.flush();
		outputStream.close();


	}




}
