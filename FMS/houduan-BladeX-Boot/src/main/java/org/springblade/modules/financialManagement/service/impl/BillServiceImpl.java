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
package org.springblade.modules.financialManagement.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springblade.common.constant.CommonConstant;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.businessManagement.entity.AirBusiness;
import org.springblade.modules.businessManagement.entity.Fee;
import org.springblade.modules.businessManagement.enums.BusinessStatus;
import org.springblade.modules.businessManagement.enums.FeeStatus;
import org.springblade.modules.businessManagement.enums.ReceivablePayableType;
import org.springblade.modules.businessManagement.service.IFeeService;
import org.springblade.modules.businessManagement.service.impl.AirBusinessServiceImpl;
import org.springblade.modules.businessManagement.service.impl.FeeServiceImpl;
import org.springblade.modules.clientManagement.entity.ClientData;
import org.springblade.modules.clientManagement.service.impl.ClientDataServiceImpl;
import org.springblade.modules.dictionaries.enums.CustomCurrency;
import org.springblade.modules.financialManagement.entity.BankAccount;
import org.springblade.modules.financialManagement.entity.Bill;
import org.springblade.modules.financialManagement.entity.BillDetail;
import org.springblade.modules.financialManagement.enums.BillStatus;
import org.springblade.modules.financialManagement.service.IBillDetailService;
import org.springblade.modules.financialManagement.vo.BillVO;
import org.springblade.modules.financialManagement.mapper.BillMapper;
import org.springblade.modules.financialManagement.service.IBillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.modules.system.entity.Tenant;
import org.springblade.modules.system.service.impl.TenantServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.springblade.common.utils.CommonUtil.excelDataJudge;
import static org.springblade.common.utils.CommonUtil.excelDataJudgeWithoutTips;

/**
 * 账单 服务实现类
 *
 * @author BladeX
 * @since 2019-10-16
 */
@Service
@AllArgsConstructor
public class BillServiceImpl extends BaseServiceImpl<BillMapper, Bill> implements IBillService {
	private IFeeService feeService;
	private IBillDetailService billDetailService;
	private AirBusinessServiceImpl airBusinessService;
	private BankAccountServiceImpl bankAccountService;
	private TenantServiceImpl tenantService;
	private ClientDataServiceImpl clientDataService;

	@Override
	public IPage<BillVO> selectBillPage(IPage<BillVO> page, BillVO bill) {
		return page.setRecords(baseMapper.selectBillPage(page, bill));
	}

	@Transactional
	public boolean createOrderInserToBill(Bill bill, String feeIds) {
		if (!this.saveOrUpdate(bill)) throw new RuntimeException("账单保存失败");
		Collection<Fee> feeList = feeService.listByIds(Func.toLongList(feeIds));
		List<Fee> ffList = new ArrayList<>(feeList.size());
		feeList.forEach(i -> ffList.add(i));
		FeeServiceImpl.countPayedAmount(ffList, billDetailService, this);
		ffList.forEach(item -> {
			if (item.getBillCreatedAmount().longValue() >= item.getAmount().longValue())
				throw new RuntimeException("账单金额已达费用金额,不能创建");
		});
		if (bill.getBillStatus() == BillStatus.SETTLED || bill.getBillStatus() == BillStatus.ARCHIVED)
			throw new RuntimeException("账单已结算或者已归档");
		if (bill.getCurrency() == null) throw new RuntimeException("没有选择账单货币");
		QueryWrapper<BillDetail> bd_qw = new QueryWrapper<>();
		bd_qw.eq("bill_id", bill.getId());
		List<BillDetail> data_billDetailList = billDetailService.list(bd_qw);
		List<BillDetail> billDetailList = new ArrayList<>(feeList.size());
		Set<Long> settlementUnitSet = new HashSet<>();
		for (Fee fee : feeList) {
			if (!bill.getType().getValue().toString().equals(fee.getType().getValue().toString()))
				throw new RuntimeException("收款类型不统一");
			BillDetail billDetail = new BillDetail();
			billDetail.setAmount(0d);
			billDetail.setBillId(bill.getId());
			billDetail.setAmount(0d);
			billDetail.setConvertAmount(0d);
			billDetail.setFeeId(fee.getId());
			billDetail.setFeeName(fee.getShortName());
			billDetail.setInternalOrderNo(fee.getInternalOrderNo());
			billDetail.setType(fee.getType());
			billDetail.setCurrency(fee.getCurrency());
			billDetail.setConvertCurrency(bill.getCurrency());
			billDetailList.add(billDetail);
			if (fee.getFeeStatus() != FeeStatus.SETTLING && fee.getFeeStatus() != FeeStatus.AUDITED)
				throw new RuntimeException("存在没有审核的费用");
			fee.setFeeStatus(FeeStatus.SETTLING);
			settlementUnitSet.add(fee.getSettlementUnit());
		}
		if (settlementUnitSet.size() != 1) throw new RuntimeException("只能创建同一结算单位的账单");
		if (!feeService.updateBatchById(feeList)) throw new RuntimeException("费用更新失败");
		for (BillDetail b0 : data_billDetailList)
			for (BillDetail b1 : billDetailList)
				if (b0.getFeeId().longValue() == b1.getFeeId().longValue()) throw new RuntimeException("账单已存在费用");
		if (!billDetailService.saveBatch(billDetailList)) throw new RuntimeException("更新失败");
		return true;
	}

