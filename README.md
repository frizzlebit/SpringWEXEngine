# SpringWEXEngine
Sample code that shows how to use the [Spring WebServiceTemplate](http://docs.spring.io/spring-ws/site/reference/html/client.html) for interacting with the [WEX 11.0.1](http://www.ibm.com/support/knowledgecenter/SS8NLW_11.0.1/com.ibm.swg.im.infosphere.dataexpl.welcome.doc/doc/watsonexplorer_11.0.1.html) example code. This code implements the same functions as two of the [Sample Watson Explorer Engine API Applications](http://www.ibm.com/support/knowledgecenter/SS8NLW_11.0.1/com.ibm.swg.im.infosphere.dataexpl.engine.srapi.doc/c_api-sample-applications.html). The rest can be implemented by you as needed.

## Project Setup
1. Generate the JAX-WS source files using the instructions at [Working with Java 1.6 and above](http://www.ibm.com/support/knowledgecenter/SS8NLW_11.0.1/com.ibm.swg.im.infosphere.dataexpl.engine.srapi.doc/c_api-setup-soap-java.html). Include these files at ```src/main/java```. Include the source instead of the generated jar so that the ```spring-boot-maven-plugin``` will package this code in your final executable jar. Any maven ```system``` dependencies are ignored by the plugin, and you cannot publish the generated ```velocity.jar``` into any public maven repository.
1. Change line 37 of the Application.java class to include the name of a search collection on your target WEX server. You will see ```The exception [search-collection-invalid-name] was thrown.``` if you fail to do this.
1. Edit the ```src/main/resources/application.properties``` to set the hostname, port and password required for your installation. You will need to change the ```service.name``` if you are on a Windows server.
1. Ensure that your target WEX Engine service is running.
1. Execute the code using: ```mvn spring-boot:run```

Note: I tried to generate the JAX-WS classes using the [maven-jaxb2-plugin](https://java.net/projects/maven-jaxb2-plugin/pages/Home) and the [glassfish maven plugin](https://jax-ws-commons.java.net/jaxws-maven-plugin/) but could not get either of them to work. Perhaps at some point IBM will upgrade the WSDL file and I can try this again.

## Improvements Over Example Code
* [Spring Boot](https://projects.spring.io/spring-boot/) provides an easy implementation which can easily be expanded into web service stubs and [actuators](http://docs.spring.io/spring-boot/docs/current/reference/html/production-ready.html) that relieve you from having to write the boilerplate code required to support multiple environments.
* Authentication is implemented for all services you may wish to support because it is wired into WebServiceTemplate used for calling any Engine API service.
* The ```template.setCheckConnectionForFault(false);``` is required to process SOAP faults in the unmarshalling code. Without this you will receive ```unexpected element (uri:"http://schemas.xmlsoap.org/soap/envelope/", local:"Fault").``` during any error.
