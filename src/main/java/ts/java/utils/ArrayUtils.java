/**
 * 
 */
package ts.java.utils;

/**
 * @author sergouniotis
 * 
 */
public final class ArrayUtils {

	private ArrayUtils() {

	}

	/**
	 * <p>
	 * Shallow clones an array returning a typecast result and handling <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * The objects in the array are not cloned, thus there is no special handling for multi-dimensional arrays.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input array.
	 * </p>
	 * 
	 * @param array
	 *        the array to shallow clone, may be <code>null</code>
	 * @return the cloned array, <code>null</code> if <code>null</code> input
	 */
	public static Object[] clone(Object[] array) {
		if (array == null) {
			return null;
		}
		return (Object[]) array.clone();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * This method returns <code>null</code> for a <code>null</code> input array.
	 * </p>
	 * 
	 * @param array
	 *        the array to clone, may be <code>null</code>
	 * @return the cloned array, <code>null</code> if <code>null</code> input
	 */
	public static byte[] clone(byte[] array) {
		if (array == null) {
			return null;
		}
		return (byte[]) array.clone();
	}
}
