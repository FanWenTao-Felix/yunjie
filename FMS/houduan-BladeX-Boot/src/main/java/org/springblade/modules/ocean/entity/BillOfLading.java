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
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 实体类
 *
 * @author BladeX
 * @since 2019-09-29
 */
@Data
@TableName("fms_bill_of_lading")
@ApiModel(value = "BillOfLading对象", description = "BillOfLading对象")
public class BillOfLading extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
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
	* 货物名称
	*/
		@ApiModelProperty(value = "货物名称")
		private String cargoName;
	/**
	* 货物描述
	*/
		@ApiModelProperty(value = "货物描述")
		private String description;
	/**
	* 唛头
	*/
		@ApiModelProperty(value = "唛头")
		private String shippingMark;
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
	* 发货人地址
	*/
		@ApiModelProperty(value = "发货人地址")
		private String shipperAddress;
	/**
	* 发货人
	*/
	@JsonSerialize(
		using = ToStringSerializer.class
	)
		@ApiModelProperty(value = "发货人")
		private Long nameShipper;
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
	* 箱量描述
	*/
		@ApiModelProperty(value = "箱量描述")
		private String cartonQuantity;
	/**
	* 货量描述
	*/
		@ApiModelProperty(value = "货量描述")
		private String cargoQuantity;
	/**
	* 正本长数
	*/
		@ApiModelProperty(value = "正本长数")
		private String original;
	/**
	* 签发地点
	*/
		@ApiModelProperty(value = "签发地点")
		private String signSite;
	/**
	* 签发日期
	*/
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
		@ApiModelProperty(value = "签发日期")
		private LocalDateTime signTime;
	/**
	* 签发人
	*/
		@ApiModelProperty(value = "签发人")
		private String signName;
	/**
	* 提单附页
	*/
		@ApiModelProperty(value = "提单附页")
		private String follower;
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


}
