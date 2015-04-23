/**
 * 
 */
package ts.java.utils.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import ts.java.utils.StringUtils;

/**
 * @author sergouniotis
 * 
 */
public class XmlSchema {

	private Schema schema;

	private SchemaFactory getSchemaFactory() {
		return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	}

	public Schema getSchema(String schemaLocation) throws SAXException {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(schemaLocation);
		Source source = new StreamSource(inputStream);
		schema = getSchemaFactory().newSchema(source);
		return schema;
	}

	public Schema getSchema(String[] resources) throws IOException, SAXException {
		Source[] schemaSources = new Source[resources.length];
		XMLReader xmlReader = XMLReaderFactory.createXMLReader();
		xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
		for (int i = 0; i < resources.length; i++) {
			String resource = resources[i];
			String trim = resource.trim();
			if (StringUtils.hasText(trim)) {
				InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(trim);
				InputSource inputSource = new InputSource(is);
				schemaSources[i] = new SAXSource(xmlReader, inputSource);
			}
		}
		SchemaFactory schemaFactory = getSchemaFactory();
		schemaFactory.setResourceResolver(new SchemaResolver());
		Schema newSchema = schemaFactory.newSchema(schemaSources);
		newSchema.newValidator().setResourceResolver(schemaFactory.getResourceResolver());
		return newSchema;
	}

}
