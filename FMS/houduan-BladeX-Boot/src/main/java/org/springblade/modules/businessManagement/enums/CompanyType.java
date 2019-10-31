package org.springblade.modules.businessManagement.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum CompanyType implements IEnum {
	CLIENT("CLIENT","委托人"),
	CONSIGNEE("CONSIGNEE","收货人"),
	CONSIGNOR("CONSIGNOR","发货人"),
	AIR_AGENT("AIR_AGENT","航司代理"),
	AIR_COMPANY("AIR_COMPANY","航空公司"),
	IN_AGENT("IN_AGENT","进仓代理"),
	BOOKING_AGENT("BOOKING_AGENT","订舱代理");

	private String type;
	private String description;

	CompanyType(String type, String description) {
		this.type = type;
		this.description = description;
	}

	@Override
	@JsonValue
	public Serializable getValue() {
		return this.type;
	}

}
