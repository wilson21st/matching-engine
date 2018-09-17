package service.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ComparatorBuilder {

	// parameter: an array of field names
	// return: a list of joined comparators
	public static <T> Comparator<T> build(String... fieldNames) {
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
				comparator = comparator.thenComparing(c);
			}
		}
		return comparator;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T> Comparator<T> getComparator(String fieldName) {
		return (o1, o2) -> {
			try {
				Comparable c1 = (Comparable) invokeGetterMethod(o1, fieldName);
				Comparable c2 = (Comparable) invokeGetterMethod(o2, fieldName);
				if (c1 == null) {
					return c2 == null ? 0 : Integer.MIN_VALUE;
				}
				if (c2 == null) {
					return Integer.MAX_VALUE;
				}
				return c1.compareTo(c2);
			} catch (Exception e) {
				log.info("Exceiption: {}", e.getMessage());
			}
			return 0;
		};
	}

	@SneakyThrows
	private static Object invokeGetterMethod(Object obj, String fieldName) {
		
		try {
			// root: string before dot
			int idx = fieldName.indexOf(".");
			String root = idx == -1 ? fieldName : fieldName.substring(0, idx);

			// next: string after dot
			String next = idx == -1 ? "" : fieldName.substring(idx + 1, fieldName.length());

			PropertyDescriptor propertyDescriptor = new PropertyDescriptor(root, obj.getClass());
			Method getter = propertyDescriptor.getReadMethod();

			if (getter != null) {
				Object object = getter.invoke(obj);
				if (object != null) {
					if (!StringUtils.isEmpty(next)) {
						// recursively called if nested object is found
						return invokeGetterMethod(object, next);
					}
					return object;
				}
			}

		} catch (Exception e) {
			log.info("Exceiption: {}", e.getMessage());
		}
		return null;
	}
}
