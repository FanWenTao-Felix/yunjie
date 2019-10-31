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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.businessManagement.entity.BusinessFile;
import org.springblade.modules.businessManagement.entity.Fee;
import org.springblade.modules.businessManagement.enums.ReceivablePayableType;
import org.springblade.modules.businessManagement.service.impl.BusinessFileServiceImpl;
import org.springblade.modules.businessManagement.service.impl.FeeServiceImpl;
import org.springblade.modules.clientManagement.entity.ClientData;
import org.springblade.modules.clientManagement.service.impl.ClientDataServiceImpl;
import org.springblade.modules.financialManagement.entity.BankAccount;
import org.springblade.modules.financialManagement.service.IBankAccountService;
import org.springblade.modules.financialManagement.service.impl.BankAccountServiceImpl;
import org.springblade.modules.ocean.entity.*;
import org.springblade.modules.ocean.service.ISeparateLadingService;
import org.springblade.modules.ocean.vo.SeaBusinessVO;
import org.springblade.modules.ocean.mapper.SeaBusinessMapper;
import org.springblade.modules.ocean.service.ISeaBusinessService;
import org.springblade.modules.system.entity.Dept;

import org.springblade.modules.system.entity.Tenant;
import org.springblade.modules.system.service.ITenantService;
import org.springblade.modules.system.service.impl.DeptServiceImpl;
import org.springblade.modules.system.service.impl.TenantServiceImpl;
import org.springblade.modules.system.service.impl.UserServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springblade.core.mp.base.BaseServiceImpl;
import static org.springblade.common.utils.CommonUtil.copyRow;

/**
 *  服务实现类
 *
 * @author BladeX
 * @since 2019-09-16
 */
@AllArgsConstructor
@Service
public class SeaBusinessServiceImpl extends BaseServiceImpl<SeaBusinessMapper, SeaBusiness> implements ISeaBusinessService {
	//private WorkNumRulesServiceImpl workNumRulesService;
	private UserServiceImpl userService;
	private SeaSpellServiceImpl seaSpellService;
	private SeaWholeServiceImpl seaWholeService;
	private ClientDataServiceImpl clientDataService;
	private FeeServiceImpl feeService;
	private BillOfLadingServiceImpl billOfLadingService;
	private BusinessFileServiceImpl businessFileService;
	private SeparateLadingServiceImpl separateLadingService;
	private DeptServiceImpl deptService;
	private BankAccountServiceImpl bankAccountService;
	private TenantServiceImpl tenantService;



	@Override
	public IPage<SeaBusinessVO> selectSeaBusinessPage(IPage<SeaBusinessVO> page, SeaBusinessVO seaBusiness) {
		return page.setRecords(baseMapper.selectSeaBusinessPage(page, seaBusiness));
	}

/*
	@Transactional
	public Boolean saveData(SeaBusiness seaBusiness) {
		String internalOrderNo = workNumRulesService.getInternalOrderNo(workNumRulesService,
			Long.valueOf(seaBusiness.getInternalOrderNo()),seaBusiness.getDestination());
		seaBusiness.setInternalOrderNo(internalOrderNo);
		if (!save(seaBusiness)) throw new RuntimeException("保存失败");
		return true;
      	}*/

