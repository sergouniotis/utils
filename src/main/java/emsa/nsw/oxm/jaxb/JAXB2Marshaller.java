/**
 * 
 */
package emsa.nsw.oxm.jaxb;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;

import ts.java.utils.ObjectUtils;
import ts.java.utils.StringUtils;
import ts.java.utils.xml.XmlSchema;

/**
 * @author sergouniotis
 * 
 */
public class JAXB2Marshaller {

	private volatile JAXBContext jaxbContext;

	private Object jaxbContextMonitor = new Object();

	private Class<?>[] classesToBeBound;

	private String contextPath;

	private String[] schemaLocations;

	private XmlSchema xmlSchema;

	public JAXB2Marshaller() {

	}

	public JAXB2Marshaller(String contextPath, String[] schemaLocations) throws JAXBException {
		this.contextPath = contextPath;
		this.schemaLocations = Arrays.copyOf(schemaLocations, schemaLocations.length);
	}

	public JAXB2Marshaller(Class<?>[] classesToBeBound) {
		this.classesToBeBound = Arrays.copyOf(classesToBeBound, classesToBeBound.length);
	}

	public void marshall(Object object, OutputStream outputStream) throws JAXBException, SAXException, IOException {
		javax.xml.bind.Marshaller marshaller = getJaxbContext().createMarshaller();
		marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setSchema(getSchema());
		marshaller.marshal(object, outputStream);
	}

	public Object unmarshall(Source source) throws JAXBException, SAXException, IOException {
		javax.xml.bind.Unmarshaller unmarshaller = getJaxbContext().createUnmarshaller();
		unmarshaller.setSchema(getSchema());
		return unmarshaller.unmarshal(source);
	}

	private Schema getSchema() throws SAXException, IOException {
		Schema schema = null;
		if (null != schemaLocations && schemaLocations.length > 0) {
			schema = getXMlSchema().getSchema(schemaLocations);
		}
		return schema;
	}

	private XmlSchema getXMlSchema() throws SAXException {
		if (null == xmlSchema) {
			xmlSchema = new XmlSchema();
		}
		return xmlSchema;
	}

	/**
	 * Return the JAXBContext used by this marshaller, lazily building it if necessary.
	 * 
	 * @throws JAXBException
	 */
	public JAXBContext getJaxbContext() throws JAXBException {
		if (this.jaxbContext != null) {
			return this.jaxbContext;
		}
		synchronized (this.jaxbContextMonitor) {
			if (this.jaxbContext == null) {
				if (StringUtils.hasLength(this.contextPath)) {
					// create JAXB context from context path
					this.jaxbContext = JAXBContext.newInstance(this.contextPath);
				} else if (!ObjectUtils.isEmpty(this.classesToBeBound)) {
					// create JAXB context from classes
					this.jaxbContext = JAXBContext.newInstance(this.classesToBeBound);
				}
			}
			return this.jaxbContext;
		}
	}

}
