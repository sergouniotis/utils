/**
 * 
 */
package ts.java.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author sergouniotis
 * 
 */
public final class CalendarUtils {

	private static final String UTC_TIMEZONE_ID = "GMT";

	public static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone(UTC_TIMEZONE_ID);

	private static final String NSW_DATE_PATERN = "dd/MM/yyyy";

	public static final String[] NSW_DATE_PATERN_ARRAY = new String[] { "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy", "MM/dd/yyyy" };

	private static final String[] NSW_UI_DATE_PATERN_ARRAY = new String[] { "dd/MM/yyyy", "dd/MM/yyyy HH:mm",
		"dd/MM/yyyy HH:mm:ss" };

	private CalendarUtils() {

	}

	// FIXME synchronized
	public static Calendar toUtcCalendar(Calendar calendar) {
		if (null == calendar) {
			return null;
		}
		if (UTC_TIMEZONE.equals(calendar.getTimeZone())) {
			return calendar;
		} else {
			// Calendar utcCalendar = GregorianCalendar.getInstance();
			Calendar utcCalendar = GregorianCalendar.getInstance(UTC_TIMEZONE);
			utcCalendar.setTime(calendar.getTime());
			// utcCalendar.setTimeZone(CalendarUtil.UTC_TIMEZONE);
			return utcCalendar;
		}

	}

	/**
	 * Creates the nsw calendar.
	 * 
	 * @return the calendar
	 */
	public static Calendar createNswCalendar() {
		return Calendar.getInstance(UTC_TIMEZONE, Locale.ENGLISH);
	}

	public static Calendar createPre1DayCalendar() {
		return roll(1, false);
	}

	public static Calendar createAfter1DayCalendar() {
		return roll(1, true);
	}

	public static Calendar createPre2DaysCalendar() {
		return roll(2, false);
	}

	public static Calendar createAfter2DaysCalendar() {
		return roll(2, true);
	}

	public static Calendar createPre7DaysCalendar() {
		return roll(7, false);
	}

	public static Calendar createAfter7DaysCalendar() {
		return roll(7, true);
	}

	private static Calendar roll(int period, boolean up) {

		Calendar time = createNswCalendar();
		if (!up) {
			period = -period;
		}
		time.add(Calendar.DATE, period);
		return time;
	}

	/**
	 * <p>
	 * Parses a string representing a date by trying a variety of different parsers.
	 * </p>
	 * 
	 * <p>
	 * The parse will try each parse pattern in turn. A parse is only deemed sucessful if it parses the whole of the input string.
	 * If no parse patterns match, a ParseException is thrown.
	 * </p>
	 * 
	 * @param str
	 *        the date to parse, not null
	 * @param parsePatterns
	 *        the date format patterns to use, see SimpleDateFormat, not null
	 * @return the parsed date
	 * @throws IllegalArgumentException
	 *         if the date string or pattern array is null
	 * @throws ParseException
	 *         if none of the date patterns were suitable
	 */
	public static Date parseDate(String str, String[] parsePatterns) throws ParseException {
		if (str == null || parsePatterns == null) {
			throw new IllegalArgumentException("Date and Patterns must not be null");
		}

		SimpleDateFormat parser = null;
		ParsePosition pos = new ParsePosition(0);
		for (int i = 0; i < parsePatterns.length; i++) {
			if (i == 0) {
				parser = new SimpleDateFormat(parsePatterns[0]);
			} else {
				parser.applyPattern(parsePatterns[i]);
			}
			pos.setIndex(0);
			Date date = parser.parse(str, pos);
			if (date != null && pos.getIndex() == str.length()) {
				return date;
			}
		}
		throw new ParseException("Unable to parse the date: " + str, -1);
	}

	/**
	 * Helper method to validate date/time string by checking against the patterns array. (may be refactored to return boolean or
	 * just void)
	 * 
	 * @param str
	 * @param parsePatterns
	 * @return
	 * @throws ParseException
	 */
	private static boolean checkUiDateString(String str, String[] parsePatterns) throws ParseException {
		boolean isValid = false;
		if (str == null || parsePatterns == null) {
			throw new IllegalArgumentException("Date and Patterns must not be null");
		}

		SimpleDateFormat parser = new SimpleDateFormat(NSW_DATE_PATERN);
		parser.setTimeZone(CalendarUtils.UTC_TIMEZONE);
		parser.setLenient(false);

		ParsePosition pos = new ParsePosition(0);
		for (int i = 0; i < parsePatterns.length; i++) {
			if (i == 0) {
				parser = new SimpleDateFormat(parsePatterns[0]);
			} else {
				parser.applyPattern(parsePatterns[i]);
			}
			pos.setIndex(0);
			Date date = parser.parse(str, pos);
			if (date != null && pos.getIndex() == str.length()) {
				isValid = true;
				break;
			}
		}
		return isValid;
		// throw new ParseException("Unable to parse the date: " + str, -1);
	}

