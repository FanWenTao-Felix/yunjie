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
package org.springblade.modules.clientManagement.vo;

import org.springblade.modules.clientManagement.entity.Supplier;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 供应商资料视图实体类
 *
 * @author BladeX
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SupplierVO对象", description = "供应商资料")
public class SupplierVO extends Supplier {
	private static final long serialVersionUID = 1L;

}
