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
package org.springblade.modules.clientManagement.service.impl;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.secure.BladeUser;
import org.springblade.modules.clientManagement.entity.CompanyType;
import org.springblade.modules.clientManagement.vo.CompanyTypeVO;
import org.springblade.modules.clientManagement.mapper.CompanyTypeMapper;
import org.springblade.modules.clientManagement.service.ICompanyTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 公司类型 服务实现类
 *
 * @author BladeX
 * @since 2019-08-20
 */
@Service
public class CompanyTypeServiceImpl extends BaseServiceImpl<CompanyTypeMapper, CompanyType> implements ICompanyTypeService {

	@Override
	public IPage<CompanyTypeVO> selectCompanyTypePage(IPage<CompanyTypeVO> page, CompanyTypeVO companyType) {
		return page.setRecords(baseMapper.selectCompanyTypePage(page, companyType));
	}

	public Boolean batchImport(MultipartFile file, BladeUser bladeUser) {
		if (bladeUser == null) return false;
		try {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
			int rowNum = xssfSheet.getLastRowNum() + 1;
			XSSFRow firstRow = xssfSheet.getRow(0);
			List<CompanyType> ct_list = new ArrayList<>(rowNum);
			if (!(firstRow.getCell(0).getStringCellValue().equals("类型代码") && firstRow.getCell(1).getStringCellValue().equals("类型名称")))
				return false;
			for (int i = 1; i < rowNum; i++) {
				XSSFRow xssfRow = xssfSheet.getRow(i);
				CompanyType ct = new CompanyType();
				ct.setCode(xssfRow.getCell(0).getStringCellValue());
				ct.setName(xssfRow.getCell(1).getStringCellValue());
				ct.setCreateTime(new Date());
				ct.setCreateUser(bladeUser.getUserId());
				ct.setUpdateUser(bladeUser.getUserId());
				ct.setCreateDept(Long.valueOf(bladeUser.getDeptId()));
				ct.setUpdateTime(new Date());
				ct_list.add(ct);
			}
			if (!ct_list.isEmpty()) return this.saveBatch(ct_list, 100);
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return false;
	}
}
