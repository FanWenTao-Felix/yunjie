package org.springblade.modules.financialManagement.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.modules.businessManagement.entity.AirBusiness;
import org.springblade.modules.businessManagement.entity.Fee;
import org.springblade.modules.businessManagement.enums.FeeStatus;
import org.springblade.modules.businessManagement.enums.ReceivablePayableType;
import org.springblade.modules.businessManagement.mapper.FeeMapper;
import org.springblade.modules.businessManagement.service.impl.AirBusinessServiceImpl;
import org.springblade.modules.businessManagement.service.impl.FeeServiceImpl;
import org.springblade.modules.dictionaries.enums.CustomCurrency;
import org.springblade.modules.dictionaries.service.IParitiesService;
import org.springblade.modules.financialManagement.entity.BankAccount;
import org.springblade.modules.financialManagement.entity.ReceivablePayable;
import org.springblade.modules.financialManagement.service.IBillDetailService;
import org.springblade.modules.financialManagement.service.IBillService;
import org.springblade.modules.financialManagement.vo.ReceivablePayableVo;
import org.springblade.modules.financialManagement.wrapper.BankAccountWrapper;
import org.springblade.modules.ocean.entity.SeaBusiness;
import org.springblade.modules.ocean.entity.SeaSpell;
import org.springblade.modules.ocean.entity.SeaWhole;
import org.springblade.modules.ocean.service.impl.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander
 * @date 2019/9/23 11:37 AM
 */
@Service
@AllArgsConstructor
public class ReceivablePayableServiceImpl {
	private AirBusinessServiceImpl airBusinessService;
	private SeaBusinessServiceImpl seaBusinessService;
	private FeeMapper feeMapper;
	private FeeServiceImpl feeService;
	private SeaSpellServiceImpl seaSpellService;
	private SeaWholeServiceImpl seaWholeService;
	private IParitiesService paritiesService;
	private IBillDetailService billDetailService;
	private IBillService billService;


	/**
	 * 获取报表应收应付列表
	 *
	 * @param receivablePayable
	 * @param query
	 * @return
	 */

	public IPage<ReceivablePayableVo> getList(ReceivablePayable receivablePayable, Query query) {
		IPage<ReceivablePayableVo> data = new Page<>();
		ReceivablePayableVo av = new ReceivablePayableVo();
		List<ReceivablePayableVo> list = feeMapper.selectReceivablePayableList(receivablePayable, query);
		List<String> internal_order_no_list = new ArrayList<>();
		for (ReceivablePayableVo item : list) internal_order_no_list.add(item.getInternalOrderNo());
		data.setSize(query.getSize());
		data.setTotal(feeMapper.selectReceivablePayableCount(receivablePayable, query));
		QueryWrapper<Fee> fqw = new QueryWrapper<>();
		fqw.in("internal_order_no", internal_order_no_list);
		List<Fee> feeList = internal_order_no_list.size() > 0 ? feeService.list(fqw) : new ArrayList<>();
		FeeServiceImpl.countPayedAmount(feeList, billDetailService, billService);//计算已结算金额
		for (ReceivablePayableVo fp : list) {
			if (fp.getPayMap() == null) fp.setPayMap(new HashMap<>());
			if (fp.getReceiveMap() == null) fp.setReceiveMap(new HashMap<>());
			if (fp.getProfitMap() == null) fp.setProfitMap(new HashMap<>());
			Map<CustomCurrency, Double> payMap = fp.getPayMap();
			Map<CustomCurrency, Double> receiveMap = fp.getReceiveMap();
			Map<CustomCurrency, Double> profitMap = fp.getProfitMap();
			for (Fee f : feeList) {
				if (!fp.getInternalOrderNo().equals(f.getInternalOrderNo())) continue;
				if (f.getCurrency() == null) continue;
				Map<CustomCurrency, Double> map = null;
				if (f.getType() == ReceivablePayableType.RECEIVABLE) map = receiveMap;
				else map = payMap;
				Double value = null;
				if (map.containsKey(f.getCurrency())) value = map.get(f.getCurrency());
				if (value == null) value = 0d;
				value += f.getAmount();
				map.put(f.getCurrency(), value);
			}
		}
		data.setRecords(list);
		return data;
	}


}
