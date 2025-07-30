package com.cicd.automation.controller;

import com.cicd.automation.dto.ApiResponse;
import com.cicd.automation.dto.PipelineCreateRequest;
import com.cicd.automation.dto.PipelineResponse;
import com.cicd.automation.service.PipelineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pipelines")
@Tag(name = "Pipeline Management", description = "APIs for managing CI/CD pipelines")
public class PipelineController {

    @Autowired
    private PipelineService pipelineService;

    @PostMapping
    @Operation(summary = "Create new pipeline", description = "Create a new CI/CD pipeline with GitHub Actions integration")
    public ResponseEntity<ApiResponse<PipelineResponse>> createPipeline(
            @Valid @RequestBody PipelineCreateRequest request,
            Authentication authentication) {

        String username = authentication.getName();
        PipelineResponse pipeline = pipelineService.createPipeline(request, username);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Pipeline created successfully", pipeline));
    }

    @GetMapping
    @Operation(summary = "Get user pipelines", description = "Retrieve all pipelines for the authenticated user")
    public ResponseEntity<ApiResponse<Page<PipelineResponse>>> getUserPipelines(
            Authentication authentication,
            Pageable pageable) {

        String username = authentication.getName();
        Page<PipelineResponse> pipelines = pipelineService.getUserPipelines(username, pageable);

        return ResponseEntity.ok(new ApiResponse<>("Pipelines retrieved successfully", pipelines));
    }

    @GetMapping("/{pipelineId}")
    @Operation(summary = "Get pipeline details", description = "Get detailed information about a specific pipeline")
    public ResponseEntity<ApiResponse<PipelineResponse>> getPipeline(
            @PathVariable Long pipelineId,
            Authentication authentication) {

        String username = authentication.getName();
        PipelineResponse pipeline = pipelineService.getPipeline(pipelineId, username);

        return ResponseEntity.ok(new ApiResponse<>("Pipeline retrieved successfully", pipeline));
    }

    @PutMapping("/{pipelineId}")
    @Operation(summary = "Update pipeline", description = "Update an existing pipeline configuration")
    public ResponseEntity<ApiResponse<PipelineResponse>> updatePipeline(
            @PathVariable Long pipelineId,
            @Valid @RequestBody PipelineCreateRequest request,
            Authentication authentication) {

        String username = authentication.getName();
        PipelineResponse pipeline = pipelineService.updatePipeline(pipelineId, request, username);

        return ResponseEntity.ok(new ApiResponse<>("Pipeline updated successfully", pipeline));
    }

    @DeleteMapping("/{pipelineId}")
    @Operation(summary = "Delete pipeline", description = "Delete a pipeline and its associated workflows")
    public ResponseEntity<ApiResponse<String>> deletePipeline(
            @PathVariable Long pipelineId,
            Authentication authentication) {

        String username = authentication.getName();
        pipelineService.deletePipeline(pipelineId, username);

        return ResponseEntity.ok(new ApiResponse<>("Pipeline deleted successfully", "Pipeline removed"));
    }

    @PostMapping("/{pipelineId}/trigger")
    @Operation(summary = "Trigger pipeline", description = "Manually trigger a pipeline execution")
    public ResponseEntity<ApiResponse<String>> triggerPipeline(
            @PathVariable Long pipelineId,
            Authentication authentication) {

        String username = authentication.getName();
        pipelineService.triggerPipeline(pipelineId, username);

        return ResponseEntity.ok(new ApiResponse<>("Pipeline triggered successfully", "Execution started"));
    }
}
