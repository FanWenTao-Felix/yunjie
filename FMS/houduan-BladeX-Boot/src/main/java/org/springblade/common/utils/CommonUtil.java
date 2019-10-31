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
package org.springblade.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.IEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springblade.common.constant.CommonConstant;
import org.springblade.common.enums.UserRole;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.businessManagement.entity.AirBusiness;
import org.springblade.modules.businessManagement.entity.Fee;
import org.springblade.modules.businessManagement.enums.BusinessStatus;
import org.springblade.modules.businessManagement.enums.FeeStatus;
import org.springblade.modules.businessManagement.service.impl.AirBusinessServiceImpl;
import org.springblade.modules.businessManagement.service.impl.FeeServiceImpl;
import org.springblade.modules.dictionaries.enums.CustomCurrency;
import org.springblade.modules.ocean.entity.SeaBusiness;
import org.springblade.modules.ocean.service.impl.SeaBusinessServiceImpl;
import org.springblade.modules.system.entity.Role;
import org.springblade.modules.system.entity.User;
import org.springblade.modules.system.service.impl.RoleMenuServiceImpl;
import org.springblade.modules.system.service.impl.RoleServiceImpl;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具类
 *
 * @author Chill
 */
public class CommonUtil {
	/**
	 * 常用的简单excel导出
	 * Map<String, String> map  第一个String 是指   输出对象变量名入   key:name,value:"名字",这里的value是excel 对应的第一row的值
	 * List<Object> list 表示输出对象
	 *
	 * @return
	 */
	public static <T> XSSFWorkbook commonExcelExport(LinkedHashMap<String, String> map, List<T> list, String sheetName) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		if (map.isEmpty() || list.isEmpty()) return workbook;
		XSSFSheet sheet = workbook.createSheet(sheetName != null ? sheetName : "文件导出");
		sheet.setDefaultColumnWidth(20);
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		/**输出首行的标题**/
		XSSFRow row0 = sheet.createRow(0);
		List<String> keyList = new ArrayList<>(map.keySet());
		for (int i = 0; i < keyList.size(); i++) {
			XSSFCell cell = row0.createCell(i);
			cell.setCellStyle(cellStyle);
			cell.setCellValue(map.get(keyList.get(i)));
		}
		/**输出内容**/
		for (int i = 1; i <= list.size(); i++) {
			Object item = list.get(i - 1);
			XSSFRow row = sheet.createRow(i);
			for (int j = 0; j < keyList.size(); j++) {
				/**字段首字母大写**/
				char[] ch = keyList.get(j).toCharArray();
				ch[0] -= 32;
				String methodName = "get" + String.valueOf(ch);
				XSSFCell cell = row.createCell(j);
				cell.setCellStyle(cellStyle);
				try {
					Method get = item.getClass().getMethod(methodName);
					Object result = get.invoke(item);
					cell.setCellValue(result.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return workbook;
	}

	/**
	 * 导出excel
	 *
	 * @param workbook
	 * @param response
	 * @param fileName
	 */
	public static void excelExport(Workbook workbook, HttpServletResponse response, String fileName) {
		String suffix = workbook instanceof HSSFWorkbook ? ".xls" : ".xlsx";
		if (fileName == null) fileName = "test";
		try {
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + suffix); // 设定输出文件头,该方法有两个参数，分别表示应答头的名字和值。XSSF:xlsx    HSSF:xls
			response.setContentType("application/msexcel");
			OutputStream outputStream = response.getOutputStream();

			workbook.write(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成excel文件在服务器上
	 *
	 * @param workbook
	 * @param filePath /user/a.xlsx          完整文件路径名
	 */
	public static void createExcelFileOnServer(Workbook workbook, String filePath) {
		try {
			File file = new File(filePath);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			workbook.write(fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getCellValue(Cell cell) {
		if (cell == null) return "";
		if (cell.getCellType() == CellType.STRING)    //字符串
			return cell.getStringCellValue();
		else if (cell.getCellType() == CellType.BOOLEAN)    //Boolean
			return String.valueOf(cell.getBooleanCellValue());
		else if (cell.getCellType() == CellType.FORMULA)   //公式
			return String.valueOf(cell.getCellFormula());
		else if (cell.getCellType() == CellType.NUMERIC)    //数字
			return String.valueOf(cell.getNumericCellValue());
		else if (cell.getCellType() == CellType.ERROR)    //故障
			return "非法字符";
		return "";
	}

	public static void getFileOnServer(String filePath, HttpServletResponse response) {
		File file = new File(filePath);
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=" + file.getName());
		if (!file.exists()) throw new RuntimeException("文件不存在");
		try {
			InputStream inputStream = new FileInputStream(filePath);
			OutputStream outputStream = response.getOutputStream();
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) outputStream.write(b, 0, length);
			outputStream.close();
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param excelTemplatePath 存放于静态目录
	 */
	public static void excelTemplatePrint(HttpServletResponse response, String excelTemplatePath, Map<String, String> map, String outputFileName) {
		Workbook workbook = excelTemplate(excelTemplatePath, map);
		excelExport(workbook, response, outputFileName);
	}

	/**
	 * @param excelTemplatePath
	 * @param map
	 * @return
	 */
	public static Workbook excelTemplate(String excelTemplatePath, Map<String, String> map) {
		try {
			ClassPathResource cpr = new ClassPathResource(excelTemplatePath);
			Workbook workbook = null;
			if (cpr.getFilename().endsWith(".xls")) workbook = new HSSFWorkbook(cpr.getInputStream());
			else workbook = new XSSFWorkbook(cpr.getInputStream());
			Sheet sheet = workbook.getSheetAt(0);
			int rownum = sheet.getLastRowNum() + 1;
			for (int i = 0; i < rownum; i++) {
				Row row = sheet.getRow(i);
				if (row == null) continue;
				int cellnum = row.getLastCellNum() + 1;
				for (int j = 0; j < cellnum; j++) {
					Cell cell = row.getCell(j);
					String value = getCellValue(cell);
					if (map.containsKey(value)) cell.setCellValue(map.get(value));
				}
			}
			return workbook;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断空运主单是否符合规则
	 *
	 * @param mainOrder
	 */
	public static String airMainOrderJudge(String mainOrder) {
		if (StringUtils.isEmpty(mainOrder)) return null;
		mainOrder = mainOrder.replaceAll("\\D", "");
		if (mainOrder.length() != 11) throw new RuntimeException("无效主单号");
		Integer last_seven = Integer.valueOf(mainOrder.substring(3, 10));
		Integer last_one = Integer.valueOf(mainOrder.substring(mainOrder.length() - 1, mainOrder.length()));
		if ((last_seven - last_one) % 7 != 0) throw new RuntimeException("无效主单号");
		mainOrder = mainOrder.substring(0, 3) + "-" + mainOrder.substring(3, 7) + " " + mainOrder.substring(7, 11);
		return mainOrder;
	}

	/**
	 * 将字符串拆分成指定的分组和分组大小的数组
	 *
	 * @param str
	 * @param lines
	 * @param num_each_line
	 * @return
	 */
	public static String[] substringToArray(String str, Integer lines, Integer num_each_line) {
		String[] data = new String[lines];
		for (int i = 0; i < data.length; i++) data[i] = "";
		if (StringUtils.isEmpty(str)) return data;
		char[] char_arry = str.toCharArray();
		int max = char_arry.length;
		int l_i = 0;
		int l_n = 0;
		for (int i = 0; i < max && i < lines * num_each_line; i++) {
			if (l_n < num_each_line) {
				data[l_i] += char_arry[i];
				l_n++;
			} else {
				l_i++;
				l_n = 0;
			}
		}
		return data;
	}

	//主单号生成

	/**
	 * 0-5尾数+11 6尾数+4   071-35251403头    071-35251790尾   40份
	 *
	 * @param begin
	 * @param end
	 */
	public static String generateMainOrderNo(String begin, String end) {
		if (StringUtils.isEmpty(begin) || StringUtils.isEmpty(end)) throw new RuntimeException("参数不能为空");
		begin = airMainOrderJudge(begin);
		end = airMainOrderJudge(end);
		Long l_begin = Long.valueOf(begin);
		Long l_end = Long.valueOf(end);
		Integer last = Integer.valueOf(begin.substring(begin.length() - 1, begin.length()));
		if (last >= 0 && last <= 5) l_begin += 11;
		else if (last == 6) l_begin += 4;
		else throw new RuntimeException("无效主单");
		if (l_begin > l_end) throw new RuntimeException("已经没有主单可以生成");
		String result = String.valueOf(l_begin);
		int count = 11 - result.length();
		for (int i = 0; i < count; i++) result = "0" + result;
		airMainOrderJudge(result);
		return result;
	}

	/**
	 * 根据 值获取枚举
	 */
	public static <T> IEnum getEnumByValue(T value, Class dc) {
		IEnum[] d = (IEnum[]) dc.getEnumConstants();
		for (int i = 0; i < d.length; i++) if (d[i].getValue().toString().equals(value.toString())) return d[i];
		return null;
	}

	/**
	 * 字符串转货币枚举值
	 */
	public static CustomCurrency getCurrencyByString(String value) {
		CustomCurrency data = (CustomCurrency) getEnumByValue(value, CustomCurrency.class);
		if (data == null) throw new RuntimeException("不合法货币枚举值");
		return data;
	}

	/**
	 * 空运业务锁单
	 *
	 * @param ids
	 * @param service
	 * @param user
	 */
	public static void airBusinessLockedJudge(String ids, AirBusinessServiceImpl service, BladeUser user, RoleServiceImpl roleService) {
		if (StringUtils.isEmpty(ids)) return;
		Collection<AirBusiness> list = service.listByIds(Func.toLongList(ids));
		for (AirBusiness item : list) {
			if (item == null) throw new RuntimeException("没有找到对应空运业务");
			if (item.getBusinessStatus() == BusinessStatus.FINISHED && (!CommonUtil.isAdmin(user, roleService)))
				throw new RuntimeException("订单已完成，无权限修改");
		}
	}

	/**
	 * 海运业务锁单
	 *
	 * @param ids
	 * @param service
	 * @param user
	 */
	public static void seaBusinessLockedJudge(String ids, SeaBusinessServiceImpl service, BladeUser user) {
		if (StringUtils.isEmpty(ids)) return;
		Collection<SeaBusiness> list = service.listByIds(Func.toLongList(ids));
		for (SeaBusiness item : list) {
			if (item == null) throw new RuntimeException("没有找到对应海运业务");
			if (item.getBusinessState() == "已完成" && (!user.getRoleId().equals(CommonConstant.ADMIN_ROLE_ID))) {
				throw new RuntimeException("订单已完成，无权限修改");
			}

		}
	}


	/**
	 * 费用锁
	 */
	public static void airFeeSaveOrUpdateJudge(Fee fee, AirBusinessServiceImpl airBusinessService, FeeServiceImpl feeService, RoleServiceImpl roleService, BladeUser user) {
		if (fee == null) throw new RuntimeException("缺失费用参数");
		if (StringUtils.isEmpty(fee.getInternalOrderNo())) throw new RuntimeException("内部单号不能为空");
		QueryWrapper<AirBusiness> ab_qw = new QueryWrapper<>();
		ab_qw.eq("internal_order_no", fee.getInternalOrderNo());
		AirBusiness airBusiness = airBusinessService.getOne(ab_qw);
		if (airBusiness == null) throw new RuntimeException("没有找到对应的空运单");
		UserRole userRole = judgeUserRole(user, roleService);
		boolean noPermission = true;
		if (userRole == UserRole.ADMINISTRATOR || userRole == UserRole.ADMIN || userRole == UserRole.FINANCE)
			noPermission = false;
		if (airBusiness.getIsVerifiedFee() && noPermission)
			throw new RuntimeException("订单费用已审核全部，不能改动");
		if (fee.getId() == null) return;//新增
		//修改时要判断
		Fee fee_data = feeService.getById(fee.getId());
		if (fee_data == null) throw new RuntimeException("没有找到对应的费用");
		if (fee_data.getIsLocked() && noPermission)
			throw new RuntimeException("费用已锁定，不能修改");
		if (fee_data.getFeeStatus() == FeeStatus.SETTLING && noPermission) throw new RuntimeException("费用结算中,不能修改");
	}


	public static void airFeeJudgeWithIds(boolean isDelete, String ids, AirBusinessServiceImpl airBusinessService, FeeServiceImpl feeService, RoleServiceImpl roleService, BladeUser user) {
		List<Long> idList = Func.toLongList(ids);
		if (idList.isEmpty()) return;
		UserRole userRole = judgeUserRole(user, roleService);
		Collection<Fee> feeCollection = feeService.listByIds(idList);
		if (feeCollection.isEmpty()) return;
		Fee[] fees = new Fee[1];
		feeCollection.forEach(i -> fees[0] = i);
		String internalOderNo = fees[0].getInternalOrderNo();
		if (StringUtils.isEmpty(internalOderNo)) throw new RuntimeException("没有找到工作单号");
		QueryWrapper<AirBusiness> aqw = new QueryWrapper<>();
		aqw.eq("internal_order_no", internalOderNo);
		AirBusiness airBusiness = airBusinessService.getOne(aqw);
		if (airBusiness == null) throw new RuntimeException("没有找到对应空运单");
		boolean noPermission = true;
		if (userRole == UserRole.ADMINISTRATOR || userRole == UserRole.ADMIN || userRole == UserRole.FINANCE)
			noPermission = false;
		if (airBusiness.getIsVerifiedFee() && noPermission)
			throw new RuntimeException("订单费用已审核全部，不能改动");
		boolean[] noPermissionArray = new boolean[1];
		noPermissionArray[0] = noPermission;
		if (!isDelete) return;
		feeCollection.forEach(item -> {
			if (item.getIsLocked() && noPermissionArray[0])
				throw new RuntimeException("费用已锁定，不能修改");
			if (item.getFeeStatus() == FeeStatus.SETTLING && noPermissionArray[0])
				throw new RuntimeException("费用结算中，不能修改");
		});


	}

	/**
	 * 复制行
	 *
	 * @param source
	 * @param target
	 */
	public static void copyRow(Row source, Row target) {
		if (target == null) return;
		source.setRowStyle(target.getRowStyle());
		source.setHeight(target.getHeight());
		for (int i = 0; i < target.getLastCellNum() + 1; i++) {
			Cell targetCell = target.getCell(i);
			Cell sourceCell = source.createCell(i);
			if (targetCell == null) continue;
			sourceCell.setCellStyle(targetCell.getCellStyle());
			sourceCell.setCellValue(getCellValue(targetCell));
		}

	}

	/**
	 * 根据用户角色判断什么角色
	 */
	public static UserRole judgeUserRole(BladeUser user, RoleServiceImpl roleService) {
		if (user == null) throw new RuntimeException("用户角色不能为空");
		if (StringUtils.isEmpty(user.getTenantId())) throw new RuntimeException("用户没有租户id");
		List<String> list = roleService.getRoleAliases(user.getRoleId());
		for (String item : list) {
			if (item.equals(UserRole.ADMIN.toString())) return UserRole.ADMIN;
			if (item.equals(UserRole.SALESMAN.toString())) return UserRole.SALESMAN;
			if (item.equals(UserRole.OPERATOR.toString())) return UserRole.OPERATOR;
			if (item.equals(UserRole.ADMINISTRATOR.toString())) return UserRole.ADMINISTRATOR;
		}
		return UserRole.OTHER;
	}

	/**
	 * 获取当前租户系统角色
	 *
	 * @return
	 */
	public static Role getTenantRole(UserRole userRole, BladeUser user, RoleServiceImpl roleService) {
		if (userRole == UserRole.OTHER) throw new RuntimeException("不是系统角色");
		if (user == null) throw new RuntimeException("用户角色不能为空");
		if (StringUtils.isEmpty(user.getTenantId())) throw new RuntimeException("用户没有租户id");
		QueryWrapper<Role> role_qw = new QueryWrapper<>();
		role_qw.eq("role_alias", userRole.getValue().toString());
		role_qw.eq("tenant_id", user.getTenantId());
		return roleService.getOne(role_qw);
	}

	public static Boolean isSystemRoleByAliasExcludeAdminstrator(String alias) {
		if (alias.equals(UserRole.ADMIN.getValue()) || alias.equals(UserRole.SALESMAN.getValue()) || alias.equals(UserRole.OPERATOR.getValue()) || alias.equals(UserRole.FINANCE.getValue()))
			return true;
		return false;
	}

	public static Boolean isSystemRoleByAlias(String alias) {
		if (isSystemRoleByAliasExcludeAdminstrator(alias) || alias.equals(UserRole.ADMINISTRATOR.getValue()))
			return true;
		return false;
	}

	public static Boolean isSystemRoleByIdExcludeAdminstrator(String id) {
		if (id.equals(UserRole.FINANCE.getTemplateRoleIdToString()) || id.equals(UserRole.ADMIN.getTemplateRoleIdToString()) || id.equals(UserRole.OPERATOR.getTemplateRoleIdToString()) || id.equals(UserRole.SALESMAN.getTemplateRoleIdToString()))
			return true;
		return false;
	}

	public static Boolean isSystemRoleById(String id) {
		if (isSystemRoleByIdExcludeAdminstrator(id) || id.equals(UserRole.ADMINISTRATOR.getTemplateRoleIdToString()))
			return true;
		return false;
	}

	public static Boolean isSystemRole(BladeUser user, RoleServiceImpl roleService) {
		if (judgeUserRole(user, roleService) == UserRole.OTHER) return false;
		return true;
	}

	public static Boolean isRoleOf(UserRole userRole, BladeUser user, RoleServiceImpl roleService) {
		if (judgeUserRole(user, roleService) == userRole) return true;
		return false;
	}

	/**
	 * 下面的方法应该，可以归纳为上面的isRoleOf来用
	 */
	public static Boolean isAdmin(BladeUser user, RoleServiceImpl roleService) {
		if (judgeUserRole(user, roleService) == UserRole.ADMIN) return true;
		return false;
	}

	public static Boolean isSalesman(BladeUser user, RoleServiceImpl roleService) {
		if (judgeUserRole(user, roleService) == UserRole.SALESMAN) return true;
		return false;
	}

	public static Boolean isOperator(BladeUser user, RoleServiceImpl roleService) {
		if (judgeUserRole(user, roleService) == UserRole.OPERATOR) return true;
		return false;
	}

	public static Boolean isAdministrator(BladeUser user, RoleServiceImpl roleService) {
		if (judgeUserRole(user, roleService) == UserRole.ADMINISTRATOR) return true;
		return false;
	}

	public static String excelDataJudge(Object data) {
		if (data == null || StringUtils.isEmpty(data.toString())) return CommonConstant.EXCEL_NODATA_TEXT;
		return data.toString();
	}

	public static String excelDataJudgeWithoutTips(Object data) {
		if (data == null || StringUtils.isEmpty(data.toString())) return "";
		return data.toString();
	}
}
