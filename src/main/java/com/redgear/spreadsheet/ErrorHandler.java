package com.redgear.spreadsheet;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
@FunctionalInterface
public interface ErrorHandler {

	void handle(ErrorData errorData);

}
