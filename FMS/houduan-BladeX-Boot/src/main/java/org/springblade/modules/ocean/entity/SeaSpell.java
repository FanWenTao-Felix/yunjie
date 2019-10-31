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
 * @since 2019-09-17
 */
@Data
@TableName("fms_sea_spell")
@ApiModel(value = "SeaSpell对象", description = "SeaSpell对象")
public class SeaSpell extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "id", type = IdType.ID_WORKER)
	private Long id;
/*	*//**
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
	@ApiModelProperty(value = "租户id")
	private String tenantId;
	/**
	 *业务订单id
	 */
	@ApiModelProperty(value = "业务订单id")
	private String   businessId;
	/**
	* 工作单号seabusiness
	*/
		@ApiModelProperty(value = "工作单号")
		private String internalOrderNo;
	/**
	* 起运港
	*/
		@ApiModelProperty(value = "起运港")
		private String portLoading;
	/**
	* 装货港POL
	*/
		@ApiModelProperty(value = "装货港POL")
		private String pol;
	/**
	* 装货港ETD
	*/
		@ApiModelProperty(value = "装货港ETD")
		private String proEtd;
	/**
	* 船名
	*/
		@ApiModelProperty(value = "船名/航次")
		private String shipsName;
	/**
	* cfs
	*/
		@ApiModelProperty(value = "cfs")
		private String cfs;
	/**
	* CFS截关时间
	*/
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
		@ApiModelProperty(value = "CFS截关时间")
		private LocalDateTime cfsClosingDate;
	/**
	* 实际离港时间
	*/@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
		@ApiModelProperty(value = "实际离港时间")
		private LocalDateTime airport;
	/**
	* 转拼港T/P
	*/
		@ApiModelProperty(value = "转拼港T/P")
		private String departureAirport;
	/**
	* 卸货港POD
	*/
		@ApiModelProperty(value = "卸货港POD")
		private String pod;
	/**
	* ETA
	*/
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
		@ApiModelProperty(value = "ETA")
		private LocalDateTime eta;
	/**
	* 卸货港CFS
	*/
		@ApiModelProperty(value = "卸货港CFS")
		private String podCfs;
	/**
	* 实际到港时间
	*/
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
		@ApiModelProperty(value = "实际到港时间")
		private LocalDateTime arrivalDat;
	/**
	* 目的港
	*/
		@ApiModelProperty(value = "目的港")
		private String departure;
	/**
	* 拼箱公司
	*/
		@ApiModelProperty(value = "拼箱公司")
		private String jointCompany;

	/**
	* 订舱号
	*/
		@ApiModelProperty(value = "订舱号")
		private String shipping;
	/**
	* 主单号
	*/
		@ApiModelProperty(value = "主单号")
		private String munualFolio;
	/**
	* 分单号
	*/
		@ApiModelProperty(value = "分单号")
		private String houseBill;
	/**
	* 运输要求
	*/
		@ApiModelProperty(value = "运输要求")
		private String transportationRequire;
	/**
	* 运输反馈
	*/
		@ApiModelProperty(value = "运输反馈")
		private String feedback;
	/**
	 * 船东
	 */
	@ApiModelProperty(value = "船东")
	private String shipowner;
}
