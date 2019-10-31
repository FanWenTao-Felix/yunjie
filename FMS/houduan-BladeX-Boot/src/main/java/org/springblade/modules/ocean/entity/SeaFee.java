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

import com.baomidou.mybatisplus.annotation.TableField;
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
import org.springblade.modules.businessManagement.enums.FeeStatus;
import org.springblade.modules.businessManagement.enums.ReceivablePayableType;

/**
 * 实体类
 *
 * @author BladeX
 * @since 2019-09-19
 */
@Data
@TableName("fms_sea_fee")
@ApiModel(value = "SeaFee对象", description = "SeaFee对象")
public class SeaFee extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	* 主键
	*//*
		@ApiModelProperty(value = "主键")
		private Long id;
	*//**
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
	*//*
		@ApiModelProperty(value = "状态")
		private Integer status;
	*//**
	* 创建部门
	*//*
		@ApiModelProperty(value = "创建部门")
		private Long createDept;*/
	/**
	 * 内部单号
	 */
	@ApiModelProperty(value = "内部单号")
	private String internalOrderNo;

	/**
	 * 费用简称
	 */
	@ApiModelProperty(value = "费用简称")
	private String shortName;
	/**
	 * 费用代码
	 */
	@ApiModelProperty(value = "费用代码")
	private String code;
	/**
	 * 费用英文名称
	 */
	@ApiModelProperty(value = "费用英文名称")
	private String englishName;
	/**
	 * 费用中文名称
	 */
	@ApiModelProperty(value = "费用中文名称")
	private String chineseName;

	/**
	 * 结算单位
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "结算单位")
	private Long settlementUnit;

	/**
	 * 费用币种
	 */
	@ApiModelProperty(value = "费用币种")
	private String currency;
	/**
	 * 费用金额
	 */
	@ApiModelProperty(value = "费用金额")
	private Double amount;

	/**
	 * 计费单位
	 */
	@ApiModelProperty(value = "计费单位")
	private String chargeUnit;

	/**
	 * 数量
	 */
	@ApiModelProperty(value = "数量")
	private Integer quantity;


	/**
	 * 单价
	 */
	@ApiModelProperty(value = "单价")
	private Double unitPrice;

	/**
	 * 费用类型 应收0 应付1
	 */
	@ApiModelProperty(value = "费用类型")
	private ReceivablePayableType type;

	/**
	 * 委托人
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "委托人")
	@TableField(exist = false)
	private Long client;

	@ApiModelProperty(value = "状态")
	private FeeStatus feeStatus;

}
