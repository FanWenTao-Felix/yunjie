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
 * @since 2019-09-05
 */
@Data
@TableName("fms_air_freight")
@ApiModel(value = "AirFreight对象", description = "AirFreight对象")
public class AirFreight implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "id", type = IdType.ID_WORKER)
	private Long id;
	/**
	* 地区
	*/
		@ApiModelProperty(value = "地区")
		private String region;
	/**
	* 起运港口
	*/
		@ApiModelProperty(value = "起运港口")
		private String originPort;
	/**
	* 到达港口
	*/
		@ApiModelProperty(value = "到达港口")
		private String destinationPort;
	/**
	* 最大值
	*/
		@ApiModelProperty(value = "最大值")
		private Double leastValue;
	/**
	* 最小值
	*/
		@ApiModelProperty(value = "最小值")
		private Double normalValue;
	/**
	* +45KGS
	*/
		@ApiModelProperty(value = "+45KGS")
		private Double fortyFive;
	/**
	* +100KGS
	*/
		@ApiModelProperty(value = "+100KGS")
		private Double oneHundred;
	/**
	* +300KGS
	*/
		@ApiModelProperty(value = "+300KGS")
		private Double threeHundred;
	/**
	* +500KGS
	*/
		@ApiModelProperty(value = "+500KGS")
		private Double fiveHundred;
	/**
	* +1000KGS
	*/
		@ApiModelProperty(value = "+1000KGS")
		private Double oneThousand;
	/**
	* ROUTING
	*/
		@ApiModelProperty(value = "ROUTING")
		private String routing;
	/**
	* 2nd
	*/
		@ApiModelProperty(value = "2nd")
		private String twoNd;
	/**
	* 3th
	*/
		@ApiModelProperty(value = "3th")
		private String threeTh;
	/**
	* 时效
	*/
		@ApiModelProperty(value = "时效")
		private String aging;
	/**
	* 备注
	*/
		@ApiModelProperty(value = "备注")
		private String remark;
	/**
	* 空运费用信息
	*/
		@ApiModelProperty(value = "空运费用信息")
		private String freigthInfoId;


}
