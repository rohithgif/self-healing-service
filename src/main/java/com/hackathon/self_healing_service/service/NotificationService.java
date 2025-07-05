package com.hackathon.self_healing_service.service;

import com.hackathon.self_healing_service.model.TransactionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SnsClient snsClient;

    @Value("${sns.alert.topic.arn}")
    private String topicArn;

    public void sendInternalIssueAlert(TransactionEvent event, String reason) {
        String subject = "Self-Healing Alert: Internal Issue Detected";
        String message = "Reason: " + reason + "\n\nEvent: " + event;

        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .subject(subject)
                .message(message)
                .build();

        snsClient.publish(request);
        System.out.println("Email alert sent via SNS to topic: " + topicArn);
    }
}
