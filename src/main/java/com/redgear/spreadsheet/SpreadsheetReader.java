package com.redgear.spreadsheet;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public interface SpreadsheetReader<T> extends AutoCloseable{

	/**
	 * Read the next line and parse it.
	 * @return A parsed object for this record, or null if EOF has been reached.
	 * @throws IOException
	 */
	T readLine() throws IOException;

	/**
	 * Read the next line but does not parse it.
	 * @return A list of Strings representing the raw data found in this line.
	 * @throws IOException
	 */
	List<String> readRawLine() throws IOException;

	/**
	 * Reads forward one line but does not attempt to parse. Good for ignoring headers.
	 * @throws IOException
	 */
	void skipLine() throws IOException;

	/**
	 * Reads forward a number of lines but does not attempt to parse any of them. Good for pagination.
	 * @param lines The number of lines to skip.
	 * @throws IOException
	 */
	void skipLines(int lines) throws IOException;

	/**
	 * Creates a stream that will lazily read the rest of the file and parse each line.
	 * @return A lazy stream of parsed lines.
	 */
	Stream<T> readRemainingLines();

	/**
	 * Reads all lines immediately and parses them all into objects. This will load the entire file in memory at once.
	 * @return A list containing all the remaining lines.
	 */
	List<T> readRemainingLinesAsList();

	/**
	 * Reads only the given number of lines and parses them immediately.
	 * @param linesToRead Maximum lines to read.
	 * @return A list containing at maximum @linesToRead lines or empty if the EOF has been reached.
	 */
	List<T> readChunk(int linesToRead);


	/**
	 * Reads one line, expecting to find headers 'similar' to those specified in the Column mappings, and will use
	 * those from this point on in the spread sheet to map columns to data values instead of by index.
	 */
	List<String> parseHeaders() throws IOException;

}
