package com.redgear.spreadsheet.converter;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class ShortConverter implements TypeConverter<Short> {
	@Override
	public Class<Short> type() {
		return Short.class;
	}

	@Override
	public Short parse(String raw) {
		return Short.parseShort(raw);
	}

	@Override
	public String serialize(Short obj) {
		return obj.toString();
	}
}
