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
package org.springblade.modules.financialManagement.entity;

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
import org.springblade.core.tenant.mp.TenantEntity;
import org.springblade.modules.businessManagement.enums.ReceivablePayableType;
import org.springblade.modules.dictionaries.enums.CustomCurrency;
import org.springblade.modules.financialManagement.enums.BillStatus;

/**
 * 账单明细实体类
 *
 * @author BladeX
 * @since 2019-10-16
 */
@Data
@TableName("fms_bill_detail")
@ApiModel(value = "BillDetail对象", description = "账单明细")
public class BillDetail extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 账单id
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "账单id")
	private Long billId;
	/**
	 * 内部单号
	 */
	@ApiModelProperty(value = "内部单号")
	private String internalOrderNo;
	/**
	 * 关联的费用id
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "关联的费用id")
	private Long feeId;
	/**
	 * 类型收款付款
	 */
	@ApiModelProperty(value = "类型收款付款")
	private ReceivablePayableType type;
	/**
	 * 费用名称
	 */
	@ApiModelProperty(value = "费用名称")
	private String feeName;
	/**
	 * 结算金额
	 */
	@ApiModelProperty(value = "结算金额")
	private Double amount;
	/**
	 * 结算币种
	 */
	@ApiModelProperty(value = "结算币种")
	private CustomCurrency currency;
	/**
	 * 转换金额
	 */
	@ApiModelProperty(value = "转换金额")
	private Double convertAmount;
	/**
	 * 转换币种
	 */
	@ApiModelProperty(value = "转换币种")
	private CustomCurrency convertCurrency;
	/**
	 * 说明
	 */
	@ApiModelProperty(value = "说明")
	private String description;

	/**
	 * 账单状态
	 */
	@TableField(exist = false)
	private BillStatus billStatus;
}
