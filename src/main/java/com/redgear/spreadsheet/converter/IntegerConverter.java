package com.redgear.spreadsheet.converter;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class IntegerConverter implements TypeConverter<Integer> {

	@Override
	public Class<Integer> type() {
		return Integer.class;
	}

	@Override
	public Integer parse(String raw) {
		return Integer.parseInt(raw);
	}

	@Override
	public String serialize(Integer obj) {
		return obj.toString();
	}
}
