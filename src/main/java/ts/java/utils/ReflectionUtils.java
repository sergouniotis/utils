/**
 * 
 */
package ts.java.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @author sergouniotis
 * 
 */
public final class ReflectionUtils {

	private ReflectionUtils() {
	}

	public static boolean isPrimitive(Class<?> type) {
		return primitiveTypeFor(type) != null;
	}

	public static Class<?> primitiveTypeFor(Class<?> wrapper) {
		if (wrapper == Boolean.class || wrapper == boolean.class) {
			return Boolean.TYPE;
		}
		if (wrapper == Byte.class || wrapper == byte.class) {
			return Byte.TYPE;
		}
		if (wrapper == Character.class || wrapper == char.class) {
			return Character.TYPE;
		}
		if (wrapper == Short.class || wrapper == short.class) {
			return Short.TYPE;
		}
		if (wrapper == Integer.class || wrapper == int.class) {
			return Integer.TYPE;
		}
		if (wrapper == Long.class || wrapper == long.class) {
			return Long.TYPE;
		}
		if (wrapper == Float.class || wrapper == float.class) {
			return Float.TYPE;
		}
		if (wrapper == Double.class || wrapper == double.class) {
			return Double.TYPE;
		}
		if (wrapper == Void.class || wrapper == void.class) {
			return Void.TYPE;
		}
		return null;
	}

	/**
	 * Returns the method with the specified property name or {@code null} if it does not exist. This method will prepend 'is' and
	 * 'get' to the property name and capitalize the first letter.
	 * 
	 * @param clazz
	 *        The class to check.
	 * @param fieldName
	 *        The property name.
	 * 
	 * @return Returns the method with the specified property or {@code null} if it does not exist.
	 */
	public static Method findAccessor(Class<?> clazz, String fieldName) throws NoSuchMethodException, SecurityException {
		String fullMethodName = StringUtils.capitalizeFirstLetter(fieldName);
		try {
			return clazz.getMethod("get" + fullMethodName);
		} catch (NoSuchMethodException e) {
			return clazz.getMethod("is" + fullMethodName);
		}
	}

	/**
	 * Returns the method with the specified property name or {@code null} if it does not exist. This method will prepend 'set' to
	 * the property name and capitalize the first letter.
	 * 
	 * @param clazz
	 *        The class to check.
	 * @param fieldName
	 *        The property name.
	 * 
	 * @return Returns the method with the specified property or {@code null} if it does not exist.
	 */
	public static Method findMutator(Class<?> clazz, String fieldName, Class<?>... parameterTypes) throws NoSuchMethodException,
			SecurityException {
		String fullMethodName = StringUtils.capitalizeFirstLetter(fieldName);
		return clazz.getMethod("set" + fullMethodName, parameterTypes);
	}

	public static Method findPublicOrPrivateMutator(Class<?> clazz, String fieldName, Class<?>... parameterTypes)
			throws NoSuchMethodException, SecurityException {
		try {
			String fullMethodName = StringUtils.capitalizeFirstLetter(fieldName);
			Method result = clazz.getDeclaredMethod("set" + fullMethodName, parameterTypes);
			result.setAccessible(true);
			return result;
		} catch (NoSuchMethodException e) {
			Class<?> parent = clazz.getSuperclass();
			if (!Object.class.equals(parent)) {
				return findPublicOrPrivateMutator(parent, fieldName, parameterTypes);
			}
		}
		throw new NoSuchMethodException("No mutator method can be found for field : " + fieldName);
	}

	/**
	 * Attempt to find a {@link Method} on the supplied class with the supplied name and no parameters. Searches all superclasses
	 * up to {@code Object}.
	 * <p>
	 * Returns {@code null} if no {@link Method} can be found.
	 * 
	 * @param clazz
	 *        the class to introspect
	 * @param name
	 *        the name of the method
	 * @return the Method object, or {@code null} if none found
	 */
	public static Method findMethod(Class<?> clazz, String name) {
		return findMethod(clazz, name, new Class[0]);
	}

