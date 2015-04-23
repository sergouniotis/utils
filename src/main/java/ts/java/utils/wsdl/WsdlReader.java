package ts.java.utils.wsdl;

import java.util.List;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import com.ibm.wsdl.Constants;
import com.ibm.wsdl.extensions.soap.SOAPOperationImpl;

public class WsdlReader {

	private Service service;

	private String targetNamespace;

	public WsdlReader(String wsdl, String serviceName) throws WSDLException {
		WSDLFactory factory = WSDLFactory.newInstance();
		WSDLReader reader = factory.newWSDLReader();
		// reader.setFeature(Constants.FEATURE_VERBOSE, true);
		reader.setFeature(Constants.FEATURE_IMPORT_DOCUMENTS, true);
		reader.setFeature(Constants.FEATURE_PARSE_SCHEMA, true);

		/*
		 * InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("locationservice.wsdl");
		 * InputSource inputSource = new InputSource(inputStream); this.definition = reader.readWSDL("src/main/resources",
		 * inputSource);
		 */

		Definition definition = reader.readWSDL(wsdl);

		this.targetNamespace = definition.getTargetNamespace();

		this.service = definition.getService(new QName(targetNamespace, serviceName));

	}

	public String getSoapAction(String portName, String operation) {

		Port port = service.getPort(portName);
		Binding binding = port.getBinding();
		BindingOperation bindingOperation = binding.getBindingOperation(operation, null, null);

		List<SOAPOperationImpl> extElements = bindingOperation.getExtensibilityElements();
		return extElements.iterator().next().getSoapActionURI();

	}

	public String getLocationUri(String portName) {

		Port port = service.getPort(portName);

		SOAPAddress soapAddress = (SOAPAddress) port.getExtensibilityElements().iterator().next();

		return soapAddress.getLocationURI();
	}

}
