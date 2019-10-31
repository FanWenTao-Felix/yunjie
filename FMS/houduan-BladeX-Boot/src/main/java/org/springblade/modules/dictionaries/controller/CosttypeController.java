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
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.dictionaries.entity.Costtype;
import org.springblade.modules.dictionaries.vo.CosttypeVO;
import org.springblade.modules.dictionaries.wrapper.CosttypeWrapper;
import org.springblade.modules.dictionaries.service.ICosttypeService;
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
@RequestMapping("dictionaries/costtype")
@Api(value = "", tags = "接口")
public class CosttypeController extends BladeController {

	private ICosttypeService costtypeService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入costtype")
	public R<CosttypeVO> detail(Costtype costtype) {
		Costtype detail = costtypeService.getOne(Condition.getQueryWrapper(costtype));
		return R.data(CosttypeWrapper.build().entityVO(detail));
	}

	/**
	* 分页 
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入costtype")
	public R<IPage<CosttypeVO>> list(Costtype costtype, Query query) {
		IPage<Costtype> pages = costtypeService.page(Condition.getPage(query), Condition.getQueryWrapper(costtype));
		return R.data(CosttypeWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入costtype")
	public R<IPage<CosttypeVO>> page(CosttypeVO costtype, Query query) {
		IPage<CosttypeVO> pages = costtypeService.selectCosttypePage(Condition.getPage(query), costtype);
		return R.data(pages);
	}

	/**
	* 新增 
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入costtype")
	public R save(@Valid @RequestBody Costtype costtype) {
		return R.status(costtypeService.save(costtype));
	}

	/**
	* 修改 
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入costtype")
	public R update(@Valid @RequestBody Costtype costtype) {
		return R.status(costtypeService.updateById(costtype));
	}

	/**
	* 新增或修改 
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入costtype")
	public R submit(@Valid @RequestBody Costtype costtype) {
		return R.status(costtypeService.saveOrUpdate(costtype));
	}

	
	/**
	* 删除 
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 7)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(costtypeService.removeByIds(Func.toLongList(ids)));
	}
	/**
	 * 下拉数据源
	 */
	@GetMapping("/select")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "下拉数据源", notes = "传入id")
	public R<List<Costtype>> select() {
		List<Costtype> list = costtypeService.list();
		return R.data(list);
	}

	@PostMapping("/uploadfile")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "上传", notes = "传入file")
	public R uploadfile(@RequestParam(value="file") MultipartFile file) {
		List<Costtype> list=new ArrayList<Costtype>();
		if(file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.')+1).toLowerCase().equals("xlsx")){
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet sheet=workbook.getSheetAt(0);//拿到第几张表

			for (int numSheet = 1; numSheet <=sheet.getLastRowNum(); numSheet++) {
				Row row = sheet.getRow(numSheet);
				if (row != null) {
					Costtype costtype = new Costtype();
					if(row.getCell(0)==null){costtype.setCostName("");}else{costtype.setCostName(row.getCell(0).toString());}
					if(row.getCell(1)==null){costtype.setCostType("");}else{costtype.setCostType(row.getCell(1).toString());}
					if(row.getCell(2)==null){ costtype.setCostAllname("");}else{costtype.setCostAllname(row.getCell(2).toString());}
					if(row.getCell(3)==null){ costtype.setCostCode("");}else{costtype.setCostCode(row.getCell(3).toString());}
					if(row.getCell(4)==null){ costtype.setCostEnname("");}else{costtype.setCostEnname(row.getCell(4).toString());}
					if(row.getCell(5)==null){ costtype.setCostDefault("");}else{costtype.setCostDefault(row.getCell(5).toString());}
					if(row.getCell(6)==null){ costtype.setCostBz("");}else{costtype.setCostBz(row.getCell(6).toString());}
					list.add(costtype);
				}else {
					System.out.println("不可重复添加");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		}else if(file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.')+1).toLowerCase().equals("xls")){
			HSSFWorkbook workbook = null;
			try {
				workbook = new HSSFWorkbook(file.getInputStream());
				HSSFSheet sheet=workbook.getSheetAt(0);//拿到第几张表
				for (int numSheet = 1; numSheet <=sheet.getLastRowNum(); numSheet++) {
					Row row = sheet.getRow(numSheet);
					if (row != null) {
						Costtype costtype = new Costtype();
						if(row.getCell(0)==null){costtype.setCostName("");}else{costtype.setCostName(row.getCell(0).toString());}
						if(row.getCell(1)==null){costtype.setCostType("");}else{costtype.setCostType(row.getCell(1).toString());}
						if(row.getCell(2)==null){ costtype.setCostAllname("");}else{costtype.setCostAllname(row.getCell(2).toString());}
						if(row.getCell(3)==null){ costtype.setCostCode("");}else{costtype.setCostCode(row.getCell(3).toString());}
						if(row.getCell(4)==null){ costtype.setCostEnname("");}else{costtype.setCostEnname(row.getCell(4).toString());}
						if(row.getCell(5)==null){ costtype.setCostDefault("");}else{costtype.setCostDefault(row.getCell(5).toString());}
						if(row.getCell(6)==null){ costtype.setCostBz("");}else{costtype.setCostBz(row.getCell(6).toString());}
						list.add(costtype);
					}else {
						System.out.println("不可重复添加");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return R.status(costtypeService.saveBatch(list));
	}

	//下载模板
	@GetMapping("/download")
	@ApiOperation(value = "下载模板", notes = "导出storage")
	public void downloadmodules(HttpServletResponse response) throws IOException {

		BladeUser user =new BladeUser();
		System.out.println("当前租户id是："+user);

		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet("Sheet1");
		//产生表格标题行
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("费用简称");
		row.createCell(1).setCellValue("费用分类");
		row.createCell(2).setCellValue("费用名称");
		row.createCell(3).setCellValue("费用代码");
		row.createCell(4).setCellValue("英文名称");
		row.createCell(5).setCellValue("默认货币");
		row.createCell(6).setCellValue("备注信息");
		//浏览器下载excel
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment;filename=fee.xls");
		response.flushBuffer();
		OutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		outputStream.flush();
		outputStream.close();
	}
}
