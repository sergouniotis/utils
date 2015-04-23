/**
 * 
 */
package ts.java.utils.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;

/**
 * @author sergouniotis
 * 
 */
public class NswTransformer {

	private static final String INDENT_AMOUNT = "{http://xml.apache.org/xslt}indent-amount";

	/**
	 * Transform.
	 * 
	 * @param xmlSource
	 *        the xml source
	 * @param outputTarget
	 *        the output target
	 * @throws TransformerFactoryConfigurationError
	 *         the transformer factory configuration error
	 * @throws TransformerException
	 *         the transformer exception
	 */
	public void transform(Source xmlSource, Result outputTarget) throws TransformerFactoryConfigurationError,
			TransformerException {
		javax.xml.transform.Transformer transformer = createNonIndentingTransformer();
		transformer.transform(xmlSource, outputTarget);
	}

	/**
	 * Transform.
	 * 
	 * @param xmlSource
	 *        the xml source
	 * @param outputTarget
	 *        the output target
	 * @throws TransformerFactoryConfigurationError
	 *         the transformer factory configuration error
	 * @throws TransformerException
	 *         the transformer exception
	 */
	public void transform(Source xmlSource, Source xsltSource, Result outputTarget) throws TransformerFactoryConfigurationError,
			TransformerException {
		javax.xml.transform.Transformer transformer = createNonIndentingTransformer(xsltSource);
		transformer.transform(xmlSource, outputTarget);
	}

	/**
	 * Creates the non indenting transformer.
	 * 
	 * @return the transformer
	 * @throws TransformerConfigurationException
	 *         the transformer configuration exception
	 * @throws TransformerFactoryConfigurationError
	 *         the transformer factory configuration error
	 */
	private javax.xml.transform.Transformer createNonIndentingTransformer() throws TransformerConfigurationException,
			TransformerFactoryConfigurationError {
		javax.xml.transform.Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(INDENT_AMOUNT, "4");
		return transformer;
	}

	/**
	 * Creates the non indenting transformer.
	 * 
	 * @return the transformer
	 * @throws TransformerConfigurationException
	 *         the transformer configuration exception
	 * @throws TransformerFactoryConfigurationError
	 *         the transformer factory configuration error
	 */
	private javax.xml.transform.Transformer createNonIndentingTransformer(Source xsltSource)
			throws TransformerConfigurationException, TransformerFactoryConfigurationError {
		javax.xml.transform.Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(INDENT_AMOUNT, "4");
		return transformer;
	}

	public String format(Source content) throws TransformerException {
		try (ByteArrayOutputStream streamOut = new ByteArrayOutputStream()) {
			StreamResult result = new StreamResult(streamOut);
			transform(content, result);
			return streamOut.toString();
		} catch (IOException e) {
			throw new TransformerException(e);
		}
	}

}
