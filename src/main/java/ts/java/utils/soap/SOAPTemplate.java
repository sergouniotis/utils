package ts.java.utils.soap;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import ts.java.utils.Assert;
import ts.java.utils.StringUtils;
import ts.java.utils.xml.DomParser;

public class SOAPTemplate {

	private SoapMessageFactory soapMessageFactory;

	private DomParser domParser;

	public SOAPTemplate() {

		soapMessageFactory = new SoapMessageFactory(SOAPConstants.SOAP_1_1_PROTOCOL);

		domParser = new DomParser();
	}

	public SOAPMessage wrap(byte[] xml, String messageId, String action, String from, String replyTo, String relatesTo)
			throws IOException, SOAPException, ParserConfigurationException, SAXException {

		Document doc = domParser.parse(xml);

		/*
		 * Create soap body
		 */
		SOAPMessage message = soapMessageFactory.createMessage();
		message.getSOAPBody().addDocument(doc);

		/*
		 * Create soap header
		 */
		SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();

		Assert.hasText(action);
		Name actionName = envelope.createName("Action", "wsa", "http://www.w3.org/2005/08/addressing");
		SOAPHeaderElement actionElement = message.getSOAPHeader().addHeaderElement(actionName);
		actionElement.addTextNode(action);

		if (StringUtils.hasText(from)) {
			Name fromName = envelope.createName("From", "wsa", "http://www.w3.org/2005/08/addressing");
			SOAPHeaderElement fromElement = message.getSOAPHeader().addHeaderElement(fromName);
			Name fromAddress = envelope.createName("Address", "wsa", "http://www.w3.org/2005/08/addressing");
			SOAPElement fromAdressElement = fromElement.addChildElement(fromAddress);
			fromAdressElement.addTextNode(from);
		}

		if (StringUtils.hasText(replyTo)) {
			Name replyToName = envelope.createName("ReplyTo", "wsa", "http://www.w3.org/2005/08/addressing");
			SOAPHeaderElement replyToElement = message.getSOAPHeader().addHeaderElement(replyToName);
			Name replyToAddress = envelope.createName("Address", "wsa", "http://www.w3.org/2005/08/addressing");
			SOAPElement replyToAddressElement = replyToElement.addChildElement(replyToAddress);
			replyToAddressElement.addTextNode(replyTo);
		}

		Assert.hasText(messageId);
		Name messageIdName = envelope.createName("MessageID", "wsa", "http://www.w3.org/2005/08/addressing");
		SOAPHeaderElement messageIdElement = message.getSOAPHeader().addHeaderElement(messageIdName);
		messageIdElement.addTextNode(messageId);

		if (StringUtils.hasText(relatesTo)) {
			Name relatesToName = envelope.createName("RelatesTo", "wsa", "http://www.w3.org/2005/08/addressing");
			SOAPHeaderElement relatesToElement = message.getSOAPHeader().addHeaderElement(relatesToName);
			relatesToElement.addTextNode(relatesTo);
		}

		return message;

	}

	public SOAPMessage create(byte[] data) throws IOException, SOAPException, ParserConfigurationException, SAXException {

		Document document = domParser.parse(data);

		SOAPMessage soapMessage = soapMessageFactory.createMessage();

		DOMSource domSource = new DOMSource(document);

		soapMessage.getSOAPPart().setContent(domSource);

		return soapMessage;
	}

}
