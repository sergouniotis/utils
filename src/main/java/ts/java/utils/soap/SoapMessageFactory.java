/**
 * 
 */
package ts.java.utils.soap;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

/**
 * @author sergouniotis
 * 
 */
public class SoapMessageFactory {

	private static final String META_FACTORY_CLASS_PROPERTY = "javax.xml.soap.MetaFactory";

	private static final String DEFAULT_META_FACTORY_CLASS = "com.sun.xml.internal.messaging.saaj.soap.SAAJMetaFactoryImpl";

	/** The message factory. */
	private volatile javax.xml.soap.MessageFactory messageFactory;

	/** The protocol. */
	private String protocol;

	/**
	 * Instantiates a new message factory helper.
	 */
	public SoapMessageFactory() {
		this(SOAPConstants.SOAP_1_1_PROTOCOL);
	}

	/**
	 * The Constructor.
	 * 
	 * @param protocol
	 *        the protocol
	 */
	public SoapMessageFactory(String protocol) {
		this.protocol = protocol;
		System.setProperty(META_FACTORY_CLASS_PROPERTY, DEFAULT_META_FACTORY_CLASS);
	}

	/**
	 * Creates the message.
	 * 
	 * @return the SOAP message
	 * @throws IOException
	 *         the IO exception
	 * @throws SOAPException
	 *         the SOAP exception
	 */
	public SOAPMessage createMessage() throws IOException, SOAPException {
		return getMessageFactory().createMessage();
	}

	/**
	 * Creates the message.
	 * 
	 * @param mimeHeaders
	 *        the mime headers
	 * @param inputStream
	 *        the input stream
	 * @return the SOAP message
	 * @throws IOException
	 *         the IO exception
	 * @throws SOAPException
	 *         the SOAP exception
	 */
	public SOAPMessage createMessage(MimeHeaders mimeHeaders, InputStream inputStream) throws IOException, SOAPException {
		return getMessageFactory().createMessage(mimeHeaders, inputStream);
	}

	/**
	 * Gets the message factory.
	 * 
	 * @return the message factory
	 * @throws SOAPException
	 *         the SOAP exception
	 */
	private MessageFactory getMessageFactory() throws SOAPException {
		javax.xml.soap.MessageFactory result = messageFactory;
		if (result == null) {
			synchronized (this) {
				result = messageFactory;
				if (result == null) {
					result = javax.xml.soap.MessageFactory.newInstance(protocol);
				}
			}
		}
		return result;
	}

	@PostConstruct
	public void afterPropertiesSet() {
		System.setProperty(META_FACTORY_CLASS_PROPERTY, DEFAULT_META_FACTORY_CLASS);
	}
}
