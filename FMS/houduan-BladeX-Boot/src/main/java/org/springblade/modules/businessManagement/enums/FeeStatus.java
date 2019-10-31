package org.springblade.modules.businessManagement.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum FeeStatus implements IEnum {
	AUDITING_FAIL(0, "审核不通过"),
	UNAUDITING(1, "未审核"),
	AUDITING(2, "审核中"),
	AUDITED(3, "审核通过"),
	SETTLING(4, "结算中"),
	FINISHED(5, "结算完成");
	private Integer status;
	private String description;

	FeeStatus(Integer status, String description) {
		this.status = status;
		this.description = description;
	}

	@Override
	@JsonValue
	public Serializable getValue() {
		return this.status;
	}

}
