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
package org.springblade.modules.financialManagement.service;

import org.springblade.modules.financialManagement.entity.Bill;
import org.springblade.modules.financialManagement.vo.BillVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 账单 服务类
 *
 * @author BladeX
 * @since 2019-10-16
 */
public interface IBillService extends IService<Bill> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param bill
	 * @return
	 */
	IPage<BillVO> selectBillPage(IPage<BillVO> page, BillVO bill);

}
