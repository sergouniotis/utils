package ts.java.utils.test.xml;

import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Test;
import org.xml.sax.SAXException;

public class XmlValidityTestCase {

	@Test
	public void testXmlValidty() {
		try {
			URL schemaFile = new URL("http://10.240.82.21:7003/crg-msgapp/service/epc.xsd");
			Source xmlFile = new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream(
					"data/EPCMessage.xml"));
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
			validator.setResourceResolver(new SchemaResolver());
			try {
				validator.validate(xmlFile);
				System.out.println(xmlFile.getSystemId() + " is valid");
			} catch (SAXException e) {
				System.out.println(xmlFile.getSystemId() + " is NOT valid");
				System.out.println("Reason: " + e.getLocalizedMessage());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
