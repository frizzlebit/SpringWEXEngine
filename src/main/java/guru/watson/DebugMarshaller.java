package guru.watson;

import javax.xml.transform.Source;

import org.guru.watson.crawler.SearchCollectionCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.oxm.mime.MimeContainer;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

/**
 * Any SOAP fault messages sent from the API will not unmarshall correctly. This
 * class extends {@link Jaxb2Marshaller} to add logic that throws a {@link RuntimeException}
 * when a fault is returned.
 * 
 * <p>The error thrown when this is not done is: {@code unexpected element (uri:"http://schemas.xmlsoap.org/soap/envelope/", local:"Fault")}
 */
public class DebugMarshaller extends Jaxb2Marshaller {
	private static final Logger logger = LoggerFactory.getLogger(SearchCollectionCrawler.class);
	
	@Override
	public Object unmarshal(Source source, MimeContainer mimeContainer) throws XmlMappingException {
		Object mimeMessage = new DirectFieldAccessor(mimeContainer).getPropertyValue("mimeMessage");
		if (mimeMessage instanceof SaajSoapMessage) {
			SaajSoapMessage soapMessage = (SaajSoapMessage) mimeMessage;
			if (soapMessage.getFaultCode() != null) {
				logger.error("fault code: {}", soapMessage.getFaultCode());
				logger.error("fault reason: {}", soapMessage.getFaultReason());
				throw new RuntimeException(soapMessage.getFaultReason());
			}
		}
		return super.unmarshal(source, mimeContainer);
	}
}
