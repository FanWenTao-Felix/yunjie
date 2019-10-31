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
package org.springblade.modules.ocean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.core.mp.base.BaseEntity;

/**
 * 实体类
 *
 * @author BladeX
 * @since 2019-09-20
 */
@Data
@TableName("fms_addition_fee")
@ApiModel(value = "AdditionFee对象", description = "AdditionFee对象")
public class AdditionFee extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 主键
	*/
		@ApiModelProperty(value = "主键")
		@JsonSerialize(using = ToStringSerializer.class)
		@TableId(value = "id", type = IdType.ID_WORKER)
		private Long id;
	/**
	* 创建人
	*//*
		@ApiModelProperty(value = "创建人")
		private Long createUser;
	*//**
	* 创建时间
	*//*
		@ApiModelProperty(value = "创建时间")
		private LocalDateTime createTime;
	*//**
	* 修改人
	*//*
		@ApiModelProperty(value = "修改人")
		private Long updateUser;
	*//**
	* 修改时间
	*//*
		@ApiModelProperty(value = "修改时间")
		private LocalDateTime updateTime;
	*//**
	* 是否已删除
	*//*
		@ApiModelProperty(value = "是否已删除")
		private Integer isDeleted;
	*//**
	* 状态
	*/
		@ApiModelProperty(value = "状态")
		private Integer status;
	/**
	* 创建部门
	*//*
		@ApiModelProperty(value = "创建部门")
		private Long createDept;*/
	/**
	 *业务订单id
	 */
	@ApiModelProperty(value = "业务订单id")
	private String   businessId;
	/**
	* 内部单号
	*/
		@ApiModelProperty(value = "内部单号")
		private String internalOrderNo;
	/**
	* 附加费名称
	*/
		@ApiModelProperty(value = "附加费名称")
		private String feeName;
	/**
	* 单位
	*/
		@ApiModelProperty(value = "单位")
		private String units;
	/**
	* 柜型
	*/
		@ApiModelProperty(value = "柜型")
		private String cabinetType;
	/**
	* 20
	*/
		@ApiModelProperty(value = "20")
		private Double twenty;
	/**
	* 40
	*/
		@ApiModelProperty(value = "40")
		private Double forty;
	/**
	* 40H
	*/
		@ApiModelProperty(value = "40H")
		private Double fortyH;
	/**
	* 45H
	*/
		@ApiModelProperty(value = "45H")
		private Double fortyFive;
	/**
	* 单票价格
	*/
		@ApiModelProperty(value = "单票价格")
		private Double singlePrice;
	/**
	* 币种
	*/
		@ApiModelProperty(value = "币种")
		private String currency;
	/**
	* 备注
	*/
		@ApiModelProperty(value = "备注")

		private String remarks;

	@ApiModelProperty(value = "费用类型")
	private Integer type;

	@ApiModelProperty(value = "结算单位")
	private Long settlementUnit;




}
