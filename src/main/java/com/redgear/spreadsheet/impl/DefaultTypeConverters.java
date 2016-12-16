package com.redgear.spreadsheet.impl;

import com.redgear.spreadsheet.converter.TypeConverter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class DefaultTypeConverters {

	public static TypeConverter<URL> url() {
		return new TypeConverter<URL>() {
			@Override
			public Class<URL> type() {
				return URL.class;
			}

			@Override
			public URL parse(@NotNull String raw) throws Exception {
				return new URL(raw);
			}

			@Override
			public String serialize(@NotNull URL obj) throws Exception {
				return obj.toString();
			}
		};
	}

	public static TypeConverter<URI> uri() {
		return new TypeConverter<URI>() {
			@Override
			public Class<URI> type() {
				return URI.class;
			}

			@Override
			public URI parse(@NotNull String raw) throws Exception {
				return new URI(raw);
			}

			@Override
			public String serialize(@NotNull URI obj) throws Exception {
				return obj.toString();
			}
		};
	}

	public static TypeConverter<BigInteger> bigInteger() {
		return new TypeConverter<BigInteger>() {
			@Override
			public Class<BigInteger> type() {
				return BigInteger.class;
			}

			@Override
			public BigInteger parse(@NotNull String raw) throws Exception {
				return new BigInteger(raw);
			}

			@Override
			public String serialize(@NotNull BigInteger obj) throws Exception {
				return obj.toString();
			}
		};
	}

	public static TypeConverter<BigDecimal> bigDecimal() {
		return new TypeConverter<BigDecimal>() {
			@Override
			public Class<BigDecimal> type() {
				return BigDecimal.class;
			}

			@Override
			public BigDecimal parse(@NotNull String raw) throws Exception {
				return new BigDecimal(raw);
			}

			@Override
			public String serialize(@NotNull BigDecimal obj) throws Exception {
				return obj.toString();
			}
		};
	}
}
