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
package org.springblade.modules.businessstatistics.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体类
 *
 * @author BladeX
 * @since 2019-09-23
 */
@Data
@TableName("fms_salesmanagement")
@ApiModel(value = "Salesmanagement对象", description = "Salesmanagement对象")
public class Businessstatistics implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 主键
	*/
		@ApiModelProperty(value = "主键")
		@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	* 创建人
	*/
		@ApiModelProperty(value = "创建人")
		private Long createUser;
	/**
	* 创建时间
	*/
		@ApiModelProperty(value = "创建时间")
		private LocalDateTime createTime;
	/**
	* 修改人
	*/
		@ApiModelProperty(value = "修改人")
		private Long updateUser;
	/**
	* 修改时间
	*/
		@ApiModelProperty(value = "修改时间")
		private LocalDateTime updateTime;
	/**
	* 是否已删除
	*/
		@ApiModelProperty(value = "是否已删除")
		private Integer isDeleted;
	/**
	* 状态
	*/
		@ApiModelProperty(value = "状态")
		private Integer status;
	/**
	* 创建部门
	*/
		@ApiModelProperty(value = "创建部门")
		private Long createDept;
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
		private Integer isMain;
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
	* 操作状态
	*/
		@ApiModelProperty(value = "操作状态")
		private Integer operationStatus;
	/**
	* 委托人
	*/
		@ApiModelProperty(value = "委托人")
		private String client;
	/**
	* 发货人id
	*/
		@ApiModelProperty(value = "发货人id")
		private Long consignerId;
	/**
	* 收货人id
	*/
		@ApiModelProperty(value = "收货人id")
		private Long consigneeId;
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
	* 货物毛量
	*/
		@ApiModelProperty(value = "货物毛量")
		private Double goodsGrossWeight;
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
	* 运价等级
	*/
		@ApiModelProperty(value = "运价等级")
		private String cargoRate;
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
	* 承运人id
	*/
		@ApiModelProperty(value = "承运人id")
		private Long carrierId;
	/**
	* 业务类型(进出口)
	*/
		@ApiModelProperty(value = "业务类型(进出口)")
		private Integer businessType;
	/**
	* 订舱状态
	*/
		@ApiModelProperty(value = "订舱状态")
		private Integer bookingStatus;
	/**
	* 订舱日期
	*/
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
	* 截关时间
	*/
		@ApiModelProperty(value = "截关时间")
		private LocalDateTime closingTime;
	/**
	* 截货时间
	*/
		@ApiModelProperty(value = "截货时间")
		private LocalDateTime cargoTerminalTime;
	/**
	* 一程目的港
	*/
		@ApiModelProperty(value = "一程目的港")
		private String fbDestinationPort;
	/**
	* 一程起飞时间
	*/
		@ApiModelProperty(value = "一程起飞时间")
		private LocalDateTime fbLaunchTime;
	/**
	* 一程到达时间
	*/
		@ApiModelProperty(value = "一程到达时间")
		private LocalDateTime fbArrivalTime;
	/**
	* 一程状态(拉货0,起飞1，到达2)
	*/
		@ApiModelProperty(value = "一程状态(拉货0,起飞1，到达2)")
		private Integer fbStatus;
	/**
	* 二程目的港
	*/
		@ApiModelProperty(value = "二程目的港")
		private String sbDestinationPort;
	/**
	* 二程起飞时间
	*/
		@ApiModelProperty(value = "二程起飞时间")
		private LocalDateTime sbLaunchTime;
	/**
	* 二程到达时间
	*/
		@ApiModelProperty(value = "二程到达时间")
		private LocalDateTime sbArrivalTime;
	/**
	* 二程状态(拉货0,起飞1，到达2)
	*/
		@ApiModelProperty(value = "二程状态(拉货0,起飞1，到达2)")
		private Integer sbStatus;
	/**
	* 入仓费用金额
	*/
		@ApiModelProperty(value = "入仓费用金额")
		private Double warehousingFee;
	/**
	* 入仓费用货币
	*/
		@ApiModelProperty(value = "入仓费用货币")
		private String warehousingFeeCurrency;
	/**
	* 打板费用
	*/
		@ApiModelProperty(value = "打板费用")
		private Double packingFee;
	/**
	* 打板费用货币
	*/
		@ApiModelProperty(value = "打板费用货币")
		private String packingFeeCurrency;
	/**
	* 航空费用
	*/
		@ApiModelProperty(value = "航空费用")
		private Double flightFee;
	/**
	* 航空费用货币
	*/
		@ApiModelProperty(value = "航空费用货币")
		private String flightFeeCurrency;
	/**
	* 业务员
	*/
		@ApiModelProperty(value = "业务员")
		private Long salesman;
	/**
	* 运输类型
	*/
		@ApiModelProperty(value = "运输类型")
		private Integer transportType;
	/**
	* 操作员
	*/
		@ApiModelProperty(value = "操作员")
		private Long operator;
	/**
	* 贸易条款
	*/
		@ApiModelProperty(value = "贸易条款")
		private String transportClause;
	/**
	* 备注
	*/
		@ApiModelProperty(value = "备注")
		private String bz;
	/**
	* 订舱类型(整箱和拼箱)
	*/
		@ApiModelProperty(value = "订舱类型(整箱和拼箱)")
		private String bookingType;
	/**
	* 提单方式（整本提单，电放提单，海运单）
	*/
		@ApiModelProperty(value = "提单方式（整本提单，电放提单，海运单）")
		private String billWay;
	/**
	* 是否主单
	*/
		@ApiModelProperty(value = "是否主单")
		private String isMinute;
	/**
	* 创建日期
	*/
		@ApiModelProperty(value = "创建日期")
		private LocalDateTime creationTime;
	/**
	* 委托人
	*/
		@ApiModelProperty(value = "委托人")
		private String consignor;
	/**
	* 发货人
	*/
		@ApiModelProperty(value = "发货人")
		private Long nameShipper;
	/**
	* 发货人地址
	*/
		@ApiModelProperty(value = "发货人地址")
		private String shipperAddress;
	/**
	* 收货人地址
	*/
		@ApiModelProperty(value = "收货人地址")
		private String consigneeShipper;
	/**
	* 起运港
	*/
		@ApiModelProperty(value = "起运港")
		private String portLoading;
	/**
	* 目的港
	*/
		@ApiModelProperty(value = "目的港")
		private String destination;
	/**
	* 中转港
	*/
		@ApiModelProperty(value = "中转港")
		private String transshipment;
	/**
	* 船东
	*/
		@ApiModelProperty(value = "船东")
		private String shipowner;
	/**
	* 货物名称
	*/
		@ApiModelProperty(value = "货物名称")
		private String cargoName;
	/**
	* 货物中文名称
	*/
		@ApiModelProperty(value = "货物中文名称")
		private String cargoChinese;
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
	/**
	* 运输方式
	*/
		@ApiModelProperty(value = "运输方式")
		private String tranType;
	/**
	* 订单类型
	*/
		@ApiModelProperty(value = "订单类型")
		private String ordertype;


}
