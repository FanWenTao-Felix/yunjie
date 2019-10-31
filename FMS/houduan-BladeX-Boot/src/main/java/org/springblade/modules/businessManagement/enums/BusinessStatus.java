package org.springblade.modules.businessManagement.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum BusinessStatus implements IEnum {
	DRAFT(0, "草稿"),
	UNAUDIT(1, "未审核"),
	AUDITING(2, "审核中"),
	AUDITED(3, "已审核"),
	FINISHED(4, "已完成"),
	REJECT(5, "已拒绝"),
	CANCEL(6, "已取消");
	private Integer status;
	private String description;

	BusinessStatus(Integer status, String description) {
		this.status = status;
		this.description = description;
	}

	@Override
	@JsonValue
	public Serializable getValue() {
		return this.status;
	}
}
