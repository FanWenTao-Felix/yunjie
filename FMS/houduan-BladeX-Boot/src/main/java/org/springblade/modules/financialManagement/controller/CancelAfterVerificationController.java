package org.springblade.modules.financialManagement.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.businessManagement.entity.Fee;
import org.springblade.modules.businessManagement.enums.FeeStatus;
import org.springblade.modules.businessManagement.vo.FeeVO;
import org.springblade.modules.financialManagement.service.impl.CancelAfterVerificationServiceImpl;
import org.springframework.web.bind.annotation.*;

/**
 * @author Alexander
 * @date 2019/9/24 3:54 PM
 */
@RestController
@AllArgsConstructor
@RequestMapping("financial/cav")
@Api(value = "核销接口", tags = "核销接口")
public class CancelAfterVerificationController extends BladeController {
	private CancelAfterVerificationServiceImpl service;

	/**
	 * 分页 应收应付
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入fee")
	public R<IPage<FeeVO>> list(Fee fee, Query query) {
		return R.data(service.getList(fee, query));
	}

	/**
	 * 修改状态
	 */
	@PostMapping("/batch/status")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R batchStatus(@ApiParam(value = "主键集合", required = true) @RequestParam String ids, Integer status) {
		return R.status(service.batchStatus(Func.toLongList(ids), (FeeStatus) CommonUtil.getEnumByValue(status, FeeStatus.class)));
	}


	/**
	 * 锁定费用状态
	 */
	@PostMapping("/batch/lockorunlock")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R batchStatus(@ApiParam(value = "主键集合", required = true) @RequestParam String ids, Boolean lock) {
		return R.status(service.batchLock(Func.toLongList(ids), lock));
	}


	/**
	 * 修改已结算金额
	@PostMapping("/settledamount")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "修改已结算金额", notes = "传入ids")
	public R<Boolean> settledAmount(@ApiParam(value = "主键集合", required = true) @RequestParam String id, Double settledAmount) {
		return R.status(service.settledAmount(id, settledAmount));
	}
	 */



}
