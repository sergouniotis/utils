/**
 * 
 */
package ts.java.utils.xml;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

/**
 * @author sergouniotis
 * 
 */
public final class NswXpath {

	private static final Logger LOGGER = LoggerFactory.getLogger(NswXpath.class);

	private NswXpath() {

	}

	private static XPath initXPath() {
		XPath xpath = XPathFactory.newInstance().newXPath();

		xpath.setNamespaceContext(new EPCNamespaceContext());

		return xpath;
	}

	public static String getElement(InputStream inputStream, String expression) throws XPathExpressionException {
		return initXPath().evaluate(expression, new InputSource(inputStream));
	}

	public static Calendar getCalendarExpression(InputStream inputStream, String expression) throws XPathExpressionException {

		InputSource is = new InputSource(inputStream);
		// return initXPath().evaluate(expression, is , XPathConstants.STRING);
		String value = initXPath().evaluate(expression, is);

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			cal.setTime(sdf.parse(value));
			return cal;
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
			throw new XPathExpressionException(e.getMessage());
		}
	}

}
