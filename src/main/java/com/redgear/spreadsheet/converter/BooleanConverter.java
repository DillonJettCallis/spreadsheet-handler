package com.redgear.spreadsheet.converter;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class BooleanConverter implements TypeConverter<Boolean> {


	@Override
	public Class<Boolean> type() {
		return Boolean.class;
	}

	@Override
	public Boolean parse(String raw) {
		return Boolean.parseBoolean(raw);
	}

	@Override
	public String serialize(Boolean obj) {
		return obj.toString();
	}
}
