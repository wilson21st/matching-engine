package service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;
import service.util.DataStore;

@Slf4j(topic = "main")
@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
		
		// start background data loader then reload every 30s
		DataStore.getInstance().start(60);
		log.info("Engine started.");
	}
}