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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.tenant.mp.TenantEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件管理实体类
 *
 * @author BladeX
 * @since 2019-09-12
 */
@Data
@TableName("fms_business_file")
@ApiModel(value = "BusinessFile对象", description = "文件管理")
public class BusinessFile extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	* 内部单号
	*/
		@ApiModelProperty(value = "内部单号")
		private String internalOrderNo;
	/**
	* 文件名
	*/
		@ApiModelProperty(value = "文件名")
		private String name;
	/**
	* 文件路径
	*/
		@ApiModelProperty(value = "文件路径")
		private String path;
	/**
	* 文件类型(空运文件，海运文件)
	*/
		@ApiModelProperty(value = "文件类型(空运文件，海运文件)")
		private Integer type;
	/**
	* 文件描述
	*/
		@ApiModelProperty(value = "文件描述")
		private String description;

	/**
	 * 用于上传的文件
	 */
	@TableField(exist = false)
	private MultipartFile file;


}
