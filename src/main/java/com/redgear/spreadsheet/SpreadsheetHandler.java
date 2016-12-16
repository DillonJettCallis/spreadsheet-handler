package com.redgear.spreadsheet;

import com.redgear.spreadsheet.converter.TypeConverter;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;

import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public interface SpreadsheetHandler<T> {

	static <T> SpreadsheetHandler<T> createSpreadsheetHandler(Class<T> clazz, TypeConverter<?>... converters) {
		return SpreadsheetHandlerFactory.defaultInstance().createSpreadsheetHandler(clazz, converters);
	}

	static <T> SpreadsheetHandler<T> createSpreadsheetHandler(Class<T> clazz, ValidatorFactory validatorFactory, TypeConverter<?>... converters) {
		return SpreadsheetHandlerFactory.defaultInstance().createSpreadsheetHandler(clazz, validatorFactory, converters);
	}

	SpreadsheetReader<T> createReader(File file) throws IOException, InvalidFormatException;

	SpreadsheetReader<T> createReader(File file, ErrorHandler errorHandler) throws IOException, InvalidFormatException;

	SpreadsheetReader<T> createCsvReader(CSVReader file) throws IOException;

	SpreadsheetReader<T> createCsvReader(CSVReader file, ErrorHandler errorHandler) throws IOException;

	SpreadsheetReader<T> createCsvReader(File file) throws IOException;

	SpreadsheetReader<T> createCsvReader(File file, ErrorHandler errorHandler) throws IOException;

	SpreadsheetReader<T> createCsvReader(InputStream file) throws IOException;

	SpreadsheetReader<T> createCsvReader(InputStream file, ErrorHandler errorHandler) throws IOException;

	SpreadsheetReader<T> createExcelReader(File file) throws IOException, InvalidFormatException;

	SpreadsheetReader<T> createExcelReader(File file, ErrorHandler errorHandler) throws IOException, InvalidFormatException;

	SpreadsheetReader<T> createExcelReader(File file, int sheet) throws IOException, InvalidFormatException;

	SpreadsheetReader<T> createExcelReader(File file, int sheet, ErrorHandler errorHandler) throws IOException, InvalidFormatException;

	SpreadsheetReader<T> createExcelReader(InputStream file) throws IOException, InvalidFormatException;

	SpreadsheetReader<T> createExcelReader(InputStream file, ErrorHandler errorHandler) throws IOException, InvalidFormatException;

	SpreadsheetReader<T> createExcelReader(InputStream file, int sheet) throws IOException, InvalidFormatException;

	SpreadsheetReader<T> createExcelReader(InputStream file, int sheet, ErrorHandler errorHandler) throws IOException, InvalidFormatException;

	SpreadsheetReader<T> createExcelReader(Workbook workbook) throws IOException, InvalidFormatException;

	SpreadsheetReader<T> createExcelReader(Workbook workbook, ErrorHandler errorHandler) throws IOException, InvalidFormatException;

	SpreadsheetReader<T> createExcelReader(Workbook workbook, int sheet) throws IOException, InvalidFormatException;

	SpreadsheetReader<T> createExcelReader(Workbook workbook, int sheet, ErrorHandler errorHandler) throws IOException, InvalidFormatException;

	SpreadsheetWriter<T> createCsvWriter(File file) throws IOException;

	SpreadsheetWriter<T> createCsvWriter(CSVWriter file) throws IOException;

	SpreadsheetWriter<T> createCsvWriter(OutputStream file) throws IOException;

	SpreadsheetWriter<T> createExcelWriter(File file, Workbook workbook) throws IOException, InvalidFormatException;

	SpreadsheetWriter<T> createExcelWriter(OutputStream file, Workbook workbook) throws IOException, InvalidFormatException;

	SpreadsheetWriter<T> createExcelWriter(File file) throws IOException, InvalidFormatException;

	SpreadsheetWriter<T> createExcelWriter(OutputStream file) throws IOException, InvalidFormatException;

	SpreadsheetWriter<T> createLegacyExcelWriter(File file) throws IOException, InvalidFormatException;

	SpreadsheetWriter<T> createLegacyExcelWriter(OutputStream file) throws IOException, InvalidFormatException;
}
