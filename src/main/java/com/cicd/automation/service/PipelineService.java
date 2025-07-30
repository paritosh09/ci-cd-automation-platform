package com.cicd.automation.service;

import com.cicd.automation.dto.PipelineCreateRequest;
import com.cicd.automation.dto.PipelineResponse;
import com.cicd.automation.model.Pipeline;
import com.cicd.automation.model.PipelineExecution;
import com.cicd.automation.model.User;
import com.cicd.automation.repository.PipelineExecutionRepository;
import com.cicd.automation.repository.PipelineRepository;
import com.cicd.automation.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PipelineService {

    private static final Logger logger = LoggerFactory.getLogger(PipelineService.class);

    @Autowired
    private PipelineRepository pipelineRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PipelineExecutionRepository executionRepository;

    @Autowired
    private GitHubService gitHubService;

    @Autowired
    private WorkflowGeneratorService workflowGeneratorService;

    public PipelineResponse createPipeline(PipelineCreateRequest request, String username) {
        logger.info("Creating pipeline '{}' for user '{}'", request.getName(), username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String[] repoParts = extractRepositoryParts(request.getRepositoryUrl());
        String owner = repoParts[0];
        String repoName = repoParts[1];

        Pipeline pipeline = new Pipeline();
        pipeline.setName(request.getName());
        pipeline.setDescription(request.getDescription());
        pipeline.setRepositoryUrl(request.getRepositoryUrl());
        pipeline.setRepositoryName(repoName);
        pipeline.setRepositoryOwner(owner);
        pipeline.setBranch(request.getBranch() != null ? request.getBranch() : "main");
        pipeline.setUser(user);

        Pipeline savedPipeline = pipelineRepository.save(pipeline);

        try {
            String workflowContent = workflowGeneratorService.generateWorkflow(savedPipeline);
            String workflowPath = gitHubService.createWorkflowFile(
                    savedPipeline.getRepositoryOwner(),
                    savedPipeline.getRepositoryName(),
                    savedPipeline.getName(),
                    workflowContent,
                    user.getGithubAccessToken());
            savedPipeline.setWorkflowFilePath(workflowPath);
            savedPipeline = pipelineRepository.save(savedPipeline);
        } catch (Exception e) {
            logger.error("Failed to create GitHub Actions workflow", e);
        }

        return convertToResponse(savedPipeline);
    }

    public Page<PipelineResponse> getUserPipelines(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Page<Pipeline> pipelines = pipelineRepository.findByUserAndStatusNot(
                user, Pipeline.PipelineStatus.DELETED, pageable);

        return pipelines.map(this::convertToResponse);
    }

    public PipelineResponse getPipeline(Long pipelineId, String username) {
        Pipeline pipeline = findPipelineByIdAndUser(pipelineId, username);
        return convertToResponse(pipeline);
    }

    public PipelineResponse updatePipeline(Long pipelineId, PipelineCreateRequest request, String username) {
        Pipeline pipeline = findPipelineByIdAndUser(pipelineId, username);

        pipeline.setName(request.getName());
        pipeline.setDescription(request.getDescription());
        if (request.getBranch() != null) {
            pipeline.setBranch(request.getBranch());
        }

        Pipeline savedPipeline = pipelineRepository.save(pipeline);
        return convertToResponse(savedPipeline);
    }

    public void deletePipeline(Long pipelineId, String username) {
        Pipeline pipeline = findPipelineByIdAndUser(pipelineId, username);
        pipeline.setStatus(Pipeline.PipelineStatus.DELETED);
        pipelineRepository.save(pipeline);
    }

    public void triggerPipeline(Long pipelineId, String username) {
        Pipeline pipeline = findPipelineByIdAndUser(pipelineId, username);

        PipelineExecution execution = new PipelineExecution(pipeline, PipelineExecution.ExecutionTrigger.MANUAL);
        execution.setBranch(pipeline.getBranch());
        executionRepository.save(execution);

        try {
            gitHubService.triggerWorkflow(
                    pipeline.getRepositoryOwner(),
                    pipeline.getRepositoryName(),
                    pipeline.getWorkflowFilePath(),
                    pipeline.getBranch(),
                    pipeline.getUser().getGithubAccessToken());
        } catch (Exception e) {
            logger.error("Failed to trigger pipeline", e);
            throw new RuntimeException("Failed to trigger pipeline: " + e.getMessage());
        }
    }

    private Pipeline findPipelineByIdAndUser(Long pipelineId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return pipelineRepository.findByIdAndUserAndStatusNot(
                pipelineId, user, Pipeline.PipelineStatus.DELETED)
                .orElseThrow(() -> new RuntimeException("Pipeline not found"));
    }

    private String[] extractRepositoryParts(String repositoryUrl) {
        String cleanUrl = repositoryUrl.replace("https://github.com/", "").replace(".git", "");
        String[] parts = cleanUrl.split("/");
        if (parts.length != 2) {
            throw new RuntimeException("Invalid repository URL format");
        }
        return parts;
    }

    private PipelineResponse convertToResponse(Pipeline pipeline) {
        PipelineResponse response = new PipelineResponse();
        response.setId(pipeline.getId());
        response.setName(pipeline.getName());
        response.setDescription(pipeline.getDescription());
        response.setRepositoryUrl(pipeline.getRepositoryUrl());
        response.setRepositoryName(pipeline.getRepositoryName());
        response.setRepositoryOwner(pipeline.getRepositoryOwner());
        response.setBranch(pipeline.getBranch());
        response.setWorkflowFilePath(pipeline.getWorkflowFilePath());
        response.setStatus(pipeline.getStatus().name());
        response.setCreatedAt(pipeline.getCreatedAt());
        response.setUpdatedAt(pipeline.getUpdatedAt());
        return response;
    }
}
