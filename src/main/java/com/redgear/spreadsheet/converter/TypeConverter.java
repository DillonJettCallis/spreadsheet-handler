package com.redgear.spreadsheet.converter;

import javax.validation.constraints.NotNull;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public interface TypeConverter<T> {

	@NotNull
	Class<T> type();

	@NotNull
	T parse(@NotNull String raw) throws Exception;

	@NotNull
	String serialize(@NotNull T obj) throws Exception;

}
