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
package org.springblade.modules.businessManagement.mapper;

import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.support.Query;
import org.springblade.modules.businessManagement.entity.Fee;
import org.springblade.modules.businessManagement.vo.FeeVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.financialManagement.entity.ReceivablePayable;
import org.springblade.modules.financialManagement.vo.ReceivablePayableVo;

import java.util.List;

/**
 * 费用表 Mapper 接口
 *
 * @author BladeX
 * @since 2019-09-09
 */
public interface FeeMapper extends BaseMapper<Fee> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param fee
	 * @return
	 */
	List<FeeVO> selectFeePage(IPage page, FeeVO fee);

	List<ReceivablePayableVo> selectReceivablePayableList(@Param("r") ReceivablePayable r, @Param("q") Query q);

	Long selectReceivablePayableCount(@Param("r") ReceivablePayable r, @Param("q") Query q);

	List<Fee> getFeeList(@Param("f") Fee f, @Param("q") Query q);

	Long getFeeCount(@Param("f")Fee f,@Param("q")Query q);

	List<Fee> getInternalOrderNo(String internalOrderNo);
	List<Fee> getUnSettledFee(@Param("fee")Fee fee);
}
