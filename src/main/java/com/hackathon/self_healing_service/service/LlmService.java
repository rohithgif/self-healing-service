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

    private static final String LLM_SERVICE_URL = "http://localhost:8000/api/query-exception/";

    public String analyze(TransactionEvent event) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    LLM_SERVICE_URL,
                    event,
                    String.class
            );
            return response.getBody();
        } catch (Exception e) {
            System.err.println("LLM service call failed: " + e.getMessage());
            return "internal issue: fallback response due to failure";
        }
    }
}