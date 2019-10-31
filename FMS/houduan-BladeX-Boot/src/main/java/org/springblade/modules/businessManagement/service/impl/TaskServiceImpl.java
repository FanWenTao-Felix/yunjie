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

import org.springblade.modules.businessManagement.entity.Task;
import org.springblade.modules.businessManagement.vo.TaskVO;
import org.springblade.modules.businessManagement.mapper.TaskMapper;
import org.springblade.modules.businessManagement.service.ITaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 作业项目 服务实现类
 *
 * @author BladeX
 * @since 2019-10-25
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

	@Override
	public IPage<TaskVO> selectTaskPage(IPage<TaskVO> page, TaskVO task) {
		return page.setRecords(baseMapper.selectTaskPage(page, task));
	}

}
