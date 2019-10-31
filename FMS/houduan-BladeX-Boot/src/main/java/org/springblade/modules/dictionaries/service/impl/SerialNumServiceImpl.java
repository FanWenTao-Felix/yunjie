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

import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.dictionaries.entity.SerialNum;
import org.springblade.modules.dictionaries.vo.SerialNumVO;
import org.springblade.modules.dictionaries.mapper.SerialNumMapper;
import org.springblade.modules.dictionaries.service.ISerialNumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 流水号表 服务实现类
 *
 * @author BladeX
 * @since 2019-08-26
 */
@Service
public class SerialNumServiceImpl extends BaseServiceImpl<SerialNumMapper, SerialNum> implements ISerialNumService {

	@Override
	public IPage<SerialNumVO> selectSerialNumPage(IPage<SerialNumVO> page, SerialNumVO serialNum) {
		return page.setRecords(baseMapper.selectSerialNumPage(page, serialNum));
	}

}
