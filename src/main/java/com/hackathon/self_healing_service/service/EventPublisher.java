package com.hackathon.self_healing_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.self_healing_service.model.TransactionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@RequiredArgsConstructor
public class EventPublisher {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${sqs.queue.url}")
    private String retryQueueUrl;

    public void publishRetryEvent(TransactionEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(retryQueueUrl)
                    .messageBody(message)
                    .build();

            sqsClient.sendMessage(request);
        } catch (Exception e) {
            System.err.println("Failed to publish retry event: " + e.getMessage());
        }
    }
}


