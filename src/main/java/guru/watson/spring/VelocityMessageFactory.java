package guru.watson.spring;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

@Component
public class VelocityMessageFactory extends SaajSoapMessageFactory {
	private static final Logger logger = LoggerFactory.getLogger(VelocityMessageFactory.class);

	@Override
	public SaajSoapMessage createWebServiceMessage() {
		logger.info("Enter createWebServiceMessage");
		return super.createWebServiceMessage();
	}

	@Override
	public SaajSoapMessage createWebServiceMessage(InputStream inputStream) throws IOException {
		logger.info("Enter createWebServiceMessage(InputStream)");
		return super.createWebServiceMessage(inputStream);
	}

	@Override
	protected void postProcess(SOAPMessage soapMessage) throws SOAPException {
		super.postProcess(soapMessage);
		logger.info(soapMessage.getSOAPBody().toString());
	}
}
