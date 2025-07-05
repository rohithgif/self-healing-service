package com.hackathon.self_healing_service.controller;

import com.hackathon.self_healing_service.model.TransactionEvent;
import com.hackathon.self_healing_service.service.HealingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/self-healing")
@RequiredArgsConstructor
public class SelfHealingController {

    private final HealingService healingService;

    @PostMapping("/trigger")
    public ResponseEntity<String> triggerHealing(@RequestBody TransactionEvent event) {
        healingService.initiateHealing(event);
        return ResponseEntity.ok("Healing initiated");
    }
}
