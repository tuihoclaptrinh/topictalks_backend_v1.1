package com.anonymity.topictalks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The {@code TopicTalksBackEndApplication} class serves as the entry point for the Spring Boot application.
 * It is annotated with {@code @SpringBootApplication}, which combines various Spring annotations, such as
 * {@code @Configuration}, {@code @EnableAutoConfiguration}, and {@code @ComponentScan}, to enable Spring Boot
 * autoconfiguration and component scanning.
 *
 * This class contains the {@code main} method, which is the starting point of the application. When executed,
 * it launches the Spring Boot application, which in turn initializes the application context and starts
 * the web server.
 */
@SpringBootApplication
public class TopicTalksBackEndApplication {

	/**
	 * The main method that starts the Spring Boot application.
	 *
	 * @param args The command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(TopicTalksBackEndApplication.class, args);
	}

}
