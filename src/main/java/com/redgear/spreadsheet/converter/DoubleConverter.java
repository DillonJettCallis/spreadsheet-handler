package com.redgear.spreadsheet.converter;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class DoubleConverter implements TypeConverter<Double> {
	@Override
	public Class<Double> type() {
		return Double.class;
	}

	@Override
	public Double parse(String raw) {
		return Double.parseDouble(raw);
	}

	@Override
	public String serialize(Double obj) {
		return obj.toString();
	}
}
