/**
 * 
 */
package ts.java.utils.test.xml;

import java.io.InputStream;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * @author sergouniotis
 * 
 */
public class SchemaResolver implements LSResourceResolver {

	@Override
	public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
		InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(systemId);
		return new Input(publicId, systemId, resourceAsStream);
	}

}
