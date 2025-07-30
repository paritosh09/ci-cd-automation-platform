package com.cicd.automation.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PipelineCreateRequest {

    @NotBlank(message = "Pipeline name is required")
    @Size(min = 3, max = 100, message = "Pipeline name must be between 3 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotBlank(message = "Repository URL is required")
    @Pattern(regexp = "^https://github\\.com/[\\w.-]+/[\\w.-]+(\\.git)?$", message = "Invalid GitHub repository URL format")
    private String repositoryUrl;

    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Invalid branch name format")
    private String branch = "main";

    private String workflowTemplate = "default";

    // Constructors
    public PipelineCreateRequest() {
    }

    public PipelineCreateRequest(String name, String repositoryUrl) {
        this.name = name;
        this.repositoryUrl = repositoryUrl;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getWorkflowTemplate() {
        return workflowTemplate;
    }

    public void setWorkflowTemplate(String workflowTemplate) {
        this.workflowTemplate = workflowTemplate;
    }
}
