package com.vedran.itsapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@EnableMongoAuditing
@SpringBootApplication
public class ItsAppApplication {

	@Bean
	@Profile("dev")
	JavaMailSender javaMailSender(@Value("${spring.mail.host}") String host,
																@Value("${spring.mail.port}") int port,
																@Value("${spring.mail.username}") String username,
																@Value("${spring.mail.password}") String password){
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(host);
		javaMailSender.setPort(port);
		javaMailSender.setUsername(username);
		javaMailSender.setPassword(password);
		return javaMailSender;
	}

	public static void main(String[] args) {
		SpringApplication.run(ItsAppApplication.class, args);
	}

}
