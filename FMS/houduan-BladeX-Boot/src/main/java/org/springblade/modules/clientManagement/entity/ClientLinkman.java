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
package org.springblade.modules.clientManagement.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.core.tenant.mp.TenantEntity;
import org.springblade.modules.clientManagement.enums.LinkmanType;

/**
 * 客户联系人实体类
 *
 * @author BladeX
 * @since 2019-10-09
 */
@Data
@TableName("fms_client_linkman")
@ApiModel(value = "ClientLinkman对象", description = "客户联系人")
public class ClientLinkman extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 客户数据id,关联client_data表
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "客户数据id,关联client_data表")
	private Long clientDataId;
	/**
	 * 联系人
	 */
	@ApiModelProperty(value = "联系人")
	private String linkman;
	/**
	 * 地址
	 */
	@ApiModelProperty(value = "地址")
	private String address;
	/**
	 * 联系电话
	 */
	@ApiModelProperty(value = "联系电话")
	private String phone;
	/**
	 * 类型0发货人,1收货人
	 */
	@ApiModelProperty(value = "类型0发货人,1收货人")
	private LinkmanType type;


}
