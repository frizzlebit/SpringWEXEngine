package guru.watson.spring;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptorAdapter;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.xml.transform.StringResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import velocity.soap.Authentication;

@Component
public class AuthnInterceptor extends ClientInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(AuthnInterceptor.class);
	
	@Autowired
	private Authentication authn;
	@Autowired
	private Jaxb2Marshaller marshaller;
	
	@Override
	public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
		logger.info("Enter handleMessage");
		try {
			SaajSoapMessage request = (SaajSoapMessage) messageContext.getRequest();
//			SOAPMessage soapMessage = request.getSaajMessage();
//			SOAPBody soapBody = soapMessage.getSOAPBody();
			
			addAuthn(request);
			
			Object payload = marshaller.unmarshal(request.getPayloadSource());
			logger.info("payload: {}",payload.getClass().getName());
			
			
			StringResult result = new StringResult();
			marshaller.marshal(payload, result);
			logger.info(result.toString());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		return true;
	}
	
	protected void addAuthn(SaajSoapMessage request) throws TransformerException {
		Transformer identityTransform = TransformerFactory.newInstance().newTransformer();
		DOMResult domResult = new DOMResult();
		identityTransform.transform(request.getPayloadSource(), domResult);
		
		Node bodyContent = domResult.getNode(); // modify this
		Document doc = (Document) bodyContent;
//		doc.createElementNS("urn:/velocity/soap", "authentication");
//		logger.info("document: {}", doc.getNamespaceURI());
//		logger.info("content name: {}", doc.getFirstChild().getNodeName());
		
		doc.getFirstChild().appendChild(authNode(doc));

		identityTransform.transform(new DOMSource(bodyContent), request.getPayloadResult());
	}
	
	protected Node authNode(Document doc) {
		Element authentication = doc.createElementNS("urn:/velocity/soap", "authentication");
		Element username = doc.createElementNS("urn:/velocity/soap", "username");
		username.setTextContent(authn.getUsername());
		Element password = doc.createElementNS("urn:/velocity/soap", "password");
		password.setTextContent(authn.getPassword());
		authentication.appendChild(username);
		authentication.appendChild(password);
		return authentication;
	}
}
