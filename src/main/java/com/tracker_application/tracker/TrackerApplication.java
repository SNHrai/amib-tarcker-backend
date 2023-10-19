package com.tracker_application.tracker;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.tracker_application.tracker.config.TwilioConfig;
import com.twilio.Twilio;

@SpringBootApplication
public class TrackerApplication {

	@Autowired
	private TwilioConfig config;

	@PostConstruct
	public void initTwilio() {
		Twilio.init(config.getAccountSid(), config.getAuthToken());
	}

	public static void main(String[] args) {
		SpringApplication.run(TrackerApplication.class, args);
	}

	private static Logger log = LoggerFactory.getLogger(TrackerApplication.class);

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
				"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

	// private void sendFakeNewsLetter(Mail mail) throws MessagingException,
	// IOException {

	// log.info("START... Sending Fake News Letter");

	// mail.setSubject("Email with Spring boot and thymeleaf template!");

	// Map<String, Object> model = new HashMap<String, Object>();
	// model.put("name", "Developer!");
	// model.put("location", "United States");
	// model.put("sign", "Java Developer");
	// model.put("type", "NEWSLETTER");
	// mail.setProps(model);

	// emailService.sendEmail(mail);
	// log.info("END... Sending Fake News Letter");
	// }

	// private void sendInlinedCssEmail(Mail mail) throws MessagingException,
	// IOException {

	// log.info("START...Sending Inlined CSS Email");

	// mail.setSubject("Email with Inlined CSS Responsive Thymeleaf Template!");

	// Map<String, Object> model = new HashMap<String, Object>();
	// model.put("name", "Peter Milanovich!");
	// model.put("address", "Company Inc, 3 Abbey Road, San Francisco CA 94102");
	// model.put("sign", "JavaByDeveloper");
	// model.put("type", "TRANSACTIONAL");
	// mail.setProps(model);

	// emailService.sendEmail(mail);
	// log.info("END... Sending Inlined CSS Email");
	// }
}
