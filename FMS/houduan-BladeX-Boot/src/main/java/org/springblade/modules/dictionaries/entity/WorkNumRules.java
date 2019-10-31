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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.tenant.mp.TenantEntity;
import org.springblade.modules.dictionaries.enums.WorkRuleUsage;
import org.springblade.modules.dictionaries.enums.ZeroingType;

/**
 * 工作号规则实体类
 *
 * @author BladeX
 * @since 2019-09-03
 */
@Data
@TableName("fms_work_num_rules")
@ApiModel(value = "WorkNumRules对象", description = "工作号规则")
public class WorkNumRules extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 前缀
	 */
	@ApiModelProperty(value = "前缀")
	private String prefix;
	/**
	 * 生成规则
	 */
	@ApiModelProperty(value = "生成规则")
	private String rule;
	/**
	 * 用途
	 */
	@ApiModelProperty(value = "用途")
	private WorkRuleUsage purpose;
	/**
	 * 当前流水号
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "当前流水号")
	private Long currentSerialNum;
	/**
	 * 归零方式
	 */
	@ApiModelProperty(value = "归零方式")
	private ZeroingType zeroingType;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remarks;
	/**
	 * 使用人员
	 */
	@ApiModelProperty(value = "使用人员")
	private String users;
	/**
	 * 生成规则选择用的年份显示位数
	 */
	@ApiModelProperty(value = "生成规则选择用的年份显示位数")
	private Integer digitOfYear;
	/**
	 * 生成规则包含月份
	 */
	@ApiModelProperty(value = "生成规则包含月份")
	private Boolean ruleMonth;
	/**
	 * 生成规则包含日
	 */
	@ApiModelProperty(value = "生成规则包含日")
	private Boolean ruleDay;
	/**
	 * 生成规则包含目的港
	 */
	@ApiModelProperty(value = "生成规则包含目的港")
	private Boolean rulePortDestination;
	/**
	 * 生成规则的流水号位数
	 */
	@ApiModelProperty(value = "生成规则的流水号位数")
	private Integer serialNumDigit;
	/**
	 * 上一次创建时间
	 */
	@ApiModelProperty(value = "上一次创建时间")
	private LocalDate lastCreateDate;


}
