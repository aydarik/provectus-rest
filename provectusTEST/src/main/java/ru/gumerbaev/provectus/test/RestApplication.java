package ru.gumerbaev.provectus.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.gumerbaev.provectus.rest.RestService;

/**
 * Application's entry point.
 */
@SpringBootApplication
public class RestApplication {

	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		return new ServletRegistrationBean(new RestService(), "/*");
	}

	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("test-spring-context.xml", "rest-spring-context.xml");
		SpringApplication.run(RestApplication.class, args);
	}
}