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
package org.springblade.modules.clientManagement.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import javax.validation.Valid;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.clientManagement.entity.ClientLinkman;
import org.springblade.modules.clientManagement.vo.ClientLinkmanVO;
import org.springblade.modules.clientManagement.wrapper.ClientLinkmanWrapper;
import org.springblade.modules.clientManagement.service.IClientLinkmanService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 客户联系人 控制器
 *
 * @author BladeX
 * @since 2019-10-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("linkman/clientlinkman")
@Api(value = "客户联系人", tags = "客户联系人接口")
public class ClientLinkmanController extends BladeController {

	private IClientLinkmanService clientLinkmanService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入clientLinkman")
	public R<ClientLinkmanVO> detail(ClientLinkman clientLinkman) {
		ClientLinkman detail = clientLinkmanService.getOne(Condition.getQueryWrapper(clientLinkman));
		return R.data(ClientLinkmanWrapper.build().entityVO(detail));
	}

	/**
	* 分页 客户联系人
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入clientLinkman")
	public R<IPage<ClientLinkmanVO>> list(ClientLinkman clientLinkman, Query query) {
		IPage<ClientLinkman> pages = clientLinkmanService.page(Condition.getPage(query), Condition.getQueryWrapper(clientLinkman));
		return R.data(ClientLinkmanWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 客户联系人
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入clientLinkman")
	public R<IPage<ClientLinkmanVO>> page(ClientLinkmanVO clientLinkman, Query query) {
		IPage<ClientLinkmanVO> pages = clientLinkmanService.selectClientLinkmanPage(Condition.getPage(query), clientLinkman);
		return R.data(pages);
	}

	/**
	* 新增 客户联系人
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入clientLinkman")
	public R save(@Valid @RequestBody ClientLinkman clientLinkman) {
		return R.status(clientLinkmanService.save(clientLinkman));
	}

	/**
	* 修改 客户联系人
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入clientLinkman")
	public R update(@Valid @RequestBody ClientLinkman clientLinkman) {
		return R.status(clientLinkmanService.updateById(clientLinkman));
	}

	/**
	* 新增或修改 客户联系人
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入clientLinkman")
	public R submit(@Valid @RequestBody ClientLinkman clientLinkman) {
		return R.status(clientLinkmanService.saveOrUpdate(clientLinkman));
	}

	
	/**
	* 删除 客户联系人
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(clientLinkmanService.removeByIds(Func.toLongList(ids)));
	}

	
}
