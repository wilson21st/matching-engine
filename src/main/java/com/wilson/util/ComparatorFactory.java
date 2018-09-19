package com.wilson.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComparatorFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ComparatorFactory.class);

	// parameter: an array of field names
	// return: a list of joined comparators
	public static <T> Comparator<T> build(String[] fields) {
		Comparator<T> comparator = null;
		for (String f : fields) {
			// descending order if ! is found
			boolean isReversed = f.contains("!");
			String fieldName = isReversed ? f.replaceAll("!", "") : f;
			Comparator<T> c = getComparator(fieldName);
			if (isReversed) {
				c = c.reversed();
			}
			if (comparator == null) {
				comparator = c;
			} else {
				// append comparator to the end
				comparator = comparator.thenComparing(c);
			}
		}
		return comparator;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T> Comparator<T> getComparator(String field) {
		return (o1, o2) -> {
			try {
				// retrieve value from getter method if exists
				// a dot indicates the field is in a nested class
				Object c1 = invokeGetterMethod(o1, field);
				Object c2 = invokeGetterMethod(o2, field);
				if (c1 == null) {
					return c2 == null ? 0 : Integer.MIN_VALUE;
				}
				if (c2 == null) {
					return Integer.MAX_VALUE;
				}
				return ((Comparable) c1).compareTo((Comparable) c2);

			} catch (Exception e) {
				LOGGER.debug("Exceiption: {}", e.getMessage());
			}
			return 0;
		};
	}

	private static Object invokeGetterMethod(Object object, String field) {

		try {

			// subfield: string after dot
			String subField = null;

			int idx = field.indexOf(".");
			if (idx != -1) {
				subField = field.substring(idx + 1, field.length());
				// extract string before dot
				field = field.substring(0, idx);
			}

			PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field, object.getClass());
			Method getter = propertyDescriptor.getReadMethod();

			if (getter != null) {
				Object value = getter.invoke(object);
				if (value != null && subField != null) {
					// recursively called if nested object is found
					return invokeGetterMethod(value, subField);
				}
				return value;
			}
			// value is not found

		} catch (Exception e) {
			LOGGER.debug("Exceiption: {}", e.getMessage());
		}
		return null;
	}
}
