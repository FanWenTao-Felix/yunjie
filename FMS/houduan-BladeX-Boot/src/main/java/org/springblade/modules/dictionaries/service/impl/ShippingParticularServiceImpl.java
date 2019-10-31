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

import org.springblade.core.tool.utils.Func;
import org.springblade.modules.dictionaries.entity.ShippingParticular;
import org.springblade.modules.dictionaries.vo.ShippingParticularVO;
import org.springblade.modules.dictionaries.mapper.ShippingParticularMapper;
import org.springblade.modules.dictionaries.service.IShippingParticularService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 *  服务实现类
 *
 * @author BladeX
 * @since 2019-09-09
 */
@Service
public class ShippingParticularServiceImpl extends ServiceImpl<ShippingParticularMapper, ShippingParticular> implements IShippingParticularService {

	@Override
	public IPage<ShippingParticularVO> selectShippingParticularPage(IPage<ShippingParticularVO> page, ShippingParticularVO shippingParticular) {
		return page.setRecords(baseMapper.selectShippingParticularPage(page, shippingParticular));
	}

	@Override
	public List<Long> selectInfoId(String ids) {
		return baseMapper.selectInfoId(Func.toStrArray(ids));
	}

	@Override
	public ShippingParticular selectShippingParticular(ShippingParticular shippingParticular) {
		return baseMapper.selectShippingParticular(shippingParticular);
	}

}
