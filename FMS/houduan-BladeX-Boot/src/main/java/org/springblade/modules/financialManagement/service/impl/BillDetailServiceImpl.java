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
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.businessManagement.entity.Fee;
import org.springblade.modules.businessManagement.service.IFeeService;
import org.springblade.modules.businessManagement.service.impl.FeeServiceImpl;
import org.springblade.modules.dictionaries.entity.Parities;
import org.springblade.modules.dictionaries.service.impl.ParitiesServiceImpl;
import org.springblade.modules.financialManagement.entity.Bill;
import org.springblade.modules.financialManagement.entity.BillDetail;
import org.springblade.modules.financialManagement.mapper.BillMapper;
import org.springblade.modules.financialManagement.vo.BillDetailVO;
import org.springblade.modules.financialManagement.mapper.BillDetailMapper;
import org.springblade.modules.financialManagement.service.IBillDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 账单明细 服务实现类
 *
 * @author BladeX
 * @since 2019-10-16
 */
@Service
@AllArgsConstructor
public class BillDetailServiceImpl extends BaseServiceImpl<BillDetailMapper, BillDetail> implements IBillDetailService {

	IFeeService feeService;
	BillMapper billMapper;
	ParitiesServiceImpl paritiesService;

	@Override
	public IPage<BillDetailVO> selectBillDetailPage(IPage<BillDetailVO> page, BillDetailVO billDetail) {
		return page.setRecords(baseMapper.selectBillDetailPage(page, billDetail));
	}

	public boolean customSaveOrderUpdate(BillDetail billDetail) {
		Bill bill = billMapper.selectById(billDetail.getBillId());
		Fee fee = feeService.getById(billDetail.getFeeId());
		if (billDetail.getCurrency() == null) throw new RuntimeException("结算货币不能为空");
		billDetail.setConvertCurrency(bill.getCurrency());
		if (billDetail.getCurrency() != bill.getCurrency()) throw new RuntimeException("账单货币与结算明细货币不一致");
		if (fee == null) {
			billDetail.setConvertAmount(billDetail.getAmount());
		} else if (fee.getCurrency() != bill.getCurrency()) {
			billDetail.setCurrency(fee.getCurrency());
			QueryWrapper<Parities> p_qw = new QueryWrapper<>();
			p_qw.lambda().eq(Parities::getOriginalCurrency, billDetail.getCurrency()).eq(Parities::getNightMoney, billDetail.getConvertCurrency()).gt(Parities::getEffectiveEnd, fee.getCreateTime()).orderByAsc(Parities::getEffectiveEnd).last("limit 1");
			Parities parities = paritiesService.getOne(p_qw);
			if (parities == null)
				throw new RuntimeException("没有找到" + fee.getCreateTime() + "的汇率" + billDetail.getConvertCurrency() + " to " + billDetail.getConvertCurrency());
			double exchange_rate = parities.getParities() / (double) parities.getCardinalNumber();
			billDetail.setConvertAmount(billDetail.getAmount() * exchange_rate);
		} else if (fee.getCurrency() == bill.getCurrency()) {
			billDetail.setConvertCurrency(bill.getCurrency());
			billDetail.setConvertAmount(billDetail.getAmount());
		}
		if (!saveOrUpdate(billDetail)) throw new RuntimeException("结算明细更新失败");
		return true;
	}

	@Transactional
	public boolean customRemoveByIds(String ids) {
		List<Long> idList = Func.toLongList(ids);
		if (idList.isEmpty()) throw new RuntimeException("至少选择一条结算明细");
		Collection<BillDetail> billDetailCollection = listByIds(idList);
		List<Long> feeIds = new ArrayList<>(billDetailCollection.size());
		billDetailCollection.forEach(item -> feeIds.add(item.getFeeId()));
		Collection<Fee> feeCollection = feeService.listByIds(feeIds);
		feeCollection.forEach(fee -> {
			billDetailCollection.forEach(billDetail -> {
				if (billDetail.getFeeId().longValue() == fee.getId().longValue()) {
					fee.setBillCreatedAmount(fee.getBillCreatedAmount() - billDetail.getAmount());
				}
			});
		});
		if (!feeService.updateBatchById(feeCollection)) throw new RuntimeException("更新失败");
		if (!removeByIds(idList)) throw new RuntimeException("更新失败");
		return true;
	}


}
