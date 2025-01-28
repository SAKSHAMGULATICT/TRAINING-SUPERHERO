package com.cleartax.training_superheroes.controllers;


import com.cleartax.training_superheroes.config.SqsConfig;
import com.cleartax.training_superheroes.dto.Superhero;
import com.cleartax.training_superheroes.dto.SuperheroRequestBody;
import com.cleartax.training_superheroes.services.SuperheroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@RestController
public class SuperheroController {

    private SuperheroService superheroService;

    @Autowired
    private SqsConfig sqsConfig;

    @Autowired
    private SqsClient sqsClient;
    @Autowired
    public SuperheroController(SuperheroService superheroService){
        this.superheroService = superheroService;
    }


    @GetMapping("/hello")
    public String hello(@RequestParam(value = "username", defaultValue = "World") String username) {
        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(sqsConfig.getQueueUrl())
                .messageBody("Hello i am saksham").build());

        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(sqsConfig.getQueueUrl())
                .maxNumberOfMessages(10)
                .build();
        ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage(receiveMessageRequest);
        System.out.println(receiveMessageResponse.messages());
        return String.format("Hello %s!,%s", username,sqsConfig.getQueueName());
    }

//    @GetMapping("/superhero")
//    public Superhero getSuperhero(@RequestParam(value = "name", defaultValue = "Batman") String name,
//                                  @RequestParam(value = "universe", defaultValue = "DC") String universe){
//        return superheroService.getSuperhero(name, universe);
//    }

    @PostMapping("/superhero")
    public Superhero persistSuperhero(@RequestBody SuperheroRequestBody superheroRequestBody){
        return superheroService.persistSuperhero(superheroRequestBody, superheroRequestBody.getUniverse());
    }
}
