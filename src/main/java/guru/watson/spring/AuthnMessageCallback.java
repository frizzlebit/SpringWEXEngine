package guru.watson.spring;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.transform.StringResult;
import org.w3c.dom.Document;

import velocity.soap.Authentication;

@Component
public class AuthnMessageCallback implements WebServiceMessageCallback {
	private static final Logger logger = LoggerFactory.getLogger(AuthnMessageCallback.class);
	
	@Autowired
	private Authentication authn;
	@Autowired
	private Jaxb2Marshaller springJaxb;
	
	@Override
	public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
		Object graph = springJaxb.unmarshal(message.getPayloadSource());
		try {
			Method method = graph.getClass().getMethod("setAuthentication", Authentication.class);
			method.invoke(graph, authn);
			logger.info("Adding authentication for: {}",graph.getClass().getSimpleName());
			SoapMessage soap = (SoapMessage) message;
			soap.setDocument(getDocument(graph));
			StringResult result = new StringResult();
			springJaxb.marshal(graph, result);
			logger.info(result.toString());
		} catch (Exception e) {
			logger.warn("No setAuthentication method for: {}", graph.getClass().getSimpleName());
		}
	}
	
	protected Document getDocument(Object graph) throws JAXBException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    Document doc = dbf.newDocumentBuilder().newDocument(); 
		JAXBContext jCtx = springJaxb.getJaxbContext();
		Marshaller mar = jCtx.createMarshaller();
		mar.marshal(graph, doc);
		return doc;
	}

}
