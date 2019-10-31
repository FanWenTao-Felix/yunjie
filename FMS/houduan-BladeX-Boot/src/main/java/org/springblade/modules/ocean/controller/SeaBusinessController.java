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
package org.springblade.modules.ocean.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springblade.common.constant.CommonConstant;
import org.springblade.common.enums.UserRole;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.businessManagement.entity.AirBusiness;
import org.springblade.modules.businessManagement.entity.Fee;
import org.springblade.modules.businessManagement.service.impl.AirBusinessServiceImpl;
import org.springblade.modules.businessManagement.service.impl.FeeServiceImpl;
import org.springblade.modules.dictionaries.entity.Parities;
import org.springblade.modules.ocean.entity.SeaSpell;
import org.springblade.modules.ocean.entity.SeaWhole;
import org.springblade.modules.ocean.service.ISeaSpellService;
import org.springblade.modules.ocean.service.ISeaWholeService;
import org.springblade.modules.ocean.service.impl.SeaBusinessServiceImpl;
import org.springblade.modules.ocean.service.impl.SeaSpellServiceImpl;
import org.springblade.modules.ocean.service.impl.SeaWholeServiceImpl;
import org.springblade.modules.system.service.impl.RoleServiceImpl;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.ocean.entity.SeaBusiness;
import org.springblade.modules.ocean.vo.SeaBusinessVO;
import org.springblade.modules.ocean.wrapper.SeaBusinessWrapper;
import org.springblade.modules.ocean.service.ISeaBusinessService;
import org.springblade.core.boot.ctrl.BladeController;

import java.io.Serializable;
import java.util.List;

/**
 *  控制器
 *
 * @author BladeX
 * @since 2019-09-16
 */
@RestController
@AllArgsConstructor
@RequestMapping("ocean/seabusiness")
@Api(value = "", tags = "接口")
public class SeaBusinessController extends BladeController {

	private ISeaBusinessService seaBusinessService;
	private SeaBusinessServiceImpl seaBusinessServices;
	private RoleServiceImpl roleService;
	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入seaBusiness")
	public R<SeaBusinessVO> detail(SeaBusiness seaBusiness) {
		SeaBusiness detail = seaBusinessService.getOne(Condition.getQueryWrapper(seaBusiness));
		return R.data(SeaBusinessWrapper.build().entityVO(detail));
	}

	/**
	* 分页
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入seaBusiness")
	public R<IPage<SeaBusinessVO>> list(SeaBusiness seaBusiness, Query query) {
		BladeUser user = getUser();
		UserRole userRole = CommonUtil.judgeUserRole(user, roleService);
		if (userRole == UserRole.SALESMAN) seaBusiness.setSalesman(user.getUserId());
		if (userRole == UserRole.SALESMAN) seaBusiness.setOperating(user.getUserId());
		IPage<SeaBusiness> pages = seaBusinessService.page(Condition.getPage(query), Condition.getQueryWrapper(seaBusiness).orderByDesc("create_time"));
		return R.data(SeaBusinessWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入seaBusiness")
	public R<IPage<SeaBusinessVO>> page(SeaBusinessVO seaBusiness, Query query) {
		IPage<SeaBusinessVO> pages = seaBusinessService.selectSeaBusinessPage(Condition.getPage(query), seaBusiness);
		return R.data(pages);
	}

	/**
	* 新增
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入seaBusiness")
	public R save(@Valid @RequestBody SeaBusiness seaBusiness) {
		BladeUser user = getUser();
		if (user.getRoleId().contains(CommonConstant.SALESMAN_ROLE_ID)) seaBusiness.setSalesman(user.getUserId());
		return R.status(seaBusinessService.save(seaBusiness));
	}

	/**
	* 修改
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入seaBusiness")
	public R update(@Valid @RequestBody SeaBusiness seaBusiness) {
		//System.out.println(seaBusiness);
		CommonUtil.seaBusinessLockedJudge(seaBusiness.getId().toString(),  seaBusinessServices, getUser());
		return R.status(seaBusinessService.updateById(seaBusiness));
	}

	/**
	* 新增或修改
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入seaBusiness")
	public R submit(@Valid @RequestBody SeaBusiness seaBusiness) {
		//System.out.println(seaBusiness);
		CommonUtil.seaBusinessLockedJudge(seaBusiness.getId().toString(),  seaBusinessServices, getUser());
		return R.status(seaBusinessService.saveOrUpdate(seaBusiness));
	}


	/**
	* 删除
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		CommonUtil.seaBusinessLockedJudge(ids,  seaBusinessServices, getUser());
		return R.status(seaBusinessServices.allRemoveById(ids));
	}

	/**
	 * 下载交接单
	 */
	@GetMapping("/deliveryReceipt")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "下载交接单", notes = "传入seaBusiness")
	public void deliveryReceipt(HttpServletResponse response,Long id ,String type) {
		seaBusinessServices.getDeliveryReceipt(response,id,type);
	}


	/**
	 * 下载海运对账单
	 */
	@GetMapping("/statement")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "海运对账单")
	public void statement(HttpServletResponse response,Long id ,String type) {
		seaBusinessServices.getStatement(response,id,type);
	}


	/**
	 * 下载海运提货单
	 */
	@GetMapping("/billLading")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "下载海运提货单")
	public void billLading(HttpServletResponse response,Long id ,String type) {
		seaBusinessServices.getBillLading(response,id,type);
	}





}
