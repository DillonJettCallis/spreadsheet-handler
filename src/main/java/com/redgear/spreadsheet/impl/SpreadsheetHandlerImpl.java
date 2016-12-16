package com.redgear.spreadsheet.impl;

import com.redgear.spreadsheet.ErrorHandler;
import com.redgear.spreadsheet.SpreadsheetHandler;
import com.redgear.spreadsheet.SpreadsheetReader;
import com.redgear.spreadsheet.SpreadsheetWriter;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class SpreadsheetHandlerImpl<T> implements SpreadsheetHandler<T> {

	private static final DefaultErrorHandler defaultHandler = new DefaultErrorHandler();

	private final HandlerMetaData<T> data;

	SpreadsheetHandlerImpl(HandlerMetaData<T> data) {
		this.data = data;
	}

	@Override
	public SpreadsheetReader<T> createReader(File file) throws IOException, InvalidFormatException {
		return createReader(file, defaultHandler);
	}

	@Override
	public SpreadsheetReader<T> createReader(File file, ErrorHandler errorHandler) throws IOException, InvalidFormatException {
		if(file == null)
			throw new IllegalArgumentException("File was null");

		if(!file.exists())
			throw new FileNotFoundException("File was not found");


		if(file.getName().endsWith(".csv"))
			return new CsvReaderImpl<>(file, data, errorHandler);
		else
			return new ExcelReaderImpl<>(data, WorkbookFactory.create(file), 0, errorHandler);
	}

	@Override
	public SpreadsheetReader<T> createCsvReader(CSVReader file) throws IOException {
		return createCsvReader(file, defaultHandler);
	}

	@Override
	public SpreadsheetReader<T> createCsvReader(CSVReader file, ErrorHandler errorHandler) throws IOException {
		return new CsvReaderImpl<>(file, data, errorHandler);
	}

	@Override
	public SpreadsheetReader<T> createCsvReader(File file) throws IOException {
		return createCsvReader(file, defaultHandler);
	}

	@Override
	public SpreadsheetReader<T> createCsvReader(File file, ErrorHandler errorHandler) throws IOException {
		return new CsvReaderImpl<>(file, data, errorHandler);
	}

	@Override
	public SpreadsheetReader<T> createCsvReader(InputStream file) throws IOException {
		return createCsvReader(file, defaultHandler);
	}

	@Override
	public SpreadsheetReader<T> createCsvReader(InputStream file, ErrorHandler errorHandler) throws IOException {
		return new CsvReaderImpl<>(file instanceof BufferedInputStream ? file : new BufferedInputStream(file), data, errorHandler);
	}

	@Override
	public SpreadsheetReader<T> createExcelReader(File file) throws IOException, InvalidFormatException {
		return createExcelReader(file, defaultHandler);
	}

	@Override
	public SpreadsheetReader<T> createExcelReader(File file, ErrorHandler errorHandler) throws IOException, InvalidFormatException {
		return createExcelReader(file, 0, errorHandler);
	}

	@Override
	public SpreadsheetReader<T> createExcelReader(File file, int sheet) throws IOException, InvalidFormatException {
		return createExcelReader(file, sheet, defaultHandler);
	}

	@Override
	public SpreadsheetReader<T> createExcelReader(File file, int sheet, ErrorHandler errorHandler) throws IOException, InvalidFormatException {
		return createExcelReader(WorkbookFactory.create(file), sheet, errorHandler);
	}

	@Override
	public SpreadsheetReader<T> createExcelReader(InputStream file) throws IOException, InvalidFormatException {
		return createExcelReader(file, defaultHandler);
	}

	@Override
	public SpreadsheetReader<T> createExcelReader(InputStream file, ErrorHandler errorHandler) throws IOException, InvalidFormatException {
		return createExcelReader(file, 0, errorHandler);
	}

	@Override
	public SpreadsheetReader<T> createExcelReader(InputStream file, int sheet) throws IOException, InvalidFormatException {
		return createExcelReader(file, sheet, defaultHandler);
	}

	@Override
	public SpreadsheetReader<T> createExcelReader(InputStream file, int sheet, ErrorHandler errorHandler) throws IOException, InvalidFormatException {
		return createExcelReader(WorkbookFactory.create(file), sheet, errorHandler);
	}

	@Override
	public SpreadsheetReader<T> createExcelReader(Workbook workbook) throws IOException, InvalidFormatException {
		return createExcelReader(workbook, defaultHandler);
	}

	@Override
	public SpreadsheetReader<T> createExcelReader(Workbook workbook, ErrorHandler errorHandler) throws IOException, InvalidFormatException {
		return new ExcelReaderImpl<>(data, workbook, 0, errorHandler);
	}

	@Override
	public SpreadsheetReader<T> createExcelReader(Workbook workbook, int sheet) throws IOException, InvalidFormatException {
		return createExcelReader(workbook, sheet, defaultHandler);
	}

	@Override
	public SpreadsheetReader<T> createExcelReader(Workbook workbook, int sheet, ErrorHandler errorHandler) throws IOException, InvalidFormatException {
		return new ExcelReaderImpl<>(data, workbook, sheet, errorHandler);
	}

	@Override
	public SpreadsheetWriter<T> createCsvWriter(CSVWriter file) throws IOException {
		return new CsvWriterImpl<>(file, data);
	}

	@Override
	public SpreadsheetWriter<T> createCsvWriter(File file) throws IOException {
		return new CsvWriterImpl<>(file, data);
	}

	@Override
	public SpreadsheetWriter<T> createCsvWriter(OutputStream file) throws IOException {
		return new CsvWriterImpl<>(file instanceof BufferedOutputStream ? file : new BufferedOutputStream(file), data);
	}

	@Override
	public SpreadsheetWriter<T> createExcelWriter(File file, Workbook workbook) throws IOException, InvalidFormatException {
		return new ExcelWriterImpl<>(file, data, workbook);
	}

	@Override
	public SpreadsheetWriter<T> createExcelWriter(OutputStream file, Workbook workbook) throws IOException, InvalidFormatException {
		return new ExcelWriterImpl<>(file instanceof BufferedOutputStream ? file : new BufferedOutputStream(file), data, workbook);
	}

	@Override
	public SpreadsheetWriter<T> createExcelWriter(File file) throws IOException, InvalidFormatException {
		return createExcelWriter(file, new XSSFWorkbook());
	}

	@Override
	public SpreadsheetWriter<T> createExcelWriter(OutputStream file) throws IOException, InvalidFormatException {
		return createExcelWriter(file, new XSSFWorkbook());
	}

	@Override
	public SpreadsheetWriter<T> createLegacyExcelWriter(File file) throws IOException, InvalidFormatException {
		return createExcelWriter(file, new HSSFWorkbook());
	}

	@Override
	public SpreadsheetWriter<T> createLegacyExcelWriter(OutputStream file) throws IOException, InvalidFormatException {
		return createExcelWriter(file, new HSSFWorkbook());
	}
}
