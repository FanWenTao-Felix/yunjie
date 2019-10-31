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
package org.springblade.modules.businessManagement.mapper;

import org.springblade.modules.businessManagement.entity.TaskName;
import org.springblade.modules.businessManagement.vo.TaskNameVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

/**
 * 作业项目名管理 Mapper 接口
 *
 * @author BladeX
 * @since 2019-10-25
 */
public interface TaskNameMapper extends BaseMapper<TaskName> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param taskName
	 * @return
	 */
	List<TaskNameVO> selectTaskNamePage(IPage page, TaskNameVO taskName);

}
