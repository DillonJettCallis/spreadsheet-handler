package com.redgear.spreadsheet.converter;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class FloatConverter implements TypeConverter<Float> {
	@Override
	public Class<Float> type() {
		return Float.class;
	}

	@Override
	public Float parse(String raw) {
		return Float.parseFloat(raw);
	}

	@Override
	public String serialize(Float obj) {
		return obj.toString();
	}
}
