package ts.java.utils.test.jaxb;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.xml.sax.SAXException;

import emsa.nsw.oxm.jaxb.JAXB2Marshaller;

public class MarshallTestCase {

	private static final String REQUEST_MESSAGE = "EPCRequest.xml";

	private static final String[] args = { "epc.xsd", "iso28005-2.xsd" };

	private static final String contextPath = "org.iso._28005_2";

//	@Test
	public void testValidityWithJAXB() throws Exception {
		try {
			InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(REQUEST_MESSAGE);
			StreamSource inputSource = new StreamSource(stream);

			JAXB2Marshaller marshaller = new JAXB2Marshaller(contextPath, args);

			marshaller.unmarshall(inputSource);

		} catch (JAXBException | SAXException | IOException e) {
			throw new RuntimeException(e);
		}
	}

}