	@Transactional
	public void getDeliveryReceipt(HttpServletResponse response, Long id, String type) {
		SeaWhole seaWhole = null;
		SeaSpell seaSpell = null;
		SeaBusiness data = null;
		String internalOrderNo = null;
		if (type.equals("1")) {
			seaWhole = seaWholeService.getById(id);
			internalOrderNo = seaWhole.getInternalOrderNo();
			System.out.println(internalOrderNo);
			data = this.getById(seaWhole.getBusinessId());
		} else if (type.equals("2")) {
			System.out.println(2);
			seaSpell = seaSpellService.getById(id);
			internalOrderNo = seaSpell.getInternalOrderNo();
			System.out.println(internalOrderNo);
			data = this.getById(seaSpell.getBusinessId());
			System.out.println(data);
		}
		Map<String, String> map = new HashMap<>();
		/*map.put("A18", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));//出单日期*/
		map.put("A1", userService.getById(data.getSalesman()).getName());//业务员
		map.put("A20", userService.getById(data.getOperating()).getName());//操作员
		List<String> strings = Arrays.asList(data.getConsignor().split(","));
		String fullName = "";
		String contactNumber ="";
		for (String s : strings) {
			ClientData carrier = clientDataService.getById(s);
			fullName += "    "+carrier.getFullName();
			contactNumber += "    "+carrier.getContactNumber();
		}
		map.put("A3", fullName);//客户名称
		map.put("A21", contactNumber);//联系电话
		if (type.equals("1")) {
			map.put("A4", seaWhole.getPortLoading()); //起运港
			map.put("A5", seaWhole.getShipowner()); //船东
			map.put("A6", seaWhole.getDestinationPort()); //目的港
		}
		if (type.equals("2")) {
			map.put("A4", seaSpell.getPortLoading()); //起运港
			map.put("A5", seaSpell.getShipowner()); //船东
			map.put("A6", seaSpell.getDeparture()); //目的港
		}
		if (!data.getSailSchedule().equals("")) map.put("A7", data.getSailSchedule()); //船期要求
		else map.put("A7", ""); //货号时间
		if (data.getGoodsTime() != null) map.put("A8", data.getGoodsTime().toString()); //货号时间
		else map.put("A8", ""); //货号时间
		map.put("A9", data.getCabinetType()); //柜型柜量
		map.put("A10", data.getCargoName()); //货名
		map.put("A11", data.getNumberUnits().toString()); //货物件数(包装)
		map.put("A12", userService.getById(data.getSalesman()).getName()); //业务员
		map.put("A13", data.getSalesPrice().toString()); //销售价
		map.put("A14", data.getShipmentClause()); //出运条款
		map.put("A15", data.getDeliveryRequirements()); //出货要求

		QueryWrapper<Tenant> queryWrapper1=new QueryWrapper<>();
		queryWrapper1.eq("tenant_id",data.getTenantId());
		Tenant dept=tenantService.getOne(queryWrapper1);
		//System.out.println(dept);
		map.put("A22", dept.getCompanyChineseName());//公司名称
		Workbook workbook = CommonUtil.excelTemplate("/static/sea_delivery.xlsx", map);
		CommonUtil.excelExport(workbook, response, internalOrderNo + "_sea_delivery");
	}


