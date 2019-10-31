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
package org.springblade.modules.ocean.wrapper;

import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.modules.ocean.entity.SeaSpell;
import org.springblade.modules.ocean.vo.SeaSpellVO;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author BladeX
 * @since 2019-09-17
 */
public class SeaSpellWrapper extends BaseEntityWrapper<SeaSpell, SeaSpellVO>  {

	public static SeaSpellWrapper build() {
		return new SeaSpellWrapper();
 	}

	@Override
	public SeaSpellVO entityVO(SeaSpell seaSpell) {
		SeaSpellVO seaSpellVO = BeanUtil.copy(seaSpell, SeaSpellVO.class);

		return seaSpellVO;
	}

}
