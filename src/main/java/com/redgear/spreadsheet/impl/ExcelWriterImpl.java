package com.redgear.spreadsheet.impl;

import com.redgear.spreadsheet.SpreadsheetWriter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class ExcelWriterImpl<T> implements SpreadsheetWriter<T> {

	private final OutputStream out;
	private final Sheet sheet;
	private final HandlerMetaData<T> data;

	private int rowIndex = 0;

	public ExcelWriterImpl(File file, HandlerMetaData<T> data, Workbook workbook) throws IOException, InvalidFormatException {
		this.out = new BufferedOutputStream(new FileOutputStream(file));
		this.sheet = workbook.createSheet();
		this.data = data;
	}

	public ExcelWriterImpl(OutputStream file, HandlerMetaData<T> data, Workbook workbook) throws IOException, InvalidFormatException {
		this.out = file;
		this.sheet = workbook.createSheet();
		this.data = data;
	}

	@Override
	public void writeRow(T obj) {
		writeFreeFormRow(data.writeRow(obj));
	}

	@Override
	public void writeAllRows(Iterable<T> list) {
		writeAllRows(list.iterator());
	}

	@Override
	public void writeAllRows(Iterator<T> iterator) {
		iterator.forEachRemaining(this::writeRow);
	}

	@Override
	public void writeAllRows(Stream<T> stream) {
		writeAllRows(stream.iterator());
	}

	@Override
	public void writeHeaders() {
		writeFreeFormRow(data.writeHeaders());
	}

	@Override
	public void writeEmpty() {
		writeFreeFormRow(Collections.emptyList());
	}

	@Override
	public void writeFreeFormRow(List<String> rowData) {
		Row row = sheet.createRow(rowIndex++);

		for(int i = 0; i < rowData.size(); i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(rowData.get(i));
		}
	}

	@Override
	public void writeFreeFormRow(String... row) {
		writeFreeFormRow(Arrays.asList(row));
	}

	@Override
	public void writeFreeFormRows(List<List<String>> rows) {
		rows.forEach(this::writeFreeFormRow);
	}

	@Override
	public void close() throws Exception {
		sheet.getWorkbook().write(out);
		out.close();
	}
}
