package guru.watson;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

import velocity.soap.Authentication;

@Configuration
public class ApplicationConfiguration {
	@Value("${service.endpoint}")
	private String endpoint;
	@Value("${service.user}")
	private String user;
	@Value("${service.password}")
	private String password;
	
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("velocity.types");
		return marshaller;
	}
	
	@Bean
	public WebServiceTemplate template(Jaxb2Marshaller marshaller) {
		WebServiceTemplate template = new WebServiceTemplate();
		template.setMarshaller(marshaller);
		template.setUnmarshaller(marshaller);
		template.setCheckConnectionForFault(false);
		template.setDefaultUri(endpoint);
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
