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
package org.springblade.modules.businessManagement.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springblade.common.constant.CommonConstant;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.businessManagement.entity.AirBusiness;
import org.springblade.modules.businessManagement.entity.BusinessFile;
import org.springblade.modules.businessManagement.entity.Fee;
import org.springblade.modules.businessManagement.enums.FeePayType;
import org.springblade.modules.businessManagement.enums.FeeStatus;
import org.springblade.modules.businessManagement.enums.ReceivablePayableType;
import org.springblade.modules.businessManagement.vo.AirBusinessVO;
import org.springblade.modules.businessManagement.mapper.AirBusinessMapper;
import org.springblade.modules.businessManagement.service.IAirBusinessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.modules.clientManagement.entity.ClientData;
import org.springblade.modules.clientManagement.entity.Supplier;
import org.springblade.modules.clientManagement.service.impl.ClientDataServiceImpl;
import org.springblade.modules.dictionaries.entity.Air;
import org.springblade.modules.dictionaries.enums.CustomCurrency;
import org.springblade.modules.dictionaries.service.impl.WorkNumRulesServiceImpl;
import org.springblade.modules.financialManagement.entity.BankAccount;
import org.springblade.modules.financialManagement.entity.BillDetail;
import org.springblade.modules.financialManagement.service.impl.BankAccountServiceImpl;
import org.springblade.modules.financialManagement.service.impl.BillDetailServiceImpl;
import org.springblade.modules.system.entity.Tenant;
import org.springblade.modules.system.service.impl.TenantServiceImpl;
import org.springblade.modules.system.service.impl.UserServiceImpl;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.springblade.common.utils.CommonUtil.*;

/**
 * 空运业务 服务实现类
 *
 * @author BladeX
 * @since 2019-09-10
 */
@AllArgsConstructor
@Service
public class AirBusinessServiceImpl extends BaseServiceImpl<AirBusinessMapper, AirBusiness> implements IAirBusinessService {
	private WorkNumRulesServiceImpl workNumRulesService;
	private FeeServiceImpl feeService;
	private BusinessFileServiceImpl businessFileService;
	private UserServiceImpl userService;
	private ClientDataServiceImpl clientDataService;
	private TenantServiceImpl tenantService;
	private BankAccountServiceImpl bankAccountService;
	private BillDetailServiceImpl billDetailService;

	@Override
	public IPage<AirBusinessVO> selectAirBusinessPage(IPage<AirBusinessVO> page, AirBusinessVO airBusiness) {
		return page.setRecords(baseMapper.selectAirBusinessPage(page, airBusiness));
	}

	@Transactional
	public Boolean saveData(AirBusiness airBusiness) {
		String internalOrderNo = workNumRulesService.getInternalOrderNo(workNumRulesService, Long.valueOf(airBusiness.getInternalOrderNo()), airBusiness.getDestinationPort());
		airBusiness.setInternalOrderNo(internalOrderNo);
		if (!save(airBusiness)) throw new RuntimeException("保存失败");
		return true;
	}


