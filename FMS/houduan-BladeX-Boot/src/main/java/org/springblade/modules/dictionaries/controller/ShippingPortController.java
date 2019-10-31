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
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.dictionaries.entity.Currency;
import org.springblade.modules.dictionaries.entity.ShippingPort;
import org.springblade.modules.dictionaries.service.IShippingPortService;
import org.springblade.modules.dictionaries.vo.ShippingPortVO;
import org.springblade.modules.dictionaries.wrapper.ShippingPortWrapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *  控制器
 *1
 * @author BladeX
 * @since 2019-08-13
 */
@RestController
@AllArgsConstructor
@RequestMapping("dictionaries/shippingport")
@Api(value = "", tags = "接口")
public class ShippingPortController extends BladeController {

	private IShippingPortService shippingPortService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入shippingPort")
	public R<ShippingPortVO> detail(ShippingPort shippingPort) {
		ShippingPort detail = shippingPortService.getOne(Condition.getQueryWrapper(shippingPort));
		return R.data(ShippingPortWrapper.build().entityVO(detail));
	}

	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入shippingPort")
	public R<IPage<ShippingPortVO>> list(ShippingPort shippingPort, Query query) {
		IPage<ShippingPort> pages = shippingPortService.page(Condition.getPage(query), Condition.getQueryWrapper(shippingPort));
		return R.data(ShippingPortWrapper.build().pageVO(pages));
	}

	/**
	 * 自定义分页
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入shippingPort")
	public R<IPage<ShippingPortVO>> page(ShippingPortVO shippingPort, Query query) {
		IPage<ShippingPortVO> pages = shippingPortService.selectShippingPortPage(Condition.getPage(query), shippingPort);
		return R.data(pages);
	}

	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入shippingPort")
	public R save(@Valid @RequestBody ShippingPort shippingPort) {
		return R.status(shippingPortService.save(shippingPort));
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入shippingPort")
	public R update(@Valid @RequestBody ShippingPort shippingPort) {
		return R.status(shippingPortService.updateById(shippingPort));
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入shippingPort")
	public R submit(@Valid @RequestBody ShippingPort shippingPort) {
		return R.status(shippingPortService.saveOrUpdate(shippingPort));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(shippingPortService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 导入
	 */
	@PostMapping("/toLead")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "导入", notes = "传入file文件")
	public R toLead(@RequestParam(value = "file") MultipartFile file) {
		List<ShippingPort> lists = new ArrayList<ShippingPort>();
		Boolean b;
		try {
			Workbook workbook = WorkbookFactory.create(file.getInputStream());
			Sheet sheet = workbook.getSheetAt(0);
			//System.out.println("数据长度"+sheet.getLastRowNum());
			if(sheet.getLastRowNum()<1){
				b=false;
			}else{
				for (int numSheet = 1; numSheet <= sheet.getLastRowNum(); numSheet++) {
					Row row = sheet.getRow(numSheet);
					if (row != null) {
						ShippingPort sPort = new ShippingPort();
						Cell cell1=row.getCell(0);
						Cell cell2=row.getCell(1);
						Cell cell3=row.getCell(2);
						Cell cell4=row.getCell(3);
						cell1.setCellType(CellType.STRING);
						cell2.setCellType(CellType.STRING);
						cell3.setCellType(CellType.STRING);
						cell4.setCellType(CellType.STRING);
						sPort.setCountryName(cell1.getStringCellValue());
						sPort.setSite(cell2.getStringCellValue());
						sPort.setSiteType(cell3.getStringCellValue());
						sPort.setPortCode(cell4.getStringCellValue());
						ShippingPort sp = shippingPortService.getOne(Condition.getQueryWrapper(sPort));
						if (sp == null) {
							lists.add(sPort);
						}
					}
				}
				b=shippingPortService.saveBatch(lists);
			}
		} catch (Exception e) {
			e.printStackTrace();
			b=false;
		}
		return R.status(b);
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
		row.createCell(0).setCellValue("国家名/州名");
		row.createCell(1).setCellValue("地点名称");
		row.createCell(2).setCellValue("地点类型");
		row.createCell(3).setCellValue("港口代码");
		//浏览器下载excel
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment;filename=shippingPort.xls");
		response.flushBuffer();
		OutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		outputStream.flush();
		outputStream.close();


	}


}
