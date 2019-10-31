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
package org.springblade.modules.businessManagement.controller;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.basicData.BasicDataController;
import org.springblade.modules.businessManagement.entity.AirBusiness;
import org.springblade.modules.businessManagement.mapper.AirBusinessMapper;
import org.springblade.modules.businessManagement.service.IAirBusinessService;
import org.springblade.modules.businessManagement.vo.AirBusinessVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.businessManagement.entity.Salesmanagement;
import org.springblade.modules.businessManagement.vo.SalesmanagementVO;
import org.springblade.modules.businessManagement.wrapper.SalesmanagementWrapper;
import org.springblade.modules.businessManagement.service.ISalesmanagementService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.modules.clientManagement.entity.ClientData;
import org.springblade.modules.clientManagement.service.impl.ClientDataServiceImpl;
import org.springblade.modules.ocean.entity.SeaBusiness;
import org.springblade.modules.ocean.service.ISeaBusinessService;
import org.springblade.modules.ocean.vo.SeaBusinessVO;
import org.springblade.modules.system.entity.User;
import org.springblade.modules.system.service.IUserService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  控制器
 *
 * @author BladeX
 * @since 2019-09-04
 */
@RestController
@AllArgsConstructor
@RequestMapping("business/salesmanagement")
@Api(value = "", tags = "接口")
public class SalesmanagementController extends BladeController {

private ISalesmanagementService salesmanagementService;
private IAirBusinessService airBusinessService;
private ISeaBusinessService seaBusinessService;
private ClientDataServiceImpl clientDataService;
private IUserService userService;
private AirBusinessMapper airBusinessMapper;

/**
 * 详情
 */
@GetMapping("/detail")
@ApiOperationSupport(order = 1)
@ApiOperation(value = "详情", notes = "传入salesmanagement")
public R<SalesmanagementVO> detail(Salesmanagement salesmanagement) {
		Salesmanagement detail = salesmanagementService.getOne(Condition.getQueryWrapper(salesmanagement));
		return R.data(SalesmanagementWrapper.build().entityVO(detail));
}

/**
 * 分页
 */
@GetMapping("/list")
@ApiOperationSupport(order = 2)
@ApiOperation(value = "分页", notes = "传入salesmanagement")
public R<IPage<SalesmanagementVO> > list(Salesmanagement salesmanagement, Query query) {
		IPage<Salesmanagement> pages = salesmanagementService.page(Condition.getPage(query), Condition.getQueryWrapper(salesmanagement));
		return R.data(SalesmanagementWrapper.build().pageVO(pages));
}

/**
 * 自定义分页
 */
@GetMapping("/page")
@ApiOperationSupport(order = 3)
@ApiOperation(value = "分页", notes = "传入salesmanagement")
public R<IPage<SalesmanagementVO> > page(SalesmanagementVO salesmanagement, Query query) {
		IPage<SalesmanagementVO> pages = salesmanagementService.selectSalesmanagementPage(Condition.getPage(query), salesmanagement);
		return R.data(pages);
}

/**
 * 新增
 */
@PostMapping("/save")
@ApiOperationSupport(order = 4)
@ApiOperation(value = "新增", notes = "传入salesmanagement")
public R save(@Valid @RequestBody Salesmanagement salesmanagement) {
		return R.status(salesmanagementService.save(salesmanagement));
}

/**
 * 修改
 */
@PostMapping("/update")
@ApiOperationSupport(order = 5)
@ApiOperation(value = "修改", notes = "传入salesmanagement")
public R update(@Valid @RequestBody Salesmanagement salesmanagement) {
		return R.status(salesmanagementService.updateById(salesmanagement));
}

/**
 * 新增或修改
 */
@PostMapping("/submit")
@ApiOperationSupport(order = 6)
@ApiOperation(value = "新增或修改", notes = "传入salesmanagement")
public R submit(@Valid @RequestBody Salesmanagement salesmanagement) {
		return R.status(salesmanagementService.saveOrUpdate(salesmanagement));
}


/**
 * 删除
 */
@PostMapping("/remove")
@ApiOperationSupport(order = 8)
@ApiOperation(value = "删除", notes = "传入ids")
public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(salesmanagementService.removeByIds(Func.toLongList(ids)));
}

