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

import org.apache.poi.xssf.usermodel.*;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.secure.BladeUser;
import org.springblade.modules.clientManagement.entity.ClientData;
import org.springblade.modules.clientManagement.enums.ClientType;
import org.springblade.modules.clientManagement.vo.ClientDataVO;
import org.springblade.modules.clientManagement.mapper.ClientDataMapper;
import org.springblade.modules.clientManagement.service.IClientDataService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 客户资料 服务实现类
 *
 * @author BladeX
 * @since 2019-08-20
 */
@Service
public class ClientDataServiceImpl extends BaseServiceImpl<ClientDataMapper, ClientData> implements IClientDataService {

	@Override
	public IPage<ClientDataVO> selectClientDataPage(IPage<ClientDataVO> page, ClientDataVO clientData) {
		return page.setRecords(baseMapper.selectClientDataPage(page, clientData));
	}

	@Transactional
	public Boolean batchImport(MultipartFile file, BladeUser bladeUser) {
		if (bladeUser == null) return false;
		if(true)throw new RuntimeException("暂时不开放功能");
		try {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
			int rowNum = xssfSheet.getLastRowNum() + 1;
			XSSFRow firstRow = xssfSheet.getRow(0);
			Date now = new Date();
			List<ClientData> cd_list = new ArrayList<>(rowNum);
			for (int i = 1; i < rowNum; i++) {
				XSSFRow xssfRow = xssfSheet.getRow(i);
				ClientData cd = new ClientData();
				String shortName = CommonUtil.getCellValue(xssfRow.getCell(0));
				String fullName = CommonUtil.getCellValue(xssfRow.getCell(1));
				String englishName = CommonUtil.getCellValue(xssfRow.getCell(2));
				String contact = CommonUtil.getCellValue(xssfRow.getCell(3));
				String fax = CommonUtil.getCellValue(xssfRow.getCell(4));
				String phone = CommonUtil.getCellValue(xssfRow.getCell(5));
				String chineseAddress = CommonUtil.getCellValue(xssfRow.getCell(6));
				String englishAddress = CommonUtil.getCellValue(xssfRow.getCell(7));
				String email = CommonUtil.getCellValue(xssfRow.getCell(8));
                cd.setShortName(shortName);
                cd.setFullName(fullName);
                cd.setEnglishName(englishName);
                cd.setContactNumber(contact);
				cd.setFaxNumber(fax);
				cd.setPhone(phone);
				cd.setChineseAddress(chineseAddress);
				cd.setEnglishAddress(englishAddress);
				cd.setEMail(email);

				cd.setCreateUser(bladeUser.getUserId());
				cd.setCreateTime(now);
				cd.setTenantId(bladeUser.getTenantId());
				cd.setCreateDept(Long.valueOf(bladeUser.getDeptId()));
				cd.setClientType(ClientType.CLIENT_SUPPLIER);
				cd_list.add(cd);
			}
			if (!cd_list.isEmpty()) return this.saveBatch(cd_list, 100);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public void batchExport(HttpServletResponse response) {
		List<ClientData> cd = this.list();
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		map.put("shortName", "客户简称");
		map.put("fullName", "全称");
		map.put("englishName", "英文名");
		map.put("contactNumber", "联系电话");
		map.put("faxNumber", "传真机");
		map.put("postalCode", "邮政编码");
		map.put("phone", "手机");
		map.put("chineseAddress", "中文地址");
		map.put("englishAddress", "英文地址");
		map.put("webAddress", "网站地址");
		map.put("eMail", "E_Mail");
		map.put("country", "国家");
		map.put("district", "所属地区");
		map.put("city", "所属城市");
		map.put("creditLevel", "信誉等级");
		map.put("unitCode", "单位代码");
		map.put("businessType", "业务类型");
		map.put("clientSource", "客户来源");
		map.put("clientGroup", "客户分组");
		map.put("clientType", "客户类型");
		CommonUtil.excelExport(CommonUtil.commonExcelExport(map, cd, "客户资料"), response, null);
	}

}
