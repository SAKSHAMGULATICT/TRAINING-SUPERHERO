package com.cleartax.training_superheroes.services;

import com.cleartax.training_superheroes.config.SqsConfig;
import com.cleartax.training_superheroes.dto.Superhero;
import com.cleartax.training_superheroes.repos.SuperheroRepository;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageResponse;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import javax.sound.midi.Soundbank;
import java.util.List;

@Service
public class SqsMessageConsumer {

    private final SqsClient sqsClient;
    private final SuperheroRepository superheroRepository;
    private final String queueUrl;

    public SqsMessageConsumer(SqsClient sqsClient, SqsConfig sqsConfig, SuperheroRepository superheroRepository) {
        this.sqsClient = sqsClient;
        this.queueUrl = sqsConfig.getQueueUrl();
        this.superheroRepository = superheroRepository;
    }

    public void pollQueueAndProcessMessages() {
        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(5)
                .waitTimeSeconds(10)
                .build();

        while (true) {

            ReceiveMessageResponse response = sqsClient.receiveMessage(request);


            if (response.messages().isEmpty()) {
                System.out.println("No messages in the queue. Polling again...");
                continue; // Go to the next iteration
            }
//            response.messages().forEach(message -> {
//                System.out.println(message);
//                System.out.println("Message received: " + message.body());
//                System.out.println("Receipt handle: " + message.receiptHandle());
//            });

            List<String> messages = response.messages().stream()
                    .map(message -> message.body())
                    .toList();

            List<String> receiptHandles = response.messages().stream()
                    .map(message -> message.receiptHandle())
                    .toList();

            messages.forEach(this::saveMessage);

            receiptHandles.forEach(this::deleteMessage);



        }
    }

    private void deleteMessage(String message) {
        DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(message)
                .build();
        sqsClient.deleteMessage(deleteRequest);
//        DeleteMessageResponse var = sqsClient.deleteMessage(deleteRequest);
//        System.out.println("Message deleted: " + var.sdkHttpResponse().isSuccessful());
    }

    private void saveMessage(String message) {
        // Check if a document with the name exists in MongoDB
//        Superhero superhero = superheroRepository.findByName(message);

        Superhero superhero = new Superhero();
        superhero.setName(message);
        superheroRepository.save(superhero);

        if (superhero != null) {
//            superhero.setName("changed");
//            superheroRepository.save(superhero);
            System.out.println("Messages are: " + message);
        } else {
            System.out.println("No superhero found with name: " + message);
        }
    }
}