@GetMapping("/airdetail")
@ApiOperationSupport(order = 9)
@ApiOperation(value = "详情")
public void airdetail(){
	BladeUser user = getUser();
	List<String> list1=new ArrayList<>();
	List<String> list2=new ArrayList<>();

	//判断当前登录租户是否是admin
	//是admin，就去掉querywapper中的tanent_id条件
	//不是admin，在querywapper中加上当前tanent_id条件
	//QueryWrapper<AirBusiness> airquery=new QueryWrapper<>();
	//airquery.eq("tenant_id",user.getTenantId());

	System.out.println("当前租户id是："+user.getTenantId());
	if(!user.getTenantId().equals("000000")){
		//当前租户不是admin
		List<AirBusiness> detail = airBusinessService.list();//拿到空运表单号存入集合
		for(int i=0; i<detail.size(); i++){list1.add(detail.get(i).getInternalOrderNo());}
		List<Salesmanagement> listsale=salesmanagementService.list();//拿到销售表单号存入集合
		for(int i=0; i<listsale.size(); i++){list2.add(listsale.get(i).getInternalOrderNo());}
		list1.removeAll(list2);//从空运集合中去除销售集合中已有的
		if(list1!=null) {
			for (int i=0; i<list1.size(); i++) {
				QueryWrapper<AirBusiness> queryWrapper=new QueryWrapper<>();
				queryWrapper.lambda().eq(AirBusiness::getInternalOrderNo,list1.get(i));
				List<AirBusiness> lsa=airBusinessService.list(queryWrapper);
				for (int j=0; j<lsa.size(); j++) {
					Salesmanagement salesmanagement=new Salesmanagement();
					salesmanagement.setTenantId(user.getTenantId());
					salesmanagement.setCreateUser(lsa.get(j).getCreateUser());

					Date date = lsa.get(j).getCreateTime();
					Instant instant = date.toInstant();
					ZoneId zoneId = ZoneId.systemDefault();
					LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
					salesmanagement.setCreateTime(localDateTime);

					salesmanagement.setUpdateUser(lsa.get(j).getUpdateUser());

					Date date1 = lsa.get(j).getUpdateTime();
					Instant instant1 = date1.toInstant();
					ZoneId zoneId1 = ZoneId.systemDefault();
					LocalDateTime localDateTime1 = instant1.atZone(zoneId1).toLocalDateTime();
					salesmanagement.setUpdateTime(localDateTime1);

					salesmanagement.setCreateDept(lsa.get(j).getCreateDept());
					//salesmanagement.setOperationStatus(lsa.get(j).getOperationStatus());
					//salesmanagement.setConsignerId(lsa.get(j).getConsignerId());
					//salesmanagement.setConsigneeId(lsa.get(j).getConsigneeId());
					salesmanagement.setConsigner(lsa.get(j).getConsigner());
					salesmanagement.setConsignerAddress(lsa.get(j).getConsignerAddress());
					salesmanagement.setConsignee(lsa.get(j).getConsignee());
					salesmanagement.setConsigneeAddress(lsa.get(j).getConsigneeAddress());
					salesmanagement.setNotifier(lsa.get(j).getNotifier());
					salesmanagement.setGoodsEnglishName(lsa.get(j).getGoodsEnglishName());
					salesmanagement.setGoodsDescription(lsa.get(j).getGoodsDescription());
					salesmanagement.setGoodsPackage(lsa.get(j).getGoodsPackage());
					salesmanagement.setCargoRate(lsa.get(j).getCargoRate());
					salesmanagement.setCarrierId(lsa.get(j).getCarrierId());
					//salesmanagement.setBusinessType(lsa.get(j).getBusinessType());
					//salesmanagement.setBookingStatus(lsa.get(j).getBookingStatus());
					salesmanagement.setBookingDate(lsa.get(j).getBookingDate());
					salesmanagement.setAirLine(lsa.get(j).getAirLine());
					salesmanagement.setFlight(lsa.get(j).getFlight());
					//salesmanagement.setClosingTime(lsa.get(j).getClosingTime());
					//salesmanagement.setCargoTerminalTime(lsa.get(j).getCargoTerminalTime());
					salesmanagement.setFbDestinationPort(lsa.get(j).getFbDestinationPort());
					salesmanagement.setFbLaunchTime(lsa.get(j).getFbLaunchTime());
					salesmanagement.setFbArrivalTime(lsa.get(j).getFbArrivalTime());
					//salesmanagement.setFbStatus(lsa.get(j).getFbStatus());
					salesmanagement.setSbDestinationPort(lsa.get(j).getSbDestinationPort());
					//salesmanagement.setSbStatus(lsa.get(j).getSbStatus());
					//salesmanagement.setWarehousingFee(lsa.get(j).getWarehousingFee());
					//salesmanagement.setWarehousingFeeCurrency(lsa.get(j).getWarehousingFeeCurrency());
					//salesmanagement.setPackingFee(lsa.get(j).getPackingFee());
					//salesmanagement.setPackingFeeCurrency(lsa.get(j).getPackingFeeCurrency());
					//salesmanagement.setFlightFee(lsa.get(j).getFlightFee());
					//salesmanagement.setFlightFeeCurrency(lsa.get(j).getFlightFeeCurrency());
					salesmanagement.setSalesman(lsa.get(j).getSalesman());
					salesmanagement.setOperator(lsa.get(j).getOperator());
					salesmanagement.setGoodsAmount(lsa.get(j).getGoodsAmount());
					salesmanagement.setGoodsGrossWeight(lsa.get(j).getGoodsGrossWeight());
					salesmanagement.setGoodsVolumn(lsa.get(j).getGoodsVolumn());
					//salesmanagement.setTransportClause(lsa.get(j).getTransportClause());//条款
					//salesmanagement.setFbAgent(lsa.get(j).getFbAgent());//航空代理
					salesmanagement.setSubOrderNo(lsa.get(j).getSubOrderNo());
					salesmanagement.setInternalOrderNo(lsa.get(j).getInternalOrderNo());
					//salesmanagement.setTransportType(lsa.get(j).getBusinessType());
					salesmanagement.setGoodsChineseName(lsa.get(j).getGoodsChineseName());
					salesmanagement.setClient(lsa.get(j).getClient().toString());
					salesmanagement.setMainOrderNo(lsa.get(j).getMainOrderNo());
					salesmanagement.setLoadingPort(lsa.get(j).getLoadingPort());
					salesmanagement.setDestinationPort(lsa.get(j).getDestinationPort());
					salesmanagement.setSbLaunchTime(lsa.get(j).getSbLaunchTime());
					salesmanagement.setSbArrivalTime(lsa.get(j).getSbArrivalTime());
					salesmanagement.setTranType("空运");
					salesmanagement.setOrdertype("0");//设置空运单
					salesmanagementService.save(salesmanagement);
				}
			}
		}else {
			System.out.println("空运重复添加");
		}
	}else {
		//当前租户是admin
		List<AirBusiness> detail = airBusinessService.list();//拿到空运表单号存入集合
		for(int i=0; i<detail.size(); i++){list1.add(detail.get(i).getInternalOrderNo());}
		List<Salesmanagement> listsale=salesmanagementService.list();//拿到销售表单号存入集合
		for(int i=0; i<listsale.size(); i++){list2.add(listsale.get(i).getInternalOrderNo());}
		list1.removeAll(list2);//从空运集合中去除销售集合中已有的
		if(list1!=null) {
			for (int i=0; i<list1.size(); i++) {
				QueryWrapper<AirBusiness> queryWrapper=new QueryWrapper<>();
				queryWrapper.lambda().eq(AirBusiness::getInternalOrderNo,list1.get(i));
				List<AirBusiness> lsa=airBusinessService.list(queryWrapper);
				for (int j=0; j<lsa.size(); j++) {
					Salesmanagement salesmanagement=new Salesmanagement();
					salesmanagement.setTenantId(user.getTenantId());
					salesmanagement.setCreateUser(lsa.get(j).getCreateUser());

					Date date = lsa.get(j).getCreateTime();
					Instant instant = date.toInstant();
					ZoneId zoneId = ZoneId.systemDefault();
					LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
					salesmanagement.setCreateTime(localDateTime);

					salesmanagement.setUpdateUser(lsa.get(j).getUpdateUser());

					Date date1 = lsa.get(j).getUpdateTime();
					Instant instant1 = date1.toInstant();
					ZoneId zoneId1 = ZoneId.systemDefault();
					LocalDateTime localDateTime1 = instant1.atZone(zoneId1).toLocalDateTime();
					salesmanagement.setUpdateTime(localDateTime1);

					salesmanagement.setCreateDept(lsa.get(j).getCreateDept());
					//salesmanagement.setOperationStatus(lsa.get(j).getOperationStatus());
					//salesmanagement.setConsignerId(lsa.get(j).getConsignerId());
					//salesmanagement.setConsigneeId(lsa.get(j).getConsigneeId());
					salesmanagement.setConsigner(lsa.get(j).getConsigner());
					salesmanagement.setConsignerAddress(lsa.get(j).getConsignerAddress());
					salesmanagement.setConsignee(lsa.get(j).getConsignee());
					salesmanagement.setConsigneeAddress(lsa.get(j).getConsigneeAddress());
					salesmanagement.setNotifier(lsa.get(j).getNotifier());
					salesmanagement.setGoodsEnglishName(lsa.get(j).getGoodsEnglishName());
					salesmanagement.setGoodsDescription(lsa.get(j).getGoodsDescription());
					salesmanagement.setGoodsPackage(lsa.get(j).getGoodsPackage());
					salesmanagement.setCargoRate(lsa.get(j).getCargoRate());
					salesmanagement.setCarrierId(lsa.get(j).getCarrierId());
					//salesmanagement.setBusinessType(lsa.get(j).getBusinessType());
					//salesmanagement.setBookingStatus(lsa.get(j).getBookingStatus());
					salesmanagement.setBookingDate(lsa.get(j).getBookingDate());
					salesmanagement.setAirLine(lsa.get(j).getAirLine());
					salesmanagement.setFlight(lsa.get(j).getFlight());
					//salesmanagement.setClosingTime(lsa.get(j).getClosingTime());
					//salesmanagement.setCargoTerminalTime(lsa.get(j).getCargoTerminalTime());
					salesmanagement.setFbDestinationPort(lsa.get(j).getFbDestinationPort());
					salesmanagement.setFbLaunchTime(lsa.get(j).getFbLaunchTime());
					salesmanagement.setFbArrivalTime(lsa.get(j).getFbArrivalTime());
					//salesmanagement.setFbStatus(lsa.get(j).getFbStatus());
					salesmanagement.setSbDestinationPort(lsa.get(j).getSbDestinationPort());
					//salesmanagement.setSbStatus(lsa.get(j).getSbStatus());
					//salesmanagement.setWarehousingFee(lsa.get(j).getWarehousingFee());
					//salesmanagement.setWarehousingFeeCurrency(lsa.get(j).getWarehousingFeeCurrency());
					//salesmanagement.setPackingFee(lsa.get(j).getPackingFee());
					//salesmanagement.setPackingFeeCurrency(lsa.get(j).getPackingFeeCurrency());
					//salesmanagement.setFlightFee(lsa.get(j).getFlightFee());
					//salesmanagement.setFlightFeeCurrency(lsa.get(j).getFlightFeeCurrency());
					salesmanagement.setSalesman(lsa.get(j).getSalesman());
					salesmanagement.setOperator(lsa.get(j).getOperator());
					salesmanagement.setGoodsAmount(lsa.get(j).getGoodsAmount());
					salesmanagement.setGoodsGrossWeight(lsa.get(j).getGoodsGrossWeight());
					salesmanagement.setGoodsVolumn(lsa.get(j).getGoodsVolumn());
					//salesmanagement.setTransportClause(lsa.get(j).getTransportClause());//条款
					//salesmanagement.setFbAgent(lsa.get(j).getFbAgent());//航空代理
					salesmanagement.setSubOrderNo(lsa.get(j).getSubOrderNo());
					salesmanagement.setInternalOrderNo(lsa.get(j).getInternalOrderNo());
					//salesmanagement.setTransportType(lsa.get(j).getBusinessType());
					salesmanagement.setGoodsChineseName(lsa.get(j).getGoodsChineseName());
					salesmanagement.setClient(lsa.get(j).getClient().toString());
					salesmanagement.setMainOrderNo(lsa.get(j).getMainOrderNo());
					salesmanagement.setLoadingPort(lsa.get(j).getLoadingPort());
					salesmanagement.setDestinationPort(lsa.get(j).getDestinationPort());
					salesmanagement.setSbLaunchTime(lsa.get(j).getSbLaunchTime());
					salesmanagement.setSbArrivalTime(lsa.get(j).getSbArrivalTime());
					salesmanagement.setTranType("空运");
					salesmanagement.setOrdertype("0");//设置空运单
					salesmanagementService.save(salesmanagement);
				}
			}
	}else {
			System.out.println("空运重复添加");
	}
	}
}


