package org.springblade.modules.dictionaries.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;


public enum CustomCurrency implements IEnum {
	CNY("CNY", "人民币"),
	HKD("HKD", "港币"),
	USD("USD", "美元"),
	GBP("GBP","英镑"),
	EUR("EUR", "欧元");
	private String currency;
	private String description;

	CustomCurrency(String currency, String description) {
		this.currency = currency;
		this.description = description;
	}

	@Override
	@JsonValue
	public String getValue() {
		return this.currency;
	}

}
