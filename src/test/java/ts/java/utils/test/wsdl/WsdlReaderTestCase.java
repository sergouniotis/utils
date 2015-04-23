/**
 * 
 */
package ts.java.utils.test.wsdl;

import javax.wsdl.WSDLException;

import org.junit.Test;

import ts.java.utils.wsdl.WsdlReader;

/**
 * @author sergouniotis
 *
 */
public class WsdlReaderTestCase {

	private static final String WSDL_LOCATION = "http://eis01.athens.intrasoft-intl.private:7013/ssn-shipparticulars-ws/ssnvesselservice/vessel.wsdl";

	@Test
	public void testWsdlReader() {
		try {

			WsdlReader wsdlReader = new WsdlReader(WSDL_LOCATION, "nsw");

		} catch (WSDLException e) {
			throw new RuntimeException(e);
		}

	}

}
