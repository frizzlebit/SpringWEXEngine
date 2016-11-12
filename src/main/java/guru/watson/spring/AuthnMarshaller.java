package guru.watson.spring;

import java.lang.reflect.Method;

import javax.xml.transform.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import velocity.soap.Authentication;

public class AuthnMarshaller extends Jaxb2Marshaller {
	private static final Logger logger = LoggerFactory.getLogger(AuthnMarshaller.class);
	
	@Autowired
	private Authentication authn;
	
	@Override
	public void marshal(Object graph, Result result) throws XmlMappingException {
		try {
			Method method = graph.getClass().getMethod("setAuthentication", Authentication.class);
			method.invoke(graph, authn);
			logger.info("Adding authentication for: {}",graph.getClass().getSimpleName());
		} catch (Exception e) {
			logger.warn("No setAuthentication method for: {}", graph.getClass().getSimpleName());
		}
		super.marshal(graph, result);
	}
}