/**
 *
 * 获取传的值
 *
 * */
@GetMapping("/findbyselect")
@ApiOperationSupport(order = 10)
@ApiOperation(value = "详情", notes = "传入type,begintime,endtime")
public void findbyselect(HttpServletResponse res,
										 @ApiParam(value = "主键", required = true)
										 @RequestParam String type,
										 @RequestParam String begintime,
										 @RequestParam String endtime) {
		LocalDateTime begin1=null;
		LocalDateTime end1=null;
		for(int i=0; i<=1; i++) {
				if(i==0) {
						DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
						LocalDateTime begin = LocalDateTime.parse(begintime+" 00:00:00",df);
						begin1=begin;
				}else {
						DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
						LocalDateTime end = LocalDateTime.parse(endtime+" 00:00:00",df);
						end1=end;
				}
		}
		if(type.equals("1")) {
			QueryWrapper<Salesmanagement> queryWrapper = new QueryWrapper<>();
			queryWrapper.between("create_time", begin1, end1);
			queryWrapper.eq("ordertype",0);
			queryWrapper.orderByDesc("update_time");
			List<Salesmanagement> list = salesmanagementService.list(queryWrapper);
			XSSFWorkbook wb = new XSSFWorkbook();//创建工作簿
			XSSFSheet sheet = wb.createSheet();//创建表
			XSSFRow row = sheet.createRow(0);//创建表头
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("类型");
			XSSFCell cell1 = row.createCell(1);
			cell1.setCellValue("订单号");
			XSSFCell cell2 = row.createCell(2);
			cell2.setCellValue("委托方");
			XSSFCell cell3 = row.createCell(3);
			cell3.setCellValue("货物名称");
			XSSFCell cell4 = row.createCell(4);
			cell4.setCellValue("HS编码");
			XSSFCell cell5 = row.createCell(5);
			cell5.setCellValue("件数");
			XSSFCell cell6 = row.createCell(6);
			cell6.setCellValue("重量KGS");
			XSSFCell cell7 = row.createCell(7);
			cell7.setCellValue("体积CBM");
			XSSFCell cell8 = row.createCell(8);
			cell8.setCellValue("计费重KGS");
			XSSFCell cell9 = row.createCell(9);
			cell9.setCellValue("货值");
			XSSFCell cell10 = row.createCell(10);
			cell10.setCellValue("起飞机场");
			XSSFCell cell11 = row.createCell(11);
			cell11.setCellValue("ETD");
			XSSFCell cell12 = row.createCell(12);
			cell12.setCellValue("航班");
			XSSFCell cell13 = row.createCell(13);
			cell13.setCellValue("中转机场");
			XSSFCell cell14 = row.createCell(14);
			cell14.setCellValue("到达机场");
			XSSFCell cell15 = row.createCell(15);
			cell15.setCellValue("ETA");
			XSSFCell cell16 = row.createCell(16);
			cell16.setCellValue("航空代理");
			XSSFCell cell17 = row.createCell(17);
			cell17.setCellValue("航空公司");
			XSSFCell cell18 = row.createCell(18);
			cell18.setCellValue("主单号MAWB");
			XSSFCell cell19 = row.createCell(19);
			cell19.setCellValue("分单号HAWB");
			XSSFCell cell20 = row.createCell(20);
			cell20.setCellValue("运输要求");
			XSSFCell cell21 = row.createCell(21);
			cell21.setCellValue("运输反馈");
			for (int i = 0; i < list.size(); i++) {
				Salesmanagement salesmanagement = list.get(i);
				//创建表头
				XSSFRow lrow = sheet.createRow(i + 1);
				//创建单元格
				XSSFCell lcell = lrow.createCell(0);
				lcell.setCellValue("AIR");
				XSSFCell lcell1 = lrow.createCell(1);
				lcell1.setCellValue(salesmanagement.getInternalOrderNo());
				XSSFCell lcell2 = lrow.createCell(2);
				lcell2.setCellValue(salesmanagement.getClient());
				XSSFCell lcell3 = lrow.createCell(3);
				lcell3.setCellValue(salesmanagement.getGoodsChineseName());
				XSSFCell lcell4 = lrow.createCell(4);
				lcell4.setCellValue("HS编码");
				XSSFCell lcell5 = lrow.createCell(5);
				lcell5.setCellValue(salesmanagement.getGoodsAmount());
				XSSFCell lcell6 = lrow.createCell(6);
				lcell6.setCellValue(salesmanagement.getGoodsGrossWeight());
				XSSFCell lcell7 = lrow.createCell(7);
				lcell7.setCellValue(salesmanagement.getGoodsGrossWeight());
				XSSFCell lcell8 = lrow.createCell(8);
				lcell8.setCellValue("计费重KGS");
				XSSFCell lcell9 = lrow.createCell(9);
				lcell9.setCellValue("货值");
				XSSFCell lcell10 = lrow.createCell(10);
				lcell10.setCellValue(salesmanagement.getLoadingPort());
				XSSFCell lcell11 = lrow.createCell(11);
				lcell11.setCellValue("预计开航时间");
				XSSFCell lcell12 = lrow.createCell(12);
				lcell12.setCellValue(salesmanagement.getFlight());
				XSSFCell lcell13 = lrow.createCell(13);
				lcell13.setCellValue(salesmanagement.getFbDestinationPort());
				XSSFCell lcell14 = lrow.createCell(14);
				lcell14.setCellValue("到达机场");
				XSSFCell lcell15 = lrow.createCell(15);
				lcell15.setCellValue("预计到达时间");
				XSSFCell lcell16 = lrow.createCell(16);
				lcell16.setCellValue("航空代理");
				XSSFCell lcell17 = lrow.createCell(17);
				lcell17.setCellValue("航空公司");
				XSSFCell lcell18 = lrow.createCell(18);
				lcell18.setCellValue(salesmanagement.getMainOrderNo());
				XSSFCell lcell19 = lrow.createCell(19);
				lcell19.setCellValue(salesmanagement.getSubOrderNo());
				XSSFCell lcell20 = lrow.createCell(20);
				lcell20.setCellValue("运输要求");
				XSSFCell lcell21 = lrow.createCell(21);
				lcell21.setCellValue("运输反馈");
			}

			String filename = "AIR-" + begintime + "-" + endtime;
			CommonUtil.excelExport(wb, res, filename);


		}else if(type.equals("2")) {
			QueryWrapper<Salesmanagement> queryWrapper = new QueryWrapper<>();

			queryWrapper.between("create_time", begin1, end1);
			queryWrapper.eq("ordertype",1);

			queryWrapper.orderByDesc("update_time");

			List<Salesmanagement> list = salesmanagementService.list(queryWrapper);

			XSSFWorkbook wb = new XSSFWorkbook();//创建工作簿
			XSSFSheet sheet = wb.createSheet();//创建表
			XSSFRow row = sheet.createRow(0);//创建表头
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("类型");
			XSSFCell cell1 = row.createCell(1);
			cell1.setCellValue("订单号");
			XSSFCell cell2 = row.createCell(2);
			cell2.setCellValue("业务员");
			XSSFCell cell3 = row.createCell(3);
			cell3.setCellValue("委托方");
			XSSFCell cell4 = row.createCell(4);
			cell4.setCellValue("货物名称");
			XSSFCell cell5 = row.createCell(5);
			cell5.setCellValue("HS编码");
			XSSFCell cell6 = row.createCell(6);
			cell6.setCellValue("货物描述");
			XSSFCell cell7 = row.createCell(7);
			cell7.setCellValue("件数");
			XSSFCell cell8 = row.createCell(8);
			cell8.setCellValue("毛重KGS");
			XSSFCell cell9 = row.createCell(9);
			cell9.setCellValue("体积CBM");
			XSSFCell cell10 = row.createCell(10);
			cell10.setCellValue("货值");
			XSSFCell cell11 = row.createCell(11);
			cell11.setCellValue("TEU");
			XSSFCell cell12 = row.createCell(12);
			cell12.setCellValue("TEU(US/CA)");
			XSSFCell cell13 = row.createCell(13);
			cell13.setCellValue("箱量");
			XSSFCell cell14 = row.createCell(14);
			cell14.setCellValue("箱型");
			XSSFCell cell15 = row.createCell(15);
			cell15.setCellValue("柜号");
			XSSFCell cell16 = row.createCell(16);
			cell16.setCellValue("装货港");
			XSSFCell cell17 = row.createCell(17);
			cell17.setCellValue("ETD");
			XSSFCell cell18 = row.createCell(18);
			cell18.setCellValue("船名");
			XSSFCell cell19 = row.createCell(19);
			cell19.setCellValue("航次");
			XSSFCell cell20 = row.createCell(20);
			cell20.setCellValue("中转港");
			XSSFCell cell21 = row.createCell(21);
			cell21.setCellValue("卸货港");
			XSSFCell cell22 = row.createCell(22);
			cell22.setCellValue("ETA");
			XSSFCell cell23 = row.createCell(23);
			cell23.setCellValue("订舱代理");
			XSSFCell cell24 = row.createCell(24);
			cell24.setCellValue("船东");
			XSSFCell cell25 = row.createCell(25);
			cell25.setCellValue("订舱号");
			XSSFCell cell26 = row.createCell(26);
			cell26.setCellValue("MB/L No.");
			XSSFCell cell27 = row.createCell(27);
			cell27.setCellValue("HB/L No.");
			XSSFCell cell28 = row.createCell(28);
			cell28.setCellValue("运输要求");
			XSSFCell cell29 = row.createCell(29);
			cell29.setCellValue("运输反馈");
			XSSFCell cell30 = row.createCell(30);
			cell30.setCellValue("发货人");
			XSSFCell cell31 = row.createCell(31);
			cell31.setCellValue("收货人");
			XSSFCell cell32 = row.createCell(32);
			cell32.setCellValue("通知人");

			for (int i = 0; i < list.size(); i++) {
				Salesmanagement salesmanagement = list.get(i);
				//创建表头
				XSSFRow lrow = sheet.createRow(i + 1);
				//创建单元格
				XSSFCell lcell = lrow.createCell(0);
				lcell.setCellValue("FCL");
				XSSFCell lcell1 = lrow.createCell(1);
				lcell1.setCellValue(salesmanagement.getInternalOrderNo());
				XSSFCell lcell2 = lrow.createCell(2);
				User user=userService.getById(salesmanagement.getSalesman());
				if(user==null){lcell2.setCellValue("");}else{lcell2.setCellValue(user.getRealName());}
				XSSFCell lcell3 = lrow.createCell(3);
				lcell3.setCellValue(salesmanagement.getClient());
				XSSFCell lcell4 = lrow.createCell(4);
				lcell4.setCellValue(salesmanagement.getGoodsChineseName());
				XSSFCell lcell5 = lrow.createCell(5);
				lcell5.setCellValue("HS编码");
				XSSFCell lcell6 = lrow.createCell(6);
				lcell6.setCellValue(salesmanagement.getGoodsDescription());
				XSSFCell lcell7 = lrow.createCell(7);
				lcell7.setCellValue(salesmanagement.getNumberUnits());
				XSSFCell lcell8 = lrow.createCell(8);
				lcell8.setCellValue(salesmanagement.getGoodsGrossWeight());
				XSSFCell lcell9 = lrow.createCell(9);
				lcell9.setCellValue(salesmanagement.getGoodsVolumn());
				XSSFCell lcell10 = lrow.createCell(10);
				lcell10.setCellValue(salesmanagement.getValue());
				XSSFCell lcell11 = lrow.createCell(11);
				lcell11.setCellValue("TEU标准箱");
				XSSFCell lcell12 = lrow.createCell(12);
				lcell12.setCellValue("TEU(US/CA)");
				XSSFCell lcell13 = lrow.createCell(13);
				lcell13.setCellValue(salesmanagement.getQuantity());
				XSSFCell lcell14 = lrow.createCell(14);
				lcell14.setCellValue(salesmanagement.getCabinetType());
				XSSFCell lcell15 = lrow.createCell(15);
				lcell15.setCellValue("柜号");
				XSSFCell lcell16 = lrow.createCell(16);
				lcell16.setCellValue(salesmanagement.getPortLoading());
				XSSFCell lcell17 = lrow.createCell(17);
				lcell17.setCellValue("预计开航时间");
				XSSFCell lcell18 = lrow.createCell(18);
				lcell18.setCellValue(salesmanagement.getSailSchedule());//船期
				XSSFCell lcell19 = lrow.createCell(19);
				lcell19.setCellValue(salesmanagement.getSailSchedule());//航次
				XSSFCell lcell20 = lrow.createCell(20);
				lcell20.setCellValue(salesmanagement.getTransshipment());
				XSSFCell lcell21 = lrow.createCell(21);
				lcell21.setCellValue(salesmanagement.getDestination());
				XSSFCell lcell22 = lrow.createCell(22);
				lcell22.setCellValue("预计到达时间");
				XSSFCell lcell23 = lrow.createCell(23);
				lcell23.setCellValue(salesmanagement.getShipowner());//订舱代理
				XSSFCell lcell24 = lrow.createCell(24);
				lcell24.setCellValue(salesmanagement.getShipowner());//船东
				XSSFCell lcell25 = lrow.createCell(25);
				lcell25.setCellValue(salesmanagement.getDestination());
				XSSFCell lcell26 = lrow.createCell(26);
				lcell26.setCellValue(salesmanagement.getMainOrderNo());
				XSSFCell lcell27 = lrow.createCell(27);
				lcell27.setCellValue(salesmanagement.getSubOrderNo());
				XSSFCell lcell28 = lrow.createCell(28);
				lcell28.setCellValue("运输要求");
				XSSFCell lcell29 = lrow.createCell(29);
				lcell29.setCellValue("运输反馈");
				XSSFCell lcell30 = lrow.createCell(30);
				lcell30.setCellValue("发货人");
				XSSFCell lcell31 = lrow.createCell(31);
				lcell31.setCellValue(salesmanagement.getConsignee());
				XSSFCell lcell32 = lrow.createCell(32);
				lcell32.setCellValue(salesmanagement.getNotifier());
			}
			String filename="FCL-SEA-" + begintime + "-" + endtime;
			CommonUtil.excelExport(wb, res, filename);
		}
}

