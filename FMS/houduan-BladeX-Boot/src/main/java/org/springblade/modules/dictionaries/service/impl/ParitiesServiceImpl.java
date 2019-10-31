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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springblade.modules.businessManagement.entity.Fee;
import org.springblade.modules.businessManagement.service.impl.AirBusinessServiceImpl;
import org.springblade.modules.businessManagement.service.impl.FeeServiceImpl;
import org.springblade.modules.dictionaries.entity.Currency;
import org.springblade.modules.dictionaries.entity.Parities;
import org.springblade.modules.dictionaries.enums.CustomCurrency;
import org.springblade.modules.dictionaries.mapper.ParitiesMapper;
import org.springblade.modules.dictionaries.service.IParitiesService;
import org.springblade.modules.dictionaries.vo.ParitiesVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 服务实现类1
 *
 * @author BladeX
 * @since 2019-08-13
 */
@Service
@AllArgsConstructor
public class ParitiesServiceImpl extends ServiceImpl<ParitiesMapper, Parities> implements IParitiesService {

	@Override
	public IPage<ParitiesVO> selectParitiesPage(IPage<ParitiesVO> page, ParitiesVO parities) {
		return page.setRecords(baseMapper.selectParitiesPage(page, parities));
	}

}
