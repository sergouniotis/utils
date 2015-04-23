package ts.java.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Provides a thread-safe date formatter.
 * 
 */
public final class DateFormatter {

	public static final String TIME_FORMAT_SHORT = "HH:mm";

	public static final String TIME_FORMAT_LONG = TIME_FORMAT_SHORT + ":ss";

	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final String DATE_TIME_FORMAT = DATE_FORMAT + " " + TIME_FORMAT_LONG;

	public static final String DATE_TIME_FORMAT_ORACLE = "YYYY-MM-DD HH24:MI:SS";

	private static final ThreadLocal<SimpleDateFormat> formatter = new ThreadLocal<SimpleDateFormat>() {

		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(DATE_TIME_FORMAT);
		}
	};

	private DateFormatter() {
		// Prevent instantiations.
	}

	/**
	 * Formats a Date into a date/time string.
	 * 
	 * @param date
	 *        the time value to be formatted into a time
	 * @return the formatted time string.
	 */
	public static String format(Date date) {
		if (null == date) {
			return null;
		}
		return formatter.get().format(date);
	}

	/**
	 * Formats a Calendar into a date/time string. Adapter method that delegates to {@link DateFormatter.format()} method
	 * 
	 * @param date
	 *        the time value to be formatted into a time
	 * @return the formatted time string.
	 */
	public static String format(Calendar c) {
		if (c == null) {
			return null;
		}
		return format(c.getTime());
	}

	/**
	 * Parses text from the beginning of the given string to produce a date. The method may not use the entire text of the given
	 * string. See the parse(String, ParsePosition) method for more information on date parsing.
	 * 
	 * @param source
	 *        A String whose beginning should be parsed.
	 * @return A Date parsed from the string.
	 * @throws ParseException
	 *         if the beginning of the specified string cannot be parsed.
	 */
	public static Date parse(String source) throws ParseException {
		return formatter.get().parse(source);
	}
}
