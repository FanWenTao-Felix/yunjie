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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.dictionaries.entity.Air;
import org.springblade.modules.dictionaries.vo.AirVO;
import org.springblade.modules.dictionaries.wrapper.AirWrapper;
import org.springblade.modules.dictionaries.service.IAirService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *  控制器
 *
 * @author BladeX
 * @since 2019-08-20
 */
@RestController
@AllArgsConstructor
@RequestMapping("dictionaries/air")
@Api(value = "", tags = "接口")
public class AirController extends BladeController {

	private IAirService airService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入air")
	public R<AirVO> detail(Air air) {
		Air detail = airService.getOne(Condition.getQueryWrapper(air));
		return R.data(AirWrapper.build().entityVO(detail));
	}

	/**
	* 分页 
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入air")
	public R<IPage<AirVO>> list(Air air, Query query) {
		IPage<Air> pages = airService.page(Condition.getPage(query), Condition.getQueryWrapper(air));
		return R.data(AirWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入air")
	public R<IPage<AirVO>> page(AirVO air, Query query) {
		IPage<AirVO> pages = airService.selectAirPage(Condition.getPage(query), air);
		return R.data(pages);
	}

	/**
	* 新增 
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入air")
	public R save(@Valid @RequestBody Air air) {
		return R.status(airService.save(air));
	}

	/**
	* 修改 
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入air")
	public R update(@Valid @RequestBody Air air) {
		return R.status(airService.updateById(air));
	}

	/**
	* 新增或修改 
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入air")
	public R submit(@Valid @RequestBody Air air) {
		return R.status(airService.saveOrUpdate(air));
	}

	
	/**
	* 删除 
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(airService.removeByIds(Func.toLongList(ids)));
	}

	@PostMapping("/uploadfile")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "上传", notes = "传入file")
	public R uploadfile(@RequestParam(value="file") MultipartFile file) {
		List<Air> list=new ArrayList<Air>();
		if(file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.')+1).toLowerCase().equals("xlsx")){
		try {
			XSSFWorkbook workbook = null;
			workbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet sheet=workbook.getSheetAt(0);//拿到第几张表
			for (int numSheet = 1; numSheet <= sheet.getLastRowNum(); numSheet++) {
				Row row = sheet.getRow(numSheet);
				if (row != null) {
					Air air = new Air();
					if(row.getCell(0)==null){air.setAirContinent("");}else{air.setAirContinent(row.getCell(0).toString());}
					if(row.getCell(1)==null){air.setAirCountryname("");}else{air.setAirCountryname(row.getCell(1).toString());}
					if(row.getCell(2)==null){air.setAirEncityname("");}else{air.setAirEncityname(row.getCell(2).toString());}
					if(row.getCell(3)==null){air.setAirChcityname("");}else{air.setAirChcityname(row.getCell(3).toString());}
					if(row.getCell(4)==null){air.setAirCode("");}else{air.setAirCode(row.getCell(4).toString());}
					if(row.getCell(5)==null){air.setFullName("");}else{air.setFullName(row.getCell(5).toString());}
					Air ai = airService.getOne(Condition.getQueryWrapper(air));
					System.out.println(ai);
					if (ai == null) {
						list.add(air);
					}
				}else {
					System.out.println("不可重复添加");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		}else if(file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.')+1).toLowerCase().equals("xls")){
			try {
				HSSFWorkbook workbook = null;
				workbook = new HSSFWorkbook(file.getInputStream());
				HSSFSheet sheet=workbook.getSheetAt(0);//拿到第几张表
				for (int numSheet = 1; numSheet <= sheet.getLastRowNum(); numSheet++) {
					Row row = sheet.getRow(numSheet);
					if (row != null) {
						Air air = new Air();
						if(row.getCell(0)==null){air.setAirContinent("");}else{air.setAirContinent(row.getCell(0).toString());}
						if(row.getCell(1)==null){air.setAirCountryname("");}else{air.setAirCountryname(row.getCell(1).toString());}
						if(row.getCell(2)==null){air.setAirEncityname("");}else{air.setAirEncityname(row.getCell(2).toString());}
						if(row.getCell(3)==null){air.setAirChcityname("");}else{air.setAirChcityname(row.getCell(3).toString());}
						if(row.getCell(4)==null){air.setAirCode("");}else{air.setAirCode(row.getCell(4).toString());}
						if(row.getCell(5)==null){air.setFullName("");}else{air.setFullName(row.getCell(5).toString());}
						Air ai = airService.getOne(Condition.getQueryWrapper(air));
						System.out.println(ai);
						if (ai == null) {
							list.add(air);
						}
					}else {
						System.out.println("不可重复添加");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return R.status(airService.saveBatch(list));
	}




	//全部导出，不可选择导出
	@GetMapping("/downloadfile")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "导出", notes = "传入ids")
	public void download(HttpServletResponse res) {
		List<Air> list=airService.list();
		try {
			XSSFWorkbook wb=new XSSFWorkbook();//创建工作簿
			XSSFSheet sheet = wb.createSheet();//创建表
			XSSFRow row=sheet.createRow(0);//创建表头
			XSSFCell cell=row.createCell(0);
			cell.setCellValue("所属区域");
			XSSFCell cell1=row.createCell(1);
			cell1.setCellValue("国家/地区");
			XSSFCell cell2=row.createCell(2);
			cell2.setCellValue("城市英文名称");
			XSSFCell cell3=row.createCell(3);
			cell3.setCellValue("城市中文名称");
			XSSFCell cell4=row.createCell(4);
			cell4.setCellValue("机场代码");
			XSSFCell cell5=row.createCell(5);
			cell5.setCellValue("机场名");
			for(int i=0;i<list.size();i++){
				Air air=list.get(i);
				//创建表头
				XSSFRow lrow = sheet.createRow(i+1);
				//创建单元格
				XSSFCell lcell = lrow.createCell(0);
				lcell.setCellValue(air.getAirContinent());
				XSSFCell lcell1 = lrow.createCell(1);
				lcell1.setCellValue(air.getAirCountryname());
				XSSFCell lcell2 = lrow.createCell(2);
				lcell2.setCellValue(air.getAirEncityname());
				XSSFCell lcell3= lrow.createCell(3);
				lcell3.setCellValue(air.getAirChcityname());
				XSSFCell lcell4= lrow.createCell(4);
				lcell4.setCellValue(air.getAirCode());
				XSSFCell lcell5= lrow.createCell(5);
				lcell5.setCellValue(air.getFullName());
			}
			res.setHeader("Content-disposition", "attachment; filename=airfreight.xlsx");// 设定输出文件头,该方法有两个参数，分别表示应答头的名字和值。XSSF:xlsx    HSSF:xls
			res.setContentType("application/msexcel");
			OutputStream outputStream = res.getOutputStream();
			wb.write(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下拉数据源
	 */
	@GetMapping("/select")
	@ApiOperationSupport(order = 11)
	@ApiOperation(value = "下拉数据源", notes = "传入id")
	public R<List<Air>> select() {
		List<Air> list = airService.list();
		return R.data(list);
	}



	//下载模板
	@GetMapping("/download")
	@ApiOperation(value = "下载模板", notes = "导出storage")
	public void downloadmodules(HttpServletResponse response) throws IOException {
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet("Sheet1");
		//产生表格标题行
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("所属区域");
		row.createCell(1).setCellValue("国家名");
		row.createCell(2).setCellValue("城市英文名");
		row.createCell(3).setCellValue("城市中文名");
		row.createCell(4).setCellValue("机场代码");
		row.createCell(5).setCellValue("全称");
		row.createCell(3).setCellValue("备注信息");
		//浏览器下载excel
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment; filename=airport.xls");
		response.flushBuffer();
		OutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		outputStream.flush();
		outputStream.close();
	}


	
}
