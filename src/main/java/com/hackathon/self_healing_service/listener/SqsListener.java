package com.hackathon.self_healing_service.listener;


import org.springframework.beans.factory.annotation.Value;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SqsListener {

    private final SqsClient sqsClient;

    @Value("${sqs.queue.url}")
    private String queueUrl;

    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

    @Scheduled(fixedDelay = 5000)
    public void pollMessages() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(5)
                .waitTimeSeconds(10) // long polling
                .build();

        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();

        for (Message message : messages) {
            System.out.println("Received message: " + message.body());

            // Deserialize the message body to TransactionEvent
            try {
                TransactionEvent event =
                        objectMapper.readValue(message.body(), com.hackathon.self_healing_service.request.TransactionEvent.class);
                System.out.println("Deserialized event: " + event.getMessage());
            } catch (Exception e) {
                System.err.println("Failed to deserialize message: " + e.getMessage());
            }

            // delete after processing
            DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(message.receiptHandle())
                    .build();
            sqsClient.deleteMessage(deleteMessageRequest);
        }
    }
}
