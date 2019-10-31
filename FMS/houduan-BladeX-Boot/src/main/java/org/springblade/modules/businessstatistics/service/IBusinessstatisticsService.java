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
package org.springblade.modules.businessstatistics.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springblade.modules.businessstatistics.entity.Businessstatistics;
import org.springblade.modules.businessstatistics.vo.BusinessstatisticsVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 *  服务类
 *
 * @author BladeX
 * @since 2019-09-23
 */
public interface IBusinessstatisticsService extends IService<Businessstatistics> {


	IPage<BusinessstatisticsVO> selectSalesmanagementPage(IPage<BusinessstatisticsVO> page, QueryWrapper<Businessstatistics> queryWrapper);

	String keygetValue(Map<String,Object> map,String key);

	List<LocalDateTime> arrayToString(Map<String,Object> sales);

	String getclient(Map<String,Object> value);
	String gettype(Map<String,Object> value);



}
