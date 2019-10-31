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

import org.springblade.modules.dictionaries.entity.AirFreightInfo;
import org.springblade.modules.dictionaries.vo.AirFreightInfoVO;
import org.springblade.modules.dictionaries.mapper.AirFreightInfoMapper;
import org.springblade.modules.dictionaries.service.IAirFreightInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 *  服务实现类
 *
 * @author BladeX
 * @since 2019-09-05
 */
@Service
public class AirFreightInfoServiceImpl extends ServiceImpl<AirFreightInfoMapper, AirFreightInfo> implements IAirFreightInfoService {

	@Override
	public IPage<AirFreightInfoVO> selectAirFreightInfoPage(IPage<AirFreightInfoVO> page, AirFreightInfoVO airFreightInfo) {
		return page.setRecords(baseMapper.selectAirFreightInfoPage(page, airFreightInfo));
	}

	@Override
	public AirFreightInfo selectAirFreightInfo(AirFreightInfo airFreightInfo) {
		return baseMapper.selectAirFreightInfo(airFreightInfo);
	}

	@Override
	public AirFreightInfo getInfo(AirFreightInfo sPort) {
		return baseMapper.getInfo(sPort);
	}

}
