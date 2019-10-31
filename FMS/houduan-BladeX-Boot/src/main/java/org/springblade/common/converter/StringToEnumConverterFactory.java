package org.springblade.common.converter;

import com.baomidou.mybatisplus.core.enums.IEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander
 * @date 2019/9/29 4:01 PM 枚举转换器
 */
public class StringToEnumConverterFactory implements ConverterFactory<String,IEnum> {
	private static final Map<Class, Converter> converterMap =  new HashMap<>();


	@Override
	public <T extends IEnum> Converter<String, T> getConverter(Class<T> targetType) {
		Converter<String, T> converter = converterMap.get(targetType);
		if(converter == null) {
			converter = new StringToEnumConverter<>(targetType);
			converterMap.put(targetType, converter);
		}
		return converter;
	}

	class StringToEnumConverter<T extends IEnum> implements Converter<String, T> {
		private Map<String, T> enumMap = new HashMap<>();

		StringToEnumConverter(Class<T> enumType) {
			T[] enums = enumType.getEnumConstants();
			for(T e : enums) {
				enumMap.put(String.valueOf(e.getValue()), e);
			}
		}
		@Override
		public T convert(String source) {
			T t = enumMap.get(source);
			if (t == null) {
				// 异常可以稍后去捕获
				throw new IllegalArgumentException("No element matches " + source);
			}
			return t;
		}
	}
}
