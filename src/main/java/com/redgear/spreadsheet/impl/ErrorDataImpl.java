package com.redgear.spreadsheet.impl;

import com.redgear.spreadsheet.ErrorData;

import java.util.List;
import java.util.Map;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class ErrorDataImpl implements ErrorData {

	private List<String> rawRow;

	private String message;

	private Throwable cause;

	private Map<String, String> fields;

	@Override
	public List<String> getRawRow() {
		return rawRow;
	}

	public void setRawRow(List<String> rawRow) {
		this.rawRow = rawRow;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}

	@Override
	public Map<String, String> getFields() {
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ErrorDataImpl errorData = (ErrorDataImpl) o;

		if (rawRow != null ? !rawRow.equals(errorData.rawRow) : errorData.rawRow != null) return false;
		if (message != null ? !message.equals(errorData.message) : errorData.message != null) return false;
		if (cause != null ? !cause.equals(errorData.cause) : errorData.cause != null) return false;
		return fields != null ? fields.equals(errorData.fields) : errorData.fields == null;

	}

	@Override
	public int hashCode() {
		int result = rawRow != null ? rawRow.hashCode() : 0;
		result = 31 * result + (message != null ? message.hashCode() : 0);
		result = 31 * result + (cause != null ? cause.hashCode() : 0);
		result = 31 * result + (fields != null ? fields.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "{ \"class\": \"" + ErrorDataImpl.class + "\"" +
				", \"rawRow\": " + rawRow +
				", \"message\": \"" + message + '\"' +
				", \"cause\": " + cause +
				", \"fields\": " + fields +
				'}';
	}
}
