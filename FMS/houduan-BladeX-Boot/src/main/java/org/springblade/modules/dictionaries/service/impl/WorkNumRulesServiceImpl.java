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
package org.springblade.modules.dictionaries.service.impl;

import lombok.AllArgsConstructor;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.dictionaries.entity.SerialNum;
import org.springblade.modules.dictionaries.entity.WorkNumRules;
import org.springblade.modules.dictionaries.enums.ZeroingType;
import org.springblade.modules.dictionaries.vo.WorkNumRulesVO;
import org.springblade.modules.dictionaries.mapper.WorkNumRulesMapper;
import org.springblade.modules.dictionaries.service.IWorkNumRulesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * 工作号规则 服务实现类
 *
 * @author BladeX
 * @since 2019-09-03
 */
@AllArgsConstructor
@Service
public class WorkNumRulesServiceImpl extends BaseServiceImpl<WorkNumRulesMapper, WorkNumRules> implements IWorkNumRulesService {
	SerialNumServiceImpl serialNumService;

	@Override
	public IPage<WorkNumRulesVO> selectWorkNumRulesPage(IPage<WorkNumRulesVO> page, WorkNumRulesVO workNumRules) {
		return page.setRecords(baseMapper.selectWorkNumRulesPage(page, workNumRules));
	}

	@Transactional
	public synchronized String getInternalOrderNo(WorkNumRulesServiceImpl service, Long id, String pod) {
		WorkNumRules d = service.getById(id);
		if (d == null) throw new RuntimeException("没有找到对应工作号");
		LocalDate d2 = LocalDate.now();
		LocalDate d1 = d.getLastCreateDate();
		if (d1 == null) d1 = d2;
		ZeroingType type = d.getZeroingType();
		int digit = d.getDigitOfYear();
		long next = d.getCurrentSerialNum() + 1;
		if (type == ZeroingType.DAY_ZEROING && d1.compareTo(d2) < 0) next = 0;
		if (type == ZeroingType.MONTH_ZEROING && (d2.getYear() > d1.getYear() || (d2.getYear() == d1.getYear() && d2.getMonth().getValue() == d1.getMonth().getValue())))
			next = 0;
		if (type == ZeroingType.YEAR_ZEROING && (d2.getYear() > d1.getYear())) next = 0;
		String year = String.valueOf(d2.getYear());
		StringBuilder month = new StringBuilder();
		StringBuilder day = new StringBuilder();
		if (digit == 0) year = "";
		if (digit == 1) year = year.substring(year.length() - 1, year.length());
		if (digit == 2) year = year.substring(year.length() - 2, year.length());
		if (digit == 4) year = year.substring(year.length() - 4, year.length());
		if (d.getRuleMonth()) {
			int m = d2.getMonthValue();
			if (m < 10) month.append(0);
			month.append(m);
		}
		if (d.getRuleDay()) {
			int d_v = d2.getDayOfMonth();
			if (d_v < 10) day.append(0);
			day.append(d_v);
		}
		StringBuilder ruleSerialNum = new StringBuilder();
		String next_string = String.valueOf(next);
		/**检查当前流水值是否到达最大*/
		StringBuilder max_serial = new StringBuilder();
		for (int i = 0; i < d.getSerialNumDigit(); i++) max_serial.append(9);
		Long max = Long.valueOf(max_serial.toString());
		if (next >= max) throw new RuntimeException("流水值到达最大值");
		for (int i = 0; i < (d.getSerialNumDigit() - next_string.length()); i++) ruleSerialNum.append(0);
		ruleSerialNum.append(next);
		if (!d.getRulePortDestination() || pod == null) pod = "";
		String internalOrderNo = d.getPrefix() + year + month + day + pod + ruleSerialNum;
		d.setCurrentSerialNum(next);
		d.setLastCreateDate(d2);
		SerialNum serialNum = new SerialNum();
		serialNum.setCurrentValue(next);
		serialNum.setDay(day.toString());
		serialNum.setMonth(month.toString());
		serialNum.setPrefix(d.getPrefix());
		if (digit == 1) serialNum.setOneDigitYear(year);
		if (digit == 2) serialNum.setTwoDigitYear(year);
		if (digit == 4) serialNum.setFourDigitYear(year);
		if (service.updateById(d) && serialNumService.save(serialNum)) return internalOrderNo;
		else throw new RuntimeException("工作号生成失败");
	}

}
