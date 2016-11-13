# SpringWEXEngine
Sample code that shows how to use Spring WebServiceTemplate for interacting with WEX 11.0.1 example code. This code implements the same functions as two of the [Sample Watson Explorer Engine API Applications](http://www.ibm.com/support/knowledgecenter/SS8NLW_11.0.1/com.ibm.swg.im.infosphere.dataexpl.engine.srapi.doc/c_api-sample-applications.html). The rest can be implemented by you as needed.

## Project Setup

1. Generate the JAX-WS source files using the instructions at [Working with Java 1.6 and above](http://www.ibm.com/support/knowledgecenter/SS8NLW_11.0.1/com.ibm.swg.im.infosphere.dataexpl.engine.srapi.doc/c_api-setup-soap-java.html). Include these files at ```src/main/java```. Include the source instead of the generated jar so that the ```spring-boot-maven-plugin``` will package this code in your final executable jar. Any maven ```system``` dependencies are ignored by the plugin, and you cannot publish the generated ```velocity.jar``` into any public maven repository.
1. Change line 37 of the Application.java class to include the name of a search collection on your target WEX server

Note: I tried to generate the JAX-WS classes using the [maven-jaxb2-plugin](https://java.net/projects/maven-jaxb2-plugin/pages/Home) and the [glassfish maven plugin](https://jax-ws-commons.java.net/jaxws-maven-plugin/) but could not get either of them to work. Perhaps at some point IBM will upgrade the WSDL file and I can try this again.

## Improvements Over Example Code

* [Spring Boot](https://projects.spring.io/spring-boot/) provides an easy implementation which can easily be expanded into web service stubs and [actuators](http://docs.spring.io/spring-boot/docs/current/reference/html/production-ready.html) that relieve you from having to write the boilerplate code required to support multiple environments.
* Authentication is implemented for all services you may wish to support because it is wired into WebServiceTemplate used for calling any Engine API service.
* The example code is a copy/paste nighmare.
