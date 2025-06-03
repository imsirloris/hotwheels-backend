package com.loris.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class HotWheelsCollectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotWheelsCollectionApplication.class, args);
	}

}
