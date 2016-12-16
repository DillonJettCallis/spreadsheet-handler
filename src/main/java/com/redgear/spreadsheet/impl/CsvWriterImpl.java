package com.redgear.spreadsheet.impl;

import com.redgear.spreadsheet.SpreadsheetWriter;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class CsvWriterImpl<T> implements SpreadsheetWriter<T> {

	private final CSVWriter writer;
	private final HandlerMetaData<T> data;

	public CsvWriterImpl(CSVWriter file, HandlerMetaData<T> data) throws IOException {
		this.writer = file;
		this.data = data;
	}

	public CsvWriterImpl(File file, HandlerMetaData<T> data) throws IOException {
		this.writer = new CSVWriter(new FileWriter(file));
		this.data = data;
	}

	public CsvWriterImpl(OutputStream file, HandlerMetaData<T> data) throws IOException {
		this.writer = new CSVWriter(new OutputStreamWriter(file));
		this.data = data;
	}

	@Override
	public void writeRow(T obj) {
		List<String> row = data.writeRow(obj);

		writer.writeNext(toArray(row));
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
		List<String> row = data.writeHeaders();

		writer.writeNext(toArray(row));
	}

	@Override
	public void writeEmpty() {
		writer.writeNext(new String[]{});
	}

	@Override
	public void writeFreeFormRow(String... row) {
		writeFreeFormRow(Arrays.asList(row));
	}

	@Override
	public void writeFreeFormRow(List<String> row) {
		writer.writeNext(toArray(row));
	}

	@Override
	public void writeFreeFormRows(List<List<String>> rows) {
		rows.forEach(this::writeFreeFormRow);
	}

	private String[] toArray(List<String> row){
		return row.toArray(new String[row.size()]);
	}

	@Override
	public void close() throws Exception {
		writer.close();
	}
}
