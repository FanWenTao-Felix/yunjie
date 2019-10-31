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
package org.springblade.modules.businessstatistics.wrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.modules.businessstatistics.entity.Businessstatistics;
import org.springblade.modules.businessstatistics.vo.BusinessstatisticsVO;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author BladeX
 * @since 2019-09-23
 */
public class BusinessstatisticsWrapper extends BaseEntityWrapper<Businessstatistics, BusinessstatisticsVO>  {

	public static BusinessstatisticsWrapper build() {
		return new BusinessstatisticsWrapper();
 	}

	@Override
	public BusinessstatisticsVO entityVO(Businessstatistics businessstatistics) {
		BusinessstatisticsVO salesmanagementVO = BeanUtil.copy(businessstatistics, BusinessstatisticsVO.class);

		return salesmanagementVO;
	}

}
