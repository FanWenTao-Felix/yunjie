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
package org.springblade.modules.dictionaries.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体类
 *
 * @author BladeX
 * @since 2019-09-09
 */
@Data
@TableName("fms_shipping_line")
@ApiModel(value = "ShippingLine对象", description = "ShippingLine对象")
public class ShippingLine implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "id", type = IdType.ID_WORKER)
	private Long id;
	/**
	* 船司代码
	*/
		@ApiModelProperty(value = "船司代码")
		private String companyCode;
	/**
	* 船司中文名
	*/
		@ApiModelProperty(value = "船司中文名")
		private String companyChinese;
	/**
	* 币种
	*/
		@ApiModelProperty(value = "币种")
		private String currency;
	/**
	* 起运港
	*/
		@ApiModelProperty(value = "起运港")
		private String originPort;
	/**
	* 杂费备注
	*/
		@ApiModelProperty(value = "杂费备注")
		private String sundryGoods;
	/**
	* 备注
	*/
		@ApiModelProperty(value = "备注")
		private String remark;
	/**
	* 优势
	*/
		@ApiModelProperty(value = "优势")
		private String advantage;
	/**
	* 状态
	*/
		@ApiModelProperty(value = "状态")
		private String state;
	/**
	* 创建时间
	*/
		@ApiModelProperty(value = "创建时间")
		private Date creationTime;
	/**
	* 更新时间
	*/
		@ApiModelProperty(value = "更新时间")
		private Date turnoverTime;


}
