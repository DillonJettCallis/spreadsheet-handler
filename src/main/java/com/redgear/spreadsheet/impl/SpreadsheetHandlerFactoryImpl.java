package com.redgear.spreadsheet.impl;

import com.redgear.spreadsheet.SpreadsheetHandler;
import com.redgear.spreadsheet.SpreadsheetHandlerFactory;
import com.redgear.spreadsheet.Column;
import com.redgear.spreadsheet.converter.*;
import com.redgear.spreadsheet.exceptions.ConversionException;
import com.redgear.spreadsheet.exceptions.ReflectionException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ValidatorFactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Dillon Jett Callis
 * @version 1.0.0
 */
public class SpreadsheetHandlerFactoryImpl implements SpreadsheetHandlerFactory {

	private static final Logger log = LoggerFactory.getLogger(SpreadsheetHandlerFactoryImpl.class);

	private static final ColumnData<?> empty = new ColumnData<>("", (a, b) -> {}, a -> null, 0, "");

	public <T> SpreadsheetHandler<T> createSpreadsheetHandler(Class<T> clazz, TypeConverter<?>... converters) {
		return createSpreadsheetHandler(clazz, null, converters);
	}

	public <T> SpreadsheetHandler<T> createSpreadsheetHandler(Class<T> clazz, ValidatorFactory validatorFactory, TypeConverter<?>... converters) {
		log.debug("Parsing class: {}", clazz);

		Map<Class<?>, TypeConverter<?>> converterMap = Arrays.stream(converters).collect(Collectors.toMap(TypeConverter::type, Function.identity()));
		addPrimitiveConverters(converterMap);

		List<ColumnData<T>> columns = new ArrayList<>();

		MethodHandles.Lookup lookup = MethodHandles.lookup();

		for (Field f : clazz.getDeclaredFields()) {
			log.debug("Found field: {}", f);
			Column c = f.getAnnotation(Column.class);

			if(c != null) {
				log.debug("Field is a column");
				String fieldName = f.getName();

				Class<?> type = f.getType();
				TypeConverter converter = converterMap.get(type);

				if(converter == null)
					throw new IllegalArgumentException("Don't know how to handle type '" + type.getName() + "' no converter supplied");


				//Set up setter
				BiConsumer<T, String> setterConsumer;

				AtomicReference<MethodHandle> setterRef = new AtomicReference<>(null);

				try { //Use a setter method if it exists.
					Method setter = clazz.getMethod("set" + camelCase(fieldName), type);

					setterRef.set(lookup.unreflect(setter));
				} catch (IllegalAccessException | NoSuchMethodException e) {
					//No method. Set field directly.

					f.setAccessible(true);

					try {
						setterRef.set(lookup.unreflectSetter(f));
					} catch (IllegalAccessException e2) {
						throw new ReflectionException("Could not access field: " + fieldName, e2);
					}
				}

				MethodHandle setterHandle = setterRef.get();

				if(setterHandle == null)
					throw new ReflectionException("Could not access field: " + fieldName);

				setterConsumer = (obj, in) -> {

					Object converted;

					try {
						if(StringUtils.isBlank(in)) {
							//Sets to null or primitive default.
							converted = getDefaultValue(type);
						} else {
							converted = converter.parse(in);
						}
					} catch (Throwable e) {
						throw new ConversionException("Could not convert type: " + (in == null ? null : in.getClass()) + " to type: " + type, e);
					}

					try {
						setterHandle.bindTo(obj).invoke(converted);
					} catch (Throwable e) {
						throw new ReflectionException("Could not set property with setter. ", e);
					}

				};



				//Set up the getter
				Function<T, String> getterFunction;

				AtomicReference<MethodHandle> getterRef = new AtomicReference<>(null);

				try {
					//Try to use getter method if it exists.
					Method getter = clazz.getMethod("get" + camelCase(fieldName));

					if(!getter.getReturnType().equals(type))
						throw new NoSuchMethodException("Result type of 'get" + camelCase(fieldName) + "()' is different than that of field " + fieldName +" conclusion: This is not a field getter. Ignoring getter now. ");

					getterRef.set(lookup.unreflect(getter));
				} catch (IllegalAccessException | NoSuchMethodException e) {
					//If there is no getter method, get the field directly.
					f.setAccessible(true);

					try {
						getterRef.set(lookup.unreflectGetter(f));
					} catch (IllegalAccessException e2) {
						throw new ReflectionException("Could not access field. ", e2);
					}
				}

				MethodHandle getterHandle = getterRef.get();

				if(getterHandle == null)
					throw new ReflectionException("Could not access field: " + fieldName);

				getterFunction = obj -> {
					try {
						Object value = getterHandle.bindTo(obj).invoke();

						if(value == null)
							return "";

						//Class was already checked by the converter map. Should be correct.
						@SuppressWarnings("unchecked")
						String result = converter.serialize(value);
						return result;
					} catch (Throwable e) {
						throw new ReflectionException("Could not get property with getter. ", e);
					}
				};

				columns.add(new ColumnData<>(fieldName, setterConsumer, getterFunction, c.index(), c.header()));
			}
			else
				log.debug("Field is not a Column");
		}

		//Sort and filter columns by index, to ensure that there are no duplicate indexes.
		int size = columns.stream().map(ColumnData::getIndex).max(Integer::compare).orElse(0) + 1;
		ColumnData[] cArray = new ColumnData[size];

		Arrays.fill(cArray, empty);

		log.debug("Found: {} columns", size);

		for(ColumnData<T> c : columns) {

			int index = c.getIndex();

			if(index < 0 || index >= size)
				throw new IndexOutOfBoundsException("Illegal index in column.");

			if(cArray[index] != empty)
				throw new IndexOutOfBoundsException("Two columns can't have the same index. ");

			cArray[index] = c;
		}

		//The array bit effectively sorts the list while validating it at the same time, but Lists are easier to work with.

		@SuppressWarnings("unchecked") //We know they're the right type, we created them right here in this method.
		List<ColumnData<T>> data = Arrays.asList(cArray);

		Constructor<T> constructor;
		try {
			constructor = clazz.getConstructor();
			constructor.setAccessible(true);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Target class must have a no-args constructor", e);
		}

		Supplier<T> objSupplier = () -> {
			try {
				return constructor.newInstance();
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
				throw new ReflectionException("Failed to create new instance of class. ", e);
			}
		};

		return new SpreadsheetHandlerImpl<>(new HandlerMetaData<>(objSupplier, data, validatorFactory == null ? null : validatorFactory.getValidator()));
	}