	public void getDeliveryReceipt(AirBusiness airBusiness, HttpServletResponse response) {
		AirBusiness data = this.getById(airBusiness.getId());
		AirBusiness main = null;
		AirBusiness sub = null;
		boolean isMain = data.getIsMain();
		if (isMain) main = data;
		else {
			sub = data;
			QueryWrapper<AirBusiness> qw = new QueryWrapper<>();
			qw.eq("is_main", 1);
			qw.eq("main_order_no", sub.getMainOrderNo());
			main = this.getOne(qw);
			if (main == null) throw new RuntimeException("没有找到主单");
		}
		Map<String, String> map = new HashMap<>();
		ClientData client = clientDataService.getById(data.getClient());
		String a3 = client == null ? CommonConstant.EXCEL_NODATA_TEXT : client.getEnglishName();
		map.put("A1", userService.getById(data.getSalesman()).getName());//业务员
		map.put("A2", userService.getById(data.getOperator()).getName());//操作员
		map.put("A3", excelDataJudge(a3));
		map.put("A4", "联系人");
		map.put("A5", "电话");
		map.put("A6", excelDataJudge(data.getLoadingPort())); //起运港
		map.put("A7", excelDataJudge(data.getGoodsEnglishName().toUpperCase()));//品名
		map.put("A8", excelDataJudge(data.getDestinationPort())); //目的港
		map.put("A9", excelDataJudge(data.getGoodsGrossWeight()));//重量
		map.put("A10", excelDataJudge(data.getGoodsAmount()));//件数
		map.put("A11", excelDataJudge(data.getGoodsVolumn()));//体积
		map.put("A12", "入仓时间");
		map.put("A13", "入仓号码");
		map.put("A14", "出货要求");
		map.put("A15", "销售价");
		map.put("A16", "拓展专员");
		ClientData booking_agent = clientDataService.getById(main.getBookingAgentId());
		String a17 = booking_agent == null ? CommonConstant.EXCEL_NODATA_TEXT : booking_agent.getEnglishName();
		map.put("A17", excelDataJudgeWithoutTips(a17));
		map.put("A18", "空运费");//
		map.put("A19", "备注");
		map.put("A20", "中港费");
		map.put("A21", "杂费");
		map.put("A22", "海底部");
		map.put("A23", "航班与日期");
		map.put("A24", "跟进时间");
		map.put("A25", excelDataJudge(main.getCargoTerminalWeight()));//主单重量
		map.put("A26", main.getMainOrderNo());//提单号
		map.put("A27", "毛利");

		CommonUtil.excelTemplatePrint(response, "/static/air_delivery_receipt.xlsx", map, data.getInternalOrderNo() + "_delivery_receipt");
	}

