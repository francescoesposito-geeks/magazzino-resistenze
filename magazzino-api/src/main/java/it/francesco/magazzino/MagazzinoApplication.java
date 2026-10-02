package it.francesco.magazzino;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//rende questa classe (main) una spring boot application
@SpringBootApplication
public class MagazzinoApplication {

	public static void main(String[] args) {
		//dove fa partire l'applicazione
		SpringApplication.run(MagazzinoApplication.class, args);
	}

}
