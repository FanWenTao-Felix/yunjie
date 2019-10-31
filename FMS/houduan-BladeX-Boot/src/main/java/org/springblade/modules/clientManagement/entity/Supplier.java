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
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 供应商资料实体类
 *
 * @author BladeX
 * @since 2019-08-28
 */
@Data
@TableName("fms_supplier")
@ApiModel(value = "Supplier对象", description = "供应商资料")
public class Supplier extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	* 客户简称
	*/
		@ApiModelProperty(value = "客户简称")
		private String shortName;
	/**
	* 全称
	*/
		@ApiModelProperty(value = "全称")
		private String fullName;
	/**
	* 英文名
	*/
		@ApiModelProperty(value = "英文名")
		private String englishName;
	/**
	* 联系电话
	*/
		@ApiModelProperty(value = "联系电话")
		private String contactNumber;
	/**
	* 传真机
	*/
		@ApiModelProperty(value = "传真机")
		private String faxNumber;
	/**
	* 邮政编码
	*/
		@ApiModelProperty(value = "邮政编码")
		private String postalCode;
	/**
	* 手机
	*/
		@ApiModelProperty(value = "手机")
		private String phone;
	/**
	* 中文地址
	*/
		@ApiModelProperty(value = "中文地址")
		private String chineseAddress;
	/**
	* 英文地址
	*/
		@ApiModelProperty(value = "英文地址")
		private String englishAddress;
	/**
	* 网站地址
	*/
		@ApiModelProperty(value = "网站地址")
		private String webAddress;
	/**
	* E_Mail
	*/
		@ApiModelProperty(value = "E_Mail")
		private String eMail;
	/**
	* 国家
	*/
		@ApiModelProperty(value = "国家")
		private String country;
	/**
	* 所属地区
	*/
		@ApiModelProperty(value = "所属地区")
		private String district;
	/**
	* 所属城市
	*/
		@ApiModelProperty(value = "所属城市")
		private String city;
	/**
	* 信誉等级
	*/
		@ApiModelProperty(value = "信誉等级")
		private Integer creditLevel;
	/**
	* 单位代码
	*/
		@ApiModelProperty(value = "单位代码")
		private String unitCode;
	/**
	* 审核状态
	*/
		@ApiModelProperty(value = "审核状态")
		private Integer examineStatus;
	/**
	* 业务类型
	*/
		@ApiModelProperty(value = "业务类型")
		private Integer businessType;
	/**
	* 客户来源
	*/
		@ApiModelProperty(value = "客户来源")
		private String clientSource;
	/**
	* 客户分组
	*/
		@ApiModelProperty(value = "客户分组")
		private String clientGroup;
	/**
	* 客户类型
	*/
		@ApiModelProperty(value = "客户类型")
		private String clientType;
	/**
	* 应收客户
	*/
		@ApiModelProperty(value = "应收客户")
		private Boolean receivableClient;
	/**
	* 应付客户
	*/
		@ApiModelProperty(value = "应付客户")
		private Boolean payableClient;
	/**
	* 共享资料
	*/
		@ApiModelProperty(value = "共享资料")
		private Boolean dataSharing;
	/**
	* 重点关注
	*/
		@ApiModelProperty(value = "重点关注")
		private Boolean focusClient;
	/**
	* 无价值客户
	*/
		@ApiModelProperty(value = "无价值客户")
		private Boolean worthlessClient;
	/**
	* 直客
	*/
		@ApiModelProperty(value = "直客")
		private Boolean directCnee;
	/**
	* 国外有分部
	*/
		@ApiModelProperty(value = "国外有分部")
		private Boolean foreignBranch;
	/**
	* 公司类型
	*/
		@ApiModelProperty(value = "公司类型")
		private String companyType;
	/**
	* 备注
	*/
		@ApiModelProperty(value = "备注")
		private String remark;
	/**
	* 财务核算代码
	*/
		@ApiModelProperty(value = "财务核算代码")
		private String financialAccountingCode;
	/**
	* 拖欠限额中的数目
	*/
		@ApiModelProperty(value = "拖欠限额中的数目")
		private Double limitedArrearsAmount;
	/**
	* 拖欠限额中的货币
	*/
		@ApiModelProperty(value = "拖欠限额中的货币")
		private String limitedArrearsCurrency;
	/**
	* 付款期限中的天数
	*/
		@ApiModelProperty(value = "付款期限中的天数")
		private Integer paytimeDay;
	/**
	* 付款期限中的时间跨度，是日结还是月结等等
	*/
		@ApiModelProperty(value = "付款期限中的时间跨度，是日结还是月结等等")
		private Integer paytimeSpan;
	/**
	* 付款期限中的日期类型，是出仓日期还是入仓日期等等
	*/
		@ApiModelProperty(value = "付款期限中的日期类型，是出仓日期还是入仓日期等等")
		private Integer paytimeType;
	/**
	* 含税
	*/
		@ApiModelProperty(value = "含税")
		private Boolean includingTax;
	/**
	* 税率(%)
	*/
		@ApiModelProperty(value = "税率(%)")
		private Double taxRate;
	/**
	* 商业范围
	*/
		@ApiModelProperty(value = "商业范围")
		private String businessRange;


}
