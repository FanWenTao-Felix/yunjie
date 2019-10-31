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
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.dictionaries.entity.AirFreight;
import org.springblade.modules.dictionaries.entity.ShippingPort;
import org.springblade.modules.dictionaries.service.IAirFreightService;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.dictionaries.entity.AirFreightInfo;
import org.springblade.modules.dictionaries.vo.AirFreightInfoVO;
import org.springblade.modules.dictionaries.wrapper.AirFreightInfoWrapper;
import org.springblade.modules.dictionaries.service.IAirFreightInfoService;
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
 * @since 2019-09-05
 */
@RestController
@AllArgsConstructor
@RequestMapping("dictionaries/airfreightinfo")
@Api(value = "", tags = "接口")
public class AirFreightInfoController extends BladeController {

	private IAirFreightInfoService airFreightInfoService;
	private IAirFreightService airFreightService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入airFreightInfo")
	public R<AirFreightInfoVO> detail(AirFreightInfo airFreightInfo) {
		AirFreightInfo detail = airFreightInfoService.getOne(Condition.getQueryWrapper(airFreightInfo));
		return R.data(AirFreightInfoWrapper.build().entityVO(detail));
	}

	/**
	* 分页
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入airFreightInfo")
	public R<IPage<AirFreightInfoVO>> list(AirFreightInfo airFreightInfo, Query query) {
		IPage<AirFreightInfo> pages = airFreightInfoService.page(Condition.getPage(query), Condition.getQueryWrapper(airFreightInfo));
		return R.data(AirFreightInfoWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入airFreightInfo")
	public R<IPage<AirFreightInfoVO>> page(AirFreightInfoVO airFreightInfo, Query query) {
		IPage<AirFreightInfoVO> pages = airFreightInfoService.selectAirFreightInfoPage(Condition.getPage(query), airFreightInfo);
		return R.data(pages);
	}

	/**
	* 新增
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入airFreightInfo")
	public R save(@Valid @RequestBody AirFreightInfo airFreightInfo) {
		return R.status(airFreightInfoService.save(airFreightInfo));
	}

	/**
	* 修改
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入airFreightInfo")
	public R update(@Valid @RequestBody AirFreightInfo airFreightInfo) {
		return R.status(airFreightInfoService.updateById(airFreightInfo));
	}

	/**
	* 新增或修改
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入airFreightInfo")
	public R submit(@Valid @RequestBody AirFreightInfo airFreightInfo) {
		return R.status(airFreightInfoService.saveOrUpdate(airFreightInfo));
	}


	/**
	* 删除
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		List<Long> mids= airFreightService.selectInfoId(ids);
		System.out.println("11111111111111111111"+mids.size());
		if(mids.size()>=1){
			airFreightService.removeByIds(mids);
		}
		return R.status(airFreightInfoService.removeByIds(Func.toLongList(ids)));
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
		row.createCell(0).setCellValue("航线编号");
		row.createCell(1).setCellValue("航线简称");
		row.createCell(2).setCellValue("起运港");
		row.createCell(3).setCellValue("航班号");
		row.createCell(4).setCellValue("机型");
		row.createCell(5).setCellValue("周期");
		row.createCell(6).setCellValue("载量");
		row.createCell(7).setCellValue("币种");
		row.createCell(8).setCellValue("杂费备注");
		row.createCell(9).setCellValue("备注");
		row.createCell(10).setCellValue("优势");
		row.createCell(11).setCellValue("状态");
		//row.createCell(8).setCellValue("创建时间");
		//row.createCell(9).setCellValue("更新时间");
		HSSFRow row1 = sheet.createRow(1);
		row1.createCell(0).setCellValue("071");
		row1.createCell(1).setCellValue("ET");
		row1.createCell(2).setCellValue("CAN");
		row1.createCell(3).setCellValue("ET3067");
		row1.createCell(4).setCellValue("B777");
		row1.createCell(5).setCellValue("1，3，5，7");
		row1.createCell(6).setCellValue("10吨");
		row1.createCell(7).setCellValue("CNY");
		row1.createCell(8).setCellValue("CAN-KUL-DES");
		row1.createCell(9).setCellValue("毛重+100kg气泡货分半");
		row1.createCell(10).setCellValue("LHR/AMS/BKK");
		row1.createCell(11).setCellValue("正常");
		//row1.createCell(8).setCellValue("2019-09-24 12:00:00");
		//row1.createCell(9).setCellValue("2019-09-24 12:00:00");
		//浏览器下载excel
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment;filename=Airline_Data.xls");
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
		List<AirFreightInfo> infoLists = new ArrayList<AirFreightInfo>();
		List<AirFreight> lists = new ArrayList<AirFreight>();
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
		if(name[0].equals("Airline")||	name[0].equals("airline")){
			try {
				Workbook workbook = WorkbookFactory.create(file.getInputStream());
				Sheet sheet = workbook.getSheetAt(0);
				//System.out.println("数据长度"+sheet.getLastRowNum());
				if(sheet.getLastRowNum()<1){
					b=false;
				}else{
					Row firstRow = sheet.getRow(0);
					System.out.println(firstRow);
					b = b && firstRow.getCell(0).getStringCellValue().equals("航线编号") ? true : false;
					b = b && firstRow.getCell(1).getStringCellValue().equals("航线简称") ? true : false;
					b = b && firstRow.getCell(2).getStringCellValue().equals("起运港") ? true : false;
					b = b && firstRow.getCell(3).getStringCellValue().equals("航班号") ? true : false;
					b = b && firstRow.getCell(4).getStringCellValue().equals("机型") ? true : false;
					b = b && firstRow.getCell(5).getStringCellValue().equals("周期") ? true : false;
					b = b && firstRow.getCell(6).getStringCellValue().equals("载重") ? true : false;
					b = b && firstRow.getCell(7).getStringCellValue().equals("币种") ? true : false;
					b = b && firstRow.getCell(8).getStringCellValue().equals("杂费备注") ? true : false;
					b = b && firstRow.getCell(9).getStringCellValue().equals("备注") ? true : false;
					b = b && firstRow.getCell(10).getStringCellValue().equals("优势") ? true : false;
					b = b && firstRow.getCell(11).getStringCellValue().equals("状态") ? true : false;
				for (int numSheet = 1; numSheet <= sheet.getLastRowNum(); numSheet++) {
					Row row = sheet.getRow(numSheet);
					if (row != null) {
						AirFreightInfo sPort = new AirFreightInfo();
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
						Cell cell12=row.getCell(11);
						cell1.setCellType(CellType.STRING);
						sPort.setNumber(cell1.getStringCellValue());
						cell2.setCellType(CellType.STRING);
						sPort.setAirLine(cell2.getStringCellValue());
						cell3.setCellType(CellType.STRING);
						sPort.setOriginPort(cell3.getStringCellValue());
						if(cell4!=null&&!"".equals(cell4)) {
							cell4.setCellType(CellType.STRING);
							sPort.setFlightNumber(cell4.getStringCellValue());
						}
						if(cell5!=null&&!"".equals(cell5)){
							cell5.setCellType(CellType.STRING);
							sPort.setType(cell5.getStringCellValue());
						}
						if(cell6!=null&&!"".equals(cell6)){
							cell6.setCellType(CellType.STRING);
							sPort.setPeriod(cell6.getStringCellValue());
						}
						if(cell7!=null&&!"".equals(cell7)){
							cell7.setCellType(CellType.STRING);
							sPort.setCapacity(cell7.getStringCellValue());
						}
						if(cell8!=null&&!"".equals(cell8)) {
							cell8.setCellType(CellType.STRING);
							sPort.setCurrency(cell8.getStringCellValue());
						}
						if(cell9!=null&&!"".equals(cell9)){
							cell9.setCellType(CellType.STRING);
							sPort.setSundryGoods(cell9.getStringCellValue());
						}
						if(cell10!=null&&!"".equals(cell10)){
							cell10.setCellType(CellType.STRING);
							sPort.setRemark(cell10.getStringCellValue());
						}
						if(cell11!=null&&!"".equals(cell11)){
							cell11.setCellType(CellType.STRING);
							sPort.setAdvantage(cell11.getStringCellValue());
						}
						if(cell12!=null&&!"".equals(cell12)){
							cell11.setCellType(CellType.STRING);
							sPort.setState(cell12.getStringCellValue());
						}
						sPort.setCreationTime(new Date());
						sPort.setTurnoverTime(new Date());
						AirFreightInfo af = airFreightInfoService.getInfo(sPort);
						if (af == null) {
							infoLists.add(sPort);
						}
					}
				}
				b=airFreightInfoService.saveBatch(infoLists);
			}
		} catch (Exception e) {
			e.printStackTrace();
			b=false;
		}
		}else {
			System.out.println("33333");
			//航空编号
			String number=name[0];
			//航线简称
			String airLine=name[1];
          //起运港
			String originPort=name[2];
			AirFreightInfo airFreightInfo=new AirFreightInfo();
			airFreightInfo.setOriginPort(originPort);
			airFreightInfo.setNumber(number);
			airFreightInfo.setAirLine(airLine);
			System.out.println(airFreightInfo);
			AirFreightInfo airFreightInfo1 =airFreightInfoService.selectAirFreightInfo(airFreightInfo);
			System.out.println(airFreightInfo1);
		    Long freigthInfoId=airFreightInfo1.getId();
			try {
				Workbook workbook = WorkbookFactory.create(file.getInputStream());
				Sheet sheet = workbook.getSheetAt(0);
				//System.out.println("数据长度"+sheet.getLastRowNum());
				if(sheet.getLastRowNum()<1){
					b=false;
				}else{
					Row firstRow = sheet.getRow(0);
					//System.out.println(firstRow);
					b = b && firstRow.getCell(0).getStringCellValue().equals("地区") ? true : false;
					b = b && firstRow.getCell(1).getStringCellValue().equals("起运港口") ? true : false;
					b = b && firstRow.getCell(2).getStringCellValue().equals("到达港口") ? true : false;
					b = b && firstRow.getCell(3).getStringCellValue().equals("最大值") ? true : false;
					b = b && firstRow.getCell(4).getStringCellValue().equals("最小值") ? true : false;
					b = b && firstRow.getCell(5).getStringCellValue().equals("+45KGS") ? true : false;
					b = b && firstRow.getCell(6).getStringCellValue().equals("+100KGS") ? true : false;
					b = b && firstRow.getCell(7).getStringCellValue().equals("+300KGS") ? true : false;
					b = b && firstRow.getCell(8).getStringCellValue().equals("+500KGS") ? true : false;
					b = b && firstRow.getCell(9).getStringCellValue().equals("+1000KGS") ? true : false;
					b = b && firstRow.getCell(10).getStringCellValue().equals("ROUTING") ? true : false;
					b = b && firstRow.getCell(11).getStringCellValue().equals("2nd") ? true : false;
					b = b && firstRow.getCell(12).getStringCellValue().equals("3th") ? true : false;
					b = b && firstRow.getCell(13).getStringCellValue().equals("时效") ? true : false;
					b = b && firstRow.getCell(14).getStringCellValue().equals("备注") ? true : false;
					for (int numSheet = 1; numSheet <= sheet.getLastRowNum(); numSheet++) {
						Row row = sheet.getRow(numSheet);
						if (row != null) {
							AirFreight airFreight = new AirFreight();
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
							Cell cell12=row.getCell(11);
							Cell cell13=row.getCell(12);
							Cell cell14=row.getCell(13);
							Cell cell15=row.getCell(14);
							if(cell1!=null&&!"".equals(cell1)) {
								cell1.setCellType(CellType.STRING);
								airFreight.setRegion(cell1.getStringCellValue());
							}
							cell2.setCellType(CellType.STRING);
							airFreight.setOriginPort(cell2.getStringCellValue());
							if(cell3!=null&&!"".equals(cell3)){
								cell3.setCellType(CellType.STRING);
								airFreight.setDestinationPort(cell3.getStringCellValue());
							}
							if(cell4!=null){
								cell4.setCellType(CellType.STRING);
								airFreight.setLeastValue(Double.parseDouble(cell4.getStringCellValue()));
							}else{
								airFreight.setLeastValue((double) 0);
							}

							if(cell5!=null){
								cell5.setCellType(CellType.STRING);
								airFreight.setNormalValue(Double.parseDouble(cell5.getStringCellValue()));
							}else{
								airFreight.setNormalValue((double) 0);
							}
							if(cell6!=null){
								cell6.setCellType(CellType.STRING);
								airFreight.setFortyFive(Double.parseDouble(cell6.getStringCellValue()));
							}else{
								airFreight.setFortyFive((double) 0);
							}
							if(cell7!=null){
								cell7.setCellType(CellType.STRING);
								airFreight.setOneHundred(Double.parseDouble(cell7.getStringCellValue()));
							}else{
								airFreight.setOneHundred((double) 0);
							}
							if(cell8!=null){
								cell8.setCellType(CellType.STRING);
								airFreight.setThreeHundred(Double.parseDouble(cell8.getStringCellValue()));
							}else{
								airFreight.setThreeHundred((double) 0);
							}
							if(cell9!=null){
							cell9.setCellType(CellType.STRING);
								airFreight.setFiveHundred(Double.parseDouble(cell9.getStringCellValue()));
							}else{
								airFreight.setFiveHundred((double) 0);
							}
							if(cell10!=null){
								cell10.setCellType(CellType.STRING);
								airFreight.setOneThousand(Double.parseDouble(cell10.getStringCellValue()));
							}else{
								airFreight.setOneThousand((double) 0);
							}
							if(cell11!=null&&!"".equals(cell11)){
								cell11.setCellType(CellType.STRING);
								airFreight.setRouting(cell11.getStringCellValue());
							}
							if(cell12!=null&&!"".equals(cell12)){
								cell12.setCellType(CellType.STRING);
								airFreight.setTwoNd(cell12.getStringCellValue());
							}
							if(cell13!=null&&!"".equals(cell13)){
								cell13.setCellType(CellType.STRING);
								airFreight.setThreeTh(cell13.getStringCellValue());
							}
							if(cell14!=null&&!"".equals(cell14)){
								cell14.setCellType(CellType.STRING);
								airFreight.setAging(cell14.getStringCellValue());
							}
							if(cell15!=null&&!"".equals(cell15)){
								cell15.setCellType(CellType.STRING);
								airFreight.setRemark(cell15.getStringCellValue());
							}
							airFreight.setFreigthInfoId(freigthInfoId.toString());
							AirFreight af = airFreightService.getAirFreight(airFreight);
							if (af == null) {
								lists.add(airFreight);
							}
						}
					}
					b=airFreightService.saveBatch(lists);
				}
			} catch (Exception e) {
				e.printStackTrace();
				b=false;
			}
		}
		return R.status(b);
	}






}
