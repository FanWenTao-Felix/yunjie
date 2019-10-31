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

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import org.springblade.modules.financialManagement.enums.InvoiceStatus;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 账单实体类
 *
 * @author BladeX
 * @since 2019-10-16
 */
@Data
@TableName("fms_bill")
@ApiModel(value = "Bill对象", description = "账单")
public class Bill extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 结算单位
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "结算单位")
	private Long settlementUnit;
	/**
	 * 到账日期
	 */
	@DateTimeFormat(
		pattern = "yyyy-MM-dd"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd"
	)
	@ApiModelProperty(value = "到账日期")
	private LocalDate gainedDate;
	/**
	 * 币种
	 */
	@ApiModelProperty(value = "币种")
	private CustomCurrency currency;
	/**
	 * 账单金额
	 */
	@ApiModelProperty(value = "账单金额")
	private Double amount;
	/**
	 * 凭证号
	 */
	@ApiModelProperty(value = "凭证号")
	private String voucherNo;
	/**
	 * 水单号
	 */
	@ApiModelProperty(value = "水单号")
	private String serialNo;
	/**
	 * 结算方式
	 */
	@ApiModelProperty(value = "结算方式")
	private String settlementType;
	/**
	 * 银行账号id
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "银行账号id")
	private Long bankAccountId;
	/**
	 * 银行账号
	 */
	@ApiModelProperty(value = "银行账号")
	private String bankAccount;
	/**
	 * 类型收款付款
	 */
	@ApiModelProperty(value = "类型收款付款")
	private ReceivablePayableType type;
	/**
	 * 开票状态
	 */
	@ApiModelProperty(value = "开票状态")
	private InvoiceStatus invoiceStatus;
	/**
	 * 开票类型
	 */
	@ApiModelProperty(value = "开票类型")
	private String invoiceType;
	/**
	 * 开票号码
	 */
	@ApiModelProperty(value = "开票号码")
	private String invoiceNo;
	/**
	 * 发票日期
	 */
	@DateTimeFormat(
		pattern = "yyyy-MM-dd"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd"
	)
	@ApiModelProperty(value = "发票日期")
	private LocalDate invoiceDate;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;
	/**
	 * 账单状态(是否确认)
	 */
	@ApiModelProperty(value = "账单状态(是否确认)")
	private Boolean isConfirm;


	/**
	 * 发票信息--币种
	 */
	@ApiModelProperty(value = "发票币种")
	private CustomCurrency invoiceCurrency;

	/**
	 * 发票信息--名称
	 */
	@ApiModelProperty(value = "发票信息(就是填写币种)")
	private String invoiceName;

	/**
	 * 发票信息--纳税人识别号
	 */
	@ApiModelProperty(value = "发票信息(纳税人识别号)")
	private String taxpayerDentification;


	/**
	 * 发票信息--电话  地址
	 */
	@ApiModelProperty(value = "发票信息--电话  地址")
	private String invoicePhoneAddress;


	/**
	 * 发票信息--开户行
	 */
	@ApiModelProperty(value = "发票信息--开户行")
	private String invoiceBank;


	/**
	 * 发票信息--账号
	 */
	@ApiModelProperty(value = "发票信息--账号")
	private String invoiceAccount;
	/**
	 * 账单状态
	 */
	@ApiModelProperty(value = "账单状态")
	private BillStatus billStatus;

	/**
	 * 账单实际收付款的货币
	 */
	@ApiModelProperty(value = "账单实际收付款的货币")
	private CustomCurrency finishedCurrency;

	/**
	 * 账单实际收付款的金额
	 */
	@ApiModelProperty(value = "账单实际收付款的金额")
	private Double finishedAmount;


}
