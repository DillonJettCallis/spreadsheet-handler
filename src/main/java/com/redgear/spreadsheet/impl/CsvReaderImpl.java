package com.redgear.spreadsheet.impl;

import com.redgear.spreadsheet.ErrorHandler;
import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class CsvReaderImpl<T> extends AbstractReader<T> {

	private final CSVReader reader;
	private final CSVIterator csvIterator;


	public CsvReaderImpl(CSVReader file, HandlerMetaData<T> data, ErrorHandler errorHandler) throws IOException {
		super(data, errorHandler);
		this.reader = file;
		this.csvIterator = new CSVIterator(reader);
	}

	public CsvReaderImpl(File file, HandlerMetaData<T> data, ErrorHandler errorHandler) throws IOException {
		super(data, errorHandler);
		this.reader = new CSVReader(new FileReader(file));
		this.csvIterator = new CSVIterator(reader);
	}

	public CsvReaderImpl(InputStream file, HandlerMetaData<T> data, ErrorHandler errorHandler) throws IOException {
		super(data, errorHandler);
		this.reader = new CSVReader(new InputStreamReader(file));
		this.csvIterator = new CSVIterator(reader);
	}

	@Override
	public List<String> readRawLine() throws IOException {
		if(csvIterator.hasNext())
			return Arrays.stream(csvIterator.next()).map(StringUtils::trimToNull).collect(Collectors.toList());
		else
			return null;
	}

	@Override
	protected Stream<List<String>> readRawRemaining() {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(csvIterator, 0), false)
				.map(Arrays::asList);
	}

	@Override
	public void close() throws Exception {
		reader.close();
	}
}
