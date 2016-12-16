package com.redgear.spreadsheet.impl;

import com.redgear.spreadsheet.ErrorHandler;
import com.redgear.spreadsheet.exceptions.ConversionException;
import com.redgear.spreadsheet.exceptions.ReflectionException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class HandlerMetaData<T> {

	private final Supplier<T> constructor;
	private final List<ColumnData<T>> columns;
	private final int maxIndex;
	private final Validator validator;

	public HandlerMetaData(Supplier<T> constructor, List<ColumnData<T>> columns, Validator validator) {
		this.constructor = constructor;
		this.columns = columns;
		//noinspection OptionalGetWithoutIsPresent - Safe because we know columns can't be empty.
		this.maxIndex = columns.stream().map(ColumnData::getIndex).max(Integer::compare).get();
		this.validator = validator;
	}

	public Optional<T> readRow(List<String> input, ErrorHandler errorHandler) {
		Map<String, String> fields = new HashMap<>();
		T next = constructor.get();

		for(int i = 0; i < columns.size() && i < input.size(); i++){
			ColumnData<T> c = columns.get(i);
			String data = input.get(i);

			fields.put(c.getFieldName(), data);

			try {
				c.getSetter().accept(next, data);
			} catch (ConversionException | ReflectionException e) {
				ErrorDataImpl errorData = new ErrorDataImpl();

				errorData.setRawRow(input);
				errorData.setMessage("Failed to set raw data: '" + data + "' into field index: " + i + " due to: " + e.getMessage());
				errorData.setCause(e);
				errorData.setFields(fields);

				errorHandler.handle(errorData);
			}
		}

		if(validator == null)
			return Optional.of(next);
		else {
			Set<ConstraintViolation<T>> constraintViolations = validator.validate(next);

			if (constraintViolations.isEmpty())
				return Optional.of(next);
			else {
				ErrorDataImpl errorData = new ErrorDataImpl();

				errorData.setFields(fields);
				errorData.setRawRow(input);
				errorData.setMessage("Row violates Constraint Validations: " + constraintViolations);
				errorData.setCause(new ConstraintViolationException("Row violates Constraint Validations.", constraintViolations));

				errorHandler.handle(errorData);

				return Optional.empty();
			}
		}
	}

	public List<String> writeRow(T output) {
		return columns.stream()
				.map(column -> column.getGetter().apply(output))
				.collect(Collectors.toList());
	}

	public List<String> writeHeaders() {
		return columns.stream().map(ColumnData::getHeader).collect(Collectors.toList());
	}

	public int getMaxIndex() {
		return maxIndex;
	}

	@Override
	public String toString() {
		return "{ \"class\": \"HandlerMetaData\" " +
				", \"constructor\": " + constructor +
				", \"columns\": " + columns +
				", \"maxIndex\": " + maxIndex +
				'}';
	}
}
