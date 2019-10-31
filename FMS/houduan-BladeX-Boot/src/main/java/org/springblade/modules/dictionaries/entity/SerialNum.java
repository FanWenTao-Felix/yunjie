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
package org.springblade.modules.dictionaries.entity;

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
 * 流水号表实体类
 *
 * @author BladeX
 * @since 2019-08-26
 */
@Data
@TableName("fms_serial_num")
@ApiModel(value = "SerialNum对象", description = "流水号表")
public class SerialNum extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 前缀
	 */
	@ApiModelProperty(value = "前缀")
	private String prefix;
	/**
	 * 四位年份
	 */
	@ApiModelProperty(value = "四位年份")
	private String fourDigitYear;
	/**
	 * 两位年份
	 */
	@ApiModelProperty(value = "两位年份")
	private String twoDigitYear;
	/**
	 * 一位年份
	 */
	@ApiModelProperty(value = "一位年份")
	private String oneDigitYear;
	/**
	 * 月份
	 */
	@ApiModelProperty(value = "月份")
	private String month;
	/**
	 * 日期
	 */
	@ApiModelProperty(value = "日期")
	private String day;
	/**
	 * 当前值
	 */
	@ApiModelProperty(value = "当前值")
	private Long currentValue;
	/**
	 * 归零值
	 */
	@ApiModelProperty(value = "归零值")
	private Long zeroingValue;
	/**
	 * 操作
	 */
	@ApiModelProperty(value = "操作")
	private Integer operation;


}
