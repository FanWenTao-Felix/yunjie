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

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 作业项目实体类
 *
 * @author BladeX
 * @since 2019-10-25
 */
@Data
@TableName("fms_task")
@ApiModel(value = "Task对象", description = "作业项目")
public class Task implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "主键")
	private Long id;
	/**
	 * 作业名称
	 */
	@ApiModelProperty(value = "作业名称")
	private String taskName;
	/**
	 * 内部订单号
	 */
	@ApiModelProperty(value = "内部订单号")
	private String internalOrderNo;
	/**
	 * 地点
	 */
	@ApiModelProperty(value = "地点")
	private String location;
	/**
	 * 服务商(关联client_data_id)
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "服务商(关联client_data_id)")
	private Long providerId;
	/**
	 * 开始时间
	 */
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty(value = "开始时间")
	private LocalDateTime beginTime;
	/**
	 * 完成时间
	 */
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty(value = "完成时间")
	private LocalDateTime finishTime;
	/**
	 * 项目要求
	 */
	@ApiModelProperty(value = "项目要求")
	private String requirement;
	/**
	 * 项目反馈
	 */
	@ApiModelProperty(value = "项目反馈")
	private String feedback;
	/**
	 * 归档
	 */
	@ApiModelProperty(value = "归档")
	private Boolean isArchive;


}
