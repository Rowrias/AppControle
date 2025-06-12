package br.com.appcontrole;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AppControleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppControleApplication.class, args);
	}

}
