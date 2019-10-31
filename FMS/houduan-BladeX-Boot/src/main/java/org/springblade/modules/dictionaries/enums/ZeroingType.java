package org.springblade.modules.dictionaries.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum ZeroingType implements IEnum {
	NO_ZEROING(0, "不归零"),
	DAY_ZEROING(1, "按日归零"),
	MONTH_ZEROING(2, "按月归零"),
	YEAR_ZEROING(3, "按年归零");
	private Integer type;
	private String description;

	ZeroingType(Integer type, String description) {
		this.type = type;
		this.description = description;
	}

	@Override
	@JsonValue
	public Serializable getValue() {
		return this.type;
	}
}
