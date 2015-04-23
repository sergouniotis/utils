/**
 * 
 */
package ts.java.utils.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author sergouniotis
 * 
 */
public class DomParser {

	/** The document builder factory. */
	private volatile DocumentBuilderFactory documentBuilderFactory;

	/**
	 * Instantiates a new dom parser.
	 */
	public DomParser() {
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
	}

	/**
	 * Parses the.
	 * 
	 * @param data
	 *        the data
	 * @return the document
	 * @throws ParserConfigurationException
	 *         the parser configuration exception
	 * @throws SAXException
	 *         the sAX exception
	 * @throws IOException
	 *         Signals that an I/O exception has occurred.
	 */
	public Document parse(byte[] data) throws ParserConfigurationException, SAXException, IOException {
		try (ByteArrayInputStream is = new ByteArrayInputStream(data)) {
			DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			return doc;
		}
	}

	/**
	 * Parses the specified file.
	 * 
	 * @param file
	 *        the file
	 * @return the document
	 * @throws ParserConfigurationException
	 *         the parser configuration exception
	 * @throws SAXException
	 *         the sAX exception
	 * @throws IOException
	 *         Signals that an I/O exception has occurred.
	 */
	public Document parse(File file) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		return doc;
	}

	/**
	 * Creates the.
	 * 
	 * @return the document
	 * @throws ParserConfigurationException
	 *         the parser configuration exception
	 */
	public Document create() throws ParserConfigurationException {
		DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
		return db.newDocument();
	}

}