	@Transactional
	public boolean insertToBill(String billId, String feeIds) {
		Bill bill = getById(billId);
		if (bill == null) throw new RuntimeException("没有找到账单");
		return createOrderInserToBill(bill, feeIds);
	}

	public boolean archive(Long billId) {
		Bill bill = getById(billId);
		if (bill == null) throw new RuntimeException("账单不存在");
		if (bill.getBillStatus() != BillStatus.UNARCHIVE && bill.getBillStatus() != BillStatus.SETTLED)
			throw new RuntimeException("请检查账单状态");
		bill.setBillStatus(BillStatus.ARCHIVED);
		if (!updateById(bill)) throw new RuntimeException("归档失败");
		return true;
	}


	@Transactional
	public boolean addToBill(List<Fee> feeList, Bill bill, BladeUser user) {
		bill = getById(bill.getId());
		QueryWrapper<BillDetail> bd_qw = new QueryWrapper<>();
		long billId = bill.getId();
		bd_qw.eq("bill_id", bill.getId());
		List<BillDetail> billDetailList = billDetailService.list(bd_qw);
		List<Long> feeIds = new ArrayList<>();
		List<BillDetail> saveBillDetailList = new ArrayList<>();
		double[] this_total = new double[1];
		feeList.forEach(fee -> {
			feeIds.add(fee.getId());
			billDetailList.forEach(billDetail -> {
				if (fee.getId().longValue() == billDetail.getFeeId().longValue()) throw new RuntimeException("费用已存在结算");
			});
			BillDetail billDetail = new BillDetail();
			billDetail.setCurrency(fee.getCurrency());
			billDetail.setConvertCurrency(fee.getCurrency());
			billDetail.setFeeName(fee.getShortName());
			billDetail.setType(fee.getType());
			billDetail.setFeeId(fee.getId());
			billDetail.setInternalOrderNo(fee.getInternalOrderNo());
			billDetail.setConvertAmount(fee.getCurrentBillAmount());
			billDetail.setAmount(fee.getCurrentBillAmount());
			billDetail.setBillId(billId);
			billDetail.setDescription(fee.getDescription());
			billDetail.setCreateUser(user.getUserId());
			this_total[0] = this_total[0] + billDetail.getAmount();
			saveBillDetailList.add(billDetail);
		});
		billDetailList.forEach(item -> this_total[0] = this_total[0] + item.getAmount());
		if (bill.getAmount() < this_total[0]) bill.setAmount(this_total[0]);
		Collection<Fee> feeCollection = feeService.listByIds(feeIds);
		feeCollection.forEach(fee0 -> {
			feeList.forEach(fee1 -> {
				if (fee0.getId().longValue() == fee1.getId().longValue())
					fee0.setBillCreatedAmount(fee0.getBillCreatedAmount() + fee1.getCurrentBillAmount());
			});
			if (fee0.getBillCreatedAmount() > fee0.getAmount()) throw new RuntimeException("结算金额超过费用金额");
			fee0.setFeeStatus(FeeStatus.SETTLING);
		});
		if (!updateById(bill)) throw new RuntimeException("更新失败");
		if (!feeService.updateBatchById(feeCollection)) throw new RuntimeException("更新失败");
		if (!billDetailService.saveBatch(saveBillDetailList)) throw new RuntimeException("更新失败");
		return true;
	}

