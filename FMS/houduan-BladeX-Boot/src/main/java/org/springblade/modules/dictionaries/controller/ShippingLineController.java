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
import org.apache.poi.ss.usermodel.*;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;

import org.springblade.modules.dictionaries.entity.ShippingParticular;
import org.springblade.modules.dictionaries.service.IShippingParticularService;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.dictionaries.entity.ShippingLine;
import org.springblade.modules.dictionaries.vo.ShippingLineVO;
import org.springblade.modules.dictionaries.wrapper.ShippingLineWrapper;
import org.springblade.modules.dictionaries.service.IShippingLineService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  控制器
 *
 * @author BladeX
 * @since 2019-09-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("dictionaries/shippingline")
@Api(value = "", tags = "接口")
public class ShippingLineController extends BladeController {

	private IShippingLineService shippingLineService;
	private IShippingParticularService shippingParticularService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入shippingLine")
	public R<ShippingLineVO> detail(ShippingLine shippingLine) {
		ShippingLine detail = shippingLineService.getOne(Condition.getQueryWrapper(shippingLine));
		return R.data(ShippingLineWrapper.build().entityVO(detail));
	}

	/**
	* 分页
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入shippingLine")
	public R<IPage<ShippingLineVO>> list(ShippingLine shippingLine, Query query) {
		IPage<ShippingLine> pages = shippingLineService.page(Condition.getPage(query), Condition.getQueryWrapper(shippingLine));
		return R.data(ShippingLineWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入shippingLine")
	public R<IPage<ShippingLineVO>> page(ShippingLineVO shippingLine, Query query) {
		IPage<ShippingLineVO> pages = shippingLineService.selectShippingLinePage(Condition.getPage(query), shippingLine);
		return R.data(pages);
	}

	/**
	* 新增
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入shippingLine")
	public R save(@Valid @RequestBody ShippingLine shippingLine) {
		return R.status(shippingLineService.save(shippingLine));
	}

	/**
	* 修改
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入shippingLine")
	public R update(@Valid @RequestBody ShippingLine shippingLine) {
		return R.status(shippingLineService.updateById(shippingLine));
	}

	/**
	* 新增或修改
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入shippingLine")
	public R submit(@Valid @RequestBody ShippingLine shippingLine) {
		return R.status(shippingLineService.saveOrUpdate(shippingLine));
	}


	/**
	* 删除
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		List<Long> mids= shippingParticularService.selectInfoId(ids);
		if(mids.size()>=1){
			shippingParticularService.removeByIds(mids);
		}
		return R.status(shippingLineService.removeByIds(Func.toLongList(ids)));
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
		row.createCell(0).setCellValue("船司代码");
		row.createCell(1).setCellValue("船司中文名");
		row.createCell(2).setCellValue("起运港");
		row.createCell(3).setCellValue("币种");
		row.createCell(4).setCellValue("杂费备注");
		row.createCell(5).setCellValue("备注");
		row.createCell(6).setCellValue("优势");
		row.createCell(7).setCellValue("状态");
		HSSFRow row1 = sheet.createRow(1);
		row1.createCell(0).setCellValue("CMA");
		row1.createCell(1).setCellValue("达飞");
		row1.createCell(2).setCellValue("CAN");
		row1.createCell(3).setCellValue("CAN");
		row1.createCell(4).setCellValue("");
		row1.createCell(5).setCellValue("");
		row1.createCell(6).setCellValue("LHR/AMS/BKK");
		row1.createCell(7).setCellValue("正常");
		//row1.createCell(8).setCellValue("2019-09-24 12:00:00");
		//row1.createCell(9).setCellValue("2019-09-24 12:00:00");
		//浏览器下载excel
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment;filename=Shipping_CMA.xls");
		response.flushBuffer();
		OutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		outputStream.flush();
		outputStream.close();
	}




	/**
	 * 导入
	 */
	@PostMapping("/toLead")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "导入", notes = "传入file文件")
	public R toLead(@RequestParam(value = "file") MultipartFile file) throws IOException {
		List<ShippingLine> infoLists = new ArrayList<ShippingLine>();
		List<ShippingParticular> lists = new ArrayList<ShippingParticular>();
		String fileName=file.getOriginalFilename();
		//截取文件名进行截取
		String fileTyle=fileName.substring(0,fileName.indexOf("."));
		if(fileTyle.indexOf(" ")!=-1){
			fileTyle=fileTyle.substring(0,fileName.indexOf(" "));
		}
		if(fileTyle.indexOf("-")!=-1){
			fileTyle=fileTyle.substring(0,fileName.indexOf("-"));
		}
		//根据_进行切割
		String[] name=fileTyle.split("_");
		Boolean b = false;
		//如果是航线
		if(name[0].equals("Shipping")||	name[0].equals("shipping")){
			try {
				Workbook workbook = WorkbookFactory.create(file.getInputStream());
				Sheet sheet = workbook.getSheetAt(0);
				//System.out.println("数据长度"+sheet.getLastRowNum());
				if(sheet.getLastRowNum()<1){
					b=false;
				}else{
					Row firstRow = sheet.getRow(0);
					System.out.println(firstRow);
					b = b && firstRow.getCell(0).getStringCellValue().equals("船司代码") ? true : false;
					b = b && firstRow.getCell(1).getStringCellValue().equals("船司中文名") ? true : false;
					b = b && firstRow.getCell(2).getStringCellValue().equals("起运港") ? true : false;
					b = b && firstRow.getCell(3).getStringCellValue().equals("币种") ? true : false;
					b = b && firstRow.getCell(4).getStringCellValue().equals("杂费备注") ? true : false;
					b = b && firstRow.getCell(5).getStringCellValue().equals("备注") ? true : false;
					b = b && firstRow.getCell(6).getStringCellValue().equals("优势") ? true : false;
					b = b && firstRow.getCell(7).getStringCellValue().equals("状态") ? true : false;
					for (int numSheet = 1; numSheet <= sheet.getLastRowNum(); numSheet++) {
						Row row = sheet.getRow(numSheet);
						if (row != null) {
							ShippingLine sLine = new ShippingLine();
							Cell cell1=row.getCell(0);
							Cell cell2=row.getCell(1);
							Cell cell3=row.getCell(2);
							Cell cell4=row.getCell(3);
							Cell cell5=row.getCell(4);
							Cell cell6=row.getCell(5);
							Cell cell7=row.getCell(6);
							Cell cell8=row.getCell(7);
							cell1.setCellType(CellType.STRING);
							cell2.setCellType(CellType.STRING);
							cell3.setCellType(CellType.STRING);
							sLine.setCompanyCode(cell1.getStringCellValue());
							sLine.setCompanyChinese(cell2.getStringCellValue());
							sLine.setOriginPort(cell3.getStringCellValue());
							if(cell4!=null&&!"".equals(cell4)) {
								cell4.setCellType(CellType.STRING);
								sLine.setCurrency(cell4.getStringCellValue());
							}
							if(cell5!=null&&!"".equals(cell5)){
								cell5.setCellType(CellType.STRING);
								sLine.setSundryGoods(cell5.getStringCellValue());
							}
							if(cell6!=null&&!"".equals(cell6)){
								cell6.setCellType(CellType.STRING);
								sLine.setRemark(cell6.getStringCellValue());
							}
							if(cell7!=null&&!"".equals(cell7)){
								cell7.setCellType(CellType.STRING);
								sLine.setAdvantage(cell7.getStringCellValue());
							}
							if(cell8!=null&&!"".equals(cell8)){
								cell8.setCellType(CellType.STRING);
								sLine.setState(cell8.getStringCellValue());
							}
							sLine.setCreationTime(new Date());
							sLine.setTurnoverTime(new Date());
							ShippingLine sl = shippingLineService.selectShippingLine(sLine);
							if (sl == null) {
								infoLists.add(sLine);
							}
						}
					}
					b=shippingLineService.saveBatch(infoLists);
				}
			} catch (Exception e) {
				e.printStackTrace();
				b=false;
			}
		}else {
			String companyCode=name[0];
			String companyChinese=name[1];
			//起运港
			String originPort=name[2];
			ShippingLine shippingLine=new ShippingLine();
			shippingLine.setOriginPort(originPort);
			shippingLine.setCompanyCode(companyCode);
			shippingLine.setCompanyChinese(companyChinese);
			//System.out.println("22222222222222222"+shippingLine);
			ShippingLine shippingLine1 =shippingLineService.getOne(Condition.getQueryWrapper(shippingLine));
			System.out.println(shippingLine1);
			Long lineId=shippingLine1.getId();
			try {
				Workbook workbook = WorkbookFactory.create(file.getInputStream());
				Sheet sheet = workbook.getSheetAt(0);
				//System.out.println("数据长度"+sheet.getLastRowNum());
				if(sheet.getLastRowNum()<1){
					b=false;
				}else{
					Row firstRow = sheet.getRow(0);
					//System.out.println(firstRow);
					b = b && firstRow.getCell(0).getStringCellValue().equals("起港口") ? true : false;
					b = b && firstRow.getCell(1).getStringCellValue().equals("目的港") ? true : false;
					b = b && firstRow.getCell(2).getStringCellValue().equals("截关/开船") ? true : false;
					b = b && firstRow.getCell(3).getStringCellValue().equals("船程") ? true : false;
					b = b && firstRow.getCell(4).getStringCellValue().equals("中转") ? true : false;
					b = b && firstRow.getCell(5).getStringCellValue().equals("GP20") ? true : false;
					b = b && firstRow.getCell(6).getStringCellValue().equals("GP40") ? true : false;
					b = b && firstRow.getCell(7).getStringCellValue().equals("HC40") ? true : false;
					b = b && firstRow.getCell(8).getStringCellValue().equals("HC45") ? true : false;
					b = b && firstRow.getCell(9).getStringCellValue().equals("运价备注") ? true : false;
					b = b && firstRow.getCell(10).getStringCellValue().equals("有效期") ? true : false;
					for (int numSheet = 1; numSheet <= sheet.getLastRowNum(); numSheet++) {
						Row row = sheet.getRow(numSheet);
						if (row != null) {
							ShippingParticular shippingParticular = new ShippingParticular();
							Cell cell1=row.getCell(0);
							Cell cell2=row.getCell(1);
							Cell cell3=row.getCell(2);
							Cell cell4=row.getCell(3);
							Cell cell5=row.getCell(4);
							Cell cell6=row.getCell(5);
							Cell cell7=row.getCell(6);
							Cell cell8=row.getCell(7);
							Cell cell9=row.getCell(8);
							Cell cell10=row.getCell(9);
							Cell cell11=row.getCell(10);
							cell1.setCellType(CellType.STRING);
							shippingParticular.setOriginPort(cell1.getStringCellValue());
							cell2.setCellType(CellType.STRING);
							shippingParticular.setDestinationPort(cell2.getStringCellValue());
							if(cell3!=null){
								cell3.setCellType(CellType.STRING);
								shippingParticular.setStopOpen(cell3.getStringCellValue());
						}
							if(cell4!=null){
								cell4.setCellType(CellType.STRING);
								shippingParticular.setBoatRide(cell4.getStringCellValue());
							}
							if(cell5!=null){
								cell5.setCellType(CellType.STRING);
								shippingParticular.setTransfer(cell5.getStringCellValue());
							}
							if(cell6!=null){
								cell6.setCellType(CellType.STRING);
								shippingParticular.setGpTwenty(Double.parseDouble(cell6.getStringCellValue()));
							}else{
								shippingParticular.setGpTwenty((double) 0);
							}
							if(cell7!=null){
								cell7.setCellType(CellType.STRING);
								shippingParticular.setGpForty(Double.parseDouble(cell7.getStringCellValue()));
							}else{
								shippingParticular.setGpForty((double) 0);
							}
							if(cell8!=null){
								cell8.setCellType(CellType.STRING);
								shippingParticular.setHcForty(Double.parseDouble(cell8.getStringCellValue()));
							}else{
								shippingParticular.setHcForty((double) 0);
							}
							if(cell9 == null){
								cell9.setCellType(CellType.STRING);
								shippingParticular.setHcFortyFive(Double.parseDouble(cell9.getStringCellValue()));
							}else{
								shippingParticular.setHcFortyFive((double) 0);
							}
							if(cell10!=null&&!"".equals(cell10)){
								System.out.println(cell10);
								cell10.setCellType(CellType.STRING);
								shippingParticular.setShippingNote(cell10.getStringCellValue());
							}
							if(cell11!=null&&!"".equals(cell11)){
								cell11.setCellType(CellType.STRING);
								shippingParticular.setPeriodValidity(cell11.getStringCellValue());
							}
							shippingParticular.setLineId(lineId.toString());
							ShippingParticular sp = shippingParticularService.selectShippingParticular(shippingParticular);
							//System.out.println(sp);
							if (sp == null) {
								lists.add(shippingParticular);
							}
						}
					}
					b=shippingParticularService.saveBatch(lists);
				}
			} catch (Exception e) {
				e.printStackTrace();
				b=false;
			}
		}
		return R.status(b);
	}






}
