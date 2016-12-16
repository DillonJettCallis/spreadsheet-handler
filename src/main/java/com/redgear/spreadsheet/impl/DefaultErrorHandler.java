package com.redgear.spreadsheet.impl;

import com.redgear.spreadsheet.ErrorData;
import com.redgear.spreadsheet.ErrorHandler;
import com.redgear.spreadsheet.exceptions.RowParseException;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class DefaultErrorHandler implements ErrorHandler {

	@Override
	public void handle(ErrorData errorData) {
		throw new RowParseException(errorData.getMessage(), errorData.getRawRow(), errorData.getCause());
	}

}
