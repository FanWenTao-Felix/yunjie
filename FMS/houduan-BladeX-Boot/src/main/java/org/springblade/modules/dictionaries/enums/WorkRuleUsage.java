package org.springblade.modules.dictionaries.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum WorkRuleUsage implements IEnum {
	AIR(0, "空运"),
	SEA(1, "出口");
	private Integer type;
	private String description;

	WorkRuleUsage(Integer type, String description) {
		this.type = type;
		this.description = description;
	}

	@Override
	@JsonValue
	public Serializable getValue() {
		return this.type;
	}
}
