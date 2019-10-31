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

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.secure.BladeUser;
import org.springblade.modules.clientManagement.entity.ClientData;
import org.springblade.modules.clientManagement.entity.Supplier;
import org.springblade.modules.clientManagement.vo.SupplierVO;
import org.springblade.modules.clientManagement.mapper.SupplierMapper;
import org.springblade.modules.clientManagement.service.ISupplierService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springblade.modules.system.service.impl.TenantServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 供应商资料 服务实现类
 *
 * @author BladeX
 * @since 2019-08-28
 */
@Service
public class SupplierServiceImpl extends BaseServiceImpl<SupplierMapper, Supplier> implements ISupplierService {
	@Override
	public IPage<SupplierVO> selectSupplierPage(IPage<SupplierVO> page, SupplierVO supplier) {
		return page.setRecords(baseMapper.selectSupplierPage(page, supplier));
	}

	public Boolean batchImport(MultipartFile file, BladeUser bladeUser) {
		if (bladeUser == null) return false;
		try {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
			int rowNum = xssfSheet.getLastRowNum() + 1;
			XSSFRow firstRow = xssfSheet.getRow(0);
			List<Supplier> s_list = new ArrayList<>(rowNum);
			Boolean judge = true;
			judge = judge && firstRow.getCell(0).getStringCellValue().equals("客户简称") ? true : false;
			judge = judge && firstRow.getCell(1).getStringCellValue().equals("全称") ? true : false;
			judge = judge && firstRow.getCell(2).getStringCellValue().equals("英文名") ? true : false;
			judge = judge && firstRow.getCell(3).getStringCellValue().equals("联系电话") ? true : false;
			judge = judge && firstRow.getCell(4).getStringCellValue().equals("传真机") ? true : false;
			judge = judge && firstRow.getCell(5).getStringCellValue().equals("邮政编码") ? true : false;
			judge = judge && firstRow.getCell(6).getStringCellValue().equals("手机") ? true : false;
			judge = judge && firstRow.getCell(7).getStringCellValue().equals("中文地址") ? true : false;
			judge = judge && firstRow.getCell(8).getStringCellValue().equals("英文地址") ? true : false;
			judge = judge && firstRow.getCell(9).getStringCellValue().equals("网站地址") ? true : false;
			judge = judge && firstRow.getCell(10).getStringCellValue().equals("E_Mail") ? true : false;
			judge = judge && firstRow.getCell(11).getStringCellValue().equals("国家") ? true : false;
			judge = judge && firstRow.getCell(12).getStringCellValue().equals("所属地区") ? true : false;
			judge = judge && firstRow.getCell(13).getStringCellValue().equals("所属城市") ? true : false;
			judge = judge && firstRow.getCell(14).getStringCellValue().equals("信誉等级") ? true : false;
			judge = judge && firstRow.getCell(15).getStringCellValue().equals("单位代码") ? true : false;
			judge = judge && firstRow.getCell(16).getStringCellValue().equals("业务类型") ? true : false;
			judge = judge && firstRow.getCell(17).getStringCellValue().equals("客户来源") ? true : false;
			judge = judge && firstRow.getCell(18).getStringCellValue().equals("客户分组") ? true : false;
			judge = judge && firstRow.getCell(19).getStringCellValue().equals("客户类型") ? true : false;
			if (!judge) return false;
			for (int i = 1; i < rowNum; i++) {
				XSSFRow xssfRow = xssfSheet.getRow(i);
				Supplier s = new Supplier();
				XSSFCell cell0 = xssfRow.getCell(0);
				XSSFCell cell1 = xssfRow.getCell(1);
				XSSFCell cell2 = xssfRow.getCell(2);
				XSSFCell cell3 = xssfRow.getCell(3);
				XSSFCell cell4 = xssfRow.getCell(4);
				XSSFCell cell5 = xssfRow.getCell(5);
				XSSFCell cell6 = xssfRow.getCell(6);
				XSSFCell cell7 = xssfRow.getCell(7);
				XSSFCell cell8 = xssfRow.getCell(8);
				XSSFCell cell9 = xssfRow.getCell(9);
				XSSFCell cell10 = xssfRow.getCell(10);
				XSSFCell cell11 = xssfRow.getCell(11);
				XSSFCell cell12 = xssfRow.getCell(12);
				XSSFCell cell13 = xssfRow.getCell(13);
				XSSFCell cell14 = xssfRow.getCell(14);
				XSSFCell cell15 = xssfRow.getCell(15);
				XSSFCell cell16 = xssfRow.getCell(16);
				XSSFCell cell17 = xssfRow.getCell(17);
				XSSFCell cell18 = xssfRow.getCell(18);
				XSSFCell cell19 = xssfRow.getCell(19);
				cell0.setCellType(CellType.STRING);
				cell1.setCellType(CellType.STRING);
				cell2.setCellType(CellType.STRING);
				cell3.setCellType(CellType.STRING);
				cell4.setCellType(CellType.STRING);
				cell5.setCellType(CellType.STRING);
				cell6.setCellType(CellType.STRING);
				cell7.setCellType(CellType.STRING);
				cell8.setCellType(CellType.STRING);
				cell9.setCellType(CellType.STRING);
				cell10.setCellType(CellType.STRING);
				cell11.setCellType(CellType.STRING);
				cell12.setCellType(CellType.STRING);
				cell13.setCellType(CellType.STRING);
				cell14.setCellType(CellType.STRING);
				cell15.setCellType(CellType.STRING);
				cell16.setCellType(CellType.STRING);
				cell17.setCellType(CellType.STRING);
				cell18.setCellType(CellType.STRING);
				cell19.setCellType(CellType.STRING);
				s.setShortName(cell0.getStringCellValue());
				s.setFullName(cell1.getStringCellValue());
				s.setEnglishName(cell2.getStringCellValue());
				s.setContactNumber(cell3.getRawValue());
				s.setFaxNumber(cell4.getStringCellValue());
				s.setPostalCode(cell5.getStringCellValue());
				s.setPhone(cell6.getStringCellValue());
				s.setChineseAddress(cell7.getStringCellValue());
				s.setEnglishAddress(cell8.getStringCellValue());
				s.setWebAddress(cell9.getStringCellValue());
				s.setEMail(cell10.getStringCellValue());
				s.setCountry(cell11.getStringCellValue());
				s.setDistrict(cell12.getStringCellValue());
				s.setCity(cell13.getStringCellValue());
				s.setCreditLevel(Integer.parseInt(cell14.getStringCellValue()));
				s.setUnitCode(cell15.getStringCellValue());
				s.setBusinessType(Integer.parseInt(cell16.getStringCellValue()));
				s.setClientSource(cell17.getStringCellValue());
				s.setClientGroup(cell18.getStringCellValue());
				s.setClientType(cell19.getStringCellValue());
				//默认没审核
				s.setExamineStatus(0);
				s.setCreateUser(bladeUser.getUserId());
				s.setUpdateUser(bladeUser.getUserId());
				s.setCreateDept(Long.valueOf(bladeUser.getDeptId()));
				s.setCreateTime(new Date());
				s.setUpdateTime(new Date());
				s_list.add(s);
			}
			if (!s_list.isEmpty()) return this.saveBatch(s_list, 100);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}


	public void batchExport(HttpServletResponse response) {
		List<Supplier> cd = this.list();
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
