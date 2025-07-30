package com.cicd.automation.model;

import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "pipeline_executions")
public class PipelineExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pipeline_id", nullable = false)
    private Pipeline pipeline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExecutionStatus status = ExecutionStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExecutionTrigger trigger;

    @Column(name = "commit_hash")
    private String commitHash;

    @Column(name = "commit_message", columnDefinition = "TEXT")
    private String commitMessage;

    @Column(name = "branch")
    private String branch;

    @Column(name = "started_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startedAt;

    @Column(name = "completed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt;

    @CreationTimestamp
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    // Constructors
    public PipelineExecution() {
    }

    public PipelineExecution(Pipeline pipeline, ExecutionTrigger trigger) {
        this.pipeline = pipeline;
        this.trigger = trigger;
        this.startedAt = new Date();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pipeline getPipeline() {
        return pipeline;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }

    public ExecutionTrigger getTrigger() {
        return trigger;
    }

    public void setTrigger(ExecutionTrigger trigger) {
        this.trigger = trigger;
    }

    public String getCommitHash() {
        return commitHash;
    }

    public void setCommitHash(String commitHash) {
        this.commitHash = commitHash;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public enum ExecutionStatus {
        PENDING, IN_PROGRESS, SUCCESS, FAILURE, CANCELLED
    }

    public enum ExecutionTrigger {
        MANUAL, PUSH, PULL_REQUEST, SCHEDULE, WEBHOOK
    }
}
