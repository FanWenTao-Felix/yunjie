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
 * @since 2019-08-26
 */
@Data
@TableName("fms_packtype")
@ApiModel(value = "Packtype对象", description = "Packtype对象")
public class Packtype implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* ID
	*/
		@ApiModelProperty(value = "ID")
		@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	* 包装单位简称
	*/
		@ApiModelProperty(value = "包装单位简称")
		private String packReferred;
	/**
	* 中文名称
	*/
		@ApiModelProperty(value = "中文名称")
		private String packCnname;
	/**
	* 英文名称
	*/
		@ApiModelProperty(value = "英文名称")
		private String packEnname;
	/**
	* EDI代码
	*/
		@ApiModelProperty(value = "EDI代码")
		private String packEdi;
	/**
	* 备注
	*/
		@ApiModelProperty(value = "备注")
		private String packBz;


}
