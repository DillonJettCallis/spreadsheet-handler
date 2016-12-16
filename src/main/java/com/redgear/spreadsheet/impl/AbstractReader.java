package com.redgear.spreadsheet.impl;

import com.redgear.spreadsheet.ErrorHandler;
import com.redgear.spreadsheet.SpreadsheetReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public abstract class AbstractReader<T> implements SpreadsheetReader<T> {

	private static final Logger log = LoggerFactory.getLogger(AbstractReader.class);

	private final HandlerMetaData<T> data;
	private final ErrorHandler errorHandler;
	private final Function<List<String>, Stream<T>> mapper;
	private Remapper remapper;

	public AbstractReader(HandlerMetaData<T> data, ErrorHandler errorHandler) {
		this.data = data;
		this.errorHandler = errorHandler;
		this.mapper = raw -> data.readRow(raw, errorHandler).map(Stream::of).orElse(Stream.empty());
		remapper = new Remapper(data);
	}

	@Override
	public T readLine() throws IOException {
		T result;

		do {
			List<String> row;

			do {
				row = readRawLine();

				if(row == null)
					return null;

				row = remapper.remap(row);

			} while(row.isEmpty() || row.stream().allMatch(StringUtils::isBlank));

			log.debug("Reading row: {}", row);

			result = data.readRow(row, errorHandler).orElse(null);
		} while(result == null);

		return result;
	}


	@Override
	public void skipLine() throws IOException {
		readRawLine();
	}

	@Override
	public void skipLines(int lines) throws IOException {
		for(int i = 0; i < lines; i++) {
			skipLine();
		}
	}

	abstract protected Stream<List<String>> readRawRemaining();

	@Override
	public Stream<T> readRemainingLines() {
		return readRawRemaining()
				.map(remapper::remap)
				.filter(row -> !row.isEmpty() && row.stream().anyMatch(StringUtils::isNotBlank))
				.flatMap(mapper);
	}

	@Override
	public List<T> readRemainingLinesAsList() {
		return readRemainingLines().collect(Collectors.toList());
	}

	@Override
	public List<T> readChunk(int linesToRead) {
		return readRemainingLines().limit(linesToRead).collect(Collectors.toList());
	}

	@Override
	public List<String> parseHeaders() throws IOException {
		remapper = new Remapper(data);
		return remapper.digest(readRawLine());
	}




}
