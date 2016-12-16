package com.redgear.spreadsheet;

import com.redgear.spreadsheet.converter.TypeConverter;
import com.redgear.spreadsheet.impl.SpreadsheetHandlerFactoryImpl;

import javax.validation.ValidatorFactory;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public interface SpreadsheetHandlerFactory {

	static SpreadsheetHandlerFactory defaultInstance() {
		return new SpreadsheetHandlerFactoryImpl();
	}

	<T> SpreadsheetHandler<T> createSpreadsheetHandler(Class<T> clazz, TypeConverter<?>... converters);

	<T> SpreadsheetHandler<T> createSpreadsheetHandler(Class<T> clazz, ValidatorFactory validatorFactory, TypeConverter<?>... converters);

}
