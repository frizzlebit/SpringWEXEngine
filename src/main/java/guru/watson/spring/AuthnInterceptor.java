package guru.watson.spring;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptorAdapter;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import velocity.soap.Authentication;

/**
 * Insert an {@code <authentication/>} node into every client request.
 * @see http://www.ibm.com/support/knowledgecenter/SS8NLW_11.0.1/com.ibm.swg.im.infosphere.dataexpl.engine.srapi.doc/c_api-codex-authentication.html
 */
@Component
public class AuthnInterceptor extends ClientInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(AuthnInterceptor.class);
	
	@Autowired
	private Authentication authn;
	@Value("${service.authnnamespace}")
	private String ns;
	
	@Override
	public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
		logger.trace("Enter handleMessage");
		try {
			SaajSoapMessage request = (SaajSoapMessage) messageContext.getRequest();
			addAuthn(request);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		return true;
	}
	
	protected void addAuthn(SaajSoapMessage request) throws TransformerException {
		Transformer identityTransform = TransformerFactory.newInstance().newTransformer();
		DOMResult domResult = new DOMResult();
		identityTransform.transform(request.getPayloadSource(), domResult);
		
		Node bodyContent = domResult.getNode();
		Document doc = (Document) bodyContent;
		doc.getFirstChild().appendChild(authNode(doc));

		identityTransform.transform(new DOMSource(bodyContent), request.getPayloadResult());
	}
	
	protected Node authNode(Document doc) {
		Element authentication = doc.createElementNS(ns, "authentication");
		Element username = doc.createElementNS(ns, "username");
		username.setTextContent(authn.getUsername());
		Element password = doc.createElementNS(ns, "password");
		password.setTextContent(authn.getPassword());
		authentication.appendChild(username);
		authentication.appendChild(password);
		return authentication;
	}
}