	@Transactional
	public void getStatement(HttpServletResponse response, Long id, String type) {
		SeaWhole seaWhole = null;
		SeaSpell seaSpell = null;
		SeaBusiness data = null;
		String internalOrderNo = null;
		if (type.equals("1")) {
			seaWhole = seaWholeService.getById(id);
			internalOrderNo = seaWhole.getInternalOrderNo();
			data = this.getById(seaWhole.getBusinessId());
		}
		if (type.equals("2")) {
			seaSpell = seaSpellService.getById(id);
			internalOrderNo = seaSpell.getInternalOrderNo();
			data = this.getById(seaSpell.getBusinessId());
		}
		Map<String, String> map = new HashMap<>();
		QueryWrapper<Tenant> queryWrapper1=new QueryWrapper<>();
		queryWrapper1.eq("tenant_id",data.getTenantId());
		Tenant dept=tenantService.getOne(queryWrapper1);
		System.out.println(dept);
		map.put("A1", dept.getCompanyChineseName());
		map.put("A2", dept.getCompanyEnglishName());
		map.put("A3", dept.getFax());
		map.put("A4", dept.getEmail());
		QueryWrapper<BankAccount> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("tenant_id",data.getTenantId());
		queryWrapper.eq("account_name",dept.getCompanyChineseName());
		BankAccount bankAccoun=bankAccountService.getOne(queryWrapper);
		System.out.println(bankAccoun.getAccountName());
		map.put("A23", bankAccoun.getAccountName());//户名
		map.put("A21", bankAccoun.getBankName());//开户行
		map.put("A22", bankAccoun.getAccountNumber());//账号
		List<String> strings = Arrays.asList(data.getConsignor().split(","));
		String fullName = "";
		if (strings.size() > 0) {
			for (String s : strings) {
				ClientData carrier = clientDataService.getById(s);
				fullName += "  "+carrier.getFullName();
			}
			map.put("A5", fullName);//对账公司全称
		} else {
			map.put("A5", "");//对账公司全称
		}
		map.put("A6", internalOrderNo);//订单号
		map.put("A7", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));//出单日期
		if (type.equals("1")) {
			map.put("A8", seaWhole.getPortLoading());
			map.put("A9", seaWhole.getDestinationPort());
			map.put("A12", seaWhole.getShipowner()); //船东
			if (seaWhole.getScheduledTime() != null) {
				map.put("A10", seaWhole.getScheduledTime().toString());//实际开航 ETD:
			} else map.put("A10", "");//实际开航 ETD:
			if (seaWhole.getArrivalDat() != null) {
				map.put("A11", seaWhole.getArrivalDat().toString());//实际到达 ETA:
			} else {
				map.put("A11", "");//实际到达 ETA:
			}
		}
		if (type.equals("2")) {
			map.put("A8", seaSpell.getPortLoading());
			map.put("A9", seaSpell.getDeparture());
			map.put("A12", seaSpell.getShipowner()); //船东
			if (seaSpell.getArrivalDat() != null) {
				map.put("A11", seaSpell.getArrivalDat().toString());//实际到达 ETA:
			} else {
				map.put("A11", "");//预计到达 ETA:
			}
			if (seaSpell.getAirport() != null) {
				map.put("A10", seaSpell.getAirport().toString());//预计开航 ETD:
			} else {
				map.put("A10", "");//预计到达 ETA:
			}
		}
		map.put("A13", data.getNumberUnits().toString() + " " + data.getGoodsPackage());//件数加包装
		map.put("A14", data.getRoughWeight().toString() + "KGS"); //重量
		map.put("A15", data.getVolume() + " CBM");
		if (type.equals("1")) {
			map.put("A16", seaWhole.getShipping());//订舱号
			map.put("A17", seaWhole.getMunualFolio());
			map.put("A20", seaWhole.getShipsNameTwo());
		}
		if (type.equals("2")) {
			map.put("A16", seaSpell.getShipping());//订舱号
			map.put("A17", seaSpell.getMunualFolio());
			map.put("A20", seaSpell.getShipsName());
		}
		map.put("A18", data.getQuantity().toString());
		Workbook workbook = CommonUtil.excelTemplate("/static/sea_statement.xlsx", map);
		QueryWrapper<Fee> fqw = new QueryWrapper<>();
		fqw.eq("internal_order_no", internalOrderNo);
		fqw.eq("type", ReceivablePayableType.RECEIVABLE.getValue());
		List<Fee> feeList = feeService.list(fqw);
		System.out.println(feeList);
		Sheet sheet = workbook.getSheetAt(0);
		Sheet sheet_clone = workbook.cloneSheet(0);
		int start = 22;
		int interval = feeList.size();
		int total = interval + sheet_clone.getLastRowNum() + 1; //插入后的行数
		Map<Integer, Row> rowMap = new HashMap();
		for (int i = 0; i < sheet_clone.getLastRowNum() + 1; i++) {
			if (i <= start - 2) rowMap.put(i, sheet_clone.getRow(i));
			else rowMap.put(i + interval, sheet_clone.getRow(i));
		}
		for (int i = 0; i < total; i++) {
			Row sourceRow = sheet.createRow(i);//覆盖重写
			if (!rowMap.containsKey(i)) {
				Fee fee = feeList.get((i - start + 1));
				copyRow(sourceRow, sheet_clone.getRow(21));
				sourceRow.getCell(0).setCellValue(fee.getShortName());
				sourceRow.getCell(3).setCellValue(fee.getCurrency().getValue());
				sourceRow.getCell(4).setCellValue(fee.getQuantity());
				sourceRow.getCell(5).setCellValue(fee.getUnitPrice());
				sourceRow.getCell(7).setCellValue(fee.getAmount());
			} else {
				Row targetRow = rowMap.get(i);
				copyRow(sourceRow, targetRow);
			}
		}
		/**
		 * 计算总价      原来是行23 行24
		 */
		Row row1 = sheet.getRow(22 + interval);
		Row row2 = sheet.getRow(23 + interval);



