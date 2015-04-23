/**
 * 
 */
package ts.java.utils;

/**
 * @author sergouniotis
 * 
 */
public final class Assert {

	private Assert() {
		// NOTE Auto-generated constructor stub
	}

	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isTrue(boolean expression) {
		isTrue(expression, "[Assertion failed] - this expression must be true");
	}

	public static void isNull(Object object, String message) {
		if (object != null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isNull(Object object) {
		isNull(object, "[Assertion failed] - the object argument must be null");
	}

	/**
	 * Assert that an object is not {@code null} .
	 * 
	 * <pre class="code">
	 * Assert.notNull(clazz, &quot;The class must not be null&quot;);
	 * </pre>
	 * 
	 * @param object
	 *        the object to check
	 * @param message
	 *        the exception message to use if the assertion fails
	 * @throws IllegalArgumentException
	 *         if the object is {@code null}
	 */
	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Assert that an object is not {@code null} .
	 * 
	 * <pre class="code">
	 * Assert.notNull(clazz);
	 * </pre>
	 * 
	 * @param object
	 *        the object to check
	 * @throws IllegalArgumentException
	 *         if the object is {@code null}
	 */
	public static void notNull(Object object) {
		notNull(object, "[Assertion failed] - this argument is required; it must not be null");
	}

	/**
	 * Assert that the given String has valid text content; that is, it must not be {@code null} and must contain at least one
	 * non-whitespace character.
	 * 
	 * <pre class="code">
	 * Assert.hasText(name, &quot;'name' must not be empty&quot;);
	 * </pre>
	 * 
	 * @param text
	 *        the String to check
	 */
	public static void hasText(String text) {
		if (!StringUtils.hasText(text)) {
			throw new IllegalArgumentException(
					"[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
		}
	}

}
