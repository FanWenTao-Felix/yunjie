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

import org.springblade.common.constant.CommonConstant;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.businessManagement.entity.BusinessFile;
import org.springblade.modules.businessManagement.vo.BusinessFileVO;
import org.springblade.modules.businessManagement.mapper.BusinessFileMapper;
import org.springblade.modules.businessManagement.service.IBusinessFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileFilter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 文件管理 服务实现类
 *
 * @author BladeX
 * @since 2019-09-12
 */
@Service
public class BusinessFileServiceImpl extends BaseServiceImpl<BusinessFileMapper, BusinessFile> implements IBusinessFileService {

	@Override
	public IPage<BusinessFileVO> selectBusinessFilePage(IPage<BusinessFileVO> page, BusinessFileVO businessFile) {
		return page.setRecords(baseMapper.selectBusinessFilePage(page, businessFile));
	}


	@Transactional
	public Boolean saveFile(BusinessFile file) {
		MultipartFile multipartFile = file.getFile();
		file.setName(multipartFile.getOriginalFilename());
		String directoryPath = CommonConstant.UPLOADFILEPATH + "/" + file.getInternalOrderNo();
		file.setPath(directoryPath);
		File directory = new File(directoryPath);
		if (!save(file)) throw new RuntimeException("保存失败");
		if (!directory.exists()) if (!directory.mkdirs()) throw new RuntimeException("文件夹创建失败");
		File final_file = new File(file.getPath() + "/" + file.getName());
		try {
			multipartFile.transferTo(final_file);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件写入失败");
		}
		return true;
	}

	@Transactional
	public Boolean removeFiles(String ids) {
		List<Long> longList = Func.toLongList(ids);
		Collection<BusinessFile> fileList = listByIds(longList);
		removeByIds(longList);
		fileList.forEach(item -> {
			File f = new File(item.getPath() + "/" + item.getName());
			if (f.exists()) if (!f.delete()) throw new RuntimeException(item.getPath() + "删除失败");
			File d = new File(item.getPath());
			File[] fl = d.listFiles();
			for (int i = 0; i < fl.length; i++) {
				if (fl[i].getName().equals(".DS_Store")) fl[i].delete();
			}
			if (d.listFiles().length == 0) if (!d.delete()) throw new RuntimeException(d.getPath() + "文件夹删除失败");

		});
		return true;
	}

	public void downloadFile(BusinessFile businessFile, HttpServletResponse response) {
		CommonUtil.getFileOnServer(businessFile.getPath() + "/" + businessFile.getName(), response);
	}

}
