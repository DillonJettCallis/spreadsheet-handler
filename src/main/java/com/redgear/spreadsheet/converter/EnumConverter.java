package com.redgear.spreadsheet.converter;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class EnumConverter<T extends Enum> implements TypeConverter<T> {

	private final Class<T> type;
	private final Map<String, T> enumConstMap = new HashMap<>();

	public EnumConverter(Class<T> type) {
		this.type = type;
		T[] enums = type().getEnumConstants();

		for(T t : enums) {
			enumConstMap.put(StringUtils.upperCase(t.name()), t);
		}
	}

	public static <T extends Enum> EnumConverter<T> of(Class<T> type) {
		return new EnumConverter<>(type);
	}

	@Override
	public Class<T> type() {
		return type;
	}

	@Override
	public T parse(String raw) {
		return enumConstMap.get(StringUtils.upperCase(StringUtils.trim(raw)));
	}

	@Override
	public String serialize(T obj) {
		return obj.name();
	}
}