	/**
	 * Attempt to find a {@link Method} on the supplied class with the supplied name and parameter types. Searches all
	 * superclasses up to {@code Object}.
	 * <p>
	 * Returns {@code null} if no {@link Method} can be found.
	 * 
	 * @param clazz
	 *        the class to introspect
	 * @param name
	 *        the name of the method
	 * @param paramTypes
	 *        the parameter types of the method (may be {@code null} to indicate any signature)
	 * @return the Method object, or {@code null} if none found
	 */
	public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.notNull(name, "Method name must not be null");
		Class<?> searchType = clazz;
		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
			for (Method method : methods) {
				if (name.equals(method.getName())
						&& (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	/**
	 * Make the given field accessible, explicitly setting it accessible if necessary. The {@code setAccessible(true)} method is
	 * only called when actually necessary, to avoid unnecessary conflicts with a JVM SecurityManager (if active).
	 * 
	 * @param field
	 *        the field to make accessible
	 * @see java.lang.reflect.Field#setAccessible
	 */
	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers()))
				&& !field.isAccessible()) {
			field.setAccessible(true);
		}
	}

	private static Constructor<?> findDefaultConstructor(Class<?> clazz) {
		/*
		 * Class<?> searchType = clazz; while (searchType != null) { Method[] methods = (searchType.isInterface() ?
		 * searchType.getMethods() : searchType.getDeclaredMethods()); for (Method method : methods) { if
		 * (name.equals(method.getName()) && (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
		 * return method; } } searchType = searchType.getSuperclass(); }
		 */
		Assert.notNull(clazz, "Class must not be null");
		Constructor<?> result = null;
		Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
		for (int i = 0; i < declaredConstructors.length; i++) {
			Constructor<?> constructor = declaredConstructors[i];
			if (constructor.getParameterTypes().length < 1) {
				constructor.setAccessible(true);
				result = constructor;
			}
		}
		return result;
	}

	/**
	 * Get the field represented by the supplied {@link Field field object} on the specified {@link Object target object}. In
	 * accordance with {@link Field#get(Object)} semantics, the returned value is automatically wrapped if the underlying field
	 * has a primitive type.
	 * <p>
	 * Thrown exceptions are handled via a call to {@link #handleReflectionException(Exception)}.
	 * 
	 * @param field
	 *        the field to get
	 * @param target
	 *        the target object from which to get the field
	 * @return the field's current value
	 */
	public static Object getField(Field field, Object target) {
		try {
			return field.get(target);
		} catch (IllegalAccessException ex) {
			throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass().getName() + ": "
					+ ex.getMessage());
		}
	}

	/**
	 * Attempt to find a {@link Field field} on the supplied {@link Class} with the supplied {@code name}. Searches all
	 * superclasses up to {@link Object}.
	 * 
	 * @param clazz
	 *        the class to introspect
	 * @param name
	 *        the name of the field
	 * @return the corresponding Field object, or {@code null} if not found
	 */
	public static Field findField(Class<?> clazz, String name) {
		return findField(clazz, name, null);
	}

	/**
	 * Attempt to find a {@link Field field} on the supplied {@link Class} with the supplied {@code name} and/or {@link Class
	 * type}. Searches all superclasses up to {@link Object}.
	 * 
	 * @param clazz
	 *        the class to introspect
	 * @param name
	 *        the name of the field (may be {@code null} if type is specified)
	 * @param type
	 *        the type of the field (may be {@code null} if name is specified)
	 * @return the corresponding Field object, or {@code null} if not found
	 */
	public static Field findField(Class<?> clazz, String name, Class<?> type) {
		Assert.notNull(clazz, "Class must not be null");
		Assert.isTrue(name != null || type != null, "Either name or type of the field must be specified");
		Class<?> searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			for (Field field : fields) {
				if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	public static <T> T initiliaze(Class<T> clazz) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		return (T) findDefaultConstructor(clazz).newInstance();
	}
}
