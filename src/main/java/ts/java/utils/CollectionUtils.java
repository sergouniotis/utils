/**
 * 
 */
package ts.java.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * The Class CollectionUtils.
 * 
 * @author sergouniotis
 */
public final class CollectionUtils {

	/**
	 * Instantiates a new collection utils.
	 */
	private CollectionUtils() {

	}

	/**
	 * Return {@code true} if the supplied Collection is {@code null} or empty. Otherwise, return {@code false}.
	 * 
	 * @param collection
	 *        the Collection to check
	 * @return whether the given Collection is empty
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}

	/**
	 * Return {@code true} if the supplied Map is {@code null} or empty. Otherwise, return {@code false}.
	 * 
	 * @param map
	 *        the Map to check
	 * @return whether the given Map is empty
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return (map == null || map.isEmpty());
	}

	/**
	 * Return {@code true} if any element in '{@code candidates}' is contained in '{@code source}'; otherwise returns
	 * {@code false}.
	 * 
	 * @param source
	 *        the source Collection
	 * @param candidates
	 *        the candidates to search for
	 * @return whether any of the candidates has been found
	 */
	public static <T> boolean containsAny(Collection<T> source, Collection<T> candidates) {
		if (isEmpty(source) || isEmpty(candidates)) {
			return false;
		}
		for (Object candidate : candidates) {
			if (source.contains(candidate)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return {@code true} if any element in '{@code candidates}' is contained in '{@code source}'; otherwise returns
	 * {@code false}.
	 * 
	 * @param source
	 *        the source Collection
	 * @param candidates
	 *        the candidates to search for
	 * @return whether any of the candidates has been found
	 */
	public static <T> boolean containsAll(Collection<T> source, Collection<T> candidates) {
		if (isEmpty(source) || isEmpty(candidates)) {
			return false;
		}
		for (Object candidate : candidates) {
			if (!source.contains(candidate)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Contais all.
	 * 
	 * @param <T>
	 *        the generic type
	 * @param current
	 *        the current
	 * @param expected
	 *        the expected
	 * @return true, if successful
	 */
	public static <T> boolean currentContainsAllExpected(Set<T> current, Set<T> expected) {
		boolean result = true;
		for (T e : expected) {
			if (!contains(current, e)) {
				result = false;
				// LOGGER.trace(e + " is not contained in current collection.");
				break;
			}
		}
		return result;
	}

	/**
	 * Contains.
	 * 
	 * @param <T>
	 *        the generic type
	 * @param current
	 *        the current
	 * @param e
	 *        the e
	 * @return true, if successful
	 */
	private static <T> boolean contains(Set<T> current, T e) {
		boolean contains = false;
		for (T c : current) {
			if (c.equals(e)) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	/**
	 * Calculated the relative complement of aCollection with respect to bCollection . This means, all the elements of aCollection
	 * that are not included in bCollection.
	 * 
	 * @param aCollection
	 *        , collection aCollection
	 * @param bCollection
	 *        , collection bCollection
	 * @return a collection that is the relative complement of aCollection with respect to bCollection.
	 */
	public static <T> Collection<T> relativeComplement(Collection<T> aCollection, Collection<T> bCollection) {

		Collection<T> copyList = new ArrayList<>(aCollection);

		for (Iterator<T> iterator = copyList.iterator(); iterator.hasNext();) {

			T a = iterator.next();

			if (bCollection.contains(a)) {
				iterator.remove();
			}

		}

		return copyList;
	}

}