	@Transactional
	public boolean customRemoveBill(Long billId) {
		Bill bill = getById(billId);
		QueryWrapper<BillDetail> bd_qw = new QueryWrapper<>();
		bd_qw.eq("bill_id", billId);
		List<BillDetail> billDetailList = billDetailService.list(bd_qw);
		List<Long> feeIds = new ArrayList<>();
		billDetailList.forEach(item -> feeIds.add(item.getFeeId()));
		Collection<Fee> feeCollection = new ArrayList<>();
		if (!feeIds.isEmpty()) feeCollection = feeService.listByIds(feeIds);
		feeCollection.forEach(fee -> {
			billDetailList.forEach(billDetail -> {
				if (fee.getId().longValue() == billDetail.getFeeId().longValue()) {
					fee.setBillCreatedAmount(fee.getBillCreatedAmount() - billDetail.getAmount());
					if (bill.getBillStatus() == BillStatus.SETTLED || bill.getBillStatus() == BillStatus.ARCHIVED)
						fee.setSettledAmount(fee.getSettledAmount() - billDetail.getAmount());
				}
			});
		});
		billDetailService.remove(bd_qw);
		removeById(billId);
		if (!feeCollection.isEmpty()) feeService.updateBatchById(feeCollection);
		return true;
	}


	public void getStatement(List<Long> bankAccountIds, Bill bill, HttpServletResponse response) {
		bill = getById(bill.getId());
		Long bankAccountId = bankAccountIds.get(0);
		BankAccount bankAccount = bankAccountService.getById(bankAccountId);
		if (bankAccount == null) bankAccount = new BankAccount();
		QueryWrapper<Tenant> tenantQueryWrapper = new QueryWrapper<>();
		tenantQueryWrapper.eq("tenant_id", bill.getTenantId());
		Tenant tenant = tenantService.getOne(tenantQueryWrapper);
		if (tenant == null) throw new RuntimeException("没有租户数据");
		ClientData clientData = clientDataService.getById(bill.getSettlementUnit());
		Map<String, String> map = new HashMap<>();
		map.put("A1", excelDataJudgeWithoutTips(tenant.getCompanyChineseName()));
		map.put("A2", excelDataJudgeWithoutTips(tenant.getCompanyEnglishName()));
		map.put("A3", excelDataJudgeWithoutTips(tenant.getAddress()));
		String phone = StringUtils.isEmpty(tenant.getContactNumber()) ? "" : "Tel: " + tenant.getContactNumber();
		String email = StringUtils.isEmpty(tenant.getEmail()) ? "" : " Email: " + tenant.getEmail();
		String fax = StringUtils.isEmpty(tenant.getFax()) ? "" : " Fax: " + tenant.getFax();
		String a4 = phone + fax + email;

		map.put("A4", a4);
		map.put("A5", excelDataJudge(clientData.getFullName()));//对账公司全称
		map.put("A6", excelDataJudge(bill.getId()));//订单号
		map.put("A7", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));//出单日期
		map.put("A8", "");
		map.put("A9", "");
		map.put("A10", "");//预计开航 ETD:
		map.put("A11", "");//预计到达 ETA:
		map.put("A12", "");// 航空公司名
		map.put("A13", "");//件数加包装
		map.put("A14", ""); //重量
		map.put("A15", "");
		map.put("A16", "");//订舱号
		map.put("A17", "");
		Workbook workbook = CommonUtil.excelTemplate("/static/air_statement.xlsx", map);
		QueryWrapper<BillDetail> bd_qw = new QueryWrapper<>();
		bd_qw.eq("bill_id", bill.getId());
		List<BillDetail> billDetailList = billDetailService.list(bd_qw);
		List<Long> feeIds = new ArrayList<>();
		billDetailList.forEach(item -> feeIds.add(item.getFeeId()));


		QueryWrapper<Fee> fqw = new QueryWrapper<>();
		fqw.in("id", feeIds);
		List<Fee> feeList = new ArrayList<>();
		if(!feeIds.isEmpty())feeList=feeService.list(fqw);


		feeList.forEach(fee -> {
			billDetailList.forEach(bd -> {
				if (fee.getId().longValue() == bd.getFeeId().longValue()) fee.setAmount(bd.getAmount());
			});
		});


		Sheet sheet = workbook.getSheetAt(0);
		Sheet sheet_clone = workbook.cloneSheet(0);
		for(int i=24;i<36;i++)sheet.createRow(i);
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
			Cell cell=row.getCell(1);
			if(cell==null)cell=row.createCell(1);
			if (i == 27) row.getCell(1).setCellValue(excelDataJudge(bankAccount.getAccountName()));
			else if (i == 28) row.getCell(1).setCellValue(excelDataJudge(bankAccount.getBankName()));
			else if (i == 29) row.getCell(1).setCellValue(excelDataJudge(bankAccount.getAccountNumber()));
			write_to++;
		}


		workbook.removeSheetAt(1);
		CommonUtil.excelExport(workbook, response, bill.getId() + "_bill_statement");
	}


}
