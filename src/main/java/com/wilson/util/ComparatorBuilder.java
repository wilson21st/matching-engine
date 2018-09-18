package com.wilson.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ComparatorBuilder<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ComparatorBuilder.class);

	// parameter: an array of field names
	// return: a list of joined comparators
	public Comparator<T> build(String[] fieldNames) {
		Comparator<T> comparator = null;
		for (String f : fieldNames) {
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
	private Comparator<T> getComparator(String fieldName) {
		return (o1, o2) -> {
			try {
				// retrieve value from getter method if exists
				// a dot indicates the field is in a nested class
				Object c1 = invokeGetterMethod(o1, fieldName);
				Object c2 = invokeGetterMethod(o2, fieldName);
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

	private Object invokeGetterMethod(Object object, String fieldName) {

		try {
			// root: string before dot
			int idx = fieldName.indexOf(".");
			String root = idx == -1 ? fieldName : fieldName.substring(0, idx);

			// next: string after dot
			String next = idx == -1 ? "" : fieldName.substring(idx + 1, fieldName.length());

			PropertyDescriptor propertyDescriptor = new PropertyDescriptor(root, object.getClass());
			Method getter = propertyDescriptor.getReadMethod();

			if (getter != null) {
				Object value = getter.invoke(object);
				if (value != null && !StringUtils.isEmpty(next)) {
					// recursively called if nested object is found
					return invokeGetterMethod(value, next);
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
