package org.springblade.modules.clientManagement.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * @author Alexander
 * @date 2019/10/24 10:29 AM
 */
public enum CompanyTypeEnum implements IEnum {
	CLIENT("CLIENT","委托人"),
	AIRLINECOMPANY_AGENT("AIRLINECOMPANY_AGENT","航司代理"),
	AIR_COMPANY("AIR_COMPANY","航空公司"),
	WAREHOUSING_AGENT("WAREHOUSING_AGENT","进仓代理"),
	BOOKING_AGENT("BOOKING_AGENT","订舱代理");

	private String code;
	private String name;

	CompanyTypeEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	@Override
	@JsonValue
	public String getValue() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}
}
