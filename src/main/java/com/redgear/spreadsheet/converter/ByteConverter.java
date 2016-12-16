package com.redgear.spreadsheet.converter;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class ByteConverter implements TypeConverter<Byte> {
	@Override
	public Class<Byte> type() {
		return Byte.class;
	}

	@Override
	public Byte parse(String raw) {
		return Byte.parseByte(raw);
	}

	@Override
	public String serialize(Byte obj) {
		return obj.toString();
	}
}
