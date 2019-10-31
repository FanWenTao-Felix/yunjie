package org.springblade.modules.clientManagement.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum ClientType implements IEnum {
	CLIENT(0, "客户"),
	SUPPLIER(1, "供应商"),
	CLIENT_SUPPLIER(2,"客户兼供应商");
	private Integer type;
	private String description;

	ClientType(Integer type, String description) {
		this.type = type;
		this.description = description;
	}

	@Override
	@JsonValue
	public Serializable getValue() {
		return this.type;
	}

	public String getDescription(){return this.description;}

}
