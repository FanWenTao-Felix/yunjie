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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import oracle.jdbc.proxy.annotation.Post;
import org.apache.poi.hssf.usermodel.HSSFCell;
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
import org.springblade.modules.dictionaries.service.ICurrencyService;
import org.springblade.modules.dictionaries.vo.CurrencyVO;
import org.springblade.modules.dictionaries.vo.ShippingPortVO;
import org.springblade.modules.dictionaries.wrapper.CurrencyWrapper;
import org.springblade.modules.dictionaries.wrapper.ShippingPortWrapper;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  控制器
 *
 * @author BladeX
 * @since 2019-08-13
 */
@RestController
@AllArgsConstructor
@RequestMapping("dictionaries/currency")
@Api(value = "", tags = "接口")
public class CurrencyController extends BladeController {

	private ICurrencyService currencyService;

	/**
	 * 详情1
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入currency")
	public R<CurrencyVO> detail(Currency currency) {
		Currency detail = currencyService.getOne(Condition.getQueryWrapper(currency));
		return R.data(CurrencyWrapper.build().entityVO(detail));
	}
	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入currency")
	public R<IPage<CurrencyVO>> list(Currency currency, Query query) {
		IPage<Currency> pages = currencyService.page(Condition.getPage(query), Condition.getQueryWrapper(currency));
		return R.data(CurrencyWrapper.build().pageVO(pages));
	}

	/**
	 * 自定义分页
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入currency")
	public R<IPage<CurrencyVO>> page(CurrencyVO currency, Query query) {
		IPage<CurrencyVO> pages = currencyService.selectCurrencyPage(Condition.getPage(query), currency);
		return R.data(pages);
	}

	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入currency")
	public R save(@Valid @RequestBody Currency currency) {
		return R.status(currencyService.save(currency));
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入currency")
	public R update(@Valid @RequestBody Currency currency) {
		return R.status(currencyService.updateById(currency));
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入currency")
	public R submit(@Valid @RequestBody Currency currency) {
		return R.status(currencyService.saveOrUpdate(currency));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(currencyService.removeByIds(Func.toLongList(ids)));
	}
	/**
	 * 导入
	 */
	@PostMapping("/toLeadCurrency")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "导入", notes = "传入file文件")
	public R toLeadCurrency(@RequestParam(value="file") MultipartFile file) {
		List<Currency> lists = new ArrayList<Currency>();
		Boolean b;
		try {
			Workbook workbook = WorkbookFactory.create(file.getInputStream());
			Sheet sheet = workbook.getSheetAt(0);
			if(sheet.getLastRowNum()<1){
				b=false;
			}else {
				for (int numSheet = 1; numSheet <= sheet.getLastRowNum(); numSheet++) {
					Row row = sheet.getRow(numSheet);
					if (row != null) {
						Currency currency = new Currency();
						Cell cell1=row.getCell(0);
						Cell cell2=row.getCell(1);
						Cell cell3=row.getCell(2);
						Cell cell4=row.getCell(3);
						cell1.setCellType(CellType.STRING);
						cell2.setCellType(CellType.STRING);
						cell3.setCellType(CellType.STRING);
						cell4.setCellType(CellType.STRING);
						currency.setCurrencyCode(cell1.getStringCellValue());
						currency.setCurrencyName(cell2.getStringCellValue());
						currency.setEnglishName(cell3.getStringCellValue());
						currency.setCurrencySymbol(cell4.getStringCellValue());
						System.out.println(currency);
						Currency c = currencyService.getOne(Condition.getQueryWrapper(currency));
						System.out.println(c);
						if (c == null) {
							lists.add(currency);
						}
					}
				}
				b=currencyService.saveBatch(lists);
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
		row.createCell(0).setCellValue("货币编号");
		row.createCell(1).setCellValue("货币名称");
		row.createCell(2).setCellValue("英文名称");
		row.createCell(3).setCellValue("货币符号");

		//浏览器下载excel
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment;filename=currency.xls");
		response.flushBuffer();
		OutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		outputStream.flush();
		outputStream.close();


	}



}
