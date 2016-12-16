package com.redgear.spreadsheet;

import com.redgear.spreadsheet.converter.DateConverter;
import com.redgear.spreadsheet.converter.EnumConverter;
import com.redgear.spreadsheet.converter.TypeConverter;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class TypeConverters {

	public static TypeConverter<java.util.Date> date(String pattern) {
		return date(DateTimeFormatter.ofPattern(pattern));
	}

	public static TypeConverter<java.util.Date> date(DateTimeFormatter formatter) {
		return new DateConverter(formatter);
	}

	public static TypeConverter<Date> sqlDate(String pattern) {
		return sqlDate(DateTimeFormatter.ofPattern(pattern));
	}

	public static TypeConverter<Date> sqlDate(DateTimeFormatter formatter) {

		return new TypeConverter<Date>() {

			@Override
			public Class<Date> type() {
				return Date.class;
			}

			@Override
			public Date parse(@NotNull String raw) {
				return Date.valueOf(LocalDate.from(formatter.parse(raw)));
			}

			@Override
			public String serialize(@NotNull Date obj) {
				return formatter.format(obj.toLocalDate());
			}
		};
	}

	public static TypeConverter<LocalDate> localDate(String pattern) {
		return localDate(DateTimeFormatter.ofPattern(pattern));
	}

	public static TypeConverter<LocalDate> localDate(DateTimeFormatter formatter) {
		return new TypeConverter<LocalDate>() {
			@Override
			public Class<LocalDate> type() {
				return LocalDate.class;
			}

			@Override
			public LocalDate parse(@NotNull String raw) {
				return LocalDate.from(formatter.parse(raw));
			}

			@Override
			public String serialize(@NotNull LocalDate obj) {
				return formatter.format(obj);
			}
		};
	}

	public static TypeConverter<LocalDateTime> localDateTime(String pattern) {
		return localDateTime(DateTimeFormatter.ofPattern(pattern));
	}

	public static TypeConverter<LocalDateTime> localDateTime(DateTimeFormatter formatter) {
		return new TypeConverter<LocalDateTime>() {
			@Override
			public Class<LocalDateTime> type() {
				return LocalDateTime.class;
			}

			@Override
			public LocalDateTime parse(@NotNull String raw) {
				return LocalDateTime.from(formatter.parse(raw));
			}

			@Override
			public String serialize(@NotNull LocalDateTime obj) {
				return formatter.format(obj);
			}
		};
	}

	public static TypeConverter<Instant> instant(String pattern) {
		return instant(DateTimeFormatter.ofPattern(pattern));
	}

	public static TypeConverter<Instant> instant(DateTimeFormatter formatter) {
		return new TypeConverter<Instant>() {
			@Override
			public Class<Instant> type() {
				return Instant.class;
			}

			@Override
			public Instant parse(@NotNull String raw) {
				return Instant.from(formatter.parse(raw));
			}

			@Override
			public String serialize(@NotNull Instant obj) {
				return formatter.format(obj);
			}
		};
	}

	public static TypeConverter<Timestamp> timestamp(String pattern) {
		return timestamp(DateTimeFormatter.ofPattern(pattern));
	}

	public static TypeConverter<Timestamp> timestamp(DateTimeFormatter formatter) {
		return new TypeConverter<Timestamp>() {
			@Override
			public Class<Timestamp> type() {
				return Timestamp.class;
			}

			@Override
			public Timestamp parse(@NotNull String raw) {
				return Timestamp.from(Instant.from(formatter.parse(raw)));
			}

			@Override
			public String serialize(@NotNull Timestamp obj) {
				return formatter.format(obj.toInstant());
			}
		};
	}

	public static <T extends Enum> TypeConverter<T> enumConverter(Class<T> type) {
		return new EnumConverter<>(type);
	}
}
