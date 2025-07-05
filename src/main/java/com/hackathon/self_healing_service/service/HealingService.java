package com.hackathon.self_healing_service.service;

import com.hackathon.self_healing_service.model.TransactionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealingService {

    private final LlmService llmService;
    private final NotificationService notificationService;
    private final EventPublisher eventPublisher;

    public void initiateHealing(TransactionEvent event) {
        String llmResponse = llmService.analyze(event);

        if (llmResponse.contains("internal issue")) {
            notificationService.sendInternalIssueAlert(event, llmResponse);
            System.out.println("Sent notification");
        } else if (llmResponse.contains("external issue")) {
            eventPublisher.publishRetryEvent(event);
        } else {
            System.out.println("Unknown issue type. Logging for investigation: " + llmResponse);
        }
    }
}