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

import org.springblade.modules.ocean.entity.BillOfLading;
import org.springblade.modules.ocean.vo.BillOfLadingVO;
import org.springblade.modules.ocean.mapper.BillOfLadingMapper;
import org.springblade.modules.ocean.service.IBillOfLadingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.base.BaseServiceImpl;

/**
 *  服务实现类
 *
 * @author BladeX
 * @since 2019-09-29
 */
@Service
public class BillOfLadingServiceImpl extends BaseServiceImpl<BillOfLadingMapper, BillOfLading> implements IBillOfLadingService {

	@Override
	public IPage<BillOfLadingVO> selectBillOfLadingPage(IPage<BillOfLadingVO> page, BillOfLadingVO billOfLading) {
		return page.setRecords(baseMapper.selectBillOfLadingPage(page, billOfLading));
	}

	@Override
	public BillOfLading getInternalOrderNo(String internalOrderNo) {
		return baseMapper.getInternalOrderNo(internalOrderNo);
	}



}
