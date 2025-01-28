package com.cleartax.training_superheroes;

import com.cleartax.training_superheroes.services.SqsMessageConsumer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class  TrainingSuperheroesApplication implements CommandLineRunner {

	public TrainingSuperheroesApplication(com.cleartax.training_superheroes.services.SqsMessageConsumer sqsMessageConsumer) {
		SqsMessageConsumer = sqsMessageConsumer;
	}

	private final SqsMessageConsumer SqsMessageConsumer;

	public static void main(String[] args) {
		SpringApplication.run(TrainingSuperheroesApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {

		// Start polling the queue
		SqsMessageConsumer.pollQueueAndProcessMessages();
	}
}
