package com.redgear.spreadsheet.converter;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class LongConverter implements TypeConverter<Long> {
	@Override
	public Class<Long> type() {
		return Long.class;
	}

	@Override
	public Long parse(String raw) {
		return Long.parseLong(raw);
	}

	@Override
	public String serialize(Long obj) {
		return obj.toString();
	}
}
