package org.springblade.modules.clientManagement.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum LinkmanType implements IEnum {
	CONSIGNOR(0, "发货人"),
	CONSIGNEE(1, "收货人"),
	NOTIFIER(2,"通知人");
	private Integer type;
	private String description;

	LinkmanType(Integer type, String description) {
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
