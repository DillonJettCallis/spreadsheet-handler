package com.redgear.spreadsheet.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class Remapper {

	private static final Logger log = LoggerFactory.getLogger(Remapper.class);

	private final Map<Integer, Integer> idMap = new HashMap<>();
	private final HandlerMetaData<?> data;

	public Remapper(HandlerMetaData<?> data) {
		this.data = data;

		List<Boolean> values = data.writeHeaders().stream().map(Remapper::stripper).map(StringUtils::isNotBlank).collect(Collectors.toList());

		for(int i = 0; i < values.size(); i++) {

			if(values.get(i)) {
				idMap.put(i, i);
			}

		}

	}

	public List<String> remap(List<String> input) {
		if(input.isEmpty())
			return input;

		List<String> output = new ArrayList<>(data.getMaxIndex());

		for(int i = 0; i <= data.getMaxIndex(); i++) {
			if(idMap.containsKey(i)) {
				int id = idMap.get(i);

				if(id < input.size())
					output.add(input.get(id));
				else
					output.add(null);
			}
			else
				output.add(null);
		}

		return output;
	}

	public List<String> digest(List<String> actualHeaders) {
		idMap.clear();

		List<String> expectedHeaders = data.writeHeaders().stream().map(Remapper::stripper).collect(Collectors.toList());
		List<String> strippedHeaders = actualHeaders.stream().map(Remapper::stripper).collect(Collectors.toList());


		for(int j = 0; j < strippedHeaders.size(); j++) {
			String actual = strippedHeaders.get(j);

			for(int i = 0; i < expectedHeaders.size(); i++) {
				String expected =  expectedHeaders.get(i);

				if(expected.equals(actual)) {
					if(idMap.containsKey(i))
						throw new RuntimeException("Spread sheet contains at least two columns with simplified header '" + actual + "'");

					idMap.put(i, j);
					break;
				}
			}
		}

		return strippedHeaders;
	}

	private static String stripper(String input) {
		return input.toLowerCase().replaceAll("[\\s\\-_\\|\\\\/]+", " ").trim();
	}

	@Override
	public String toString() {
		return "{ \"class\": \"Remapper\" " +
				", \"idMap\": " + idMap +
				", \"data\": " + data +
				'}';
	}
}
