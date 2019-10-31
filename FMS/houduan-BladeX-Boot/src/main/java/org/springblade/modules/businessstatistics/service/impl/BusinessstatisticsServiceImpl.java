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
package org.springblade.modules.businessstatistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springblade.modules.businessstatistics.entity.Businessstatistics;
import org.springblade.modules.businessstatistics.vo.BusinessstatisticsVO;
import org.springblade.modules.businessstatistics.mapper.BusinessstatisticsMapper;
import org.springblade.modules.businessstatistics.service.IBusinessstatisticsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *  服务实现类
 *
 * @author BladeX
 * @since 2019-09-23
 */
@Service
public class BusinessstatisticsServiceImpl extends ServiceImpl<BusinessstatisticsMapper, Businessstatistics> implements IBusinessstatisticsService {

	@Override
	public IPage<BusinessstatisticsVO> selectSalesmanagementPage(IPage<BusinessstatisticsVO> page, QueryWrapper<Businessstatistics> queryWrapper) {
		return page.setRecords(baseMapper.selectSalesmanagementPage(page,queryWrapper));
	}




	public List<LocalDateTime> arrayToString(Map<String,Object> sales){
		Object b;
		Object e;
		LocalDateTime begindate=null;
		LocalDateTime enddate=null;
		for (Map.Entry<String, Object> entry : sales.entrySet()) {
			if (entry.getKey().equals("begindate")) {
				b = entry.getValue();
				DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime begin = LocalDateTime.parse(b+" 00:00:00",df);
				begindate=begin;
			} else if(entry.getKey().equals("enddate")){
				e = entry.getValue();
				DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime end = LocalDateTime.parse(e+" 00:00:00",df);
				enddate=end;
			}
		}
		List<LocalDateTime> list=new ArrayList<>();
		list.add(begindate);
		list.add(enddate);
		return list;
	}

	@Override
	public String getclient(Map<String, Object> value) {
		String client=null;
		for (Map.Entry<String, Object> entry : value.entrySet()) {
			if (entry.getKey().equals("client")) {
				client= (String) entry.getValue();
			}
		}
		return client;
	}
	@Override
	public String gettype(Map<String, Object> value) {
		String type=null;
		for (Map.Entry<String, Object> entry : value.entrySet()) {
			if (entry.getKey().equals("tranType")) {
				type= (String) entry.getValue();
			}
		}
		return type;
	}


	public String keygetValue(Map<String,Object> map, String key){
		String value="";
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry1 = (Map.Entry) it.next();
			Object obj = entry1.getKey();
			if (obj != null && obj.equals(key)) {
				value= (String) entry1.getValue();
			}
		}
		return value;
	}



}
