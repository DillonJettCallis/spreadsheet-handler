package com.redgear.spreadsheet;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public interface SpreadsheetWriter<T> extends AutoCloseable{

	/**
	 * Serializes obj and writes it out.
	 * @param obj Object to Serialize
	 */
	void writeRow(T obj);

	/**
	 * Serializes all instances returned by the iterable and writes them out.
	 * @param obj Iterable of objects to Serialize
	 */
	void writeAllRows(Iterable<T> obj);

	/**
	 * Serializes all instances returned by the iterator and writes them out.
	 * @param obj Iterator of objects to Serialize
	 */
	void writeAllRows(Iterator<T> obj);

	/**
	 * Serializes all instances returned by the stream and writes them out.
	 * @param obj Stream of objects to Serialize
	 */
	void writeAllRows(Stream<T> obj);

	/**
	 * Writes out the headers of all columns.
	 */
	void writeHeaders();

	/**
	 * Writes a totally blank row.
	 */
	void writeEmpty();

	/**
	 * Writes out the given strings in one row.
	 * @param row List of strings to write into this row.
	 */
	void writeFreeFormRow(List<String> row);

	/**
	 * Writes out the given strings in one row.
	 * @param row List of strings to write into this row.
	 */
	void writeFreeFormRow(String... row);

	/**
	 * Writes each list of strings into a seperate row.
	 * @param rows List of rows to write.
	 */
	void writeFreeFormRows(List<List<String>> rows);

}
