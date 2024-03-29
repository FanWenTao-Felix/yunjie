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
package org.springblade.modules.businessManagement.service.impl;

import org.springblade.modules.businessManagement.entity.TaskName;
import org.springblade.modules.businessManagement.vo.TaskNameVO;
import org.springblade.modules.businessManagement.mapper.TaskNameMapper;
import org.springblade.modules.businessManagement.service.ITaskNameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 作业项目名管理 服务实现类
 *
 * @author BladeX
 * @since 2019-10-25
 */
@Service
public class TaskNameServiceImpl extends ServiceImpl<TaskNameMapper, TaskName> implements ITaskNameService {

	@Override
	public IPage<TaskNameVO> selectTaskNamePage(IPage<TaskNameVO> page, TaskNameVO taskName) {
		return page.setRecords(baseMapper.selectTaskNamePage(page, taskName));
	}

}