	private static String camelCase(String in) {
		return Character.toUpperCase(in.charAt(0)) + in.substring(1);
	}

	private static void addPrimitiveConverters(Map<Class<?>, TypeConverter<?>> converterMap) {
		addPrimitive(converterMap, int.class, Integer.class, new IntegerConverter());
		addPrimitive(converterMap, long.class, Long.class, new LongConverter());
		addPrimitive(converterMap, boolean.class, Boolean.class, new BooleanConverter());
		addPrimitive(converterMap, float.class, Float.class, new FloatConverter());
		addPrimitive(converterMap, double.class, Double.class, new DoubleConverter());
		addPrimitive(converterMap, short.class, Short.class, new ShortConverter());
		addPrimitive(converterMap, byte.class, Byte.class, new ByteConverter());
		addPrimitive(converterMap, char.class, Character.class, new CharConverter());

		add(converterMap, new StringConverter());
		add(converterMap, DefaultTypeConverters.bigDecimal());
		add(converterMap, DefaultTypeConverters.bigInteger());
		add(converterMap, DefaultTypeConverters.uri());
		add(converterMap, DefaultTypeConverters.url());
	}

	private static void add(Map<Class<?>, TypeConverter<?>> converterMap, TypeConverter def) {
		converterMap.putIfAbsent(def.type(), def);
	}

	private static <T> void addPrimitive(Map<Class<?>, TypeConverter<?>> converterMap, Class prim, Class<T> wrapper, TypeConverter<T> def) {
		if (converterMap.containsKey(wrapper)) {
			converterMap.put(prim, converterMap.get(wrapper));
		} else {
			converterMap.put(prim, def);
			converterMap.put(wrapper, def);
		}
	}

	private static Object getDefaultValue(Class<?> clazz) {
		if(clazz.equals(int.class))
			return 0;
		else if (clazz.equals(long.class))
			return 0L;
		else if (clazz.equals(short.class))
			return (short) 0;
		else if (clazz.equals(byte.class))
			return (byte) 0;
		else if (clazz.equals(float.class))
			return 0.0F;
		else if (clazz.equals(double.class))
			return 0.0D;
		else if (clazz.equals(char.class))
			return (char) 0;
		else if (clazz.equals(boolean.class))
			return false;
		else
			return null;
	}

}
