package org.springblade.modules.businessManagement.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum BookingStatus implements IEnum {
	UNBOOKED(0, "未订舱"),
	BOOKED(1, "已订舱");
	private Integer status;
	private String description;

	BookingStatus(Integer status, String description) {
		this.status = status;
		this.description = description;
	}

	@JsonValue
	@Override
	public Serializable getValue() {
		return this.status;
	}

}
