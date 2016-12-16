package com.redgear.spreadsheet.converter;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class CharConverter implements TypeConverter<Character> {
	@Override
	public Class<Character> type() {
		return null;
	}

	@Override
	public Character parse(String raw) {
		return raw.charAt(0);
	}

	@Override
	public String serialize(Character obj) {
		return obj.toString();
	}
}
