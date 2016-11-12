package guru.watson.collection;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.xml.transform.StringResult;

import velocity.soap.Authentication;
import velocity.types.SearchCollectionStatus;
import velocity.types.SearchCollectionStatusResponse;

@Component
public class SearchCollection extends WebServiceGatewaySupport {
	private static final Logger logger = LoggerFactory.getLogger(SearchCollection.class);
	
	@Value("${service.endpoint}")
	private String endpoint;
	@Autowired
	private Authentication authn;
	
	public SearchCollection(Jaxb2Marshaller marshaller) {
		this.setMarshaller(marshaller);
		this.setUnmarshaller(marshaller);
	}
	
	public String searchCollectionStatus(String collection) throws XmlMappingException, IOException {
		logger.info("Enter searchCollectionStatus");
		
		SearchCollectionStatus request = new SearchCollectionStatus();
		request.setCollection(collection);
		request.setAuthentication(authn);
		
		SearchCollectionStatusResponse response = 
				(SearchCollectionStatusResponse) getWebServiceTemplate().marshalSendAndReceive(endpoint,request);
		
		StringResult sResult =  new StringResult();
		getWebServiceTemplate().getMarshaller().marshal(response.getVseStatus(), sResult);
		return sResult.toString();
	}
}
