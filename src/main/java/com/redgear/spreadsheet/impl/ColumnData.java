package com.redgear.spreadsheet.impl;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class ColumnData<T> {

	private final String fieldName;
	private final BiConsumer<T, String> setter;
	private final Function<T, String> getter;
	private final int index;
	private final String header;

	public ColumnData(String fieldName, BiConsumer<T, String> setter, Function<T, String> getter, int index, String header) {
		this.fieldName = fieldName;
		this.setter = setter;
		this.getter = getter;
		this.index = index;
		this.header = header;
	}

	public int getIndex() {
		return index;
	}

	public String getHeader() {
		return header;
	}

	public String getFieldName() {
		return fieldName;
	}

	public BiConsumer<T, String> getSetter() {
		return setter;
	}

	public Function<T, String> getGetter() {
		return getter;
	}

	@Override
	public String toString() {
		return "{ \"class\": \"ColumnData\" " +
				", \"fieldName\": \"" + fieldName + '\"' +
				", \"setter\": " + setter +
				", \"getter\": " + getter +
				", \"index\": " + index +
				", \"header\": \"" + header + '\"' +
				'}';
	}
}
