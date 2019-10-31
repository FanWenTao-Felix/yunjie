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
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体类
 *
 * @author BladeX
 * @since 2019-08-20
 */
@Data
@TableName("fms_billway")
@ApiModel(value = "Billway对象", description = "Billway对象")
public class Billway implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 报关方式ID
	*/
		@ApiModelProperty(value = "报关方式ID")
		@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	* 代码值
	*/
		@ApiModelProperty(value = "代码值")
		private String billwayInfo;
	/**
	* 状态
	*/
		@ApiModelProperty(value = "状态")
		private String status;
	/**
	* 备注信息
	*/
		@ApiModelProperty(value = "备注信息")
		private String billwayBz;


}
