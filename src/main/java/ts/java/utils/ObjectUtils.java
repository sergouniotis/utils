package ts.java.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class ObjectUtils {

	private static final String NULL_STRING = "null";

	private ObjectUtils() {

	}

	/**
	 * Determine the class name for the given object.
	 * <p>
	 * Returns <code>"null"</code> if <code>obj</code> is <code>null</code>.
	 * 
	 * @param obj
	 *        the object to introspect (may be <code>null</code>)
	 * @return the corresponding class name
	 */
	public static String nullSafeClassName(Object obj) {
		return (obj != null ? obj.getClass().getName() : NULL_STRING);
	}

	/**
	 * Determine whether the given array is empty: i.e. {@code null} or of zero length.
	 * 
	 * @param array
	 *        the array to check
	 */
	public static boolean isEmpty(Object[] array) {
		return (array == null || array.length == 0);
	}

	/**
	 * Given the source object and the destination, which must be the same class or a subclass, copy all fields, including
	 * inherited fields. Designed to work on objects with public no-arg constructors.
	 * 
	 * @throws IllegalArgumentException
	 *         if the arguments are incompatible
	 */
	public static void shallowCopyFieldState(final Object src, final Object dest, FieldCallback fc, FieldFilter ff)
			throws IllegalArgumentException {
		if (src == null) {
			throw new IllegalArgumentException("Source for field copy cannot be null");
		}
		if (dest == null) {
			throw new IllegalArgumentException("Destination for field copy cannot be null");
		}
		if (!src.getClass().isAssignableFrom(dest.getClass())) {
			throw new IllegalArgumentException("Destination class [" + dest.getClass().getName()
					+ "] must be same or subclass as source class [" + src.getClass().getName() + "]");
		}
		doWithFields(src.getClass(), fc, ff);
	}

	/**
	 * Given the source object and the destination, which must be the same class or a subclass, copy all fields, including
	 * inherited fields. Designed to work on objects with public no-arg constructors.
	 * 
	 * @throws IllegalArgumentException
	 *         if the arguments are incompatible
	 */
	public static void shallowCopyFieldState(final Object src, final Object dest) throws IllegalArgumentException {
		shallowCopyFieldState(src, dest, new FieldCallback() {

			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				ReflectionUtils.makeAccessible(field);
				Object srcValue = field.get(src);
				field.set(dest, srcValue);
			}
		}, COPYABLE_FIELDS);
	}

	/**
	 * Invoke the given callback on all fields in the target class, going up the class hierarchy to get all declared fields.
	 * 
	 * @param clazz
	 *        the target class to analyze
	 * @param fc
	 *        the callback to invoke for each field
	 * @param ff
	 *        the filter that determines the fields to apply the callback to
	 */
	public static void doWithFields(Class<?> clazz, FieldCallback fc, FieldFilter ff) throws IllegalArgumentException {

		// Keep backing up the inheritance hierarchy.
		Class<?> targetClass = clazz;
		do {
			Field[] fields = targetClass.getDeclaredFields();
			for (Field field : fields) {
				// Skip static and final fields.
				if (ff != null && !ff.matches(field)) {
					continue;
				}
				try {
					fc.doWith(field);
				} catch (IllegalAccessException ex) {
					throw new IllegalStateException("Shouldn't be illegal to access field '" + field.getName() + "': " + ex);
				}
			}
			targetClass = targetClass.getSuperclass();
		}
		while (targetClass != null && targetClass != Object.class);
	}

	/**
	 * Callback interface invoked on each field in the hierarchy.
	 */
	public interface FieldCallback {

		/**
		 * Perform an operation using the given field.
		 * 
		 * @param field
		 *        the field to operate on
		 */
		void doWith(Field field) throws IllegalArgumentException, IllegalAccessException;
	}

	/**
	 * Callback optionally used to filter fields to be operated on by a field callback.
	 */
	public interface FieldFilter {

		/**
		 * Determine whether the given field matches.
		 * 
		 * @param field
		 *        the field to check
		 */
		boolean matches(Field field);
	}

	/**
	 * Pre-built FieldFilter that matches all non-static, non-final fields.
	 */
	public static final FieldFilter COPYABLE_FIELDS = new FieldFilter() {

		public boolean matches(Field field) {
			return !(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()));
		}
	};

	/**
	 * NOTE This MUST be used on Serializable objects
	 * 
	 * @param object
	 * @return
	 */
	public static <T> T cloneSerializable(T object) throws Exception {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object result = ois.readObject();
			ois.close();
			oos.close();
			return (T) result;
		} catch (Exception e) {
			throw e;
		}
	}

}
