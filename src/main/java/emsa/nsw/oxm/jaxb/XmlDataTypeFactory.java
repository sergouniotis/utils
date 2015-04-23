/**
 * 
 */
package emsa.nsw.oxm.jaxb;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * @author sergouniotis
 * 
 */
public final class XmlDataTypeFactory {

	private static volatile DatatypeFactory instance;

	private static Class<XmlDataTypeFactory> lock = XmlDataTypeFactory.class;

	private XmlDataTypeFactory() {
	}

	/**
	 * 
	 * FIXME is thread safe Gets the single instance of XmlDataTypeFactory.
	 * 
	 * @return single instance of XmlDataTypeFactory
	 * @throws DatatypeConfigurationException
	 *         the datatype configuration exception
	 */
	private static DatatypeFactory getInstance() throws DatatypeConfigurationException {
		DatatypeFactory result = instance;
		if (null == result) {
			synchronized (lock) {
				result = instance;
				if (null == result) {
					instance = result = DatatypeFactory.newInstance();
				}
			}
		}
		return instance;
	}

	/**
	 * 
	 * 
	 * @param gregorianCal
	 *        the gregorian cal
	 * @return the xML gregorian calendar
	 * @throws DatatypeConfigurationException
	 */
	public static XMLGregorianCalendar getXMLGregorianCalendar(final Calendar gregorianCal) throws DatatypeConfigurationException {

		XMLGregorianCalendar calendar = getInstance()
		// .newXMLGregorianCalendar( (GregorianCalendar)gregorianCal);
		.newXMLGregorianCalendar(gregorianCal.get(Calendar.YEAR), gregorianCal.get(Calendar.MONTH) + 1,
				gregorianCal.get(Calendar.DATE), gregorianCal.get(Calendar.HOUR_OF_DAY), gregorianCal.get(Calendar.MINUTE),
				gregorianCal.get(Calendar.SECOND), DatatypeConstants.FIELD_UNDEFINED,
				// gregorianCal.get(GregorianCalendar.MILLISECOND),
				// NOTE UTC time
				// 0
				(gregorianCal.get(Calendar.ZONE_OFFSET) + gregorianCal.get(Calendar.DST_OFFSET)) / 60000);
		//
		// NOTE XMLGregorianCalendarImpl is Sun proprietary API and may be
		// removed in a future release
		// com.sun.org.apache.xerces.internal.jaxp.datatype.
		//
		// XMLGregorianCalendarImpl.createDateTime(gregorianCal
		// .get(GregorianCalendar.YEAR), gregorianCal
		// .get(GregorianCalendar.MONTH) + 1, gregorianCal
		// .get(GregorianCalendar.DATE), gregorianCal
		// .get(GregorianCalendar.HOUR_OF_DAY), gregorianCal
		// .get(GregorianCalendar.MINUTE), gregorianCal
		// .get(GregorianCalendar.SECOND));

		return calendar;
	}

	/**
	 * Gets the xml date.
	 * 
	 * @param gregorianCal
	 *        the gregorian cal
	 * @return the xml date
	 * @throws DatatypeConfigurationException
	 *         the datatype configuration exception
	 */
	private static XMLGregorianCalendar getXmlDate(final Calendar gregorianCal) throws DatatypeConfigurationException {
		DatatypeFactory df = getInstance();
		XMLGregorianCalendar xgc = df.newXMLGregorianCalendar();
		xgc.setYear(gregorianCal.get(Calendar.YEAR));
		xgc.setMonth(gregorianCal.get(Calendar.MONTH) + 1);
		xgc.setDay(gregorianCal.get(Calendar.DATE));
		return xgc;
	}

	public static XMLGregorianCalendar toXmlDate(Calendar cal) throws DatatypeConfigurationException {
		GregorianCalendar gc = new GregorianCalendar();
		XMLGregorianCalendar xmlCal = null;

		if (null != cal) {
			gc.setTime(cal.getTime());
			xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
					cal.get(Calendar.DATE), DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED,
					DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED);

		}
		return xmlCal;
	}

	public static XMLGregorianCalendar toXmlDatetime(Calendar cal) throws DatatypeConfigurationException {
		GregorianCalendar gc = new GregorianCalendar();
		XMLGregorianCalendar xmlCal = null;
		if (null != cal) {
			gc.setTime(cal.getTime());
			xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
					cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND),
					DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED);
		}
		return xmlCal;
	}

}
