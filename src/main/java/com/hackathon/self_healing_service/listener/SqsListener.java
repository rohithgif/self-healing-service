package com.hackathon.self_healing_service.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.self_healing_service.model.TransactionEvent;
import com.hackathon.self_healing_service.service.HealingService;
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
    private final HealingService healingService;

    @Value("${sqs.queue.url}")
    private String queueUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedDelay = 5000)
    public void pollMessages() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(5)
                .waitTimeSeconds(10)
                .build();

        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();

        for (Message message : messages) {
            System.out.println("📩 Received message: " + message.body());

            try {
                TransactionEvent event =
                        objectMapper.readValue(message.body(), TransactionEvent.class);

                System.out.println("Deserialized event: " + event.getMessage());

                // 🔥 Trigger healing
                healingService.initiateHealing(event);

            } catch (Exception e) {
                System.err.println("Failed to deserialize/process message: " + e.getMessage());
            }

            sqsClient.deleteMessage(DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(message.receiptHandle())
                    .build());
        }
    }
}