	public void getWaybillShow(AirBusiness airBusiness, HttpServletResponse response) {
		AirBusiness data = this.getById(airBusiness.getId());
		AirBusiness main = null;
		AirBusiness sub = null;
		boolean isMain = data.getIsMain();
		if (isMain) main = data;
		else {
			sub = data;
			QueryWrapper<AirBusiness> qw = new QueryWrapper<>();
			qw.eq("is_main", 1);
			qw.eq("main_order_no", sub.getMainOrderNo());
			main = this.getOne(qw);
			if (main == null) throw new RuntimeException("没有找到主单");
		}
		Map<String, String> map = new HashMap<>();
		map.put("A1", excelDataJudge(data.getLoadingPort()));//#起飞机场代码#
		map.put("A2", excelDataJudge(data.getConsigner()));//#发货人#
		map.put("A3", excelDataJudge(data.getNotifier()));//通知人
		map.put("A4", main.getAirFeePayType() == FeePayType.PREPAID ? "FREIGHT PREPAID" : "FREIGHT COLLECT");//#支付信息#
		map.put("A5", excelDataJudge(data.getLoadingPort()));//#起飞机场简称#
		map.put("A6", excelDataJudge(main.getSbDestinationPort()));//#中转机场代码#
		ClientData carrier = clientDataService.getById(main.getCarrierId());
		String a7 = carrier == null ? CommonConstant.EXCEL_NODATA_TEXT : carrier.getEnglishName();
		map.put("A7", excelDataJudge(a7.toUpperCase()));//#航空公司#
		map.put("A8", "");//#货主运费支付方式#
		map.put("A9", "");//#计费价币种#
		map.put("A10", excelDataJudge(data.getDestinationPort()));//#到达机场代码#
		map.put("A11", excelDataJudge(main.getAirLine()));//#起飞机场航班#
		map.put("A12", excelDataJudge(data.getNotifier()));//#运输要求#
		map.put("A13", excelDataJudge(data.getInAmount()));//#件数#
		map.put("A14", excelDataJudge(data.getInWeight()));//#毛重#
		map.put("A15", "");//#计费重#
		map.put("A16", "");//#计费价金额#
		map.put("A17", "");//#计费运价#
		/** A18
		 #货物名称#

		 DIMS:#货物尺寸#
		 */
		map.put("A18", "");
		map.put("A19", "VOL: " + excelDataJudgeWithoutTips(data.getInVolumn()));//VOL: #体积#CBM
		map.put("A20", "");//#计费运价#
		map.put("A21", "");//#其它费用#
		map.put("A22", "");//#其它费用合计Agent#
		map.put("A23", "");//#其它费用合计Carrier#
		map.put("A24", "");//#预付费用合计#
		map.put("A25", "");//#到付费用合计#
		map.put("A26", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));//#签发日期#
		map.put("A27", excelDataJudge(data.getLoadingPortFullName()));//#签发地点#
		map.put("A28", "");//#航空公司大字logo
		CommonUtil.excelTemplatePrint(response, "/static/air_waybill_show.xlsx", map, data.getInternalOrderNo() + "_waybill_show");
	}

	public void getWaybill(AirBusiness airBusiness, HttpServletResponse response, String printer) {
		AirBusiness data = this.getById(airBusiness.getId());
		AirBusiness main = null;
		AirBusiness sub = null;
		boolean isMain = data.getIsMain();
		if (isMain) main = data;
		else {
			sub = data;
			QueryWrapper<AirBusiness> qw = new QueryWrapper<>();
			qw.eq("is_main", 1);
			qw.eq("main_order_no", sub.getMainOrderNo());
			main = this.getOne(qw);
			if (main == null) throw new RuntimeException("没有找到主单");
		}
		Map<String, String> map = new HashMap<>();
		String mainOrderNo = data.getMainOrderNo().replaceAll("\\D", "");
		map.put("A1", mainOrderNo.substring(0, 3));
		map.put("A2", mainOrderNo.substring(3, 7) + " " + mainOrderNo.substring(7, 11));
		map.put("A3", main.getLoadingPort());
		//a4-a8
		String consignerInfo = (data.getConsigner() + data.getConsignerAddress()).toUpperCase();
		String[] consignerInfoArray = CommonUtil.substringToArray(consignerInfo, 5, 20);
		for (int i = 0; i < consignerInfoArray.length; i++) map.put("A" + (4 + i), consignerInfoArray[i]);
		//a9-a13
		String consigneeInfo = (data.getConsignee() + data.getConsigneeAddress()).toLowerCase();
		String[] consigneeInfoArray = CommonUtil.substringToArray(consigneeInfo, 5, 20);
		for (int i = 0; i < consigneeInfoArray.length; i++) map.put("A" + (9 + i), consigneeInfoArray[i]);
		if (main.getAirFeePayType() == null) throw new RuntimeException("空运运费支付方式不能为空");
		map.put("A14", main.getAirFeePayType() == FeePayType.PREPAID ? "FREIGHT PREPAID" : "FREIGHT COLLECT");

		map.put("A15", excelDataJudge(data.getLoadingPortFullName()));
		map.put("A16", excelDataJudge(main.getFbDestinationPort()));//头程目的港三字代码
		ClientData carrier = clientDataService.getById(main.getCarrierId());
		String carrierCode = carrier == null ? CommonConstant.EXCEL_NODATA_TEXT : carrier.getUnitCode();
		map.put("A17", excelDataJudge(carrierCode));//头程航空公司二字代码
		map.put("A18", excelDataJudge(main.getSbDestinationPort()));//"二程目的港三字代码"
		map.put("A19", excelDataJudge(main.getSbDestinationPort()));//二程航空公司二字代码
		map.put("A20", "");//三程目的港三字代码
		map.put("A21", "");//三程航空公司二字代码

		map.put("A22", excelDataJudge(main.getLadingBillFeeCurrency()));//付款方式
		map.put("A23", main.getAirFeePayType() == FeePayType.PREPAID ? "PP" : "PC");
		map.put("A24", main.getOtherFeePayType() == FeePayType.PREPAID ? "PP" : "PC");
		map.put("A25", excelDataJudgeWithoutTips(data.getDeclaredValueForCarriage()));//声明运输价值
		map.put("A26", excelDataJudgeWithoutTips(data.getDeclaredValueForCustoms()));//声明海关价值
		map.put("A27", excelDataJudge(data.getDestinationPortFullName()));//目的港全称


		String a28_m = main.getFbLaunchTime().getMonth().toString().substring(0, 3);
		Integer a28_i = main.getFbLaunchTime().getDayOfMonth();
		String a28_d = (a28_i >= 10 ? a28_i.toString() : "0" + a28_i.toString()) + ".";
		map.put("A28", main.getFlight() + "/" + a28_d + a28_m);//航班号与日期 KE320/03.AUG
		map.put("A29", data.getAmountOfInsurance() <= 0d ? "" : data.getAmountOfInsurance().toString());//保险金额
		String[] a30_31 = CommonUtil.substringToArray(data.getNotifier(), 2, 30);
		map.put("A30", a30_31[0]);//另行通知1
		map.put("A31", a30_31[1]);//另行通知2
		map.put("A32", data.getGoodsAmount().toString());//件数
		map.put("A33", data.getGoodsGrossWeight().toString());//毛重
		map.put("A34", "K");
		map.put("A35", data.getCargoRate());//运价等级
		Double a36 = 0d;

		QueryWrapper<AirBusiness> qw2 = new QueryWrapper<>();
		qw2.eq("main_order_no", data.getMainOrderNo());
		List<AirBusiness> airBusinessList = new ArrayList<>();
		airBusinessList = list(qw2);
		//收费重量 选最大那个 a36
		for (AirBusiness item : airBusinessList)
			if (item.getGoodsChargeableWeight() >= a36) a36 = item.getGoodsChargeableWeight();
		map.put("A36", a36.toString());
		map.put("A37", main.getPublishFare().toString());
		Double count1 = a36 * main.getPublishFare();
		map.put("A38", count1.toString());// a36*a37;

		// data.getGoodsLength().toString() + "*" + data.getGoodsLength().toString() + "*" + data.getGoodsLength().toString() + " cm/" + data.getGoodsAmount().toString();
		if (airBusinessList.size() > 1 && isMain) {
			map.put("A39", "CONSOL");//CONSOL
			map.put("A40", "");
		} else if ((airBusinessList.size() > 1 && !isMain) || airBusinessList.size() == 1) {
			String[] goodsName = CommonUtil.substringToArray(data.getGoodsEnglishName(), 2, 30);
			map.put("A39", goodsName[0]);
			map.put("A40", goodsName[1]);
		}

		map.put("A41", "");

		//a48-53 尺寸  入仓
		String[] names = CommonUtil.substringToArray(data.getGoodsEnglishName(), 6, 20);
		for (int i = 0; i < names.length; i++) map.put("A" + (48 + i), names[i]);

		String[] otherFeeList = main.getOtherFeeList().split(",");
		Map<String, Double> fee_map = new HashMap<>();
		for (int i = 0; i < otherFeeList.length; i++) {
			String[] fs = otherFeeList[i].split(":");
			String c = fs[0];
			Double f = Double.parseDouble(fs[1]);
			fee_map.put(c, f);
		}

		//其他费用打印  A200 开始 ，最多6条费用
		Double count2 = 0d;
		int fee_i = 0;
		for (String key : fee_map.keySet()) {
			Double f = fee_map.get(key);
			count2 += f;
			map.put("A" + (200 + fee_i * 2), key.toUpperCase() + ":");
			map.put("A" + (200 + fee_i * 2 + 1), f.toString());
			if (fee_i >= 6) continue;
			fee_i++;
		}
		for (int i = 0; i < 6; i++) {
			String a = "A" + (200 + i * 2);
			String b = "A" + (200 + i * 2 + 1);
			if (!map.containsKey(a)) map.put(a, "");
			if (!map.containsKey(b)) map.put(b, "");
		}
		//a42-a47 唛头
		String[] marks = CommonUtil.substringToArray(data.getMark(), 6, 20);
		for (int i = 0; i < marks.length; i++) map.put("A" + (42 + i), marks[i]);
		map.put("A54", data.getGoodsAmount().toString());
		map.put("A55", data.getGoodsGrossWeight().toString());
		map.put("A56", count1.toString());
		map.put("A57", count1.toString());
		map.put("A58", count2.toString());
		Double count3 = (count1 + count1);
		map.put("A59", count3.toString());
		ClientData airline_company_agent = clientDataService.getById(main.getAirlineCompanyAgentId());
		String a60 = airline_company_agent == null ? CommonConstant.EXCEL_NODATA_TEXT : airline_company_agent.getEnglishName().toUpperCase();
		map.put("A60", excelDataJudge(a60));//航司代理人
		map.put("A61", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
		map.put("A62", excelDataJudge(data.getLoadingPortFullName()));//起运港全称
		map.put("A63", excelDataJudge(printer));//谁打印就谁
		map.put("A64", excelDataJudge(data.getCargoTerminalVolumn().toString()));//打单体积
		CommonUtil.excelTemplatePrint(response, "/static/air_waybill.xlsx", map, data.getInternalOrderNo() + "_waybill");
	}

	/**
	 * 删除空运单相关数据 包括文件 包括费用
	 *
	 * @param p
	 * @return
	 */
	@Transactional
	public Boolean customRemoveById(String p) {
		List<Long> ids = Func.toLongList(p);
		Collection<AirBusiness> airBusinessCollection = this.listByIds(ids);
		List<String> internal_order_no_list = new ArrayList<>();
		for (AirBusiness item : airBusinessCollection) internal_order_no_list.add(item.getInternalOrderNo());
		QueryWrapper<Fee> fqw = new QueryWrapper<>();
		fqw.in("internal_order_no", internal_order_no_list);
		List<Fee> feeList = feeService.list(fqw);
		QueryWrapper<BusinessFile> bfqw = new QueryWrapper<>();
		bfqw.in("internal_order_no", internal_order_no_list);
		List<BusinessFile> businessFileList = businessFileService.list(bfqw);
		List<Long> fileIdList = new ArrayList<>(businessFileList.size());
		List<String> feeIdList = new ArrayList<>(feeList.size());
		for (Fee i : feeList) feeIdList.add(i.getId().toString());
		for (BusinessFile i : businessFileList) fileIdList.add(i.getId());
		QueryWrapper<BillDetail> bd_qw = new QueryWrapper<>();
		bd_qw.eq("internal_order_no", internal_order_no_list);
		billDetailService.remove(bd_qw);
		if (ids.size() > 0) removeByIds(ids);
		if (feeIdList.size() > 0) feeService.removeByIds(feeIdList);
		if (fileIdList.size() > 0) businessFileService.removeFiles(StringUtils.join(fileIdList.toArray(), ","));
		return true;
	}


	public void getStatement(List<Long> bankAccountIds, AirBusiness airBusiness, HttpServletResponse response) {
		Long bankAccountId = bankAccountIds.get(0);
		BankAccount bankAccount = bankAccountService.getById(bankAccountId);
		if (bankAccount == null) bankAccount = new BankAccount();
		AirBusiness data = this.getById(airBusiness.getId());
		AirBusiness main = null;
		AirBusiness sub = null;
		QueryWrapper<Tenant> tenantQueryWrapper = new QueryWrapper<>();
		tenantQueryWrapper.eq("tenant_id", data.getTenantId());
		Tenant tenant = tenantService.getOne(tenantQueryWrapper);
		if (tenant == null) throw new RuntimeException("没有租户数据");
		boolean isMain = data.getIsMain();
		if (isMain) main = data;
		else {
			sub = data;
			QueryWrapper<AirBusiness> qw = new QueryWrapper<>();
			qw.eq("is_main", 1);
			qw.eq("main_order_no", sub.getMainOrderNo());
			main = this.getOne(qw);
			if (main == null) throw new RuntimeException("没有找到主单");
		}
		Map<String, String> map = new HashMap<>();
		String mainOrderNo = data.getMainOrderNo().replaceAll("\\D", "");
		ClientData clientData = clientDataService.getById(data.getClient());
		map.put("A1", excelDataJudgeWithoutTips(tenant.getCompanyChineseName()));
		map.put("A2", excelDataJudgeWithoutTips(tenant.getCompanyEnglishName()));
		map.put("A3", excelDataJudgeWithoutTips(tenant.getAddress()));
		String phone = StringUtils.isEmpty(tenant.getContactNumber()) ? "" : "Tel: " + tenant.getContactNumber();
		String email = StringUtils.isEmpty(tenant.getEmail()) ? "" : " Email: " + tenant.getEmail();
		String fax = StringUtils.isEmpty(tenant.getFax()) ? "" : " Fax: " + tenant.getFax();
		String a4 = phone + fax + email;

		map.put("A4", a4);
		map.put("A5", excelDataJudge(clientData.getFullName()));//对账公司全称
		map.put("A6", excelDataJudge(data.getInternalOrderNo()));//订单号
		map.put("A7", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));//出单日期
		map.put("A8", excelDataJudge(data.getLoadingPortFullName()));
		map.put("A9", excelDataJudge(data.getDestinationPortFullName()));
		map.put("A10", "");//预计开航 ETD:
		map.put("A11", "");//预计到达 ETA:
		ClientData carrier = clientDataService.getById(main.getCarrierId());
		String a12 = carrier == null ? CommonConstant.EXCEL_NODATA_TEXT : carrier.getFullName();
		map.put("A12", excelDataJudge(a12));// 航空公司名
		map.put("A13", data.getGoodsAmount().toString() + " " + data.getGoodsPackage());//件数加包装
		map.put("A14", data.getCargoTerminalWeight().toString() + "KGS"); //重量
		map.put("A15", data.getCargoTerminalVolumn() + " CBM");
		map.put("A16", "");//订舱号
		map.put("A17", excelDataJudge(data.getMainOrderNo()));
		Workbook workbook = CommonUtil.excelTemplate("/static/air_statement.xlsx", map);
		QueryWrapper<Fee> fqw = new QueryWrapper<>();
		fqw.eq("internal_order_no", data.getInternalOrderNo());
		fqw.eq("type", ReceivablePayableType.RECEIVABLE.getValue());
		List<Fee> feeList = feeService.list(fqw);
		Sheet sheet = workbook.getSheetAt(0);
		Sheet sheet_clone = workbook.cloneSheet(0);
		Map<CustomCurrency, Double> feeTotalMap = new HashMap<>();
		int write_to = 0;
		int start = 21;
		write_to += start;
		for (int i = 0; i < feeList.size(); i++) {
			Row row = sheet.createRow(start + i);
			Fee fee = feeList.get(i);
			CommonUtil.copyRow(row, sheet_clone.getRow(start));
			row.getCell(0).setCellValue(fee.getShortName());
			row.getCell(1).setCellValue(fee.getChineseName());
			row.getCell(3).setCellValue(fee.getCurrency().getValue());
			row.getCell(4).setCellValue(fee.getQuantity());
			row.getCell(5).setCellValue(fee.getUnitPrice());
			row.getCell(7).setCellType(CellType.NUMERIC);
			row.getCell(7).setCellValue(fee.getAmount());
			if (!feeTotalMap.containsKey(fee.getCurrency())) feeTotalMap.put(fee.getCurrency(), 0d);
			Double amount = feeTotalMap.get(fee.getCurrency());
			amount += fee.getAmount();
			feeTotalMap.put(fee.getCurrency(), amount);
		}
		write_to += feeList.size();
		sheet.createRow(write_to);
		write_to++;
		int j = 0;
		for (CustomCurrency currency : feeTotalMap.keySet()) {
			Row row = sheet.createRow(write_to);
			write_to++;
			if (j >= feeTotalMap.size() - 1) {
				CommonUtil.copyRow(row, sheet_clone.getRow(23));
			} else {
				CommonUtil.copyRow(row, sheet_clone.getRow(22));
			}
			row.getCell(6).setCellType(CellType.NUMERIC);
			row.getCell(6).setCellValue(currency.getValue());
			row.getCell(7).setCellValue(feeTotalMap.get(currency));
			j++;
		}
		if(feeList.size()==0)write_to+=2;
		else if(feeList.size()==1)write_to++;

		for (int i = 24; i < 34; i++) {
			Row row = sheet.createRow(write_to);
			CommonUtil.copyRow(row, sheet_clone.getRow(i));
			if (i == 27) row.getCell(1).setCellValue(excelDataJudge(bankAccount.getAccountName()));
			else if (i == 28) row.getCell(1).setCellValue(excelDataJudge(bankAccount.getBankName()));
			else if (i == 29) row.getCell(1).setCellValue(excelDataJudge(bankAccount.getAccountNumber()));
			write_to++;
		}
		workbook.removeSheetAt(1);
		CommonUtil.excelExport(workbook, response, data.getInternalOrderNo() + "_statement");
	}

	@Transactional
	public boolean verifyAllFee(String internalOrderNo) {
		if (StringUtils.isEmpty(internalOrderNo)) throw new RuntimeException("内部单号不能为空");
		QueryWrapper<AirBusiness> qw = new QueryWrapper<>();
		qw.eq("internal_order_no", internalOrderNo);
		AirBusiness airBusiness = getOne(qw);
		if (airBusiness == null) throw new RuntimeException("没有找到对应空运单");
		airBusiness.setIsVerifiedFee(true);
		QueryWrapper<Fee> fee_qw = new QueryWrapper<>();
		fee_qw.eq("internal_order_no", internalOrderNo);
		List<Fee> feeList = feeService.list(fee_qw);
		feeList.forEach(i -> i.setFeeStatus(FeeStatus.AUDITED));
		if (!(feeService.updateBatchById(feeList) && updateById(airBusiness))) throw new RuntimeException("审核全部失败");
		return true;
	}

}
