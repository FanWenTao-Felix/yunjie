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
import java.io.Serializable;

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
@TableName("fms_shipping_particular")
@ApiModel(value = "ShippingParticular对象", description = "ShippingParticular对象")
public class ShippingParticular implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "id", type = IdType.ID_WORKER)
	private Long id;
	/**
	* 起运港
	*/
		@ApiModelProperty(value = "起运港")
		private String originPort;
	/**
	* 目的港
	*/
		@ApiModelProperty(value = "目的港")
		private String destinationPort;
	/**
	* 截关/开船
	*/
		@ApiModelProperty(value = "截关/开船")
		private String stopOpen;
	/**
	* 船程
	*/
		@ApiModelProperty(value = "船程")
		private String boatRide;
	/**
	* 中转
	*/
		@ApiModelProperty(value = "中转")
		private String transfer;
	/**
	* GP20
	*/
		@ApiModelProperty(value = "GP20")
		private Double gpTwenty;
	/**
	* GP40
	*/
		@ApiModelProperty(value = "GP40")
		private Double gpForty;
	/**
	* HC40
	*/
		@ApiModelProperty(value = "HC40")
		private Double hcForty;
	/**
	* HC45
	*/
		@ApiModelProperty(value = "HC45")
		private Double hcFortyFive;
	/**
	* 运价备注
	*/
		@ApiModelProperty(value = "运价备注")
		private String shippingNote;
	/**
	* 有效期
	*/
		@ApiModelProperty(value = "有效期")
		private String periodValidity;
	/**
	* 船司id
	*/
		@ApiModelProperty(value = "船司id")
		private String lineId;


}
