package org.guru.watson.crawler;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import velocity.soap.Authentication;
import velocity.types.SearchCollectionCrawlerStart;

@Component
public class SearchCollectionCrawler extends WebServiceGatewaySupport {
	private static final Logger logger = LoggerFactory.getLogger(SearchCollectionCrawler.class);
	
	@Value("${service.endpoint}")
	private String endpoint;
	@Autowired
	private Authentication authn;
	
	/*
	 * Declaring a constructor allows Spring to inject types
	 */
	public SearchCollectionCrawler(Jaxb2Marshaller marshaller) {
		this.setMarshaller(marshaller);
		this.setUnmarshaller(marshaller);
	}
	
	//[-t new|resume|resume-and-idle|refresh-inplace|refresh-new|apply-changes]
	public String searchCollectionCrawlerStart(String subcollection, String type, String collection) {
		logger.info("Enter searchCollectionCrawlerStart");
		
		SearchCollectionCrawlerStart request = new SearchCollectionCrawlerStart();
    	request.setCollection(collection);
    	request.setSubcollection(subcollection);
    	request.setType(type);
    	request.setAuthentication(authn);
    	
    	getWebServiceTemplate().marshalSendAndReceive(endpoint,request);
    	
    	/*
    	 * The service implementation return value is void. We send a String response.
    	 */
		String template = "Started '${collection}' using endpoint: ${endpoint}";
		Map<String,String> vMap = new HashMap<String,String>();
		vMap.put("endpoint", endpoint);
		vMap.put("collection", collection);
		StrSubstitutor sub = new StrSubstitutor(vMap);
		return sub.replace(template);
	}
}
