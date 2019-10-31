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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Scope;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.businessManagement.entity.Fee;
import org.springblade.modules.businessManagement.enums.FeeStatus;
import org.springblade.modules.businessManagement.enums.ReceivablePayableType;
import org.springblade.modules.businessManagement.vo.FeeVO;
import org.springblade.modules.businessManagement.mapper.FeeMapper;
import org.springblade.modules.businessManagement.service.IFeeService;
import org.springblade.modules.dictionaries.entity.Parities;
import org.springblade.modules.dictionaries.enums.CustomCurrency;
import org.springblade.modules.dictionaries.service.IParitiesService;
import org.springblade.modules.dictionaries.service.impl.ParitiesServiceImpl;
import org.springblade.modules.financialManagement.entity.Bill;
import org.springblade.modules.financialManagement.entity.BillDetail;
import org.springblade.modules.financialManagement.enums.BillStatus;
import org.springblade.modules.financialManagement.service.IBillDetailService;
import org.springblade.modules.financialManagement.service.IBillService;
import org.springblade.modules.financialManagement.service.impl.BillDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 费用表 服务实现类
 *
 * @author BladeX
 * @since 2019-09-09
 */
@Service
public class FeeServiceImpl extends BaseServiceImpl<FeeMapper, Fee> implements IFeeService {
	@Autowired
	private ParitiesServiceImpl paritiesService;
	@Autowired
	@Lazy
	private IBillDetailService billDetailService;
	@Lazy
	@Autowired
	private IBillService billService;

	@Override
	public IPage<FeeVO> selectFeePage(IPage<FeeVO> page, FeeVO fee) {
		return page.setRecords(baseMapper.selectFeePage(page, fee));
	}

	@Transactional
	public Boolean createPayable(List<Long> ids) {
		QueryWrapper<Fee> qw = new QueryWrapper<>();
		qw.eq("type", 0);
		qw.in("id", ids);
		List<Fee> list = this.list(qw);
		List<Fee> payList = new ArrayList<>();
		for (Fee item : list) {
			Fee data = new Fee();
			data.setShortName(item.getShortName());
			data.setAmount(item.getAmount());
			data.setChineseName(item.getChineseName());
			data.setEnglishName(item.getEnglishName());
			data.setCode(item.getCode());
			data.setCreateUser(item.getCreateUser());
			data.setCreateTime(new Date());
			data.setCreateDept(item.getCreateDept());
			data.setCurrency(item.getCurrency());
			data.setFeeStatus(FeeStatus.UNAUDITING);
			data.setInternalOrderNo(item.getInternalOrderNo());
			data.setQuantity(item.getQuantity());
			data.setType(ReceivablePayableType.PAYABLE);
			data.setUnitPrice(item.getUnitPrice());
			data.setChargeUnit(item.getChargeUnit());
			data.setSettlementUnit(-1l);
			payList.add(data);
		}
		if (!saveBatch(payList)) throw new RuntimeException("生成失败");
		return true;
	}


	public List<Fee> getInternalOrderNo(String internalOrderNo) {
		return baseMapper.getInternalOrderNo(internalOrderNo);
	}

