package guru.watson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

import guru.watson.collection.SearchCollection;
import guru.watson.crawler.SearchCollectionCrawler;
import guru.watson.spring.AuthnInterceptor;
import velocity.soap.Authentication;

@SpringBootApplication
public class Application {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	@Value("${service.endpoint}")
	private String endpoint;
	@Value("${service.user}")
	private String user;
	@Value("${service.password}")
	private String password;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	CommandLineRunner crawler(SearchCollectionCrawler scCrawler, SearchCollection search) {
		return args -> {
			String collection = "MySearchCollection";

			if (args.length > 0) {
				collection = args[0];
			}
			logger.info(search.searchCollectionStatus(collection));
			logger.info(scCrawler.searchCollectionCrawlerStart("staging", "refresh-new", collection));
		};
	}
	
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("velocity.types");
		return marshaller;
	}
	
	@Bean
	public WebServiceTemplate template(Jaxb2Marshaller marshaller, AuthnInterceptor authn) {
		/*
		 * algorithm: http://docs.spring.io/spring-ws/site/apidocs/org/springframework/ws/client/core/WebServiceTemplate.html
		 */
		WebServiceTemplate template = new WebServiceTemplate();
		template.setMarshaller(marshaller);
		template.setUnmarshaller(marshaller);
		template.setCheckConnectionForFault(false);
		template.setDefaultUri(endpoint);
		template.setInterceptors(new ClientInterceptor[] {authn});
		return template;
	}
	
	@Bean
	public Authentication authn() {
    	Authentication authn = new Authentication();
    	authn.setUsername(user);
    	authn.setPassword(password);
    	return authn;
	}	
}
