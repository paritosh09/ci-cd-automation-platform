package com.cicd.automation.service;

import com.cicd.automation.repository.PipelineExecutionRepository;
import com.cicd.automation.repository.PipelineRepository;
import com.cicd.automation.security.WebhookSignatureValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WebhookService {

    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);

    @Autowired
    private WebhookSignatureValidator signatureValidator;

    @Autowired
    private PipelineRepository pipelineRepository;

    @Autowired
    private PipelineExecutionRepository executionRepository;

    @Value("${github.webhook.secret:default-secret}")
    private String webhookSecret;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void processGitHubWebhook(String payload, String event, String signature) {
        logger.info("Processing GitHub webhook event: {}", event);

        if (signature != null && !signatureValidator.validateGitHubSignature(payload, signature, webhookSecret)) {
            logger.warn("Invalid webhook signature");
            throw new RuntimeException("Invalid webhook signature");
        }

        try {
            JsonNode webhookData = objectMapper.readTree(payload);

            switch (event) {
                case "workflow_run":
                    handleWorkflowRunEvent(webhookData);
                    break;
                case "push":
                    handlePushEvent(webhookData);
                    break;
                default:
                    logger.debug("Unhandled webhook event: {}", event);
            }
        } catch (Exception e) {
            logger.error("Error processing GitHub webhook", e);
            throw new RuntimeException("Failed to process webhook: " + e.getMessage());
        }
    }

    private void handleWorkflowRunEvent(JsonNode webhookData) {
        logger.info("Handling workflow run event");
        // Implementation for workflow run event processing
    }

    private void handlePushEvent(JsonNode webhookData) {
        logger.info("Handling push event");
        // Implementation for push event processing
    }
}
