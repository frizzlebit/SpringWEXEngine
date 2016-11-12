package guru.watson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import guru.watson.collection.SearchCollection;
import guru.watson.crawler.SearchCollectionCrawler;

@SpringBootApplication
public class Application {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	CommandLineRunner crawler(SearchCollectionCrawler scCrawler, SearchCollection search) {
		return args -> {
			String collection = "MyCollectionName";

			if (args.length > 0) {
				collection = args[0];
			}
			logger.info(search.searchCollectionStatus(collection));
			logger.info(scCrawler.searchCollectionCrawlerStart("staging", "refresh-new", collection));
		};
	}
}
