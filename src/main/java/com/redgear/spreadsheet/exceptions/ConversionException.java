package com.redgear.spreadsheet.exceptions;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class ConversionException extends RuntimeException {

	public ConversionException(String message) {
		super(message);
	}

	public ConversionException(String message, Throwable cause) {
		super(message, cause);
	}
}
