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
@TableName("fms_air")
@ApiModel(value = "Air对象", description = "Air对象")
public class Air implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 空运港ID
	*/
		@ApiModelProperty(value = "空运港ID")
		@TableId(value = "id", type = IdType.AUTO)
		private Long id;
	/**
	 * 所属区域
	 */
		@ApiModelProperty(value = "所属区域")
		private String airContinent;
	/**
	* 国家名/洲名
	*/
		@ApiModelProperty(value = "国家/地区")
		private String airCountryname;
	/**
	 * 城市英文名称
	 */
		@ApiModelProperty(value = "城市英文名称")
		private String airEncityname;
	/**
	 * 城市中文名称
	 */
		@ApiModelProperty(value = "城市中文名称")
		private String airChcityname;
	/**
	* 机场代码
	*/
		@ApiModelProperty(value = "机场代码")
		private String airCode;
	/**
	 * 机场名
	 */
		@ApiModelProperty(value = "机场名")
		private String fullName;

}
