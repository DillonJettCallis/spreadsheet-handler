package com.redgear.spreadsheet;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public interface ErrorData {

	List<String> getRawRow();

	String getMessage();

	Throwable getCause();

	Map<String, String> getFields();

}
