package com.redgear.spreadsheet.converter;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class StringConverter implements TypeConverter<String> {


	@Override
	public Class<String> type() {
		return String.class;
	}

	@Override
	public String parse(String raw) {
		return raw;
	}

	@Override
	public String serialize(String obj) {
		return obj;
	}
}
