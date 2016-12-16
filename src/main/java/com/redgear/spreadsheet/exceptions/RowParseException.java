package com.redgear.spreadsheet.exceptions;

import java.util.List;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class RowParseException extends RuntimeException {

	private final List<String> rawRow;

	public RowParseException(String message, List<String> rawRow) {
		super(message);
		this.rawRow = rawRow;
	}

	public RowParseException(String message, List<String> rawRow, Throwable cause) {
		super(message, cause);
		this.rawRow = rawRow;
	}

	public List<String> getRawRow() {
		return rawRow;
	}

}
