package org.springblade.modules.financialManagement.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.modules.financialManagement.entity.BankAccount;
import org.springblade.modules.financialManagement.entity.ReceivablePayable;
import org.springblade.modules.financialManagement.service.impl.ReceivablePayableServiceImpl;
import org.springblade.modules.financialManagement.vo.BankAccountVO;
import org.springblade.modules.financialManagement.vo.ReceivablePayableVo;
import org.springblade.modules.financialManagement.wrapper.BankAccountWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alexander
 * @date 2019/9/23 11:25 AM
 */
@RestController
@AllArgsConstructor
@RequestMapping("financial/rp")
@Api(value = "应收应付", tags = "应收应付接口")
public class ReceivablePayableController extends BladeController {
	private ReceivablePayableServiceImpl service;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入receivablePayable")
	public R<ReceivablePayableVo> detail(ReceivablePayable receivablePayable) {
		return R.data(null);
	}

	/**
	 * 分页 应收应付
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入receivablePayable")
	public R<IPage<ReceivablePayableVo>> list(ReceivablePayable receivablePayable, Query query) {
		return R.data(service.getList(receivablePayable, query));
	}

}
