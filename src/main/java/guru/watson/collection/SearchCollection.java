package guru.watson.collection;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.xml.transform.StringResult;

import velocity.soap.Authentication;
import velocity.types.SearchCollectionStatus;
import velocity.types.SearchCollectionStatusResponse;

@Component
public class SearchCollection {
	private static final Logger logger = LoggerFactory.getLogger(SearchCollection.class);
	
	@Autowired
	private WebServiceTemplate template;
	@Autowired
	private Authentication authn;
	
	public String searchCollectionStatus(String collection) throws XmlMappingException, IOException {
		logger.info("Enter searchCollectionStatus");
		
		SearchCollectionStatus request = new SearchCollectionStatus();
		request.setCollection(collection);
		request.setAuthentication(authn);
		
		SearchCollectionStatusResponse response = 
				(SearchCollectionStatusResponse) template.marshalSendAndReceive(request);
		
		StringResult sResult =  new StringResult();
		template.getMarshaller().marshal(response.getVseStatus(), sResult);
		return sResult.toString();
	}
}
