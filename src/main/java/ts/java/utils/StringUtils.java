/**
 * 
 */
package ts.java.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author sergouniotis
 * 
 */
public final class StringUtils {

	public static final String EMPTY_STRING = "";

	public static final String EMPTY_VALUE = "_NULL";

	private StringUtils() {

	}

	public static boolean isValidNumber(String value) {

		if (!hasText(value)) {
			return false;
		}

		int size = value.length();

		for (int i = 0; i < size; i++) {
			char aChar = value.charAt(i);
			if (!Character.isDigit(aChar)) {
				return false;
			}
		}

		return size > 0;
	}

	public static boolean isEmptyString(String value) {
		return null != value && EMPTY_STRING.equals(value);
	}

	public static boolean hasNullValue(String value) {
		return null != value && EMPTY_VALUE.equals(value);
	}

	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	public static String streamToString(InputStream in) throws IOException {
		StringBuilder out = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			out.append(line);
		}
		br.close();
		return out.toString();
	}

	/**
	 * Convenience method to return a Collection as a CSV String. E.g. useful for {@code toString()} implementations.
	 * 
	 * @param coll
	 *        the Collection to display
	 * @return the delimited String
	 */
	public static String collectionToCommaDelimitedString(Collection<?> coll) {
		return collectionToDelimitedString(coll, ",");
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV) String. E.g. useful for {@code toString()}
	 * implementations.
	 * 
	 * @param coll
	 *        the Collection to display
	 * @param delim
	 *        the delimiter to use (probably a ",")
	 * @return the delimited String
	 */
	public static String collectionToDelimitedString(Collection<?> coll, String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV) String. E.g. useful for {@code toString()}
	 * implementations.
	 * 
	 * @param coll
	 *        the Collection to display
	 * @param delim
	 *        the delimiter to use (probably a ",")
	 * @param prefix
	 *        the String to start each element with
	 * @param suffix
	 *        the String to end each element with
	 * @return the delimited String
	 */
	public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix) {
		if (CollectionUtils.isEmpty(coll)) {
			return EMPTY_STRING;
		}
		StringBuilder sb = new StringBuilder();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	public static String toCommaSeparatedString(Set<String> collectionOfStrings) {
		if (CollectionUtils.isEmpty(collectionOfStrings)) {
			return EMPTY_STRING;
		}
		StringBuilder result = new StringBuilder();
		for (String string : collectionOfStrings) {
			result.append(string);
			result.append(",");
		}
		return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
	}

	/**
	 * Convenience method to convert a CSV string list to a set. Note that this will suppress duplicates.
	 * 
	 * @param str
	 *        the input String
	 * @return a Set of String entries in the list
	 */
	public static Set<String> commaDelimitedListToSet(String str) {
		Set<String> set = new TreeSet<String>();
		String[] tokens = commaDelimitedListToStringArray(str);
		for (String token : tokens) {
			set.add(token);
		}
		return set;
	}

	/**
	 * Convert a CSV list into an array of Strings.
	 * 
	 * @param str
	 *        the input String
	 * @return an array of Strings, or the empty array in case of empty input
	 */
	public static String[] commaDelimitedListToStringArray(String str) {
		return delimitedListToStringArray(str, ",");
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>
	 * A single delimiter can consists of more than one character: It will still be considered as single delimiter string, rather
	 * than as bunch of potential delimiter characters - in contrast to {@code tokenizeToStringArray}.
	 * 
	 * @param str
	 *        the input String
	 * @param delimiter
	 *        the delimiter between elements (this is a single delimiter, rather than a bunch individual delimiter characters)
	 * @return an array of the tokens in the list
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(String str, String delimiter) {
		return delimitedListToStringArray(str, delimiter, null);
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>
	 * A single delimiter can consists of more than one character: It will still be considered as single delimiter string, rather
	 * than as bunch of potential delimiter characters - in contrast to {@code tokenizeToStringArray}.
	 * 
	 * @param str
	 *        the input String
	 * @param delimiter
	 *        the delimiter between elements (this is a single delimiter, rather than a bunch individual delimiter characters)
	 * @param charsToDelete
	 *        a set of characters to delete. Useful for deleting unwanted line breaks: e.g. "\r\n\f" will delete all new lines and
	 *        line feeds in a String.
	 * @return an array of the tokens in the list
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] { str };
		}
		List<String> result = new ArrayList<>();
		if ("".equals(delimiter)) {
			for (int i = 0; i < str.length(); i++) {
				result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
			}
		} else {
			int pos = 0;
			int delPos;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				// Add rest of String, but not in case of empty input.
				result.add(deleteAny(str.substring(pos), charsToDelete));
			}
		}
		return toStringArray(result);
	}

	/**
	 * Delete any character in a given String.
	 * 
	 * @param inString
	 *        the original String
	 * @param charsToDelete
	 *        a set of characters to delete. E.g. "az\n" will delete 'a's, 'z's and new lines.
	 * @return the resulting String
	 */
	public static String deleteAny(String inString, String charsToDelete) {
		if (!hasLength(inString) || !hasLength(charsToDelete)) {
			return inString;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < inString.length(); i++) {
			char c = inString.charAt(i);
			if (charsToDelete.indexOf(c) == -1) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * Split a String at the first occurrence of the delimiter. Does not include the delimiter in the result.
	 * 
	 * @param toSplit
	 *        the string to split
	 * @param delimiter
	 *        to split the string up with
	 * @return a two element array with index 0 being before the delimiter, and index 1 being after the delimiter (neither element
	 *         includes the delimiter); or {@code null} if the delimiter wasn't found in the given input String
	 */
	public static String[] split(String toSplit, String delimiter) {
		if (!hasLength(toSplit) || !hasLength(delimiter)) {
			return null;
		}
		int offset = toSplit.indexOf(delimiter);
		if (offset < 0) {
			return null;
		}
		String beforeDelimiter = toSplit.substring(0, offset);
		String afterDelimiter = toSplit.substring(offset + delimiter.length());
		return new String[] { beforeDelimiter, afterDelimiter };
	}

	/**
	 * Copy the given Collection into a String array. The Collection must contain String elements only.
	 * 
	 * @param collection
	 *        the Collection to copy
	 * @return the String array ({@code null} if the passed-in Collection was {@code null})
	 */
	public static String[] toStringArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return collection.toArray(new String[collection.size()]);
	}

	/**
	 * Uncapitalize the first letter of the given string
	 * 
	 * @param name
	 * @return
	 */
	public static String uncapitalizeFirstLetter(String name) {
		char[] string = name.toCharArray();
		string[0] = Character.toLowerCase(string[0]);
		return new String(string);
	}

	/**
	 * Capitalize the first letter of the given string
	 * 
	 * @param value
	 * @return
	 */
	public static String capitalizeFirstLetter(String value) {
		char[] string = value.toCharArray();
		string[0] = Character.toUpperCase(string[0]);
		return new String(string);
	}

	public static String padRight(String s, int n) {
		if (n <= 0) {
			return s;
		}
		return String.format("%1$-" + n + "s", s);
	}

	public static String padLeft(String s, int n) {
		if (n <= 0) {
			return s;
		}
		return String.format("%1$" + n + "s", s);
	}

}
