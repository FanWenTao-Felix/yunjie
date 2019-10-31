package org.springblade.modules.businessManagement.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum FeePayType implements IEnum {
	PREPAID(0, "预付"),
	COLLECT(1, "到付");
	private Integer status;
	private String description;

	FeePayType(Integer status, String description) {
		this.status = status;
		this.description = description;
	}

	@Override
	@JsonValue
	public Serializable getValue() {
		return this.status;
	}

}
