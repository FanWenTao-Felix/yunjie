package org.springblade.modules.businessManagement.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum ReceivablePayableType implements IEnum {
	RECEIVABLE(0, "应收"),
	PAYABLE(1, "应付");
	private Integer status;
	private String description;

	ReceivablePayableType(Integer status, String description) {
		this.status = status;
		this.description = description;
	}

	@Override
	@JsonValue
	public Serializable getValue() {
		return this.status;
	}

}
