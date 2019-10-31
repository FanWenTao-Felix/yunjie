package org.springblade.modules.businessManagement.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum BusinessType implements IEnum {
	IMPORT(0, "进口"),
	EXPORT(1, "出口");
	private Integer status;
	private String description;

	BusinessType(Integer status, String description) {
		this.status = status;
		this.description = description;
	}

	@Override
	@JsonValue
	public Serializable getValue() {
		return this.status;
	}

}
