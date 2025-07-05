package com.hackathon.self_healing_service.service;

import com.hackathon.self_healing_service.model.TransactionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LlmService {

    private final RestTemplate restTemplate;

    @Value("${llm.service.url}")
    private String llmServiceUrl;

//    public String analyze(TransactionEvent event) {
//        ResponseEntity<String> response = restTemplate.postForEntity(llmServiceUrl, event, String.class);
//        return response.getBody();
//    }

        public String analyze(TransactionEvent event) {
            String message = String.valueOf(event.getMessage()).toLowerCase();

            if (message.contains("db") || message.contains("internal")) {
                return "internal issue: simulated database error";
            }

            if (message.contains("api") || message.contains("partner") || message.contains("external")) {
                return "external issue: simulated third-party service failure";
            }

            return "unknown issue: unable to classify";
        }
    }

