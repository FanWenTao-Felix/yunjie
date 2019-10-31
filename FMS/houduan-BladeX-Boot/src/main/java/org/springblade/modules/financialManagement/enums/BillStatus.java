package org.springblade.modules.financialManagement.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;


public enum BillStatus implements IEnum {
	UNARCHIVE(0, "未归档"),
	SETTLED(1,"已结算"),
	ARCHIVED(2, "已归档");
	private Integer type;
	private String description;

	BillStatus(Integer type, String description) {
		this.type = type;
		this.description = description;
	}

	@Override
	@JsonValue
	public Integer getValue() {
		return this.type;
	}

	public String getDescription() {
		return this.description;
	}
}
