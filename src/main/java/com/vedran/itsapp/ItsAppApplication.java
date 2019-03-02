package com.vedran.itsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class ItsAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItsAppApplication.class, args);
	}

}
