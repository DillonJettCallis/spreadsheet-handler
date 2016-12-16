package com.redgear.spreadsheet.converter;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class DateConverter implements TypeConverter<Date> {

	private final DateTimeFormatter formatter;

	public DateConverter(String dateFormat) {
		this(DateTimeFormatter.ofPattern(dateFormat));
	}

	public DateConverter(DateTimeFormatter formatter) {
		this.formatter = formatter;
	}

	@Override
	public Class<Date> type() {
		return Date.class;
	}

	@Override
	public Date parse(String raw) {
		return Date.from(Instant.from(formatter.parse(raw)));
	}

	@Override
	public String serialize(Date obj) {
		return formatter.format(obj.toInstant());
	}
}
