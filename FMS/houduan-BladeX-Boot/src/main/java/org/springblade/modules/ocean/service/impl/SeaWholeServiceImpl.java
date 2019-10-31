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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.businessManagement.entity.AirBusiness;
import org.springblade.modules.businessManagement.entity.BusinessFile;
import org.springblade.modules.businessManagement.entity.Fee;
import org.springblade.modules.businessManagement.service.impl.BusinessFileServiceImpl;
import org.springblade.modules.businessManagement.service.impl.FeeServiceImpl;
import org.springblade.modules.dictionaries.service.impl.WorkNumRulesServiceImpl;
import org.springblade.modules.ocean.entity.BillOfLading;
import org.springblade.modules.ocean.entity.SeaWhole;
import org.springblade.modules.ocean.entity.SeparateLading;
import org.springblade.modules.ocean.vo.SeaWholeVO;
import org.springblade.modules.ocean.mapper.SeaWholeMapper;
import org.springblade.modules.ocean.service.ISeaWholeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;
import org.springblade.core.mp.base.BaseServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *  服务实现类
 *
 * @author BladeX
 * @since 2019-09-17
 */
@AllArgsConstructor
@Service
public class SeaWholeServiceImpl extends BaseServiceImpl<SeaWholeMapper, SeaWhole> implements ISeaWholeService {
	private WorkNumRulesServiceImpl workNumRulesService;
	private FeeServiceImpl feeService;
	private BillOfLadingServiceImpl billOfLadingService;
	private BusinessFileServiceImpl businessFileService;
	private SeparateLadingServiceImpl separateLadingService;

	@Transactional
	public  Boolean allRemoveById(String id) {
		List<Long> ids = Func.toLongList(id);
		System.out.println(ids);
		Collection<SeaWhole> seaWholeCollection = this.listByIds(ids);
		List<String> internalList = new ArrayList<>();
		for (SeaWhole item : seaWholeCollection) internalList.add(item.getInternalOrderNo());
		System.out.println(internalList);
		boolean err = true;
      if(internalList.size()>0){
	QueryWrapper<Fee> fqw = new QueryWrapper<>();
	fqw.in("internal_order_no", internalList);
	List<Fee> feeList = feeService.list(fqw);
	System.out.println(feeList);

	System.out.println(feeList);
	QueryWrapper<BusinessFile> bfqw = new QueryWrapper<>();
	bfqw.in("internal_order_no", internalList);
	List<BusinessFile> businessFileList = businessFileService.list(bfqw);
	System.out.println(businessFileList);

	QueryWrapper<BillOfLading> bol = new QueryWrapper<>();
	bol.in("internal_order_no", internalList);
	List<BillOfLading>  billOfLadingList = billOfLadingService.list(bol);

	QueryWrapper<SeparateLading> sl = new QueryWrapper<>();
	sl.in("internal_order_no", internalList);
	List<SeparateLading>  separateLadingList = separateLadingService.list(sl);
	List<Long> fileIdList = new ArrayList<>(businessFileList.size());
	List<String> feeIdList = new ArrayList<>(feeList.size());
	List<Long> billOfLadingIdList = new ArrayList<>(billOfLadingList.size());
	List<Long> separateLadingIdList = new ArrayList<>(separateLadingList.size());

	for (Fee i : feeList) feeIdList.add(i.getId().toString());
	for (BusinessFile i : businessFileList) fileIdList.add(i.getId());
	for (BillOfLading i : billOfLadingList) billOfLadingIdList.add(i.getId());
	for (SeparateLading i : separateLadingList) separateLadingIdList.add(i.getId());

	if (ids.size() > 0 && !removeByIds(ids)) err = false;
	if (feeIdList.size() > 0 && !feeService.removeByIds(feeIdList)) err = false;
	if (billOfLadingIdList.size() > 0 && !billOfLadingService.removeByIds(billOfLadingIdList)) err = false;
	if (separateLadingIdList.size() > 0 && !separateLadingService.removeByIds(separateLadingIdList)) err = false;
	if (fileIdList.size() > 0 && !businessFileService.removeFiles(StringUtils.join(fileIdList.toArray(), ","))) err = false;
   }else{
		  if (ids.size() > 0 && !removeByIds(ids)) err = false;
	  }
		return err;
	}

	@Override
	public IPage<SeaWholeVO> selectSeaWholePage(IPage<SeaWholeVO> page, SeaWholeVO seaWhole) {
		return page.setRecords(baseMapper.selectSeaWholePage(page, seaWhole));
	}

	@Transactional
	public Boolean saveData(SeaWhole seaWhole) {
		String internalOrderNo = workNumRulesService.getInternalOrderNo(workNumRulesService, Long.valueOf(seaWhole.getInternalOrderNo()),seaWhole.getDestinationPort());
		seaWhole.setInternalOrderNo(internalOrderNo);
		if (!save(seaWhole)) throw new RuntimeException("保存失败");
		return true;
	}

	@Override
	public SeaWhole getByBusinessId(Long id) {
		return baseMapper.getByBusinessId(Func.toLong(id));
	}

}
