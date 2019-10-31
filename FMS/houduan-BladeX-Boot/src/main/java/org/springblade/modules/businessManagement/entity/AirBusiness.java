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
package org.springblade.modules.businessManagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.tenant.mp.TenantEntity;
import org.springblade.modules.businessManagement.enums.*;
import org.springblade.modules.dictionaries.enums.CustomCurrency;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 空运业务实体类
 *
 * @author BladeX
 * @since 2019-09-10
 */
@Data
@TableName("fms_air_business")
@ApiModel(value = "AirBusiness对象", description = "空运业务")
public class AirBusiness extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 内部单号
	 */
	@ApiModelProperty(value = "内部单号")
	private String internalOrderNo;
	/**
	 * 主单号
	 */
	@ApiModelProperty(value = "主单号")
	private String mainOrderNo;
	/**
	 * 分单号
	 */
	@ApiModelProperty(value = "分单号")
	private String subOrderNo;
	/**
	 * 是否主单
	 */
	@ApiModelProperty(value = "是否主单")
	private Boolean isMain;
	/**
	 * 起运港
	 */
	@ApiModelProperty(value = "起运港")
	private String loadingPort;
	/**
	 * 目的港
	 */
	@ApiModelProperty(value = "目的港")
	private String destinationPort;

	/**
	 * 业务状态
	 */
	@ApiModelProperty(value = "业务状态")
	private BusinessStatus businessStatus;

	/**
	 * 委托人
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "委托人")
	private Long client;
	/**
	 * 发货人电话
	 */
	@ApiModelProperty(value = "发货人电话")
	private String consignerPhone;
	/**
	 * 收货人电话
	 */
	@ApiModelProperty(value = "收货人电话")
	private String consigneePhone;
	/**
	 * 发货人
	 */
	@ApiModelProperty(value = "发货人")
	private String consigner;
	/**
	 * 发货人地址
	 */
	@ApiModelProperty(value = "发货人地址")
	private String consignerAddress;
	/**
	 * 收货人
	 */
	@ApiModelProperty(value = "收货人")
	private String consignee;
	/**
	 * 收货人地址
	 */
	@ApiModelProperty(value = "收货人地址")
	private String consigneeAddress;
	/**
	 * 通知人
	 */
	@ApiModelProperty(value = "通知人")
	private String notifier;
	/**
	 * 通知人电话
	 */
	@ApiModelProperty(value = "通知人电话")
	private String notifierPhone;
	/**
	 * 通知人地址
	 */
	@ApiModelProperty(value = "通知人地址")
	private String notifierAddress;
	/**
	 * 货物中文名称
	 */
	@ApiModelProperty(value = "货物中文名称")
	private String goodsChineseName;
	/**
	 * 货物英文名
	 */
	@ApiModelProperty(value = "货物英文名")
	private String goodsEnglishName;
	/**
	 * 货物描述
	 */
	@ApiModelProperty(value = "货物描述")
	private String goodsDescription;
	/**
	 * 货物件数
	 */
	@ApiModelProperty(value = "货物件数")
	private Integer goodsAmount;
	/**
	 * 货物包装
	 */
	@ApiModelProperty(value = "货物包装")
	private String goodsPackage;
	/**
	 * 货物毛重
	 */
	@ApiModelProperty(value = "货物毛重")
	private Double goodsGrossWeight;
	/**
	 * 货物长
	 */
	@ApiModelProperty(value = "货物长")
	private Double goodsLength;
	/**
	 * 货物宽
	 */
	@ApiModelProperty(value = "货物宽")
	private Double goodsWidth;
	/**
	 * 货物高
	 */
	@ApiModelProperty(value = "货物高")
	private Double goodsHeight;
	/**
	 * 货物体积
	 */
	@ApiModelProperty(value = "货物体积")
	private Double goodsVolumn;
	/**
	 * 货物收费重量
	 */
	@ApiModelProperty(value = "货物收费重量")
	private Double goodsChargeableWeight;

	/**
	 * 收费价格/kg
	 */
	@ApiModelProperty(value = "收费价格/kg")
	private Double chargePrice;

	/**
	 * 收费货币
	 */
	@ApiModelProperty(value = "收费货币")
	private CustomCurrency chargeCurrency;

	/**
	 * 运价等级
	 */
	@ApiModelProperty(value = "运价等级")
	private String cargoRate;

	/**
	 * 起运港全称
	 */
	@ApiModelProperty(value = "起运港全称")
	private String loadingPortFullName;
	/**
	 * 目的港全称
	 */
	@ApiModelProperty(value = "目的港全称")
	private String destinationPortFullName;

	/**
	 * 唛头
	 */
	@ApiModelProperty(value = "唛头")
	private String mark;

	/**
	 * 入货重量
	 */
	@ApiModelProperty(value = "入货重量")
	private Double inWeight;
	/**
	 * 入货收费重量
	 */
	@ApiModelProperty(value = "入货收费重量")
	private Double inChargeableWeight;
	/**
	 * 入货件数
	 */
	@ApiModelProperty(value = "入货件数")
	private Integer inAmount;
	/**
	 * 入货体积
	 */
	@ApiModelProperty(value = "入货体积")
	private Double inVolumn;
	/**
	 * 报关重量
	 */
	@ApiModelProperty(value = "报关重量")
	private Double customsDeclarationWeight;
	/**
	 * 报关件数
	 */
	@ApiModelProperty(value = "报关件数")
	private Integer customsDeclarationAmount;
	/**
	 * 货站重量
	 */
	@ApiModelProperty(value = "货站重量")
	private Double cargoTerminalWeight;
	/**
	 * 货站体积
	 */
	@ApiModelProperty(value = "货站体积")
	private Double cargoTerminalVolumn;

	/**
	 * 运输声明价值
	 */
	@ApiModelProperty(value = "运输声明价值")
	private String declaredValueForCarriage;


	/**
	 * 海关声明价值
	 */
	@ApiModelProperty(value = "海关声明价值")
	private String declaredValueForCustoms;

	/**
	 * 保险金额
	 */
	@ApiModelProperty(value = "保险金额")
	private Double amountOfInsurance;


	/**
	 * 承运人id
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "承运人id")
	private Long carrierId;
	/**
	 * 业务类型(进出口)
	 */
	@ApiModelProperty(value = "业务类型(进出口)")
	private BusinessType businessType;

	/**
	 * 空运费支付类型(预付，到付)
	 */
	@ApiModelProperty(value = "空运费支付类型(预付，到付)")
	private FeePayType airFeePayType;

	/**
	 * 提单费用货币币种
	 */
	@ApiModelProperty(value = "提单费用货币币种")
	private CustomCurrency ladingBillFeeCurrency;

	/**
	 * 其他费用列表
	 */
	@ApiModelProperty(value = "其他费用列表")
	private String otherFeeList;

	/**
	 * 其他费支付类型(预付，到付)
	 */
	@ApiModelProperty(value = "其他费支付类型(预付，到付)")
	private FeePayType otherFeePayType;


	/**
	 * 航司代理id
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "航司代理id")
	private Long airlineCompanyAgentId;

	/**
	 * 订舱代理id
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "订舱代理id")
	private Long bookingAgentId;
	/**
	 * 订舱状态
	 */
	@ApiModelProperty(value = "订舱状态")
	private BookingStatus bookingStatus;
	/**
	 * 订舱日期
	 */
	@DateTimeFormat(
		pattern = "yyyy-MM-dd"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd"
	)
	@ApiModelProperty(value = "订舱日期")
	private LocalDate bookingDate;


	/**
	 * 航线
	 */
	@ApiModelProperty(value = "航线")
	private String airLine;
	/**
	 * 航班
	 */
	@ApiModelProperty(value = "航班")
	private String flight;

	/**
	 * 航班时间
	 */
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty(value = "航班时间")
	private LocalDateTime flightTime;

	/**
	 * 截单时间
	 */
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty(value = "截单时间")
	private LocalDateTime documentaryOffDate;

	/**
	 * 公布运价
	 */
	@ApiModelProperty(value = "公布运价")
	private Double publishFare;

	/**
	 * 截关时间

	 @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss"
	 )
	 @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss"
	 )
	 @ApiModelProperty(value = "截关时间")
	 private LocalDateTime closingTime;
	 /**
	  * 另行通知
	 @ApiModelProperty(value = "另行通知")
	 private String handlingInformation;
	 /**
	  * 截货时间
	 @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss"
	 )
	 @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss"
	 )

	 @ApiModelProperty(value = "截货时间")
	 private LocalDateTime cargoTerminalTime;
	 */

	/**
	 * 一程目的港
	 */
	@ApiModelProperty(value = "一程目的港")
	private String fbDestinationPort;
	/**
	 * 一程起飞时间
	 */
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty(value = "一程起飞时间")
	private LocalDateTime fbLaunchTime;
	/**
	 * 一程到达时间
	 */
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty(value = "一程到达时间")
	private LocalDateTime fbArrivalTime;
	/**
	 * 一程状态(拉货0,起飞1，到达2)
	 */
	@ApiModelProperty(value = "一程状态(拉货0,起飞1，到达2)")
	private FlightStatus fbStatus;
	/**
	 * 二程目的港
	 */
	@ApiModelProperty(value = "二程目的港")
	private String sbDestinationPort;
	/**
	 * 二程起飞时间
	 */
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty(value = "二程起飞时间")
	private LocalDateTime sbLaunchTime;
	/**
	 * 二程到达时间
	 */
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty(value = "二程到达时间")
	private LocalDateTime sbArrivalTime;
	/**
	 * 二程状态(拉货0,起飞1，到达2)
	 */
	@ApiModelProperty(value = "二程状态(拉货0,起飞1，到达2)")
	private FlightStatus sbStatus;

	/**
	 * 业务员
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "业务员")
	private Long salesman;

	/**
	 * 操作员
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "操作员")
	private Long operator;


	/**
	 * 进仓代理
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "进仓代理")
	private Long warehousingAgentId;

	/**
	 * 航程
	 */
	@ApiModelProperty(value = "航程")
	private String voyage;

	/**
	 * 货物尺寸描述
	 */
	@ApiModelProperty(value = "货物尺寸描述")
	private String measurement;

	/**
	 * 费用是否审核全部了,是就费用不能添加和修改
	 */
	private Boolean isVerifiedFee;

	/**
	 * 货物尺寸
	 */
	@ApiModelProperty(value = "货物尺寸分别录入信息")
	private String sizeList;

	/**
	 * 作业项目
	 */
	@TableField(exist = false)
	@ApiModelProperty(value = "作业项目")
	private String task;
	/**
	 * 作业时间
	 */
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty(value = "作业项目")
	@TableField(exist = false)
	private LocalDateTime taskTime;
}
