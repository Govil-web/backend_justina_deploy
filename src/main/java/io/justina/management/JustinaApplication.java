package io.justina.management;

import io.justina.management.config.security.SecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class JustinaApplication {

	public static void main(String[] args) {
		SpringApplication.run(JustinaApplication.class, args);
	}

}
