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
package org.springblade.modules.financialManagement.service.impl;

import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.financialManagement.entity.BankAccount;
import org.springblade.modules.financialManagement.vo.BankAccountVO;
import org.springblade.modules.financialManagement.mapper.BankAccountMapper;
import org.springblade.modules.financialManagement.service.IBankAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 银行账号 服务实现类
 *
 * @author BladeX
 * @since 2019-09-23
 */
@Service
public class BankAccountServiceImpl extends BaseServiceImpl<BankAccountMapper, BankAccount> implements IBankAccountService {

	@Override
	public IPage<BankAccountVO> selectBankAccountPage(IPage<BankAccountVO> page, BankAccountVO bankAccount) {
		return page.setRecords(baseMapper.selectBankAccountPage(page, bankAccount));
	}

}
