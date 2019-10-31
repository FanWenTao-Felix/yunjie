package org.springblade.modules.financialManagement.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum InvoiceStatus implements IEnum {
	NO_APPLICATION_NO_CONFIRM(0, "未申请未确认"),
	APPLICATION_NO_CONFIRM(1, "已申请未确认"),
	NO_APPLICATION_CONFIRM(2, "未申请已确认"),
	APPLICATION_CONFIRM(3, "已申请已确认");
	private Integer type;
	private String description;

	InvoiceStatus(Integer type, String description) {
		this.type = type;
		this.description = description;
	}

	@Override
	@JsonValue
	public Serializable getValue() {
		return this.type;
	}

	public String getDescription() {
		return this.description;
	}
}