	/**
	 * 汇率转换成人民币
	 *
	 * @param feeList
	 * @return
	 */
	public static List<Fee> currencyExchange(List<Fee> feeList, IParitiesService paritiesService) {
		if (feeList.isEmpty()) return feeList;
		List<CustomCurrency> currencyList = new ArrayList<>();
		Date the_earliest_fee_date = feeList.get(0).getCreateTime();
		for (Fee fee : feeList) {
			if (fee.getCreateTime().getTime() < the_earliest_fee_date.getTime())
				the_earliest_fee_date = fee.getCreateTime();
			if (fee.getCurrency() != CustomCurrency.CNY) currencyList.add(fee.getCurrency());
		}
		if (currencyList.isEmpty()) {
			feeList.forEach(fee -> {
				fee.setConvertAmount(fee.getAmount());
				fee.setConvertCurrency(fee.getCurrency());
			});
			return feeList;
		}
		try {
			Date[] dates = new Date[1];
			dates[0] = the_earliest_fee_date;
			QueryWrapper<Parities> p_qw = new QueryWrapper<>();
			p_qw.lambda().in(Parities::getOriginalCurrency, currencyList).and(obj -> obj.eq(Parities::getNightMoney, CustomCurrency.CNY).gt(Parities::getEffectiveEnd, dates[0]));
			List<Parities> paritiesList = paritiesService.list(p_qw);
			for (Fee fee : feeList) {
				if (fee.getCurrency() == CustomCurrency.CNY) {
					fee.setConvertAmount(fee.getAmount());
					fee.setConvertCurrency(fee.getCurrency());
				} else {
					Parities p = null;
					for (Parities p_item : paritiesList)
						if (CommonUtil.getCurrencyByString(p_item.getOriginalCurrency()) == fee.getCurrency() && fee.getCreateTime().getTime() >= p_item.getValidStart().getTime() && fee.getCreateTime().getTime() <= p_item.getEffectiveEnd().getTime())
							p = p_item;
					if (p == null)
						throw new RuntimeException("没有找到" + fee.getCreateTime() + "--" + fee.getCurrency() + "转换成成" + CustomCurrency.CNY + "的汇率");
					double exchangeRate = p.getParities() / (double) p.getCardinalNumber();
					fee.setConvertCurrency(CustomCurrency.CNY);
					fee.setConvertAmount(fee.getAmount() * exchangeRate);
				}
			}
		} catch (Exception e) {
			feeList.forEach(fee -> {
				fee.setConvertCurrency(fee.getCurrency());
				fee.setConvertAmount(0d);
			});
		}
		return feeList;
	}

	/**
	 * 计算已结算金额
	 *
	 * @param feeList
	 * @param billDetailService
	 * @return
	 */
	public static List<Fee> countPayedAmount(List<Fee> feeList, IBillDetailService billDetailService, IBillService billService) {
		if (feeList.isEmpty()) return feeList;
		List<Long> feeIds = new ArrayList<>();
		for (Fee fee : feeList) feeIds.add(fee.getId());
		QueryWrapper<BillDetail> bd_qw = new QueryWrapper<>();
		bd_qw.in("fee_id", feeIds);
		List<BillDetail> billDetailList = billDetailService.list(bd_qw);
		List<Long> billIds = new ArrayList<>();
		billDetailList.forEach(i -> billIds.add(i.getBillId()));
		QueryWrapper<Bill> b_qw = new QueryWrapper<>();
		b_qw.in("id", billIds);
		List<Bill> billList = new ArrayList<>();
		if (!billIds.isEmpty()) billList = billService.list(b_qw);
		billList.forEach(bill -> {
			billDetailList.forEach(billDetail -> {
				if (bill.getId().longValue() == billDetail.getBillId().longValue())
					billDetail.setBillStatus(bill.getBillStatus());
			});
		});

		for (Fee fee : feeList) {
			double sa = 0d;
			double ba = 0d;
			for (BillDetail billDetail : billDetailList) {
				if (fee.getId().longValue() == billDetail.getFeeId().longValue()) {
					ba += billDetail.getAmount();
					if (billDetail.getBillStatus() == BillStatus.SETTLED || billDetail.getBillStatus() == BillStatus.ARCHIVED)
						sa += billDetail.getAmount();
				}
			}
			fee.setSettledAmount(sa);
			fee.setBillCreatedAmount(ba);
		}
		return feeList;
	}

	public IPage<Fee> getFeePageAfterCount(Fee fee, Query query) {
		QueryWrapper<Fee> qw = Condition.getQueryWrapper(fee);
		qw.orderByAsc("type");
		IPage<Fee> pages = page(Condition.getPage(query), qw);
		currencyExchange(pages.getRecords(), paritiesService);
		countPayedAmount(pages.getRecords(), billDetailService, billService);
		return pages;
	}

	@Transactional
	public boolean customRemoveByIds(String ids) {
		List<Long> idList = Func.toLongList(ids);
		if (idList.isEmpty()) return true;
		QueryWrapper<BillDetail> bd_qw = new QueryWrapper<>();
		bd_qw.in("fee_id", idList);
		List<BillDetail> bdList = billDetailService.list(bd_qw);
		Set<Long> billIdSet = new HashSet<>();
		bdList.forEach(item -> billIdSet.add(item.getBillId()));
		Collection<Bill> billCollection = billService.listByIds(billIdSet);
		billCollection.forEach(item -> {
			if (item.getBillStatus() != BillStatus.UNARCHIVE) throw new RuntimeException("账单存在已结算费用");
		});
		billDetailService.remove(bd_qw);
		removeByIds(idList);
		return true;
	}
}
