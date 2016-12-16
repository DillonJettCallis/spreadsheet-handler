package com.redgear.spreadsheet.exceptions;


/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class ReflectionException extends RuntimeException {

	public ReflectionException(String message) {
		super(message);
	}


	public ReflectionException(String message, Throwable cause) {
		super(message, cause);
	}

}
