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
package org.springblade.modules.ocean.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springblade.modules.businessManagement.entity.Fee;
import org.springblade.modules.businessManagement.enums.FeeStatus;
import org.springblade.modules.businessManagement.enums.ReceivablePayableType;
import org.springblade.modules.ocean.entity.SeaFee;
import org.springblade.modules.ocean.vo.SeaFeeVO;
import org.springblade.modules.ocean.mapper.SeaFeeMapper;
import org.springblade.modules.ocean.service.ISeaFeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;
import org.springblade.core.mp.base.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 *  服务实现类
 *
 * @author BladeX
 * @since 2019-09-19
 */
@Service
public class SeaFeeServiceImpl extends BaseServiceImpl<SeaFeeMapper, SeaFee> implements ISeaFeeService {

	@Override
	public IPage<SeaFeeVO> selectSeaFeePage(IPage<SeaFeeVO> page, SeaFeeVO seaFee) {
		return page.setRecords(baseMapper.selectSeaFeePage(page, seaFee));
	}


	@Transactional
	public Boolean createPayable(List<Long> ids) {
		QueryWrapper<SeaFee> qw = new QueryWrapper<>();
		qw.eq("type", 0);
		qw.in("id", ids);
		List<SeaFee> list = this.list(qw);
		List<SeaFee> payList = new ArrayList<>();
		for (SeaFee item : list) {
			SeaFee data = new SeaFee();
			data.setShortName(item.getShortName());
			data.setAmount(item.getAmount());
			data.setChineseName(item.getChineseName());
			data.setEnglishName(item.getEnglishName());
			data.setCode(item.getCode());
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

}
