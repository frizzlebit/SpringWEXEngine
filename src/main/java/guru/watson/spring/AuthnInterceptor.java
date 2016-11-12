package guru.watson.spring;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptorAdapter;
import org.springframework.ws.context.MessageContext;
import org.springframework.xml.transform.StringResult;

import velocity.soap.Authentication;

@Component
public class AuthnInterceptor extends ClientInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(AuthnInterceptor.class);
	
	@Autowired
	private Unmarshaller unmarshaller;
	@Autowired
	private Marshaller marshaller;
	
	@Override
	public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
		try {
			Object payload = unmarshaller.unmarshal(messageContext.getRequest().getPayloadSource());
			logger.info("payload: {}",payload.getClass().getName());
			
			StringResult result = new StringResult();
			marshaller.marshal(payload, result);
			logger.info(result.toString());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		return true;
	}
}
