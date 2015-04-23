package ts.java.utils.test.xslt;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;

public class XSLTTest {

	private static final String EPC_REQUEST_MESSAGE = "EPCRequest.xml";

	private static final String EIS_XSL = "eis.xsl";

	private static String IDENT = "{http://xml.apache.org/xslt}indent-amount";

	@Test
	public void transform() throws TransformerConfigurationException, TransformerException {

		InputStream xsl = Thread.currentThread().getContextClassLoader().getResourceAsStream(EIS_XSL);
		InputStream xml = Thread.currentThread().getContextClassLoader().getResourceAsStream(EPC_REQUEST_MESSAGE);

		TransformerFactory factory = TransformerFactory.newInstance();
		StreamSource xslStream = new StreamSource(xsl);
		Transformer transformer = factory.newTransformer(xslStream);
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		transformer.setOutputProperty(IDENT, "4");

		StreamSource in = new StreamSource(xml);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		// StreamResult out = new StreamResult(System.out);
		StreamResult out = new StreamResult(output);
		transformer.transform(in, out);

		String value = new String(output.toByteArray());
		String withSender = value.replace("_SENDER_", "adsfasdf");

		String withMsRefId = withSender.replace("_MSRefId_", UUID.randomUUID().toString());

		String withShipcallId = withMsRefId.replace("_ShipCallId_", "GR1639");

		System.out.println(withShipcallId);

	}
}
