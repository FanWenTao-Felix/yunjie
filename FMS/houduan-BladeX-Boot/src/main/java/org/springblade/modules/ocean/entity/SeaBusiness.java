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
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.core.mp.base.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 实体类
 *
 * @author BladeX
 * @since 2019-09-16
 */
@Data
@TableName("fms_sea_business")
@ApiModel(value = "SeaBusiness对象", description = "SeaBusiness对象")
public class SeaBusiness extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "id", type = IdType.ID_WORKER)
	private Long id;
	/**
	 * 租户id
	 */
	@ApiModelProperty(value = "租户id")
	private String tenantId;
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
	 *//*
		@ApiModelProperty(value = "状态")
		private Integer status;
	*//**
	 * 创建部门
	 *//*
		@ApiModelProperty(value = "创建部门")
		private Long createDept;*/
	/**
	 * 工作单号
	 */
	@ApiModelProperty(value = "工作单号")
	private String internalOrderNo;
	/**
	 * 订舱类型(整箱和拼箱)
	 */
	@ApiModelProperty(value = "订舱类型")
	private String bookingType;
	/**
	 * 提单方式（整本提单，电放提单，海运单）
	 */
	@ApiModelProperty(value = "提单方式")
	private String billWay;
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
	private Long operating;
	/**
	 * 是否主单
	 */
	@ApiModelProperty(value = "是否主单")
	private String isMinute;
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
	 * 创建日期
	 */
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty(value = "创建日期")
	private LocalDateTime creationTime;
	/**
	 * 委托人
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "委托人")
	private String consignor;
	//private String consignor;

	/**
	 * 发货人
	 */

	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "发货人")
	private Long nameShipper;
	/**
	 * 发货人地址
	 */
	@ApiModelProperty(value = "发货人地址")
	private String shipperAddress;
	/**
	 * 收货人
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "收货人")
	private Long consignee;
	/**
	 * 收货人地址
	 */
	@ApiModelProperty(value = "收货人地址")
	private String consigneeShipper;
	/**
	 * 通知人
	 */
	@ApiModelProperty(value = "通知人")
	private String notifier;
	/**
	 * 起运港
	 *//*
	@ApiModelProperty(value = "起运港")
	private String portLoading;
	*//**
	 * 目的港
	 *//*
	@ApiModelProperty(value = "目的港")
	private String destination;
	*//**
	 * 中转港
	 *//*
	@ApiModelProperty(value = "中转港")
	private String transshipment;
	*//**
	 * 船东
	 *//*
	@ApiModelProperty(value = "船东")
	private String shipowner;*/
	/**
	 * 货物名称
	 */
	@ApiModelProperty(value = "货物名称")
	private String cargoName;
	/**
	 * 货物中文名称
	 */
	@ApiModelProperty(value = "货物中文名称")
	//@OneToOne(cascade = {String.CASE_INSENSITIVE_ORDER})
	private String cargoChinese;


	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty(value = "货好时间")
	private LocalDateTime goodsTime;

	/**
	 * 船期
	 */
	@ApiModelProperty(value = "船期")
	private String sailSchedule;
	/**
	 * 货物描述
	 */
	@ApiModelProperty(value = "货物描述")
	private String description;
	/**
	 * 货物备注
	 */
	@ApiModelProperty(value = "货物备注")
	private String cargoRemark;
	/**
	 * 货物件数(包装)
	 */
	@ApiModelProperty(value = "货物件数(包装)")
	private Integer numberUnits;
	/**
	 * 货物(包装)
	 */
	@ApiModelProperty(value = "货物包装")
	private String goodsPackage;

	/**
	 * 货物毛重
	 */
	@ApiModelProperty(value = "货物毛重")
	private Double roughWeight;
	/**
	 * 货物体积
	 */
	@ApiModelProperty(value = "货物体积")
	private Double volume;
	/**
	 * 货值
	 */
	@ApiModelProperty(value = "货值")
	private Double value;


	@ApiModelProperty(value = "出货要求")
	private String deliveryRequirements;




	/**
	 * 出货条款
	 */
	@ApiModelProperty(value = "出货条款")
	private String shipmentClause;

	/**
	 * 销售价
	 */
	@ApiModelProperty(value = "销售价")
	private Double salesPrice;
	/**
	 * 唛头
	 */
	@ApiModelProperty(value = "唛头")
	private String shippingMark;
	/**
	 * 柜型
	 */
	@ApiModelProperty(value = "柜型")
	private String cabinetType;
	/**
	 * 柜量
	 */
	@ApiModelProperty(value = "柜量")
	private Integer quantity;
	/**
	 * 报关行信息
	 */
	@ApiModelProperty(value = "报关行信息")
	private String customsBroker;
	/**
	 * 拖车行信息
	 */
	@ApiModelProperty(value = "拖车行信息")
	private String trailerCompany;
	/**
	 * 业务状态
	 */
	@ApiModelProperty(value = "业务状态")
	private String businessState;

	@ApiModelProperty(value = "费用名称")
	private String feeName;

	@ApiModelProperty(value = "币种")
	private String currency;

	/**
	 * 交接方式
	 */
	@ApiModelProperty(value = "交接方式")
	private String connectWay;
	/**
	 * 运费支付方式
	 */
	@ApiModelProperty(value = "运费支付方式")
	private String freightPayWay;
	/**
	 * 放货方式
	 */
	@ApiModelProperty(value = "放货方式")
	private String releaseCargoWay;
	/**
	 * 供货人
	 */
	@ApiModelProperty(value = "供货人")
	private String supplier;
	/**
	 * 放货人代理
	 */
	@ApiModelProperty(value = "放货人代理")
	private String consigneeAgent;

	/**
	 * 进仓代理
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "进仓代理")
	private Long warehousingAgentId;


}
