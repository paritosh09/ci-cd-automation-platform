package com.cicd.automation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class GitHubService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubService.class);

    @Value("${github.api.base-url:https://api.github.com}")
    private String githubApiBaseUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GitHubService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String createWorkflowFile(String owner, String repo, String pipelineName,
            String workflowContent, String accessToken) throws Exception {

        String workflowFileName = pipelineName.toLowerCase().replaceAll("[^a-z0-9]", "-") + ".yml";
        String workflowPath = ".github/workflows/" + workflowFileName;

        String url = String.format("%s/repos/%s/%s/contents/%s",
                githubApiBaseUrl, owner, repo, workflowPath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("message", "Add CI/CD workflow for " + pipelineName);
        requestBody.put("content", Base64.getEncoder().encodeToString(workflowContent.getBytes()));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                return workflowPath;
            } else {
                throw new RuntimeException("Failed to create workflow file");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating workflow file: " + e.getMessage());
        }
    }

    public void triggerWorkflow(String owner, String repo, String workflowPath,
            String branch, String accessToken) throws Exception {

        String workflowFileName = workflowPath.replace(".github/workflows/", "");
        String url = String.format("%s/repos/%s/%s/actions/workflows/%s/dispatches",
                githubApiBaseUrl, owner, repo, workflowFileName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("ref", branch);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to trigger workflow");
        }
    }
}
