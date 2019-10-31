package org.springblade.modules.businessManagement.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum FlightStatus implements IEnum {
	NONE(0,"无状态"),
	CARGO(1, "拉货中"),
	LAUNCH(2, "起飞"),
	ARRIVED(3, "到达");

	private Integer status;
	private String description;

	FlightStatus(Integer status, String description) {
		this.status = status;
		this.description = description;
	}

	@Override
	@JsonValue
	public Serializable getValue() {
		return this.status;
	}
	public String getDescription() {
		return this.description;
	}
}
