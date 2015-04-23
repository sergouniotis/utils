/**
 * 
 */
package ts.java.utils.test.soap;

import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ts.java.utils.soap.SoapMessageFactory;
import ts.java.utils.xml.NswTransformer;

/**
 * @author sergouniotis
 * 
 */
public class SoapTestCase {

	@Test
	public void testSoap() {

		try {

			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("message.xml");

			SoapMessageFactory soapMessageFactory = soapMessageFactory = new SoapMessageFactory(SOAPConstants.SOAP_1_1_PROTOCOL);

			SOAPMessage soapMessage = soapMessageFactory.createMessage(new MimeHeaders(), is);

			SOAPBody soapBody = soapMessage.getSOAPBody();

			SOAPHeader soapHeader = soapMessage.getSOAPHeader();

			Source content = soapMessage.getSOAPPart().getContent();

			NswTransformer transformer = new NswTransformer();

			String data = transformer.format(content);

			System.out.println(data);

			// NodeList element = soapHeader.getElementsByTagNameNS("http://www.w3.org/2005/08/addressing", "Action");

			// Node node = element.item(0);

			// System.out.println(format(node));

			// format body payload
			Document doc = soapBody.extractContentAsDocument();

			System.out.println(format(doc));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Format.
	 * 
	 * @param source
	 *        the source
	 * @return the string
	 * @throws TransformerFactoryConfigurationError
	 *         the transformer factory configuration error
	 * @throws TransformerException
	 *         the transformer exception
	 */
	private String format(Node node) throws TransformerFactoryConfigurationError, TransformerException {
		/*
		 * Source
		 */
		DOMSource domSource = new DOMSource(node);

		/*
		 * Result
		 */
		StringWriter writer = new StringWriter();
		Result streamResult = new StreamResult(writer);

		new NswTransformer().transform(domSource, streamResult);
		return writer.toString();
	}

}
