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
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 银行账号实体类
 *
 * @author BladeX
 * @since 2019-09-23
 */
@Data
@TableName("fms_bank_account")
@ApiModel(value = "BankAccount对象", description = "银行账号")
public class BankAccount extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 账号
	*/
		@ApiModelProperty(value = "账号")
		private String accountNumber;
	/**
	* 户名
	*/
		@ApiModelProperty(value = "户名")
		private String accountName;
	/**
	* 开户银行
	*/
		@ApiModelProperty(value = "开户银行")
		private String bankName;
	/**
	* 货币
	*/
		@ApiModelProperty(value = "货币")
		private String currency;
	/**
	* 企业签章
	*/
		@ApiModelProperty(value = "企业签章")
		private String enterpriseSignature;
	/**
	* 工商登记号
	*/
		@ApiModelProperty(value = "工商登记号")
		private String commercialRegistrationNo;
	/**
	* 银行电话
	*/
		@ApiModelProperty(value = "银行电话")
		private String bankPhone;
	/**
	* 税务登记号
	*/
		@ApiModelProperty(value = "税务登记号")
		private String taxRegistrationNo;
	/**
	* 财务电话
	*/
		@ApiModelProperty(value = "财务电话")
		private String financialPhone;
	/**
	* 地址
	*/
		@ApiModelProperty(value = "地址")
		private String address;
	/**
	* swift code
	*/
		@ApiModelProperty(value = "swift code")
		private String swiftCode;
	/**
	* 可用
	*/
		@ApiModelProperty(value = "可用")
		private Boolean disabled;


}
