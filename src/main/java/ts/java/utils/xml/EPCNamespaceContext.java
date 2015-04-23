package ts.java.utils.xml;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

public class EPCNamespaceContext implements NamespaceContext {

	private static final String EPC_NAMESPACE = "http://www.iso.org/28005-2";

	private static final String PREFIX = "epc";

	private static final String XML = "xml";

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.namespace.NamespaceContext#getNamespaceURI(java.lang.String)
	 */
	@Override
	public String getNamespaceURI(String prefix) {
		if (prefix == null) {
			// throw new NullPointerException("Null prefix");
			return EPC_NAMESPACE;
		} else if (PREFIX.equals(prefix)) {
			return EPC_NAMESPACE;
		} else if (XML.equals(prefix)) {
			return XMLConstants.XML_NS_URI;
		}
		return XMLConstants.NULL_NS_URI;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.namespace.NamespaceContext#getPrefix(java.lang.String)
	 */
	@Override
	public String getPrefix(String namespaceURI) {
		return PREFIX;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.namespace.NamespaceContext#getPrefixes(java.lang.String)
	 */
	@Override
	public Iterator<?> getPrefixes(String namespaceURI) {
		throw new UnsupportedOperationException();
	}

}
