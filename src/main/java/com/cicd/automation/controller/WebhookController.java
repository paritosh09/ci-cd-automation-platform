package com.cicd.automation.controller;

import com.cicd.automation.service.WebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhook")
@Tag(name = "Webhook Management", description = "APIs for handling webhook events")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @Autowired
    private WebhookService webhookService;

    @PostMapping("/github")
    @Operation(summary = "GitHub webhook handler", description = "Handle GitHub webhook events")
    public ResponseEntity<String> handleGitHubWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "X-GitHub-Event") String event,
            @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature) {

        logger.info("Received GitHub webhook event: {}", event);

        try {
            webhookService.processGitHubWebhook(payload, event, signature);
            return ResponseEntity.ok("Webhook processed successfully");
        } catch (Exception e) {
            logger.error("Error processing webhook: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error processing webhook: " + e.getMessage());
        }
    }
}