		row1.getCell(6).setCellValue("USSSS");
		row1.getCell(7).setCellValue(100.32d);
		row2.getCell(3).setCellValue("USDD:CNNY=7.33"); //USD:CNY=6.8000
		row2.getCell(6).setCellValue("CNYYY");
		row2.getCell(7).setCellValue(105052.12d);
		workbook.removeSheetAt(1);
		CommonUtil.excelExport(workbook, response, internalOrderNo + "_statement");
	}

	@Transactional
	public void getBillLading(HttpServletResponse response, Long id, String type) {
		SeaWhole seaWhole = null;
		SeaSpell seaSpell = null;
		SeaBusiness data = null;
		BillOfLading bDate = null;
		String internalOrderNo = null;
		if (type.equals("1")) {
			seaWhole = seaWholeService.getById(id);
			internalOrderNo = seaWhole.getInternalOrderNo();
			bDate = billOfLadingService.getInternalOrderNo(internalOrderNo);
			data = this.getById(seaWhole.getBusinessId());
		}
		if (type.equals("2")) {
			//System.out.println(2);
			seaSpell = seaSpellService.getById(id);
			internalOrderNo = seaSpell.getInternalOrderNo();
			bDate = billOfLadingService.getInternalOrderNo(internalOrderNo);
			data = this.getById(seaSpell.getBusinessId());
		}
		Map<String, String> map = new HashMap<>();
		if (bDate == null) new RuntimeException("请添加提货信息");
		ClientData clientData = clientDataService.getById(bDate.getNameShipper());
		map.put("A1", clientData.getEnglishName());//发货人英文名称
		map.put("A2", clientData.getEnglishAddress());//发货人地址
		map.put("A3", clientData.getContactNumber());//发货人电话
		ClientData clientData1 = clientDataService.getById(bDate.getConsignee());
		map.put("A4", clientData1.getEnglishName());//收货人英文名称
		map.put("A5", clientData1.getEnglishAddress());//收货人地址
		map.put("A6", clientData1.getContactNumber());//收货人电话
		//ClientData clientData2 = clientDataService.getById(data.getConsignor());
		System.out.println("sss" + bDate);
		map.put("A7", bDate.getNotifier());//通知人
		map.put("A13", bDate.getShippingMark());//
		map.put("A14", bDate.getNumberUnits().toString() + " " + data.getGoodsPackage());//件数加包装
		map.put("A15", bDate.getCargoName());//货物名称
		map.put("A16", bDate.getDescription());//货物描述
		map.put("A17", bDate.getRoughWeight().toString() + "KGS"); //重量
		map.put("A18", bDate.getVolume() + " CBM");
		//map.put("A19", data.getCabinetType()+"-"+data.getQuantity()); //柜型柜量
		System.out.println("sss" + bDate.getCargoQuantity());
		map.put("A19", bDate.getCargoQuantity()); //柜型描述
		map.put("A20", bDate.getFreightPayWay()); //运费支付方式
		map.put("A21", bDate.getConsigneeAgent()); //放货人代理
		map.put("A24", bDate.getSignName()); //签发人
		map.put("A22", internalOrderNo);//订单号
		if (bDate.getSignTime() != null) {
			map.put("A25", bDate.getSignTime().toString()); //签发日期
		} else {
			map.put("A25", ""); //签发日期
		}
		if (type.equals("1")) {//整单
			map.put("A8", seaWhole.getPortLoading());//起运港
			map.put("A9", seaWhole.getShipowner());//船东
			map.put("A10", seaWhole.getShipsNameTwo());//船名2/航次2
			map.put("A11", seaWhole.getPol());//装货港
			map.put("A12", seaWhole.getUnload());//卸货港
			map.put("A23", seaWhole.getMunualFolio());//提单号
		}
		if (type.equals("2")) {//拼单
			map.put("A8", seaSpell.getPortLoading());//起运港
			map.put("A10", seaSpell.getShipsName());//船名2/航次2
			map.put("A9", seaSpell.getShipowner());//船东
			map.put("A11", seaSpell.getPol());//装货港
			map.put("A12", seaSpell.getPod());//卸货港
			map.put("A23", seaSpell.getMunualFolio());//提单号
		}
		Workbook workbook = CommonUtil.excelTemplate("/static/sea_pick.xlsx", map);
		CommonUtil.excelExport(workbook, response, internalOrderNo + "_pick");

	}


	@Transactional
	public Boolean allRemoveById(String d) {
		List<Long> ids = Func.toLongList(d);
		Collection<SeaBusiness> seaBusinessCollection = this.listByIds(ids);
		System.out.println(seaBusinessCollection);
		List<Long> wbusinessId = new ArrayList<>();
		List<Long> sbusinessId = new ArrayList<>();
		for (SeaBusiness item : seaBusinessCollection){
			if(item.getBookingType().equals("1")) wbusinessId.add(item.getId());
			if(item.getBookingType().equals("2")) sbusinessId.add(item.getId());
		}
		boolean err = true;
		if(wbusinessId.size()>0){
			QueryWrapper<SeaWhole> swhole = new QueryWrapper<>();
			swhole.in("business_id", wbusinessId);
			List<SeaWhole> swholeList = seaWholeService.list(swhole);
			System.out.println(swholeList);
			List<String> internalList = new ArrayList<>();
			for (SeaWhole item : swholeList) internalList.add(item.getInternalOrderNo());
			if(internalList.size()>0){
				QueryWrapper<Fee> fqw = new QueryWrapper<>();
				fqw.in("internal_order_no", internalList);
				List<Fee> feeList = feeService.list(fqw);
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
				List<Long> wholeIDList = new ArrayList<>(swholeList.size());
				List<Long> billOfLadingIdList = new ArrayList<>(billOfLadingList.size());
				List<Long> separateLadingIdList = new ArrayList<>(separateLadingList.size());
				for (Fee i : feeList) feeIdList.add(i.getId().toString());
				for (BusinessFile i : businessFileList) fileIdList.add(i.getId());
				for (SeaWhole i : swholeList) wholeIDList.add(i.getId());
				for (BillOfLading i : billOfLadingList) billOfLadingIdList.add(i.getId());
				for (SeparateLading i : separateLadingList) separateLadingIdList.add(i.getId());

				if (ids.size() > 0 && removeByIds(ids)) err = false;
				if(wholeIDList.size() >0 && seaWholeService.removeByIds(wholeIDList)) err = false;
				if (feeIdList.size() > 0 && feeService.removeByIds(feeIdList)) err = false;
				if (billOfLadingIdList.size() > 0 && billOfLadingService.removeByIds(billOfLadingIdList)) err = false;
				if (separateLadingIdList.size() > 0 && separateLadingService.removeByIds(separateLadingIdList)) err = false;
				if (fileIdList.size() > 0 && !businessFileService.removeFiles(StringUtils.join(fileIdList.toArray(), ","))) err = false;
			}else{
				if (ids.size() > 0 && removeByIds(ids)) err = false;
			}

		}
		if(sbusinessId.size()>0){
			QueryWrapper<SeaSpell> ssell = new QueryWrapper<>();
			ssell.in("business_id", sbusinessId);
			List<SeaSpell> ssellList = seaSpellService.list(ssell);
			List<String> internalList = new ArrayList<>();
			for (SeaSpell item : ssellList) internalList.add(item.getInternalOrderNo());
			if(internalList.size()>0) {
				QueryWrapper<Fee> fqw = new QueryWrapper<>();
				fqw.in("internal_order_no", internalList);
				List<Fee> feeList = feeService.list(fqw);
				System.out.println(feeList);
				QueryWrapper<BusinessFile> bfqw = new QueryWrapper<>();
				bfqw.in("internal_order_no", internalList);
				List<BusinessFile> businessFileList = businessFileService.list(bfqw);
				//System.out.println(businessFileList);
				QueryWrapper<BillOfLading> bol = new QueryWrapper<>();
				bol.in("internal_order_no", internalList);
				List<BillOfLading> billOfLadingList = billOfLadingService.list(bol);
				QueryWrapper<SeparateLading> sl = new QueryWrapper<>();
				sl.in("internal_order_no", internalList);
				List<SeparateLading> separateLadingList = separateLadingService.list(sl);
				//	System.out.println(businessFileList);
				List<Long> fileIdList = new ArrayList<>(businessFileList.size());
				List<String> feeIdList = new ArrayList<>(feeList.size());
				List<Long> ssellIDList = new ArrayList<>(ssellList.size());
				List<Long> billOfLadingIdList = new ArrayList<>(billOfLadingList.size());
				List<Long> separateLadingIdList = new ArrayList<>(separateLadingList.size());
				for (Fee i : feeList) feeIdList.add(i.getId().toString());
				for (BusinessFile i : businessFileList) fileIdList.add(i.getId());
				for (SeaSpell i : ssellList) ssellIDList.add(i.getId());
				for (BillOfLading i : billOfLadingList) billOfLadingIdList.add(i.getId());
				for (SeparateLading i : separateLadingList) separateLadingIdList.add(i.getId());
				if (ids.size() > 0 && !removeByIds(ids)) err = false;
				if (ssellIDList.size() > 0 && !seaWholeService.removeByIds(ssellIDList)) err = false;
				if (feeIdList.size() > 0 && !feeService.removeByIds(feeIdList)) err = false;
				if (billOfLadingIdList.size() > 0 && !billOfLadingService.removeByIds(billOfLadingIdList)) err = false;
				if (separateLadingIdList.size() > 0 && !separateLadingService.removeByIds(separateLadingIdList))
					err = false;
				if (fileIdList.size() > 0 && !businessFileService.removeFiles(StringUtils.join(fileIdList.toArray(), ",")))
					err = false;
			}else{
				System.out.println("进来-------------"+ids);
				if (ids.size() > 0 && removeByIds(ids)) err = false;
			 }
			}
		if (err) throw new RuntimeException("删除失败");
		return true;
	}


}
