package org.springblade.modules.financialManagement.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.Query;
import org.springblade.modules.businessManagement.entity.AirBusiness;
import org.springblade.modules.businessManagement.entity.Fee;
import org.springblade.modules.businessManagement.enums.FeeStatus;
import org.springblade.modules.businessManagement.mapper.FeeMapper;
import org.springblade.modules.businessManagement.service.impl.AirBusinessServiceImpl;
import org.springblade.modules.businessManagement.service.impl.FeeServiceImpl;
import org.springblade.modules.businessManagement.vo.FeeVO;
import org.springblade.modules.financialManagement.entity.Bill;
import org.springblade.modules.financialManagement.entity.BillDetail;
import org.springblade.modules.financialManagement.service.IBillDetailService;
import org.springblade.modules.financialManagement.service.IBillService;
import org.springblade.modules.ocean.entity.AdditionFee;
import org.springblade.modules.ocean.entity.SeaBusiness;
import org.springblade.modules.ocean.service.impl.AdditionFeeServiceImpl;
import org.springblade.modules.ocean.service.impl.SeaBusinessServiceImpl;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Alexander
 * @date 2019/9/24 4:04 PM
 */
@Service
@AllArgsConstructor
public class CancelAfterVerificationServiceImpl {
	private FeeMapper feeMapper;
	private FeeServiceImpl feeService;
	private AdditionFeeServiceImpl additionFeeService;
	private SeaBusinessServiceImpl seaBusinessService;
	private AirBusinessServiceImpl airBusinessService;
	private IBillService billService;
	private IBillDetailService billDetailService;

	public IPage<FeeVO> getList(Fee fee, Query query) {
		IPage<FeeVO> data = new Page<>();
		data.setSize(query.getSize());
		data.setCurrent(query.getCurrent());
		data.setTotal(feeMapper.getFeeCount(fee, query));
		List<Fee> fl = feeMapper.getFeeList(fee, query);
		FeeServiceImpl.countPayedAmount(fl, billDetailService, billService);
		List<FeeVO> list = new ArrayList<>(fl.size());
		List<Long> feeIds = new ArrayList<>();
		for (Fee item : fl) {
			FeeVO f = new FeeVO();
			f.setId(item.getId());
			feeIds.add(item.getId());
			f.setInternalOrderNo(item.getInternalOrderNo());
			f.setClient(item.getClient());
			f.setAmount(item.getAmount());
			f.setShortName(item.getShortName());
			f.setSettlementUnit(item.getSettlementUnit());
			f.setCurrency(item.getCurrency());
			f.setShortName(item.getShortName());
			f.setEnglishName(item.getEnglishName());
			f.setChineseName(item.getChineseName());
			f.setFeeStatus(item.getFeeStatus());
			f.setInternalOrderNo(item.getInternalOrderNo());
			f.setType(item.getType());
			f.setCreateTime(item.getCreateTime());
			f.setCreateUser(item.getCreateUser());
			f.setSettledAmount(item.getSettledAmount());
			f.setBillCreatedAmount(item.getBillCreatedAmount());
			f.setIsLocked(item.getIsLocked());
			list.add(f);
		}
		data.setRecords(list);
		return data;
	}

	public Boolean batchStatus(List<Long> ids, FeeStatus status) {
		if (status != FeeStatus.AUDITED) throw new RuntimeException("目前只能审核费用");
		List<Fee> list = feeMapper.selectBatchIds(ids);
		//Collection<AdditionFee> additionFeeList = additionFeeService.listByIds(ids);
		list.forEach(item -> {
			if (item.getFeeStatus() == FeeStatus.AUDITING_FAIL || item.getFeeStatus() == FeeStatus.AUDITING || item.getFeeStatus() == FeeStatus.UNAUDITING)
				item.setFeeStatus(status);
		});
		//additionFeeList.forEach(item -> item.setStatus(item.getStatus() < 6 && (Integer.valueOf(status.getValue().toString()) == 0 || Integer.valueOf(status.getValue().toString()) > item.getStatus()) ? Integer.valueOf(status.getValue().toString()) : item.getStatus()));
		boolean ok = true;
		if (!list.isEmpty()) ok = feeService.updateBatchById(list);
		if (!ok) throw new RuntimeException("更改失败");
		return true;
	}

	public Boolean batchLock(List<Long> ids, Boolean lock) {
		List<Fee> list = feeMapper.selectBatchIds(ids);
		for (Fee item : list) item.setIsLocked(lock);
		if (!feeService.updateBatchById(list)) throw new RuntimeException("更新失败");
		return true;
	}


}