	/**
	 * 
	 * 
	 * @param value
	 * @return
	 * @throws ParseException
	 */
	public static Calendar parseDate(String value) throws ParseException {
		// first check the validity of the date string
		boolean valid = checkUiDateString(value, NSW_UI_DATE_PATERN_ARRAY);
		if (!valid) {
			throw new ParseException("Unable to parse the date: " + value + ". Please use the pattern dd/MM/yyyy", -1);
		}

		// create a new utc calendar (current date/time)
		Calendar calendar = Calendar.getInstance(CalendarUtils.UTC_TIMEZONE);
		if (value.indexOf(" ") > -1) {
			value = value.substring(0, value.indexOf(" "));
		}
		String[] artifacts = value.split("/");
		// parse day/month/year from the incoming string
		int day = Integer.parseInt(artifacts[0]);
		int month = Integer.parseInt(artifacts[1]);
		int year = Integer.parseInt(artifacts[2]);
		// int day = Integer.valueOf(value.substring(0, 2));
		// int month = Integer.valueOf(value.substring(3, 5));
		// int year = Integer.valueOf(value.substring(6, 10));

		// set the new parsed date values on the calendar
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.YEAR, year);

		return calendar;
	}

	/**
	 * 
	 * 
	 * @param value
	 * @return
	 * @throws ParseException
	 */
	public static Calendar parseDateTime(String value) throws ParseException {
		// first check the validity of the date string
		boolean valid = checkUiDateString(value, NSW_UI_DATE_PATERN_ARRAY);
		if (!valid) {
			throw new ParseException("Unable to parse the date: " + value + ". Please use the pattern dd/MM/yyyy HH:mm", -1);
		}

		// create a new utc calendar (current date/time)
		Calendar calendar = Calendar.getInstance(CalendarUtils.UTC_TIMEZONE);

		String datePart = value;
		if (datePart.indexOf(" ") > -1) {
			datePart = datePart.substring(0, datePart.indexOf(" "));
		}
		String[] dateArtifacts = datePart.split("/");
		// parse day/month/year from the incoming string
		int day = Integer.parseInt(dateArtifacts[0]);
		int month = Integer.parseInt(dateArtifacts[1]);
		int year = Integer.parseInt(dateArtifacts[2]);
		// int day = Integer.valueOf(value.substring(0, 2));
		// int month = Integer.valueOf(value.substring(3, 5));
		// int year = Integer.valueOf(value.substring(6, 10));

		String timePart = value;
		if (timePart.indexOf(" ") > -1) {
			timePart = timePart.substring(timePart.indexOf(" ") + 1, timePart.length());
		}
		String[] timeArtifacts = timePart.split(":");

		int hour = 0;
		int min = 0;
		if (timeArtifacts.length >= 2) {
			hour = Integer.valueOf(timeArtifacts[0]);
			min = Integer.valueOf(timeArtifacts[1]);
		}
		// if (value.length() >= 16) {
		// hour = Integer.valueOf(value.substring(11, 13));
		// min = Integer.valueOf(value.substring(14, 16));
		// }

		// set the new parsed date values on the calendar
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.YEAR, year);

		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, 0);
		return calendar;
	}

	public static Calendar copyDate(Calendar calendar) {
		if (null == calendar) {
			return null;
		}
		if (CalendarUtils.UTC_TIMEZONE.equals(calendar.getTimeZone())) {
			return calendar;
		} else {
			// NOTE take locale from constants
			Calendar utc = Calendar.getInstance(CalendarUtils.UTC_TIMEZONE, Locale.ENGLISH);
			utc.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
			utc.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
			utc.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			return utc;
		}
	}

	public static Calendar copyDateTime(Calendar calendar) {
		if (null == calendar) {
			return null;
		}
		if (CalendarUtils.UTC_TIMEZONE.equals(calendar.getTimeZone())) {
			return calendar;
		} else {
			// NOTE take locale from constants
			Calendar utc = Calendar.getInstance(CalendarUtils.UTC_TIMEZONE, Locale.ENGLISH);
			utc.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
			utc.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
			utc.set(Calendar.DATE, calendar.get(Calendar.DATE));
			utc.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
			utc.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
			utc.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
			return utc;
		}
	}

}