@GetMapping("/seadetail")
@ApiOperationSupport(order = 11)
@ApiOperation(value = "详情")
public void seadetail(){
	BladeUser user = getUser();
	List<String> list1=new ArrayList<>();
	List<String> list2=new ArrayList<>();
	if(user.getTenantId().equals("000000")) {
		//当前租户是admin
		List<SeaBusiness> detail = seaBusinessService.list();//拿到海运表单号存入集合
		for (int i = 0; i < detail.size(); i++) {
			list1.add(detail.get(i).getInternalOrderNo());
		}
		List<Salesmanagement> listsale = salesmanagementService.list();//拿到销售表单号存入集合
		for (int i = 0; i < listsale.size(); i++) {
			list2.add(listsale.get(i).getInternalOrderNo());
		}
		list1.removeAll(list2);//从空运集合中去除销售集合中已有的
		if (list1 != null) {
			for (int i = 0; i < list1.size(); i++) {
				QueryWrapper<SeaBusiness> queryWrapper = new QueryWrapper<>();
				queryWrapper.lambda().eq(SeaBusiness::getInternalOrderNo, list1.get(i));
				List<SeaBusiness> lsa = seaBusinessService.list(queryWrapper);
				for (int j = 0; j < lsa.size(); j++) {
					Salesmanagement salesmanagement = new Salesmanagement();
					//salesmanagement.setTenantId(lsa.get(j).getTenantId());
					salesmanagement.setCreateUser(lsa.get(j).getCreateUser());

					Date date1 = lsa.get(j).getCreateTime();
					Instant instant1 = date1.toInstant();
					ZoneId zoneId1 = ZoneId.systemDefault();
					LocalDateTime localDateTime1 = instant1.atZone(zoneId1).toLocalDateTime();
					salesmanagement.setCreateTime(localDateTime1);

					salesmanagement.setUpdateUser(lsa.get(j).getUpdateUser());
					//salesmanagement.setUpdateTime(lsa.get(j).getUpdateTime());
					salesmanagement.setIsDeleted(lsa.get(j).getIsDeleted());
					salesmanagement.setStatus(lsa.get(j).getStatus());
					salesmanagement.setCreateDept(lsa.get(j).getCreateDept());
					salesmanagement.setInternalOrderNo(lsa.get(j).getInternalOrderNo());
					salesmanagement.setBookingType(lsa.get(j).getBookingType());
					salesmanagement.setBillWay(lsa.get(j).getBillWay());
					salesmanagement.setSalesman(lsa.get(j).getSalesman());
					salesmanagement.setOperator(lsa.get(j).getOperating());
					salesmanagement.setIsMinute(lsa.get(j).getIsMinute());
					salesmanagement.setMainOrderNo(lsa.get(j).getMainOrderNo());
					salesmanagement.setSubOrderNo(lsa.get(j).getSubOrderNo());
					salesmanagement.setCreateTime(lsa.get(j).getCreationTime());
					salesmanagement.setClient(lsa.get(j).getConsignor());
					ClientData clientData = clientDataService.getById(lsa.get(j).getNameShipper());
					if (clientData == null) {
						salesmanagement.setConsigner("");
					} else {
						salesmanagement.setConsigner(clientData.getFullName());
					}
					salesmanagement.setShipperAddress(lsa.get(j).getShipperAddress());
					Salesmanagement consignee = salesmanagementService.getById(lsa.get(j).getConsignee());
					if (consignee == null) {
						salesmanagement.setConsignee("");
					} else {
						salesmanagement.setConsignee(consignee.getConsignee());
					}
					salesmanagement.setConsigneeShipper(lsa.get(j).getConsigneeShipper());
					salesmanagement.setNotifier(lsa.get(j).getNotifier());
					//salesmanagement.setPortLoading(lsa.get(j).getPortLoading);
					//salesmanagement.setDestinationPort(lsa.get(j).getDestination());
					//salesmanagement.setFbDestinationPort(lsa.get(j).getTransshipment());
					//salesmanagement.setShipowner(lsa.get(j).getShipowner());
					salesmanagement.setCargoName(lsa.get(j).getCargoName());
					salesmanagement.setGoodsChineseName(lsa.get(j).getCargoChinese());
					salesmanagement.setGoodsTime(lsa.get(j).getGoodsTime());
					salesmanagement.setSailSchedule(lsa.get(j).getSailSchedule());
					salesmanagement.setDescription(lsa.get(j).getDescription());
					salesmanagement.setCargoRemark(lsa.get(j).getCargoRemark());
					salesmanagement.setNumberUnits(lsa.get(j).getNumberUnits());//货物件数
					salesmanagement.setGoodsGrossWeight(lsa.get(j).getRoughWeight());//货物重量
					salesmanagement.setGoodsVolumn(lsa.get(j).getVolume());//货物体积
					salesmanagement.setFeeName(lsa.get(j).getFeeName());
					salesmanagement.setSalesPrice(lsa.get(j).getSalesPrice());
					salesmanagement.setCurrency(lsa.get(j).getCurrency());
					salesmanagement.setDeliveryRequirements(lsa.get(j).getDeliveryRequirements());
					salesmanagement.setShippingMark(lsa.get(j).getShippingMark());
					salesmanagement.setCabinetType(lsa.get(j).getCabinetType());
					salesmanagement.setGoodsAmount(lsa.get(j).getQuantity());//货物柜量
					salesmanagement.setCustomsBroker(lsa.get(j).getCustomsBroker());
					salesmanagement.setTrailerCompany(lsa.get(j).getTrailerCompany());
					salesmanagement.setBusinessState(lsa.get(j).getBusinessState());
					salesmanagement.setConnectWay(lsa.get(j).getConnectWay());
					salesmanagement.setFreightPayWay(lsa.get(j).getFreightPayWay());
					salesmanagement.setReleaseCargoWay(lsa.get(j).getReleaseCargoWay());
					salesmanagement.setSupplier(lsa.get(j).getSupplier());
					//salesmanagement.setConsigneeagent(lsa.get(j).getConsigneeAgent());
					salesmanagement.setShipmentClause(lsa.get(j).getShipmentClause());
					salesmanagement.setGoodsPackage(lsa.get(j).getGoodsPackage());
					salesmanagement.setWarehousingAgentId(lsa.get(j).getWarehousingAgentId());
					salesmanagement.setTranType("FCL");
					salesmanagement.setOrdertype("1");//设置空运单
					salesmanagementService.save(salesmanagement);
				}
			}
		} else {
			System.out.println("海运重复添加");
		}
	}else {
		//当前租户不是admin
		List<SeaBusiness> detail = seaBusinessService.list();//拿到海运表单号存入集合
		for (int i = 0; i < detail.size(); i++) {
			list1.add(detail.get(i).getInternalOrderNo());
		}
		List<Salesmanagement> listsale = salesmanagementService.list();//拿到销售表单号存入集合
		for (int i = 0; i < listsale.size(); i++) {
			list2.add(listsale.get(i).getInternalOrderNo());
		}
		list1.removeAll(list2);//从空运集合中去除销售集合中已有的
		if (list1 != null) {
			for (int i = 0; i < list1.size(); i++) {
				QueryWrapper<SeaBusiness> queryWrapper = new QueryWrapper<>();
				queryWrapper.lambda().eq(SeaBusiness::getInternalOrderNo, list1.get(i));
				List<SeaBusiness> lsa = seaBusinessService.list(queryWrapper);
				for (int j = 0; j < lsa.size(); j++) {
					Salesmanagement salesmanagement = new Salesmanagement();
					//salesmanagement.setTenantId(lsa.get(j).getTenantId());
					salesmanagement.setCreateUser(lsa.get(j).getCreateUser());

					Date date1 = lsa.get(j).getCreateTime();
					Instant instant1 = date1.toInstant();
					ZoneId zoneId1 = ZoneId.systemDefault();
					LocalDateTime localDateTime1 = instant1.atZone(zoneId1).toLocalDateTime();
					salesmanagement.setCreateTime(localDateTime1);

					salesmanagement.setUpdateUser(lsa.get(j).getUpdateUser());
					//salesmanagement.setUpdateTime(lsa.get(j).getUpdateTime());
					salesmanagement.setIsDeleted(lsa.get(j).getIsDeleted());
					salesmanagement.setStatus(lsa.get(j).getStatus());
					salesmanagement.setCreateDept(lsa.get(j).getCreateDept());
					salesmanagement.setInternalOrderNo(lsa.get(j).getInternalOrderNo());
					salesmanagement.setBookingType(lsa.get(j).getBookingType());
					salesmanagement.setBillWay(lsa.get(j).getBillWay());
					salesmanagement.setSalesman(lsa.get(j).getSalesman());
					salesmanagement.setOperator(lsa.get(j).getOperating());
					salesmanagement.setIsMinute(lsa.get(j).getIsMinute());
					salesmanagement.setMainOrderNo(lsa.get(j).getMainOrderNo());
					salesmanagement.setSubOrderNo(lsa.get(j).getSubOrderNo());
					salesmanagement.setCreateTime(lsa.get(j).getCreationTime());
					salesmanagement.setClient(lsa.get(j).getConsignor());
					ClientData clientData = clientDataService.getById(lsa.get(j).getNameShipper());
					if (clientData == null) {
						salesmanagement.setConsigner("");
					} else {
						salesmanagement.setConsigner(clientData.getFullName());
					}
					salesmanagement.setShipperAddress(lsa.get(j).getShipperAddress());
					Salesmanagement consignee = salesmanagementService.getById(lsa.get(j).getConsignee());
					if (consignee == null) {
						salesmanagement.setConsignee("");
					} else {
						salesmanagement.setConsignee(consignee.getConsignee());
					}
					salesmanagement.setConsigneeShipper(lsa.get(j).getConsigneeShipper());
					salesmanagement.setNotifier(lsa.get(j).getNotifier());
					//salesmanagement.setPortLoading(lsa.get(j).getPortLoading);
					//salesmanagement.setDestinationPort(lsa.get(j).getDestination());
					//salesmanagement.setFbDestinationPort(lsa.get(j).getTransshipment());
					//salesmanagement.setShipowner(lsa.get(j).getShipowner());
					salesmanagement.setCargoName(lsa.get(j).getCargoName());
					salesmanagement.setGoodsChineseName(lsa.get(j).getCargoChinese());
					salesmanagement.setGoodsTime(lsa.get(j).getGoodsTime());
					salesmanagement.setSailSchedule(lsa.get(j).getSailSchedule());
					salesmanagement.setDescription(lsa.get(j).getDescription());
					salesmanagement.setCargoRemark(lsa.get(j).getCargoRemark());
					salesmanagement.setNumberUnits(lsa.get(j).getNumberUnits());//货物件数
					salesmanagement.setGoodsGrossWeight(lsa.get(j).getRoughWeight());//货物重量
					salesmanagement.setGoodsVolumn(lsa.get(j).getVolume());//货物体积
					salesmanagement.setFeeName(lsa.get(j).getFeeName());
					salesmanagement.setSalesPrice(lsa.get(j).getSalesPrice());
					salesmanagement.setCurrency(lsa.get(j).getCurrency());
					salesmanagement.setDeliveryRequirements(lsa.get(j).getDeliveryRequirements());
					salesmanagement.setShippingMark(lsa.get(j).getShippingMark());
					salesmanagement.setCabinetType(lsa.get(j).getCabinetType());
					salesmanagement.setGoodsAmount(lsa.get(j).getQuantity());//货物柜量
					salesmanagement.setCustomsBroker(lsa.get(j).getCustomsBroker());
					salesmanagement.setTrailerCompany(lsa.get(j).getTrailerCompany());
					salesmanagement.setBusinessState(lsa.get(j).getBusinessState());
					salesmanagement.setConnectWay(lsa.get(j).getConnectWay());
					salesmanagement.setFreightPayWay(lsa.get(j).getFreightPayWay());
					salesmanagement.setReleaseCargoWay(lsa.get(j).getReleaseCargoWay());
					salesmanagement.setSupplier(lsa.get(j).getSupplier());
					//salesmanagement.setConsigneeagent(lsa.get(j).getConsigneeAgent());
					salesmanagement.setShipmentClause(lsa.get(j).getShipmentClause());
					salesmanagement.setGoodsPackage(lsa.get(j).getGoodsPackage());
					salesmanagement.setWarehousingAgentId(lsa.get(j).getWarehousingAgentId());
					salesmanagement.setTranType("FCL");
					salesmanagement.setOrdertype("1");//设置空运单
					salesmanagementService.save(salesmanagement);
				}
			}
		} else {
			System.out.println("海运重复添加");
		}
	}
}

/**
 * 下拉数据源
 */
@GetMapping("/selectorder")
@ApiOperationSupport(order = 11)
@ApiOperation(value = "下拉数据源", notes = "传入id")
public R<List<Salesmanagement> > select() {
		List<Salesmanagement> list = salesmanagementService.list();
		return R.data(list);
}




}
