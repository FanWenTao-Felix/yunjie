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
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体类
 *
 * @author BladeX
 * @since 2019-08-20
 */
@Data
@TableName("fms_costtype")
@ApiModel(value = "Costtype对象", description = "Costtype对象")
public class Costtype implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 费用ID
	*/
		@ApiModelProperty(value = "费用ID")
		@TableId(value = "id", type = IdType.AUTO)
		private Long id;
	/**
	* 费用简称
	*/
		@ApiModelProperty(value = "费用简称")
		private String costName;
	/**
	* 费用分类
	*/
		@ApiModelProperty(value = "费用分类")
		private String costType;
	/**
	* 费用名称
	*/
		@ApiModelProperty(value = "费用名称")
		private String costAllname;
	/**
	* 英文名称
	*/
		@ApiModelProperty(value = "英文名称")
		private String costEnname;
	/**
	* 默认货币
	*/
		@ApiModelProperty(value = "默认货币")
		private String costDefault;

	/**
	 * 费用代码
	 */
		@ApiModelProperty(value = "费用代码")
		private String costCode;

	/**
	* 状态
	*/
		@ApiModelProperty(value = "状态")
		private String status;
	/**
	* 备注信息
	*/
		@ApiModelProperty(value = "备注信息")
		private String costBz;


}
